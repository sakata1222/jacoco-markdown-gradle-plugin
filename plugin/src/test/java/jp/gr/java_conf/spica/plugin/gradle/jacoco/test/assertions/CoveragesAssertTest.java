package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import org.junit.jupiter.api.Test;

class CoveragesAssertTest {

  private final Coverages actual = new Coverages(
      Arrays.asList(
          new Coverage("type1", 10, 20),
          new Coverage("type2", 11, 21),
          new Coverage("type3", 12, 22)
      )
  );

  private final CoveragesAssert coveragesAssert = new CoveragesAssert(actual);

  @Test
  void isEqualTo_fail_when_keys_are_different() {
    assertThatThrownBy(() -> coveragesAssert.isEqualTo(
        new Coverages(
            Arrays.asList(
                new Coverage("type1", 10, 20),
                new Coverage("type2", 11, 21),
                new Coverage("type4", 12, 22)
            )
        )
    )).isInstanceOf(AssertionError.class);
  }

  @Test
  void isEqualTo_fail_when_value_is_different() {
    assertThatThrownBy(() -> coveragesAssert.isEqualTo(
        new Coverages(
            Arrays.asList(
                new Coverage("type1", 10, 20),
                new Coverage("type2", 11, 21),
                new Coverage("type3", 13, 22)
            )
        )
    ))
        .isInstanceOf(AssertionError.class)
        .hasMessage(
            "Assertion for the type(type3) failed."
                + " Expected \"covered\" is \"13\", but actual is \"12\"");
  }

}
