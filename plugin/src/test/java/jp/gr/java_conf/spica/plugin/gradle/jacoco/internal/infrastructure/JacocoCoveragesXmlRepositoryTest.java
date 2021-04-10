package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import static jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions.CustomAssertions.assertThat;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassName;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JacocoCoveragesXmlRepositoryTest {

  private JacocoCoveragesXmlRepository repository;

  @BeforeEach
  void beforeEach() throws ParserConfigurationException {
    repository = new JacocoCoveragesXmlRepository(
        DocumentBuilderFactory.newInstance(),
        new File(this.getClass().getResource("/sample.xml").getFile()));
  }

  @Test
  void readAll() {
    Map<ClassName, Coverages> expectedClassToCoverages = new TreeMap<>(
        ClassName.CANONICAL_NAME_COMPARATOR);
    expectedClassToCoverages.put(
        new ClassName("jp.gr.java_conf.saka.github.actions.sandbox.list.LinkedList$Node"),
        new Coverages(Arrays.asList(
            new Coverage("INSTRUCTION", 6, 0),
            new Coverage("LINE", 3, 0),
            new Coverage("COMPLEXITY", 1, 0),
            new Coverage("METHOD", 1, 0),
            new Coverage("CLASS", 1, 0)
        ))
    );
    expectedClassToCoverages.put(
        new ClassName("jp.gr.java_conf.saka.github.actions.sandbox.list.LinkedList"),
        new Coverages(Arrays.asList(
            new Coverage("INSTRUCTION", 119, 0),
            new Coverage("BRANCH", 20, 2),
            new Coverage("LINE", 32, 0),
            new Coverage("COMPLEXITY", 16, 2),
            new Coverage("METHOD", 7, 0),
            new Coverage("CLASS", 1, 0)
        ))
    );
    expectedClassToCoverages.put(
        new ClassName("jp.gr.java_conf.saka.github.actions.sandbox.utilities.StringUtils"),
        new Coverages(Arrays.asList(
            new Coverage("INSTRUCTION", 6, 3),
            new Coverage("LINE", 2, 1),
            new Coverage("COMPLEXITY", 2, 1),
            new Coverage("METHOD", 2, 1),
            new Coverage("CLASS", 1, 0)
        ))
    );
    CoverageReport report = repository.readAll();
    assertThat(report).isEqualTo(new CoverageReport(
        new CoverageSummary(Arrays.asList(
            new Coverage("INSTRUCTION", 230, 15),
            new Coverage("BRANCH", 31, 3),
            new Coverage("LINE", 64, 5),
            new Coverage("COMPLEXITY", 30, 8),
            new Coverage("METHOD", 16, 5),
            new Coverage("CLASS", 7, 0)
        )),
        new ClassCoverages(
            expectedClassToCoverages
        ))
    );
  }
}
