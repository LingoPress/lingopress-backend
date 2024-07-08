package com.kidchang.lingopress.mail;

import lombok.Builder;

@Builder
public record EmailMessage(
        String to,
        String subject,
        String message
) {
}
