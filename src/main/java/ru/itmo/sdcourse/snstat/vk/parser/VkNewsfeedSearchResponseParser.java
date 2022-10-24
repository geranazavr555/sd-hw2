package ru.itmo.sdcourse.snstat.vk.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.Validate;
import ru.itmo.sdcourse.snstat.vk.exception.VkException;
import ru.itmo.sdcourse.snstat.vk.response.VkNewsfeedSearchResponse;

import java.util.Optional;

public class VkNewsfeedSearchResponseParser implements VkResponseParser<VkNewsfeedSearchResponse> {
    private static final String RESPONSE_MEMBER_NAME = "response";
    private static final String COUNT_MEMBER_NAME = "count";
    private static final String NEXT_FROM_MEMBER_NAME = "next_from";
    private static final String TOTAL_COUNT_MEMBER_NAME = "total_count";

    private static final String ERROR_MEMBER_NAME = "error";
    private static final String ERROR_MESSAGE_MEMBER_NAME = "error_msg";

    @Override
    public VkNewsfeedSearchResponse parse(String apiResponse) {
        JsonElement rootJsonElement = JsonParser.parseString(apiResponse);
        JsonObject rootJsonObject = rootJsonElement.getAsJsonObject();
        if (!rootJsonObject.has(RESPONSE_MEMBER_NAME))
            throwApiException(rootJsonObject);

        JsonObject jsonObject = rootJsonObject.getAsJsonObject(RESPONSE_MEMBER_NAME);
        checkHasMember(jsonObject, COUNT_MEMBER_NAME);
        checkHasMember(jsonObject, TOTAL_COUNT_MEMBER_NAME);

        int count = jsonObject.getAsJsonPrimitive(COUNT_MEMBER_NAME).getAsInt();
        long totalCount = jsonObject.getAsJsonPrimitive(TOTAL_COUNT_MEMBER_NAME).getAsLong();

        if (!jsonObject.has(NEXT_FROM_MEMBER_NAME))
           return new VkNewsfeedSearchResponse(count, Optional.empty(), totalCount);

        String nextFrom = jsonObject.getAsJsonPrimitive(NEXT_FROM_MEMBER_NAME).getAsString();
        return new VkNewsfeedSearchResponse(count, Optional.of(nextFrom), totalCount);
    }

    private void throwApiException(JsonObject rootJsonObject) {
        checkHasMember(rootJsonObject, ERROR_MEMBER_NAME);
        JsonObject errorJsonObject = rootJsonObject.getAsJsonObject(ERROR_MEMBER_NAME);
        checkHasMember(errorJsonObject, ERROR_MESSAGE_MEMBER_NAME);
        throw new VkException(errorJsonObject.getAsJsonPrimitive(ERROR_MESSAGE_MEMBER_NAME).getAsString());
    }

    private void checkHasMember(JsonObject jsonObject, String member) {
        Validate.isTrue(jsonObject.has(member), "Expected '%s' in json", member);
    }
}
