package com.kidchang.lingopress.translate.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslateTextResponse {

    private String translatedText;

}
