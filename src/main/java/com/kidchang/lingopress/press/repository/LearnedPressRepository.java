package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearnedPressRepository extends JpaRepository<LearnedPress, Long> {

    Optional<LearnedPress> findByUserAndPress(User user, Press press);

    Slice<LearnedPress> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
}
