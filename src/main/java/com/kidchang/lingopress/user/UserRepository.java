package com.kidchang.lingopress.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
