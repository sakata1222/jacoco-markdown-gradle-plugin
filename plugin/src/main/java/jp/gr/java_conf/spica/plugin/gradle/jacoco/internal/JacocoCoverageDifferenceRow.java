package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.function.Function;

public class JacocoCoverageDifferenceRow implements JacocoCoverageRow {

  private final JacocoCoverage current;
  private final JacocoCoverage previous;

  public JacocoCoverageDifferenceRow(JacocoCoverage current,
      JacocoCoverage previous) {
    this.current = current;
    this.previous = previous;
  }

  @Override
  public String type() {
    return current.type();
  }

  @Override
  public String missedPerTotal() {
    return beforeAfter(JacocoCoverageRow::missedPerTotal);
  }

  @Override
  public String coveragePercent() {
    return beforeAfter(JacocoCoverageRow::coveragePercent);
  }

  private String beforeAfter(Function<JacocoCoverageRow, String> filedPicker) {
    return strikethroughText(filedPicker.apply(previous)) + " " + filedPicker.apply(current);
  }

  private String strikethroughText(String s) {
    return "~~" + s + "~~";
  }
}
