package ru.itmo.sdcourse.snstat.vk.response;

import java.util.Optional;

public record VkNewsfeedSearchResponse(int count, Optional<String> nextFrom, long totalCount) implements VkResponse {
}
