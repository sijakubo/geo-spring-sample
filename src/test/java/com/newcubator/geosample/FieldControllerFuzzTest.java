package com.newcubator.geosample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import com.newcubator.geosample.entrypoints.FieldController;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = FieldControllerFuzzTest.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FieldControllerFuzzTest {

    @Autowired
    private MockMvc mockMvc;

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

    @FuzzTest
    public void fuzzTestHello(FuzzedDataProvider data) throws Exception {
        String name = data.consumeRemainingAsString();

        MockHttpServletResponse response = mockMvc.perform(post(FieldController.MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "id": "af569aa4-6505-460e-9804-7dd3b7724481",
                        "name": "%s",
                        "geometry": {
                            "type": "Polygon",
                            "coordinates": [[
                                [5.636421388870787, 51.540107133536],
                                [5.6366004895073445, 51.539749496736675],
                                [5.636609915874317, 51.5392159677649],
                                [5.636572210454403, 51.538336510750554],
                                [5.640814067888925, 51.537650522482465],
                                [5.640474719301864, 51.54027129374816],
                                [5.636421388870787, 51.540107133536]
                            ]]
                        }
                    }""".formatted(name)))
            .andReturn().getResponse();

        Assertions.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}
