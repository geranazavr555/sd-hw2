package ru.itmo.sdcourse.snstat.vk.client;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

public class VkNewsfeedSearchClient extends VkClient {
    private static final String METHOD = "newsfeed.search";

    @Inject
    public VkNewsfeedSearchClient(@Named("vkSecure") boolean secure,
                                  @Named("vkHost") String host,
                                  @Named("vkPort") int port,
                                  @Named("vkAccessToken") String accessToken) {
        super(secure, host, port, METHOD, accessToken);
    }

    public String fetch(String query, long startTimeUnixSeconds, long endTimeUnixSeconds, @Nullable String startFrom) {
        Map<String, String> params = Map.of(
                "q", query,
                "start_time", String.valueOf(startTimeUnixSeconds),
                "end_time", String.valueOf(endTimeUnixSeconds)
        );

        if (startFrom != null) {
            params = new HashMap<>(params);
            params.put("start_from", startFrom);
        }

        return super.fetch(params);
    }

    public String fetch(String query, long startTimeUnixSeconds, long endTimeUnixSeconds) {
        return fetch(query, startTimeUnixSeconds, endTimeUnixSeconds, null);
    }
}
