package com.newcubator.geosample.domain.field;

import java.util.UUID;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Polygon;
import org.geolatte.geom.PositionSequenceBuilders;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;

@DataJpaTest
@ContextConfiguration(initializers = FieldRepositoryTest.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FieldRepositoryTest {

    public static JdbcDatabaseContainer postgreSQLContainer = new PostgisContainerProvider()
        .newInstance()
        .withDatabaseName("integration-tests-db")
        .withUsername("sa")
        .withPassword("sa");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            postgreSQLContainer.start();
            TestPropertyValues.of(
                "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    FieldRepository fieldRepository;

    @Test
    void shouldValidateField() {
        Polygon geometry = new Polygon(
            PositionSequenceBuilders.variableSized(G2D.class)
                .add(5.8208837124389, 51.0596004663904)
                .add(5.83490292265498, 51.0571257015788)
                .add(5.87078646658134, 51.0451607414904)
                .add(5.79146302423308, 51.0612386272784)
                .add(5.8208837124389, 51.0596004663904)
                .toPositionSequence(),
            CoordinateReferenceSystems.WGS84
        );

        String testFieldName = "test field";
        Assertions.assertTrue(fieldRepository.isValid(testFieldName, geometry));
        fieldRepository.save(new Field(UUID.randomUUID(), testFieldName, geometry));

        Assertions.assertFalse(fieldRepository.isValid(testFieldName, geometry));
    }
}
