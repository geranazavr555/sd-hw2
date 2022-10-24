package ru.itmo.sdcourse.snstat;

public interface SocialNetworkRecentStatisticsProvider {
    PostsRecentStatistics getPostsWithHashtagForLastHours(String hashtag, int hours);
}
