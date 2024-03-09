package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnedPressRepository extends JpaRepository<LearnedPress, Long> {

    Optional<LearnedPress> findByUserAndPress(User user, Press press);
}
