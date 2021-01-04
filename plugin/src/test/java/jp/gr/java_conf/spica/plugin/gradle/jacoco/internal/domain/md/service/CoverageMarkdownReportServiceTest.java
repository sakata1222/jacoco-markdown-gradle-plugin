package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdown;
import org.junit.jupiter.api.Test;

class CoverageMarkdownReportServiceTest {

  @Test
  void currentReport() {
    Coverages coverages = new Coverages(
        Arrays.asList(
            new Coverage("type1", 10, 10),
            new Coverage("type2", 5, 15),
            new Coverage("type3", 15, 5)
        )
    );
    CoverageTypes types = new CoverageTypes(Arrays.asList("type1", "type3"));
    CoverageMarkdownReportService service = new CoverageMarkdownReportService();
    CoverageMarkdown md = service.currentReport(types, coverages);
    assertThat(md.toMarkdown()).isEqualTo(""
        + "|Type |Missed/Total|Coverage|\n"
        + "|:--- |        ---:|    ---:|\n"
        + "|type1|       10/20|  50.00%|\n"
        + "|type3|        5/20|  75.00%|\n");
  }

  @Test
  void differenceReport() {
    Coverages current = new Coverages(
        Arrays.asList(
            new Coverage("type1", 15, 5),
            new Coverage("type2", 5, 15),
            new Coverage("type3", 15, 5)
        )
    );
    Coverages previous = new Coverages(
        Arrays.asList(
            new Coverage("type1", 10, 10),
            new Coverage("type2", 5, 15)
        )
    );
    CoverageTypes types = new CoverageTypes(Arrays.asList("type1", "type2", "type3"));
    CoverageMarkdownReportService service = new CoverageMarkdownReportService();
    CoverageMarkdown md = service.differenceReport(types, previous, current);
    assertThat(md.toMarkdown()).isEqualTo(""
        + "|Type |      Missed/Total|           Coverage|\n"
        + "|:--- |              ---:|               ---:|\n"
        + "|type1|    ~~10/20~~ 5/20|   ~~50.00~~ 75.00%|\n"
        + "|type2|(Not Changed)15/20|(Not Changed)25.00%|\n"
        + "|type3|              5/20|             75.00%|\n");
  }
}
