package com.newcubator.geosample

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class IntegrationTest extends Specification {
  static {
    MockHttpServletResponse.metaClass.getJson << { -> return new JsonSlurper().parseText(getContentAsString(StandardCharsets.UTF_8)) }
    MockHttpServletResponse.metaClass.getSlurped << { -> return new JsonSlurper().parseText(getContentAsString(StandardCharsets.UTF_8)) }
    MockHttpServletResponse.metaClass.getJsonPretty << { -> JsonOutput.prettyPrint(JsonOutput.toJson(json)) }
  }
}
