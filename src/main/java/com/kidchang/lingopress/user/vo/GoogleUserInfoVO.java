package com.kidchang.lingopress.user.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GoogleUserInfoVO {
    private String sub;
    private String email;
    private String name;

}
