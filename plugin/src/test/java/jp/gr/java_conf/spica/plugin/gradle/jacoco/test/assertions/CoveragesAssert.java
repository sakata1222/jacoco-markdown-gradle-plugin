package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import org.assertj.core.api.AbstractAssert;

public class CoveragesAssert extends AbstractAssert<CoveragesAssert, Coverages> {

  CoveragesAssert(Coverages actual) {
    super(actual, CoveragesAssert.class);
  }

  public CoveragesAssert isEqualTo(Coverages expected) {
    Map<String, Coverage> actualCoverages = actual.typeToCoverage();
    Map<String, Coverage> expectedCoverages = expected.typeToCoverage();
    assertThat(actualCoverages.keySet())
        .isEqualTo(expectedCoverages.keySet());
    actualCoverages.entrySet().forEach(entry -> {
          Coverage e = expectedCoverages.get(entry.getKey());
          new CoverageAssert(entry.getValue())
              .isEqualTo(e);
        }
    );
    return this;
  }
}
