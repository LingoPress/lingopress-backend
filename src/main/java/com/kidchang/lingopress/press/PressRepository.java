package com.kidchang.lingopress.press;

import com.kidchang.lingopress._base.constant.CategoryEnum;
import com.kidchang.lingopress._base.constant.LanguageEnum;
import com.kidchang.lingopress.press.dto.response.PressResponse;
import com.kidchang.lingopress.press.entity.Press;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PressRepository extends JpaRepository<Press, Long> {

    @Query(
            "SELECT new com.kidchang.lingopress.press.dto.response.PressResponse(p.id, p.title, p.content, p.imageUrl, p.originalUrl, p.totalContentLine, p.rating, p.publishedAt, pt.translatedTitle, p.author, p.publisher, pt.translatedLanguage, p.category) "
                    + " FROM Press AS p LEFT OUTER JOIN PressTranslation AS pt ON p.id = pt.press.id WHERE p.category = :category AND p.language = :targetLanguage AND pt.translatedLanguage = :userLanguage")
    Slice<PressResponse> findAllByTargetLanguageAndUserLanguage(LanguageEnum targetLanguage, LanguageEnum userLanguage, Pageable pageable, CategoryEnum category);

    @Query(
            "SELECT new com.kidchang.lingopress.press.dto.response.PressResponse(p.id, p.title, p.content, p.imageUrl, p.originalUrl, p.totalContentLine, p.rating, p.publishedAt, pt.translatedTitle, p.author, p.publisher, pt.translatedLanguage, p.category) "
                    + " FROM Press AS p LEFT OUTER JOIN PressTranslation AS pt ON p.id = pt.press.id WHERE p.category = :category AND pt.translatedLanguage = :userLanguage AND p.accessLevel = 'public'")
    Slice<PressResponse> findAllByUserLanguage(LanguageEnum userLanguage, Pageable pageable, CategoryEnum category);

    @Query(
            "SELECT new com.kidchang.lingopress.press.dto.response.PressResponse(p.id, p.title, p.content, p.imageUrl, p.originalUrl, p.totalContentLine, p.rating, p.publishedAt, pt.translatedTitle, p.author, p.publisher, pt.translatedLanguage, p.category) "
                    + " FROM Press AS p LEFT OUTER JOIN PressTranslation AS pt ON p.id = pt.press.id WHERE p.id = :pressId AND pt.translatedLanguage = :userLanguage")
    Optional<PressResponse> findByIdAndUserLanguage(Long pressId, LanguageEnum userLanguage);

    @Query(
            "SELECT new com.kidchang.lingopress.press.dto.response.PressResponse(p.id, p.title, p.content, p.imageUrl, p.originalUrl, p.totalContentLine, p.rating, p.publishedAt, pt.translatedTitle, p.author, p.publisher, pt.translatedLanguage, p.category) "
                    + " FROM Press AS p LEFT OUTER JOIN PressTranslation AS pt ON p.id = pt.press.id LEFT JOIN p.owners po WHERE p.category = :category AND pt.translatedLanguage = :userLanguage AND po.id = :userId")
    Slice<PressResponse> findAllByTargetLanguageAndUserLanguageAndUserId(LanguageEnum userLanguage, Pageable pageable, CategoryEnum category, Long userId);


//    @Query(
//            "SELECT new com.kidchang.lingopress.press.dto.response.PressResponse(p.id, p.title, p.content, p.imageUrl, p.originalUrl, p.totalContentLine, p.rating, p.publishedAt, pt.translatedTitle, p.author, p.publisher, pt.translatedLanguage, p.category) "
//                    + " FROM Press AS p LEFT OUTER JOIN PressTranslation AS pt ON p.id = pt.press.id LEFT JOIN p.owners po WHERE p.category = :category AND p.language = :targetLanguage AND pt.translatedLanguage = :userLanguage AND po.id = :userId")
//    Slice<PressResponse> findAllByTargetLanguageAndUserLanguageAndUserId(LanguageEnum targetLanguage, LanguageEnum userLanguage, Pageable pageable, CategoryEnum category, Long userId);
}
