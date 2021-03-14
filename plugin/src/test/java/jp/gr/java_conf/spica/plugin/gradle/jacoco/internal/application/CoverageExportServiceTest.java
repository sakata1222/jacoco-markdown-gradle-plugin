package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Collections;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverageLimit;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.ClassCoverageMarkdownTable;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageSummaryMarkdownTable;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.ClassCoverageMarkdownReportService;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageSummaryMarkdownReportService;
import org.junit.jupiter.api.Test;

class CoverageExportServiceTest {

  @Test
  void export_write_difference_to_both_stdout_and_file_when_stdout_opt_is_true()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageSummaryMarkdownReportService reportService = mock(
        CoverageSummaryMarkdownReportService.class
    );
    ClassCoverageMarkdownReportService classReportService = mock(
        ClassCoverageMarkdownReportService.class
    );
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Collections.singletonList("foo"));
    CoverageReport jacocoReport = mock(CoverageReport.class);
    CoverageSummary currentSummary = mock(CoverageSummary.class);
    when(jacocoReport.summary()).thenReturn(currentSummary);
    ClassCoverages classCoverages = mock(ClassCoverages.class);
    when(jacocoReport.classToCoverages()).thenReturn(classCoverages);
    when(jacocoRepository.readAll()).thenReturn(jacocoReport);
    CoverageSummary previousSummary = mock(CoverageSummary.class);
    when(readRepository.readAll()).thenReturn(previousSummary);
    CoverageSummaryMarkdownTable md = mock(CoverageSummaryMarkdownTable.class);
    ClassCoverageMarkdownTable classMd = mock(ClassCoverageMarkdownTable.class);
    when(reportService.differenceReport(eq(types), same(previousSummary), same(currentSummary)))
        .thenReturn(md);
    ClassCoverageLimit limit = new ClassCoverageLimit(1);
    when(classReportService.report(same(classCoverages), same(limit))).thenReturn(classMd);
    when(md.toMarkdown()).thenReturn("difference-report");
    when(classMd.toMarkdown()).thenReturn("");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        classReportService,
        markdownWriter,
        stdout
    );
    ExportRequest request = new ExportRequest(true, true, true, limit, types, true, true);

    service.export(request);

    verify(writeRepository, times(1)).writeAll(currentSummary);
    verify(stdout, times(1)).println("difference-report");
    verify(markdownWriter, times(1)).write("difference-report");
  }

  @Test
  void export_does_not_write_json_file()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageSummaryMarkdownReportService reportService = mock(
        CoverageSummaryMarkdownReportService.class
    );
    ClassCoverageMarkdownReportService classReportService = mock(
        ClassCoverageMarkdownReportService.class
    );
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Collections.singletonList("foo"));
    CoverageReport jacocoReport = mock(CoverageReport.class);
    CoverageSummary currentSummary = mock(CoverageSummary.class);
    when(jacocoReport.summary()).thenReturn(currentSummary);
    ClassCoverages classCoverages = mock(ClassCoverages.class);
    when(jacocoReport.classToCoverages()).thenReturn(classCoverages);
    when(jacocoRepository.readAll()).thenReturn(jacocoReport);

    CoverageSummary previousSummary = mock(CoverageSummary.class);
    when(readRepository.readAll()).thenReturn(previousSummary);
    CoverageSummaryMarkdownTable md = mock(CoverageSummaryMarkdownTable.class);
    ClassCoverageMarkdownTable classMd = mock(ClassCoverageMarkdownTable.class);
    when(reportService.differenceReport(eq(types), same(previousSummary), same(currentSummary)))
        .thenReturn(md);
    ClassCoverageLimit limit = new ClassCoverageLimit(1);
    when(classReportService.report(same(classCoverages), same(limit))).thenReturn(classMd);
    when(md.toMarkdown()).thenReturn("difference-report");
    when(classMd.toMarkdown()).thenReturn("");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        classReportService,
        markdownWriter,
        stdout
    );
    ExportRequest request = new ExportRequest(true, true, false, limit, types, false, true);

    service.export(request);

    verify(writeRepository, never()).writeAll(any());
    verify(stdout, times(1)).println("difference-report");
    verify(markdownWriter, times(1)).write("difference-report");
  }

  @Test
  void export_does_not_write_md_file()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageSummaryMarkdownReportService reportService = mock(
        CoverageSummaryMarkdownReportService.class
    );
    ClassCoverageMarkdownReportService classReportService = mock(
        ClassCoverageMarkdownReportService.class
    );
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Collections.singletonList("foo"));
    CoverageReport jacocoReport = mock(CoverageReport.class);
    CoverageSummary currentSummary = mock(CoverageSummary.class);
    when(jacocoReport.summary()).thenReturn(currentSummary);
    ClassCoverages classCoverages = mock(ClassCoverages.class);
    when(jacocoReport.classToCoverages()).thenReturn(classCoverages);
    when(jacocoRepository.readAll()).thenReturn(jacocoReport);
    CoverageSummary previousSummary = mock(CoverageSummary.class);
    when(readRepository.readAll()).thenReturn(previousSummary);
    CoverageSummaryMarkdownTable md = mock(CoverageSummaryMarkdownTable.class);
    ClassCoverageMarkdownTable classMd = mock(ClassCoverageMarkdownTable.class);
    when(reportService.differenceReport(eq(types), same(previousSummary), same(currentSummary)))
        .thenReturn(md);
    ClassCoverageLimit limit = new ClassCoverageLimit(1);
    when(classReportService.report(same(classCoverages), same(limit))).thenReturn(classMd);
    when(md.toMarkdown()).thenReturn("difference-report");
    when(classMd.toMarkdown()).thenReturn("");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        classReportService,
        markdownWriter,
        stdout
    );
    ExportRequest request = new ExportRequest(true, true, false, limit, types, true, false);

    service.export(request);

    verify(writeRepository, times(1)).writeAll(currentSummary);
    verify(stdout, times(1)).println("difference-report");
    verify(markdownWriter, never()).write(anyString());
  }

  @Test
  void export_write_current_to_only_file_when_stdout_opt_is_false()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageSummaryMarkdownReportService reportService = mock(
        CoverageSummaryMarkdownReportService.class
    );
    ClassCoverageMarkdownReportService classReportService = mock(
        ClassCoverageMarkdownReportService.class
    );
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Collections.singletonList("foo"));
    CoverageReport jacocoReport = mock(CoverageReport.class);
    CoverageSummary currentSummary = mock(CoverageSummary.class);
    when(jacocoReport.summary()).thenReturn(currentSummary);
    ClassCoverages classCoverages = mock(ClassCoverages.class);
    when(jacocoReport.classToCoverages()).thenReturn(classCoverages);
    when(jacocoRepository.readAll()).thenReturn(jacocoReport);
    CoverageSummaryMarkdownTable md = mock(CoverageSummaryMarkdownTable.class);
    ClassCoverageMarkdownTable classMd = mock(ClassCoverageMarkdownTable.class);
    when(reportService.currentReport(eq(types), same(currentSummary)))
        .thenReturn(md);
    ClassCoverageLimit limit = new ClassCoverageLimit(1);
    when(classReportService.report(same(classCoverages), same(limit))).thenReturn(classMd);
    when(md.toMarkdown()).thenReturn("current-report");
    when(classMd.toMarkdown()).thenReturn("");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        classReportService,
        markdownWriter,
        stdout
    );

    ExportRequest request = new ExportRequest(false, false, false, limit, types, true, true);
    service.export(request);

    verify(writeRepository, times(1)).writeAll(currentSummary);
    verify(stdout, never()).println(anyString());
    verify(markdownWriter, times(1)).write("current-report");
  }

  @Test
  void export_throws_unchecked_exception()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageSummaryMarkdownReportService reportService = mock(
        CoverageSummaryMarkdownReportService.class
    );
    ClassCoverageMarkdownReportService classReportService = mock(
        ClassCoverageMarkdownReportService.class
    );
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Collections.singletonList("foo"));
    CoverageReport jacocoReport = mock(CoverageReport.class);
    CoverageSummary currentSummary = mock(CoverageSummary.class);
    when(jacocoReport.summary()).thenReturn(currentSummary);
    ClassCoverages classCoverages = mock(ClassCoverages.class);
    when(jacocoReport.classToCoverages()).thenReturn(classCoverages);
    when(jacocoRepository.readAll()).thenReturn(jacocoReport);
    CoverageSummaryMarkdownTable md = mock(CoverageSummaryMarkdownTable.class);
    ClassCoverageMarkdownTable classMd = mock(ClassCoverageMarkdownTable.class);
    when(reportService.currentReport(eq(types), same(currentSummary)))
        .thenReturn(md);
    ClassCoverageLimit limit = new ClassCoverageLimit(1);
    when(classReportService.report(same(classCoverages), same(limit))).thenReturn(classMd);
    when(md.toMarkdown()).thenReturn("current-report");
    when(classMd.toMarkdown()).thenReturn("");
    doThrow(new IOException()).when(markdownWriter).write(anyString());

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        classReportService,
        markdownWriter,
        stdout
    );

    ExportRequest request = new ExportRequest(false, false, false, limit, types, true, true);
    assertThatThrownBy(() -> service.export(request))
        .isInstanceOf(UncheckedIOException.class);
  }
}
