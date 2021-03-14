package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverageLimit;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;

public class ExportRequest {

  private final boolean diffEnabled;
  private final boolean stdout;
  private final boolean classListEnabled;
  private final ClassCoverageLimit classCoverageLimit;
  private final CoverageTypes targetTypes;
  private final boolean outputJson;
  private final boolean outputMd;

  public ExportRequest(boolean diffEnabled, boolean stdout, boolean classListEnabled,
      ClassCoverageLimit classCoverageLimit,
      CoverageTypes targetTypes, boolean outputJson, boolean outputMd) {
    this.diffEnabled = diffEnabled;
    this.stdout = stdout;
    this.classListEnabled = classListEnabled;
    this.classCoverageLimit = classCoverageLimit;
    this.targetTypes = targetTypes;
    this.outputJson = outputJson;
    this.outputMd = outputMd;
  }

  public boolean diffEnabled() {
    return diffEnabled;
  }

  public boolean stdout() {
    return stdout;
  }

  public boolean classListEnabled() {
    return classListEnabled;
  }

  public ClassCoverageLimit classCoverageLimit() {
    return classCoverageLimit;
  }

  public CoverageTypes getTargetTypes() {
    return targetTypes;
  }

  public boolean outputJson() {
    return outputJson;
  }

  public boolean outputMd() {
    return outputMd;
  }
}
