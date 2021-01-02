package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.Map;

public interface JacocoReport extends JacocoCoverageRows {

  Map<String, JacocoCoverage> summary();
}
