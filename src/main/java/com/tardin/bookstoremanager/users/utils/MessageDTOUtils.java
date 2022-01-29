package com.tardin.bookstoremanager.users.utils;

import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.entity.User;

public class MessageDTOUtils {

    private MessageDTOUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static MessageDTO createdMessage(User createdUser) {
        return returnMessage(createdUser, "created");
    }

    public static MessageDTO updatedMessage(User updatedUser) {
        return returnMessage(updatedUser, "updated");
    }

    private static MessageDTO returnMessage(User user, String action) {
        String updatedUsername = user.getUsername();
        Long createdId = user.getId();
        var userCreatedMessage = String.format("User %s with ID %s successfully %s", updatedUsername, createdId, action);
        return MessageDTO.builder()
                .message(userCreatedMessage)
                .build();
    }
}
