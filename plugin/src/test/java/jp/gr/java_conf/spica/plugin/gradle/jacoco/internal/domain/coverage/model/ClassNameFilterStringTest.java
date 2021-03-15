package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassNameFilterStringTest {

  @Test
  void isRegex_returns_true() {
    assertThat(new ClassNameFilterString("^a$").isRegex()).isTrue();
  }

  @Test
  void isRegex_returns_false_when_only_hat_is_contained() {
    assertThat(new ClassNameFilterString("^aa").isRegex()).isFalse();
  }

  @Test
  void isRegex_returns_false_when_dollar_is_contained() {
    assertThat(new ClassNameFilterString("aa$").isRegex()).isFalse();
  }

  @Test
  void isRegex_returns_false_when_string_is_empty() {
    assertThat(new ClassNameFilterString("").isRegex()).isFalse();
  }
}
