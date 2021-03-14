package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import groovy.lang.Closure;
import java.io.Serializable;
import javax.inject.Inject;
import org.gradle.util.Configurable;
import org.gradle.util.ConfigureUtil;

public class JacocoMarkdownClassListCondition implements Serializable,
    Configurable<JacocoMarkdownClassListCondition> {

  private static final long serialVersionUID = 1L;

  private int limit;

  @Inject
  public JacocoMarkdownClassListCondition() {
    this.limit = 5;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  @Override
  @SuppressWarnings("rawtypes") // the interface is defined with rawtypes..
  public JacocoMarkdownClassListCondition configure(Closure closure) {
    ConfigureUtil.configureSelf(closure, this);
    return this;
  }
}
