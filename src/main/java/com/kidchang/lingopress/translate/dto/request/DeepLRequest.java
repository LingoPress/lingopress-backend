package com.kidchang.lingopress.translate.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeepLRequest {

    private String[] text;
    private String target_lang;

}
