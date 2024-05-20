package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.press.entity.PressTranslationContentLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PressTranslationContentLineRepository extends JpaRepository<PressTranslationContentLine, Long> {
    Optional<PressTranslationContentLine> findByPressIdAndLineNumberAndTranslatedLanguage(Long pressId, Integer lineNumber, LanguageEnum translatedLanguage);
}
