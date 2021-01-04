package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public interface IOwnCoveragesWriteRepository {

  void writeAll(Coverages coverages);

}
