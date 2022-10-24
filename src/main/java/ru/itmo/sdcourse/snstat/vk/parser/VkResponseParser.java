package ru.itmo.sdcourse.snstat.vk.parser;

import ru.itmo.sdcourse.snstat.vk.response.VkResponse;

public interface VkResponseParser<T extends VkResponse> {
     T parse(String apiResponse);
}
