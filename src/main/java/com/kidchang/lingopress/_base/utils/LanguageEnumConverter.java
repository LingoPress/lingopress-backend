package com.kidchang.lingopress._base.utils;

import com.kidchang.lingopress._base.constant.LanguageEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LanguageEnumConverter implements AttributeConverter<LanguageEnum, String> {

    @Override
    public String convertToDatabaseColumn(LanguageEnum attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public LanguageEnum convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        for (LanguageEnum language : LanguageEnum.values()) {
            if (language.getValue().equalsIgnoreCase(dbData)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}
