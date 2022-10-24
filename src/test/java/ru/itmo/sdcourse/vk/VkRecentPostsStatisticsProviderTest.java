package ru.itmo.sdcourse.vk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.itmo.sdcourse.snstat.PostsRecentStatistics;
import ru.itmo.sdcourse.snstat.SocialNetworkRecentStatisticsProvider;
import ru.itmo.sdcourse.snstat.vk.VkRecentPostsStatisticsProvider;
import ru.itmo.sdcourse.snstat.vk.client.VkNewsfeedSearchClient;
import ru.itmo.sdcourse.snstat.vk.parser.VkNewsfeedSearchResponseParser;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class VkRecentPostsStatisticsProviderTest {
    @Mock
    private VkNewsfeedSearchClient vkNewsfeedSearchClient;

    @Test
    public void simple() {
        long time = new Date().getTime() / 1000;

        when(vkNewsfeedSearchClient.fetch("#test", time - 60 * 60 + 1, time))
                .thenReturn(generateAnswer(5));

        SocialNetworkRecentStatisticsProvider provider =
                new VkRecentPostsStatisticsProvider(vkNewsfeedSearchClient, new VkNewsfeedSearchResponseParser());
        PostsRecentStatistics statistics = provider.getPostsWithHashtagForLastHours("test", 1);

        assertEquals(5, statistics.getCountByAgeHours(1));
    }

    @Test
    public void manyHours() {
        long time = new Date().getTime() / 1000;

        when(vkNewsfeedSearchClient.fetch("#test", time - 60 * 60 + 1, time))
                .thenReturn(generateAnswer(5));
        when(vkNewsfeedSearchClient.fetch("#test", time - 2 * 60 * 60 + 1, time - 60 * 60))
                .thenReturn(generateAnswer(8));
        when(vkNewsfeedSearchClient.fetch("#test", time - 3 * 60 * 60 + 1, time - 2 * 60 * 60))
                .thenReturn(generateAnswer(15));

        SocialNetworkRecentStatisticsProvider provider =
                new VkRecentPostsStatisticsProvider(vkNewsfeedSearchClient, new VkNewsfeedSearchResponseParser());
        PostsRecentStatistics statistics = provider.getPostsWithHashtagForLastHours("test", 3);

        assertEquals(5, statistics.getCountByAgeHours(1));
        assertEquals(8, statistics.getCountByAgeHours(2));
        assertEquals(15, statistics.getCountByAgeHours(3));
    }

    private String generateAnswer(int count) {
        StringBuilder items = new StringBuilder();
        items.append("{},".repeat(count));
        items.deleteCharAt(items.length() - 1);
        return """
                {  "response": {
                    "count": %d,
                    "items": [
                      %s
                    ],
                    "total_count": %d
                  }
                }""".formatted(count, items.toString(), count);
    }
}
