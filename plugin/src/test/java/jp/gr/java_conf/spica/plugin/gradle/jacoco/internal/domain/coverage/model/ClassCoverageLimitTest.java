package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassCoverageLimitTest {

  @Test
  void limit_returns_positive_value_when_input_is_positive() {
    assertThat(new ClassCoverageLimit(1).limit()).isEqualTo(1);
  }

  @Test
  void limit_returns_max_when_input_is_zero() {
    assertThat(new ClassCoverageLimit(0).limit()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  void limit_returns_max_when_input_is_negative() {
    assertThat(new ClassCoverageLimit(-1).limit()).isEqualTo(Integer.MAX_VALUE);
  }
}
