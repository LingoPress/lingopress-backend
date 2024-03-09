package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.LearnedPressContentLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnedPressContentLineRepository extends
    JpaRepository<LearnedPressContentLine, Integer> {

}
