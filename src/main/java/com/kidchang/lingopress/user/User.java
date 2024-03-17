package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String role;

    @Builder
    public User(String username, String nickname, String password, String role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

}
