package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import org.assertj.core.api.AbstractAssert;

public class ClassToCoverageAssert extends AbstractAssert<ClassToCoverageAssert, ClassCoverages> {

  public ClassToCoverageAssert(
      ClassCoverages classCoverages) {
    super(classCoverages, ClassToCoverageAssert.class);
  }

  public ClassToCoverageAssert isEqualTo(ClassCoverages expected) {
    Map<String, Coverages> actualMap = actual.classCanonicalNameToCoveragesMap();
    Map<String, Coverages> expectedMap = expected.classCanonicalNameToCoveragesMap();

    assertThat(actualMap.keySet()).isEqualTo(expectedMap.keySet());
    actualMap.forEach((k, v) ->
        new CoveragesAssert(v).isEqualTo(expectedMap.get(k)));
    return this;
  }
}
