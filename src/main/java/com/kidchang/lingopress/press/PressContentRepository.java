package com.kidchang.lingopress.press;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PressContentRepository extends JpaRepository<PressContent, Long> {

}
