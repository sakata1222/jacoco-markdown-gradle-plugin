package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.infrastructure;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassName;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JacocoCoveragesXmlRepository implements IJacocoCoverageRepository {

  private final DocumentBuilder documentBuilder;
  private final File jacocoXmlFile;

  public JacocoCoveragesXmlRepository(DocumentBuilderFactory documentBuilderFactory,
      File jacocoXmlFile) throws ParserConfigurationException {
    documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
    documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
    documentBuilderFactory
        .setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
    documentBuilderFactory
        .setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
    this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
    this.jacocoXmlFile = jacocoXmlFile;
  }

  @Override
  public CoverageReport readAll() {
    try {
      Document doc = documentBuilder.parse(jacocoXmlFile);
      Element rootReport = doc.getDocumentElement();
      List<Coverage> summaryCoverages = parseCounters(rootReport);
      Map<ClassName, Coverages> clazzToCoverages = nodeListAsStream(
          rootReport.getElementsByTagName("package"))
          .map(Element.class::cast)
          .flatMap(pkgNode -> nodeListAsStream(pkgNode.getElementsByTagName("class")))
          .map(Element.class::cast)
          .collect(Collectors.toMap(
              clazzNode -> new ClassName(clazzNode.getAttribute("name").replace("/", ".")),
              clazzNode -> new Coverages(parseCounters(clazzNode))));
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

  private List<Coverage> parseCounters(Element counterHolder) {
    NodeList childNodes = counterHolder.getChildNodes();
    return nodeListAsStream(childNodes)
        .filter(node -> node.getNodeName().equals("counter"))
        .map(Element.class::cast)
        .map(element ->
            new Coverage(
                element.getAttribute("type"),
                Integer.parseInt(element.getAttribute("covered")),
                Integer.parseInt(element.getAttribute("missed"))))
        .collect(Collectors.toList());
  }

  private Stream<Node> nodeListAsStream(NodeList nodeList) {
    return IntStream.range(0, nodeList.getLength())
        .mapToObj(nodeList::item);
  }
}
