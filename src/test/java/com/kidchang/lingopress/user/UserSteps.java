package com.kidchang.lingopress.user;

import org.jetbrains.annotations.NotNull;

public class UserSteps {
    @NotNull
    public static User getNewUser() {
        return new User(
                1L,
                "test",
                "test",
                "test",
                "test",
                "ROLE_USER",
                "test",
                "test",
                UserStatusEnum.ACTIVE
        );
    }
}