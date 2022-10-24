package ru.itmo.sdcourse;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.itmo.sdcourse.snstat.PostsRecentStatistics;
import ru.itmo.sdcourse.snstat.SocialNetworkRecentStatisticsModule;
import ru.itmo.sdcourse.snstat.SocialNetworkRecentStatisticsProvider;

public class Application {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new SocialNetworkRecentStatisticsModule());

        SocialNetworkRecentStatisticsProvider provider = injector.getInstance(SocialNetworkRecentStatisticsProvider.class);

        int n = 24;
        PostsRecentStatistics result = provider.getPostsWithHashtagForLastHours("Россия", n);
        for (int i = 0; i < n; i++) {
            System.out.println(result.getCountByAgeHours(i + 1));
        }
    }
}
