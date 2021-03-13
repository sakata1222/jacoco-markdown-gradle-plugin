package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverageLimit;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.ClassCoverageMarkdownRowBuilder;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.ClassCoverageMarkdownTable;

public class ClassCoverageMarkdownReportService {

  public ClassCoverageMarkdownTable report(ClassCoverages classCoverages,
      ClassCoverageLimit limit) {
    ClassCoverageMarkdownTable md = new ClassCoverageMarkdownTable();
    classCoverages.branchMissedWorstN(limit.limit()).stream()
        .map(ClassCoverageMarkdownRowBuilder::classRow)
        .forEach(md::addRow);
    return md;
  }
}
