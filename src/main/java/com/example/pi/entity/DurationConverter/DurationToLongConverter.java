package com.example.pi.entity.DurationConverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = false)
public class DurationToLongConverter implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return (duration == null) ? null : duration.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return (dbData == null) ? null : Duration.ofMinutes(dbData);
    }
}