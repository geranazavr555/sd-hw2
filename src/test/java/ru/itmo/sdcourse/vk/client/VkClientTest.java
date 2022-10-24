package ru.itmo.sdcourse.vk.client;

import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.itmo.sdcourse.snstat.vk.client.VkClient;
import ru.itmo.sdcourse.snstat.vk.exception.VkException;

import java.util.Map;
import java.util.Random;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;
import static org.junit.Assert.*;

public class VkClientTest {
    private static final int PORT = new Random().nextInt(20000, 65535);

    private StubServer stubServer;

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
    public void simplePingPong() {
        whenHttp(stubServer)
                .match(method(Method.GET), startsWithUri("/method/"))
                .then(stringContent("OK"));

        VkClient client = new TestVkClient("user.info", "abacaba");
        String result = client.fetch(Map.of());
        assertEquals("OK", result);
    }

    @Test
    public void params() {
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

        VkClient client = new TestVkClient("newsfeed.search", "abacaba");
        String result = client.fetch(Map.of(
                "q", "query",
                "start_time", "1666547983",
                "end_time", "1666548000"
        ));
        assertEquals("OK", result);
    }

    @Test(expected = VkException.class)
    public void httpNot200() {
        whenHttp(stubServer).match(alwaysFalse()).then();

        VkClient client = new TestVkClient("user.info", "abacaba");
        client.fetch(Map.of());
    }

    @Test(expected = RuntimeException.class)
    public void illegalUri() {
        VkClient client = new TestVkClient("\\\\", "\\\\");
        client.fetch(Map.of());
    }

    private static class TestVkClient extends VkClient {
        public TestVkClient(String method, String accessToken) {
            super(false, "localhost", PORT, method, accessToken);
        }
    }
}
