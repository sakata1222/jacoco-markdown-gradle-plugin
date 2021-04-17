package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ClassNameRegexExcludeFilterTest {

  @Test
  void constructor_throws_Exception_when_argument_is_not_enclosed_hat_and_dollar() {
    ClassNameFilterString filterString = new ClassNameFilterString("foo.bar.MyClass");
    assertThatThrownBy(() ->
        new ClassNameRegexExcludeFilter(filterString))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void matches_return_true_when_matches() {
    assertThat(
        new ClassNameRegexExcludeFilter(new ClassNameFilterString("/foo.bar.MyClass/"))
            .matches(new ClassName("foo.bar.MyClass")))
        .isTrue();
  }

  @Test
  void matches_return_false_when_matches_partially() {
    assertThat(
        new ClassNameRegexExcludeFilter(new ClassNameFilterString("/foo.bar/"))
            .matches(new ClassName("foo.bar.MyClass")))
        .isFalse();
  }

  @Test
  void matches_return_true_when_regex_is_used() {
    assertThat(
        new ClassNameRegexExcludeFilter(new ClassNameFilterString("/foo.bar.*/"))
            .matches(new ClassName("foo.bar.MyClass")))
        .isTrue();
  }

  @Test
  void matches_return_true_when_full_regex_is_used() {
    assertThat(
        new ClassNameRegexExcludeFilter(new ClassNameFilterString("/foo.bar.*/"))
            .matches(new ClassName("foo.bar.MyClass")))
        .isTrue();
  }
}
