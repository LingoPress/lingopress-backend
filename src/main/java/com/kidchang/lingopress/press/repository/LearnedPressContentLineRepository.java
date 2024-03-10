package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.LearnedPressContentLine;
import com.kidchang.lingopress.press.entity.PressContentLine;
import com.kidchang.lingopress.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnedPressContentLineRepository extends
    JpaRepository<LearnedPressContentLine, Integer> {

    Optional<LearnedPressContentLine> findByLearnedPressAndPressContent(LearnedPress learnedPress,
        PressContentLine pressContent);

    List<LearnedPressContentLine> findByUserAndLearnedPress(User user,
        LearnedPress learnedPress);
}
