package com.flowlinx.fix.server.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime locDate) {
        return (locDate == null ? null : Time.valueOf(locDate));
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlDate) {
        return (sqlDate == null ? null : sqlDate.toLocalTime());
    }

}
