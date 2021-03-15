package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ClassNameFilterStringTest {

  @Test
  void isRegex_returns_true() {
    assertThat(new ClassNameFilterString("/a/").isRegex()).isTrue();
  }

  @Test
  void regex_returns_regex() {
    assertThat(new ClassNameFilterString("/a/").regex()).isEqualTo("a");
  }

  @Test
  void isRegex_returns_false_when_only_hat_is_contained() {
    assertThat(new ClassNameFilterString("/aa").isRegex()).isFalse();
  }

  @Test
  void regex_throws_Exception_when_the_string_is_not_regex() {
    assertThatThrownBy(() -> new ClassNameFilterString("/aa").regex())
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void isRegex_returns_false_when_dollar_is_contained() {
    assertThat(new ClassNameFilterString("aa/").isRegex()).isFalse();
  }

  @Test
  void isRegex_returns_false_when_string_is_empty() {
    assertThat(new ClassNameFilterString("").isRegex()).isFalse();
  }
}
