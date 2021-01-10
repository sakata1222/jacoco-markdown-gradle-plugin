package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoverageDifferenceTest {

  @Test
  void can_not_create_different_types_difference() {
    Coverage typeA = new Coverage("typeA", 10, 10);
    Coverage typeB = new Coverage("typeB", 10, 10);
    assertThatThrownBy(() ->
        new CoverageDifference(typeA, typeB)
    ).isInstanceOf(IllegalArgumentException.class);
  }
}
