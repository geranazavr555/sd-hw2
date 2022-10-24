package ru.itmo.sdcourse.snstat.vk;

import org.apache.commons.lang3.Validate;
import ru.itmo.sdcourse.snstat.PostsRecentStatistics;
import ru.itmo.sdcourse.snstat.SocialNetworkRecentStatisticsProvider;
import ru.itmo.sdcourse.snstat.impl.PostsRecentStatisticsImpl;
import ru.itmo.sdcourse.snstat.vk.client.VkNewsfeedSearchClient;
import ru.itmo.sdcourse.snstat.vk.parser.VkNewsfeedSearchResponseParser;
import ru.itmo.sdcourse.snstat.vk.response.VkNewsfeedSearchResponse;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;

public class VkRecentPostsStatisticsProvider implements SocialNetworkRecentStatisticsProvider {
    private final VkNewsfeedSearchClient client;
    private final VkNewsfeedSearchResponseParser parser;

    @Inject
    public VkRecentPostsStatisticsProvider(VkNewsfeedSearchClient client, VkNewsfeedSearchResponseParser parser) {
        this.client = client;
        this.parser = parser;
    }

    @Override
    public PostsRecentStatistics getPostsWithHashtagForLastHours(String hashtag, int hours) {
        Validate.inclusiveBetween(1, 24, hours);

        hashtag = "#" + hashtag;

        long[] result = new long[hours];

        Instant toInstant = Instant.now();
        for (int i = 0; i < hours; i++) {
            Instant toInstantMinusHour = toInstant.minus(Duration.ofHours(1));
            Instant fromInstant = toInstantMinusHour.plus(Duration.ofSeconds(1));

            result[i] = getPostsCountWithHashtagBetween(hashtag,
                    fromInstant.getEpochSecond(), toInstant.getEpochSecond());

            toInstant = toInstantMinusHour;
        }

        return new PostsRecentStatisticsImpl(result);
    }

    private long getPostsCountWithHashtagBetween(String hashtag, long fromUnixSeconds, long toUnixSeconds) {
        String json = client.fetch(hashtag, fromUnixSeconds, toUnixSeconds);
        VkNewsfeedSearchResponse response = parser.parse(json);
        return response.totalCount();
    }
}
