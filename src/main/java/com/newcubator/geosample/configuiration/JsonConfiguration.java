package com.newcubator.geosample.configuiration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.geolatte.geom.json.GeolatteGeomModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
public class JsonConfiguration {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        "uuuu-MM-dd'T'HH:mm:ss[.SSS][X]");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
        return builder -> {
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(ZonedDateTime.class,
                new ZonedDateTimeSerializer(dateTimeFormatter));

            builder.modulesToInstall(
                new GeolatteGeomModule(),
                javaTimeModule);

            builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.serializers(createDefaultListSerializer());
            builder.failOnUnknownProperties(false);
        };
    }

    private JsonSerializer<List> createDefaultListSerializer() {
        return new JsonSerializer<>() {

            @Override
            public Class<List> handledType() {
                return List.class;
            }

            @Override
            @SuppressWarnings("unchecked")
            public void serialize(List value, JsonGenerator gen, SerializerProvider provider) throws
                IOException {
                gen.writeStartArray(value);

                Objects.requireNonNullElse(value, Collections.emptyList())
                    .forEach(item -> {
                        try {
                            provider.defaultSerializeValue(item, gen);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                gen.writeEndArray();
            }
        };
    }
}
