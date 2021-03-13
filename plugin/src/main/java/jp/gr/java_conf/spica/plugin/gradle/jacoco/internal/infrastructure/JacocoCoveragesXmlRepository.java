package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import groovy.util.Node;
import groovy.util.XmlParser;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassName;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class JacocoCoveragesXmlRepository implements IJacocoCoverageRepository {

  private final XmlParser xmlParser;
  private final File jacocoXmlFile;

  public JacocoCoveragesXmlRepository(XmlParser xmlParser, File jacocoXmlFile) {
    this.xmlParser = xmlParser;
    this.jacocoXmlFile = jacocoXmlFile;
    initParser();
  }

  private void initParser() {
    try {
      this.xmlParser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
      this.xmlParser
          .setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public CoverageReport readAll() {
    try {
      Node root = xmlParser.parse(jacocoXmlFile);
      List<Coverage> summaryCoverages = parseCounters(root);
      @SuppressWarnings("unchecked")
      Map<ClassName, Coverages> clazzToCoverages = ((List<Node>) root.get("package")).stream()
          .flatMap(pkgNode -> ((List<Node>) pkgNode.get("class")).stream())
          .collect(Collectors.toMap(
              clazzNode -> new ClassName(clazzNode.get("@name").toString()),
              clazzNode -> new Coverages(parseCounters(clazzNode))
          ));
      return new CoverageReport(
          new CoverageSummary(summaryCoverages),
          new ClassCoverages(clazzToCoverages)
      );
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (SAXException e) {
      throw new IllegalStateException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private List<Coverage> parseCounters(Node counterHolder) {
    return ((List<Node>) counterHolder.get("counter")).stream()
        .map(node -> new Coverage(
            node.get("@type").toString(),
            Integer.parseInt(node.get("@covered").toString()),
            Integer.parseInt(node.get("@missed").toString())))
        .collect(Collectors.toList());
  }

}
