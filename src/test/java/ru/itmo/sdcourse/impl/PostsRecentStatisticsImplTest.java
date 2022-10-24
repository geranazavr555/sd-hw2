package ru.itmo.sdcourse.impl;

import org.junit.Test;
import ru.itmo.sdcourse.snstat.PostsRecentStatistics;
import ru.itmo.sdcourse.snstat.impl.PostsRecentStatisticsImpl;

import static org.junit.Assert.*;

public class PostsRecentStatisticsImplTest {
    @Test
    public void ok() {
        long[] values = {9, 8, 7, 3, 4, 5, 6, 7, 8};
        PostsRecentStatistics postsRecentStatistics = new PostsRecentStatisticsImpl(values);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], postsRecentStatistics.getCountByAgeHours(i + 1));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeValue() {
        long[] values = {9, 8, 7, 3, -1, 5, 6, 7, 8};
        new PostsRecentStatisticsImpl(values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void indexOutOfBounds() {
        long[] values = {9, 8, 7, 3, 5, 5, 6, 7, 8};
        new PostsRecentStatisticsImpl(values).getCountByAgeHours(256);
    }
}
