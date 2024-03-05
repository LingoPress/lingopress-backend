package com.kidchang.lingopress.translate.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeepLResponse {

    private Translation[] translations;

    @Data
    @NoArgsConstructor
    public static class Translation {

        private String detected_source_language;
        private String text;
    }
}
