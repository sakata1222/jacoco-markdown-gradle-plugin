package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class CoverageAssertTest {

  @Test
  void isEqualTo_success() {
    CoverageAssert coverageAssert = new CoverageAssert(new Coverage("dummy", 10, 20));
    assertThatCode(
        () -> coverageAssert.isEqualTo(new Coverage("dummy", 10, 20))
    ).doesNotThrowAnyException();
  }

  @TestFactory
  Stream<DynamicTest> isEqualTo() {
    Map<Coverage, String> targetToErrorMsg = new LinkedHashMap<>();
    targetToErrorMsg.put(new Coverage("dummY", 10, 20),
        "Expected \"type\" is \"dummY\", but actual is \"dummy\"");
    targetToErrorMsg.put(new Coverage("dummy", 11, 20),
        "Assertion for the type(dummy) failed. "
            + "Expected \"covered\" is \"11\", but actual is \"10\"");
    targetToErrorMsg.put(new Coverage("dummy", 10, 21),
        "Assertion for the type(dummy) failed. "
            + "Expected type is \"21\", but actual is \"20\"");
    assertThat(targetToErrorMsg).hasSize(3);
    CoverageAssert coverageAssert = new CoverageAssert(new Coverage("dummy", 10, 20));
    return targetToErrorMsg.entrySet().stream().map(
        e ->
            dynamicTest(
                "Expected error msg:" + e.getValue(),
                () -> {
                  Coverage mismatchExpected = e.getKey();
                  assertThatThrownBy(() ->
                      coverageAssert.isEqualTo(mismatchExpected))
                      .isInstanceOf(AssertionError.class)
                      .hasMessage(e.getValue());
                }
            )
    );
  }
}
