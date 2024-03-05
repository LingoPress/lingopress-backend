package com.kidchang.lingopress.user;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "lingopress_user")
@Getter
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Integer id;

    // googleId
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
