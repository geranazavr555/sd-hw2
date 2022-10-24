package ru.itmo.sdcourse.vk.client;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.itmo.sdcourse.snstat.SocialNetworkRecentStatisticsProvider;
import ru.itmo.sdcourse.snstat.vk.VkRecentPostsStatisticsProvider;
import ru.itmo.sdcourse.snstat.vk.client.VkNewsfeedSearchClient;

import java.util.Random;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;
import static org.junit.Assert.assertEquals;

public class VkNewsfeedSearchClientTest {
    private static final int PORT = new Random().nextInt(20000, 65535);

    private static Injector injector;

    private StubServer stubServer;

    @BeforeClass
    public static void prepareGuice() {
        injector = Guice.createInjector(new VkNewsfeedSearchClientTestModule());
    }

    @Before
    public void createStubServer() {
        stubServer = new StubServer(PORT).run();
    }

    @After
    public void shutdownStubServer() {
        stubServer.stop();
        stubServer = null;
    }

    @Test
    public void simple() {
        whenHttp(stubServer)
                .match(
                        method(Method.GET),
                        startsWithUri("/method/"),
                        parameter("q", "query"),
                        parameter("start_time", "1666547983"),
                        parameter("end_time", "1666548000"),
                        parameter("access_token", "abacaba"),
                        parameter("v", "5.131"),
                        custom(call -> call.getParameters().size() == 5)
                ).then(stringContent("OK"));

        VkNewsfeedSearchClient client = injector.getInstance(VkNewsfeedSearchClient.class);
        String response = client.fetch("query", 1666547983, 1666548000);
        assertEquals("OK", response);
    }

    @Test
    public void simpleWithStartFrom() {
        whenHttp(stubServer)
                .match(
                        method(Method.GET),
                        startsWithUri("/method/"),
                        parameter("q", "query"),
                        parameter("start_time", "1666547983"),
                        parameter("end_time", "1666548000"),
                        parameter("access_token", "abacaba"),
                        parameter("v", "5.131"),
                        parameter("start_from", "ababa"),
                        custom(call -> call.getParameters().size() == 6)
                ).then(stringContent("OK"));

        VkNewsfeedSearchClient client = injector.getInstance(VkNewsfeedSearchClient.class);
        String response = client.fetch("query", 1666547983, 1666548000, "ababa");
        assertEquals("OK", response);
    }

    public static class VkNewsfeedSearchClientTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(SocialNetworkRecentStatisticsProvider.class).to(VkRecentPostsStatisticsProvider.class);

            bind(boolean.class).annotatedWith(Names.named("vkSecure")).toInstance(false);
            bind(String.class).annotatedWith(Names.named("vkHost")).toInstance("localhost");
            bind(int.class).annotatedWith(Names.named("vkPort")).toInstance(PORT);
            bind(String.class).annotatedWith(Names.named("vkAccessToken")).toInstance("abacaba");
        }
    }
}
