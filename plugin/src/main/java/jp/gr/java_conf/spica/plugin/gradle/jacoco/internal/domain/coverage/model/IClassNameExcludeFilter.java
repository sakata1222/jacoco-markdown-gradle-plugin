package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public interface IClassNameExcludeFilter {

  boolean matches(ClassName className);
}
