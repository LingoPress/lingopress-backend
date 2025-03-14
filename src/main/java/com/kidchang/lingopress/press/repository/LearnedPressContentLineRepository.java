package com.kidchang.lingopress.press.repository;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.press.dto.response.PressContentLineResponse;
import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.LearnedPressContentLine;
import com.kidchang.lingopress.press.entity.PressContentLine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearnedPressContentLineRepository extends
        JpaRepository<LearnedPressContentLine, Integer> {

    Optional<LearnedPressContentLine> findByLearnedPressAndPressContentLine(
            LearnedPress learnedPress,
            PressContentLine pressContentLine);

// 내가 짠 코드
//    @Query(
//        "SELECT new com.kidchang.lingopress.press.dto.response.PressContentLineResponse(pcl.id, pcl.lineNumber, lpcl.userTranslatedLine, pcl.translatedLineText, pcl.lineText, lpcl.isCorrect) "
//            + "FROM PressContentLine AS pcl "
//            + "left outer JOIN LearnedPressContentLine AS lpcl ON pcl.press.id = :pressId  "
//            + "WHERE lpcl.user.id = :userId AND lpcl.pressContentLineNumber = pcl.lineNumber")
// gpt 수정 원본
//    @Query(
//        "SELECT new com.kidchang.lingopress.press.dto.response.PressContentLineResponse(pcl.id, pcl.lineNumber, lpcl.userTranslatedLine, pcl.translatedLineText, pcl.lineText, CASE WHEN lpcl.id IS NULL THEN false ELSE lpcl.isCorrect END) "
//            + "FROM PressContentLine AS pcl "
//            + "LEFT OUTER JOIN LearnedPressContentLine AS lpcl ON lpcl.pressContentLineNumber = pcl.lineNumber AND lpcl.user.id = :userId "
//            + "WHERE pcl.press.id = :pressId")

    // gpt 수정 수정본
    @Query(
            "SELECT new com.kidchang.lingopress.press.dto.response.PressContentLineResponse(pcl.id, pcl.lineNumber, lpcl.userTranslatedLine, ptcl.translatedLineContent, pcl.lineText, lpcl.isCorrect, lpcl.memo, pcl.press.id, pcl.startSecond, pcl.endSecond) "
                    + "FROM PressContentLine AS pcl "
                    + "LEFT OUTER JOIN LearnedPressContentLine AS lpcl ON lpcl.lineNumber = pcl.lineNumber AND lpcl.user.id = :userId AND lpcl.press.id = :pressId "
                    + "LEFT OUTER JOIN PressTranslationContentLine AS ptcl ON ptcl.lineNumber = pcl.lineNumber AND ptcl.press.id = pcl.press.id AND ptcl.translatedLanguage = :userLanguage "
                    + "WHERE pcl.press.id = :pressId "
                    + "ORDER BY pcl.lineNumber ASC")
    List<PressContentLineResponse> findByUserAndPressAndPressContent(@Param("userId") Long user,
                                                                     @Param("pressId") Long pressId, @Param("userLanguage") LanguageEnum userLanguage);


    //해당 유저가 작성한 learnedPressContentLine 중 메모가 작성된 것만 가져온다.
    @Query("SELECT lpcl FROM LearnedPressContentLine AS lpcl "
            + "WHERE lpcl.user.id = :userId AND lpcl.memo IS NOT NULL OR lpcl.memo != ''")
    Slice<LearnedPressContentLine> findByUserAndMemoIsNotNull(@Param("userId") Long userId, Pageable pageable);
}