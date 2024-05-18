package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@Entity
@Table(name = "lingopress_user")
@Data
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // provider + id
    @Column(unique = true)
    @Schema(description = "provider + id로 구성된 유저 아이디", example = "g_1234567890")
    private String username;

    // 이름
    private String nickname;
    private String password;
    private String email;

    @Schema(description = "ROLE", example = "ROLE_USER")
    private String role;

    // provider : google이 들어감
    private String provider;

    // providerId : 구글 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Schema(description = "유저 상태", example = "ACTIVE")
    @ColumnDefault("'ACTIVE'")
    private UserStatusEnum status = UserStatusEnum.ACTIVE;

    @Schema(description = "유저의 언어", example = "ko")
    @Enumerated(EnumType.STRING)
    private LanguageEnum user_language = LanguageEnum.KOREAN;

    @Schema(description = "공부하길 원하는 언어", example = "en")
    @Enumerated(EnumType.STRING)
    private LanguageEnum target_language = LanguageEnum.ENGLISH;


    @Builder
    public User(String username, String nickname, String password, String role, String provider, String providerId, String email) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
    }

}
