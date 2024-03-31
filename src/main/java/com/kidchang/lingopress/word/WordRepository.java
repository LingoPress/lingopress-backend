package com.kidchang.lingopress.word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<WordToLearn, Long> {

    List<WordToLearn> findAllByUserIdAndPressIdOrderById(Long userId, Long pressId);

    List<WordToLearn> findAllByUserId(Long userId);
}
