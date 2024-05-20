package com.kidchang.lingopress.translate.dto.request;


import com.kidchang.lingopress._base.constant.LanguageEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LingoGptRequest {

    private String original_text;
    private String translated_text;
    private String word;
    private LanguageEnum target_language;
    private LanguageEnum user_language;
}
