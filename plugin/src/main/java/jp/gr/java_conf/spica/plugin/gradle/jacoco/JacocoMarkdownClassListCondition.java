package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import groovy.lang.Closure;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.gradle.util.Configurable;

public class JacocoMarkdownClassListCondition implements Serializable,
    Configurable<JacocoMarkdownClassListCondition> {

  private static final long serialVersionUID = 1L;

  private int limit;
  private List<String> excludes;
  private double branchCoverageLessThan;

  @Inject
  public JacocoMarkdownClassListCondition() {
    this.limit = 5;
    this.excludes = new ArrayList<>();
    this.branchCoverageLessThan = 0;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public List<String> getExcludes() {
    return excludes;
  }

  public void setExcludes(List<String> excludes) {
    this.excludes = excludes;
  }

  public double getBranchCoverageLessThan() {
    return branchCoverageLessThan;
  }

  public void setBranchCoverageLessThan(double branchCoverageLessThan) {
    this.branchCoverageLessThan = branchCoverageLessThan;
  }

  @Override
  @SuppressWarnings({"rawtypes", "deprecation"}) // the interface is defined with rawtypes..
  public JacocoMarkdownClassListCondition configure(Closure closure) {
    // org.gradle.util.ConfigureUtil is deprecated,
    // but org.gradle.util.internal.ConfigureUtil which is available in gradle 7.1 or later
    // is not deprecated
    org.gradle.util.ConfigureUtil.configureSelf(closure, this);
    return this;
  }
}
