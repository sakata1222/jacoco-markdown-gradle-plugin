package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

public interface JacocoCoverageRow {

  String type();

  String missedPerTotal();

  String coveragePercent();
}
