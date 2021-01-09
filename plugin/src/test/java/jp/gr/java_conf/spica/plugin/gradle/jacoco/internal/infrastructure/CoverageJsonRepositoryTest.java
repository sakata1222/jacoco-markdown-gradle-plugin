package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import static jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions.CustomAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.test.TestPaths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
    Coverages coverages = repository.readAll();
    Coverages expected = new Coverages(
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
  void writeAll() {
    File output = TestPaths.temp().resolve("jacocoSummary.json").toFile();
    CoverageJsonRepository repository = new CoverageJsonRepository(output);
    repository.writeAll(new Coverages(
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
