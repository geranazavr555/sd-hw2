package ru.itmo.sdcourse.snstat;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ru.itmo.sdcourse.snstat.vk.VkRecentPostsStatisticsProvider;

public class SocialNetworkRecentStatisticsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SocialNetworkRecentStatisticsProvider.class).to(VkRecentPostsStatisticsProvider.class);

        bind(boolean.class).annotatedWith(Names.named("vkSecure")).toInstance(true);
        bind(String.class).annotatedWith(Names.named("vkHost")).toInstance("api.vk.com");
        bind(int.class).annotatedWith(Names.named("vkPort")).toInstance(443);
        bind(String.class).annotatedWith(Names.named("vkAccessToken")).toInstance(System.getenv("VK_ACCESS_TOKEN"));
    }
}
