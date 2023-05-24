package com.newcubator.geosample.entrypoints


import com.newcubator.geosample.IntegrationTest
import com.newcubator.geosample.domain.field.Field
import com.newcubator.geosample.domain.field.FieldRepository
import de.xm.yangyin.Comparisons
import org.geolatte.geom.G2D
import org.geolatte.geom.Polygon
import org.geolatte.geom.PositionSequenceBuilders
import org.geolatte.geom.crs.CoordinateReferenceSystems
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgisContainerProvider
import spock.lang.Shared

import static de.xm.yangyin.FileSnapshots.current
import static de.xm.yangyin.FileSnapshots.snapshot
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [Initializer.class])
class FieldControllerTest extends IntegrationTest {

  @Shared
  public static JdbcDatabaseContainer postgreSQLContainer = new PostgisContainerProvider()
    .newInstance()
    .withDatabaseName("integration-tests-db")
    .withUsername("sa")
    .withPassword("sa");


  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      postgreSQLContainer.start()
      TestPropertyValues.of(
        "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
        "spring.datasource.username=" + postgreSQLContainer.getUsername(),
        "spring.datasource.password=" + postgreSQLContainer.getPassword()
      ).applyTo(configurableApplicationContext.getEnvironment());
    }
  }

  @Autowired
  MockMvc mockMvc

  @Autowired
  FieldRepository fieldRepository

  def 'should get fields'() {
    given:
      fieldRepository.save(new Field(
        UUID.fromString('ad68f894-c16b-4953-b577-7cddb3e85ae5'), "initSampleField",
        new Polygon(
          PositionSequenceBuilders.variableSized(G2D.class)
            .add(5.8208837124389, 51.0596004663904)
            .add(5.83490292265498, 51.0571257015788)
            .add(5.87078646658134, 51.0451607414904)
            .add(5.79146302423308, 51.0612386272784)
            .add(5.8208837124389, 51.0596004663904)
            .toPositionSequence(),
          CoordinateReferenceSystems.WGS84
        )
      ))

    when:
      def response = mockMvc.perform(get(FieldController.MAPPING))
        .andReturn().response

    then:
      response.status == HttpStatus.OK.value()
      current(response.getJson(), Comparisons.JSON) == snapshot(response.getJson(), Comparisons.JSON)
  }

  @Transactional
  def 'should create a field'() {
    when:
      def response = mockMvc.perform(post(FieldController.MAPPING)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""{
            "id": "af569aa4-6505-460e-9804-7dd3b7724481",
            "name": "test-field",
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
        }"""))
        .andReturn().response

    then:
      response.status == HttpStatus.OK.value()
      current(response.getJson(), Comparisons.JSON) == snapshot(response.getJson(), Comparisons.JSON)
  }

  @Transactional
  def 'should fail to create a field with the same name'() {
    given:
      fieldRepository.save(new Field(
        UUID.fromString('ad68f894-c16b-4953-b577-7cddb3e85ae5'), "test-field",
        new Polygon(
          PositionSequenceBuilders.variableSized(G2D.class)
            .add(5.8208837124389, 51.0596004663904)
            .add(5.83490292265498, 51.0571257015788)
            .add(5.87078646658134, 51.0451607414904)
            .add(5.79146302423308, 51.0612386272784)
            .add(5.8208837124389, 51.0596004663904)
            .toPositionSequence(),
          CoordinateReferenceSystems.WGS84
        )
      ))

    when:
      def response = mockMvc.perform(post(FieldController.MAPPING)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""{
            "id": "af569aa4-6505-460e-9804-7dd3b7724481",
            "name": "test-field",
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
        }"""))
        .andReturn().response

    then:
      response.status == HttpStatus.BAD_REQUEST.value()
  }

  @Transactional
  def 'should fail to create a field with an overlapping geometry'() {
    given:
      fieldRepository.save(new Field(
        UUID.fromString('ad68f894-c16b-4953-b577-7cddb3e85ae5'), "some field name",
        new Polygon(
          PositionSequenceBuilders.variableSized(G2D.class)
            .add(5.6385371559564135, 51.54096478133687)
            .add(5.6385371559564135, 51.5390174083297)
            .add(5.642752217264388, 51.5390174083297)
            .add(5.642752217264388, 51.54096478133687)
            .add(5.6385371559564135, 51.54096478133687)
            .toPositionSequence(),
          CoordinateReferenceSystems.WGS84
        )
      ))

    when:
      def response = mockMvc.perform(post(FieldController.MAPPING)
        .contentType(MediaType.APPLICATION_JSON)
        .content("""{
            "id": "af569aa4-6505-460e-9804-7dd3b7724481",
            "name": "test-field",
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
        }"""))
        .andReturn().response

    then:
      response.status == HttpStatus.BAD_REQUEST.value()
  }
}
