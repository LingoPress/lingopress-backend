package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress.press.entity.PressContent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressContentRepository extends JpaRepository<PressContent, Long> {

    List<PressContent> getAllByPressId(Long id);
}
