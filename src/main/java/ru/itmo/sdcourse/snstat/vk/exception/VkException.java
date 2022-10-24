package ru.itmo.sdcourse.snstat.vk.exception;

public class VkException extends RuntimeException {
    public VkException(String message) {
        super(message);
    }

    public VkException(String message, Throwable cause) {
        super(message, cause);
    }

    public VkException(Throwable cause) {
        super(cause);
    }
}
