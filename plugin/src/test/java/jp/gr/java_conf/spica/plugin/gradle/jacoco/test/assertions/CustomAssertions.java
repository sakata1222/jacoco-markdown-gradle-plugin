package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;

public class CustomAssertions {

  private CustomAssertions() {
  }

  public static CoveragesAssert assertThat(Coverages actual) {
    return new CoveragesAssert(actual);
  }
}
