package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils.CustomCollectors;

public class CoverageJsonRepository implements IOwnCoveragesReadRepository,
    IOwnCoveragesWriteRepository {

  private final JsonSlurper jsonSlurper;
  private final File jsonFile;

  public CoverageJsonRepository(File jsonFile) {
    this(new JsonSlurper(), jsonFile);
  }

  public CoverageJsonRepository(JsonSlurper jsonSlurper, File jsonFile) {
    this.jsonSlurper = jsonSlurper;
    this.jsonFile = jsonFile;
  }

  @Override
  public void writeAll(CoverageSummary coverages) {
    try (Writer writer = Files.newBufferedWriter(jsonFile.toPath(), StandardCharsets.UTF_8)) {
      writer.write(new JsonBuilder(toMap(coverages)).toPrettyString());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public CoverageSummary readAll() {
    if (!jsonFile.exists()) {
      return null;
    }
    return fromMap((Map<String, Map<String, Object>>) jsonSlurper.parse(jsonFile));
  }

  private Map<String, CoverageJson> toMap(CoverageSummary coverages) {
    return coverages.typeToCoverage().entrySet().stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Map.Entry::getKey,
            e -> new CoverageJson(e.getValue())
        ));
  }

  private CoverageSummary fromMap(Map<String, Map<String, Object>> map) {
    return new CoverageSummary(map.values().stream()
        .map(CoverageJson::new)
        .map(CoverageJson::toCoverage)
        .collect(Collectors.toList())
    );
  }

  static class CoverageJson {

    private final String type;
    private final int covered;
    private final int missed;

    public CoverageJson(Coverage coverage) {
      this.type = coverage.getType();
      this.covered = coverage.getCovered();
      this.missed = coverage.getMissed();
    }

    public CoverageJson(Map<String, Object> map) {
      this.type = (String) map.get("type");
      this.covered = (int) map.get("covered");
      this.missed = (int) map.get("missed");
    }

    public Coverage toCoverage() {
      return new Coverage(
          this.type,
          this.covered,
          this.missed
      );
    }

    @SuppressWarnings("unused")
    private String getType() {
      return type;
    }

    @SuppressWarnings("unused")
    private int getCovered() {
      return covered;
    }

    @SuppressWarnings("unused")
    private int getMissed() {
      return missed;
    }
  }
}
