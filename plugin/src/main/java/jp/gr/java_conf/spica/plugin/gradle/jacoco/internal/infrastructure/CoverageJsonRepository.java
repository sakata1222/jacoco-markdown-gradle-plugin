package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import groovy.json.JsonBuilder;
import groovy.json.JsonSlurper;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Locale;
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

  private Map<String, Map<String, Object>> toMap(CoverageSummary coverages) {
    return coverages.typeToCoverage().entrySet().stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Map.Entry::getKey,
            e -> {
              CoverageJson json = new CoverageJson(e.getValue());
              return json.toMap();
            }
        ));
  }

  private CoverageSummary fromMap(Map<String, Map<String, Object>> map) {
    try {
      return new CoverageSummary(map.values().stream()
          .filter(value -> !value.isEmpty())
          .map(CoverageJson::new)
          .map(CoverageJson::toCoverage)
          .collect(Collectors.toList())
      );
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          String.format(Locale.ENGLISH, "Unexpected error occurred. Input %s", map), e);
    }
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
      this.covered = (int) map.getOrDefault("covered", -1);
      this.missed = (int) map.getOrDefault("missed", -1);
    }

    public Coverage toCoverage() {
      return new Coverage(
          this.type,
          this.covered,
          this.missed
      );
    }

    Map<String, Object> toMap() {
      // use LinkedHashMap to keep the order of JSON.
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("type", type);
      map.put("covered", covered);
      map.put("missed", missed);
      return map;
    }
  }
}
