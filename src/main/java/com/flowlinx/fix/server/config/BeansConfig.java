package com.flowlinx.fix.server.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.classmap.CopyByReference;
import org.dozer.classmap.MappingFileData;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@EnableScheduling
@Configuration
public class BeansConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        return objectMapper;
    }

    @Bean(name = "org.dozer.Mapper")
    public Mapper dozerBeanMapper() {
        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(beanMappingBuilder());
        return dozerBeanMapper;
    }

    public BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {

            @Override
            protected void configure() {
            }

            @Override
            public MappingFileData build() {
                MappingFileData data = super.build();
                org.dozer.classmap.Configuration configuration = data.getConfiguration();
                if (configuration == null) {
                    configuration = new org.dozer.classmap.Configuration();
                    data.setConfiguration(configuration);
                }

                configuration.getCopyByReferences().add(new CopyByReference(LocalDate.class.getCanonicalName()));
                configuration.getCopyByReferences().add(new CopyByReference(LocalTime.class.getCanonicalName()));
                configuration.getCopyByReferences().add(new CopyByReference(LocalDateTime.class.getCanonicalName()));
                return data;
            }
        };
    }

    @Autowired
    private void registerSerializersDeserializers(List<ObjectMapper> objectMappers) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
        simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        objectMappers.forEach(objectMapper -> objectMapper.registerModule(simpleModule));
    }

}