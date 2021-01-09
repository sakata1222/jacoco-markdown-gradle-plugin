package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import org.assertj.core.api.AbstractAssert;

public class CoverageAssert extends AbstractAssert<CoverageAssert, Coverage> {

  CoverageAssert(Coverage actual) {
    super(actual, CoverageAssert.class);
  }

  public CoverageAssert isEqualTo(Coverage expected) {
    if (!actual.getType().equals(expected.getType())) {
      failWithMessage("Expected \"type\" is \"%s\", but actual is \"%s\"",
          expected.getType(), actual.getType());
    }
    if (!(actual.getCovered() == expected.getCovered())) {
      failWithMessage(
          "Assertion for the type(%s) failed. Expected \"covered\" is \"%d\", but actual is \"%d\"",
          expected.getType(), expected.getCovered(), actual.getCovered());
    }
    if (!(actual.getMissed() == expected.getMissed())) {
      failWithMessage(
          "Assertion for the type(%s) failed. Expected type is \"%s\", but actual is \"%s\"",
          expected.getType(), expected.getMissed(), actual.getMissed());
    }
    return this;
  }
}
