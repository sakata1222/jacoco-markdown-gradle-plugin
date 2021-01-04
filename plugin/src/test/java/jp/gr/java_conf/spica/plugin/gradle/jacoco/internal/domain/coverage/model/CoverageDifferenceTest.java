package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoverageDifferenceTest {

  @Test
  void can_not_create_different_types_difference() {
    assertThatThrownBy(() ->
        new CoverageDifference(
            new Coverage("typeA", 10, 10),
            new Coverage("typeB", 10, 10)
        )
    ).isInstanceOf(IllegalArgumentException.class);
  }
}
