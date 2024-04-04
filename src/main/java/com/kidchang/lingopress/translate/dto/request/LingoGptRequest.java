package com.kidchang.lingopress.translate.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LingoGptRequest {

    private String original_text;
    private String word;
}
