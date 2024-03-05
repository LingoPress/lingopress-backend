package com.kidchang.lingopress.jwt;

import com.kidchang.lingopress.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByUser(User user);

    RefreshToken findByUser(User user);
}
