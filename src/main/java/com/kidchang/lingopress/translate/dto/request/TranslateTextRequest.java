package com.kidchang.lingopress.translate.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TranslateTextRequest {

    private String originalText;

}
