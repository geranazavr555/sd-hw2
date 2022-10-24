package ru.itmo.sdcourse.vk.parser;

import com.google.common.io.Resources;
import com.google.gson.JsonSyntaxException;
import org.junit.Test;
import ru.itmo.sdcourse.snstat.vk.exception.VkException;
import ru.itmo.sdcourse.snstat.vk.parser.VkNewsfeedSearchResponseParser;
import ru.itmo.sdcourse.snstat.vk.response.VkNewsfeedSearchResponse;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class VkNewsfeedSearchResponseParserTest {
    @Test
    public void ok() {
        VkNewsfeedSearchResponseParser parser = new VkNewsfeedSearchResponseParser();
        VkNewsfeedSearchResponse result = parser.parse(readTest("Ok"));

        assertEquals(14, result.count());
        assertEquals(14, result.totalCount());
        assertTrue(result.nextFrom().isPresent());
        assertEquals("4/-36637883_9468", result.nextFrom().get());
    }

    @Test
    public void okNoNextFrom() {
        VkNewsfeedSearchResponseParser parser = new VkNewsfeedSearchResponseParser();
        VkNewsfeedSearchResponse result = parser.parse(readTest("OkNoNextFrom"));

        assertEquals(5, result.count());
        assertEquals(15, result.totalCount());
        assertFalse(result.nextFrom().isPresent());
    }

    @Test(expected = VkException.class)
    public void apiError() {
        VkNewsfeedSearchResponseParser parser = new VkNewsfeedSearchResponseParser();
        parser.parse(readTest("ApiError"));
    }

    @Test(expected = JsonSyntaxException.class)
    public void malformedJson() {
        VkNewsfeedSearchResponseParser parser = new VkNewsfeedSearchResponseParser();
        parser.parse(readTest("MalformedJson"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noFields() {
        VkNewsfeedSearchResponseParser parser = new VkNewsfeedSearchResponseParser();
        parser.parse(readTest("NoFields"));
    }

    private String readTest(String name) {
        URL resource = getClass().getResource("parserTest" + name + ".json");
        try {
            assert resource != null;
            return Resources.toString(resource, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
