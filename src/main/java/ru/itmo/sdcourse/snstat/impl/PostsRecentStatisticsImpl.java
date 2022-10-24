package ru.itmo.sdcourse.snstat.impl;

import org.apache.commons.lang3.Validate;
import ru.itmo.sdcourse.snstat.PostsRecentStatistics;

public class PostsRecentStatisticsImpl implements PostsRecentStatistics {
    private final long[] postsCountByAgeHours;

    public PostsRecentStatisticsImpl(long[] postsCountByAgeHours) {
        this.postsCountByAgeHours = postsCountByAgeHours;
        for (long postsCount : postsCountByAgeHours) {
            Validate.isTrue(postsCount >= 0);
        }
    }

    @Override
    public long getCountByAgeHours(int ageHours) {
        Validate.inclusiveBetween(1, postsCountByAgeHours.length, ageHours);
        return postsCountByAgeHours[ageHours - 1];
    }
}
