package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.PressContentLine;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressContentRepository extends JpaRepository<PressContentLine, Long> {

    List<PressContentLine> findAllByPressId(Long id);

    Optional<PressContentLine> findByPressIdAndLineNumber(Long pressId, Long aLong);
}
