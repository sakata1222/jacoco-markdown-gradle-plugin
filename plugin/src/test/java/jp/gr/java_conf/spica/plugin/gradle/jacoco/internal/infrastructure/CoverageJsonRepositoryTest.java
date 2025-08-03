package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import static jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions.CustomAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.test.TestPaths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoverageJsonRepositoryTest {

  @BeforeAll
  static void beforeAll() {
    TestPaths.initTemp();
  }

  @AfterAll
  static void afterAll() {
    TestPaths.removeTemp();
  }

  @Test
  void readAll() {
    CoverageJsonRepository repository = new CoverageJsonRepository(
        TestPaths.testData().resolve("jacocoSummary.json").toFile());
    CoverageSummary coverages = repository.readAll();
    CoverageSummary expected = new CoverageSummary(
        Arrays.asList(
            new Coverage("INSTRUCTION", 230, 15),
            new Coverage("BRANCH", 31, 3),
            new Coverage("LINE", 64, 5),
            new Coverage("COMPLEXITY", 30, 8),
            new Coverage("METHOD", 16, 5),
            new Coverage("CLASS", 7, 0)
        )
    );
    assertThat(coverages)
        .isEqualTo(expected);
  }

  @Test
  @DisplayName("plugin version v1.4.0 or earlier write empty summary with Gradle 9,"
      + " so newer plugin should be able to read it")
  void readAllEmpty() {
    CoverageJsonRepository repository = new CoverageJsonRepository(
        TestPaths.testData().resolve("jacocoSummaryEmpty.json").toFile());
    CoverageSummary coverages = repository.readAll();
    CoverageSummary expected = new CoverageSummary(new ArrayList<>());
    assertThat(coverages)
        .isEqualTo(expected);
  }

  @Test
  @DisplayName("Just for test, read all throws detailed exception")
  void readAllThrowsDetailedException() {
    CoverageJsonRepository repository = new CoverageJsonRepository(
        TestPaths.testData().resolve("jacocoSummaryInvalid.json").toFile());
    // BEGIN LONG LINE
    assertThatThrownBy(() -> repository.readAll())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            "Unexpected error occurred. Input {INSTRUCTION={missed=15, covered=230}, BRANCH={missed=3, covered=31}}")
        .cause()
        .hasMessageContaining(
            "Duplicated keys are detected. Values of the duplicated key:Coverage{type='null', covered=230, missed=15}, Coverage{type='null', covered=31, missed=3}");
    // END LONG LINE
  }

  @Test
  void writeAll() {
    File output = TestPaths.temp().resolve("jacocoSummary.json").toFile();
    CoverageJsonRepository repository = new CoverageJsonRepository(output);
    repository.writeAll(new CoverageSummary(
        Arrays.asList(
            new Coverage("INSTRUCTION", 230, 15),
            new Coverage("BRANCH", 31, 3),
            new Coverage("LINE", 64, 5),
            new Coverage("COMPLEXITY", 30, 8),
            new Coverage("METHOD", 16, 5),
            new Coverage("CLASS", 7, 0)
        ))
    );
    assertThat(output)
        .hasSameTextualContentAs(TestPaths.testData().resolve("jacocoSummary.json").toFile(),
            StandardCharsets.UTF_8);
  }
}
