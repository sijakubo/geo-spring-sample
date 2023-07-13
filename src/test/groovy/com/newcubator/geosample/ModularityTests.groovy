package com.newcubator.geosample

import org.springframework.modulith.core.ApplicationModules
import spock.lang.Specification

class ModularityTests extends Specification {

  def "should be modular"() {
    expect:
      ApplicationModules.of(GeoSampleApplication.class).verify();
  }
}
