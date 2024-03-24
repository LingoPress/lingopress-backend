package com.kidchang.lingopress.word;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<WordToLearn, Long> {

    List<WordToLearn> findAllByUserIdAndPressId(Long userId, Long pressId);

    List<WordToLearn> findAllByUserId(Long userId);
}
