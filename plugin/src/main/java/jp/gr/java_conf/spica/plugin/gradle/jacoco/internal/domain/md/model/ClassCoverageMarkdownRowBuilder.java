package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Arrays;
import java.util.List;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;

public class ClassCoverageMarkdownRowBuilder {

  private ClassCoverageMarkdownRowBuilder() {
  }

  public static MarkdownTableRow header() {
    return new MarkdownTableRow(Arrays.asList(
        MarkdownTableCol.header("Class"),
        MarkdownTableCol.header("Instructions(C0)"),
        MarkdownTableCol.header("Branches(C1)"))
    );
  }

  public static List<MarkdownTableColAlignment> alignments() {
    return Arrays.asList(
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.RIGHT,
        MarkdownTableColAlignment.RIGHT
    );
  }

  public static MarkdownTableRow classRow(ClassCoverage classCoverage) {
    return new MarkdownTableRow(Arrays.asList(
        new MarkdownTableCol(classCoverage.classCanonicalName()),
        new MarkdownTableCol(
            classCoverage.getInstructionCoverage().map(Coverage::missedPerTotalAndPercentage)
                .orElse("-")),
        new MarkdownTableCol(
            classCoverage.getBranchesCoverage().map(Coverage::missedPerTotalAndPercentage)
                .orElse("-")
        ))
    );
  }
}
