package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JacocoJsonReport implements JacocoReport, JacocoCoverageRows {

  private final Object root;

  public JacocoJsonReport(Object root) {
    this.root = root;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, JacocoCoverage> summary() {
    return (Map<String, JacocoCoverage>) root;
  }

  @Override
  public List<JacocoCoverageRow> rows() {
    return summary().values().stream().collect(Collectors.toList());
  }
}
