package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;

public class ExportRequest {

  private final boolean diffEnabled;
  private final boolean stdout;
  private final CoverageTypes targetTypes;

  public ExportRequest(boolean diffEnabled, boolean stdout,
      CoverageTypes targetTypes) {
    this.diffEnabled = diffEnabled;
    this.stdout = stdout;
    this.targetTypes = targetTypes;
  }

  public boolean diffEnabled() {
    return diffEnabled;
  }

  public boolean stdout() {
    return stdout;
  }

  public CoverageTypes getTargetTypes() {
    return targetTypes;
  }
}
