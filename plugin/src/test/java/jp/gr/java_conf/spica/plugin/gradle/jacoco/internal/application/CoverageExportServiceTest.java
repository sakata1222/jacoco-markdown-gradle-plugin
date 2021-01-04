package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.application;

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
import java.util.Arrays;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageTypes;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IJacocoCoverageRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesReadRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.IOwnCoveragesWriteRepository;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model.CoverageMarkdown;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.service.CoverageMarkdownReportService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CoverageExportServiceTest {

  @Test
  void export_write_difference_to_both_stdout_and_file_when_stdout_opt_is_true()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageMarkdownReportService reportService = mock(CoverageMarkdownReportService.class);
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Arrays.asList("foo"));
    Coverages jacoco = mock(Coverages.class);
    when(jacocoRepository.readAll()).thenReturn(jacoco);
    Coverages previous = mock(Coverages.class);
    when(readRepository.readAll()).thenReturn(previous);
    CoverageMarkdown md = mock(CoverageMarkdown.class);
    when(reportService.differenceReport(eq(types), same(previous), same(jacoco)))
        .thenReturn(md);
    when(md.toMarkdown()).thenReturn("difference-report");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        markdownWriter,
        stdout
    );
    ExportRequest request = new ExportRequest(true, true, types);

    service.export(request);

    verify(stdout, times(1)).println(eq("difference-report"));
    verify(markdownWriter, times(1)).write(eq("difference-report"));
  }

  @Test
  void export_write_current_to_only_file_when_stdout_opt_is_false()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageMarkdownReportService reportService = mock(CoverageMarkdownReportService.class);
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Arrays.asList("foo"));
    Coverages jacoco = mock(Coverages.class);
    when(jacocoRepository.readAll()).thenReturn(jacoco);
    CoverageMarkdown md = mock(CoverageMarkdown.class);
    when(reportService.currentReport(eq(types), same(jacoco)))
        .thenReturn(md);
    when(md.toMarkdown()).thenReturn("current-report");

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        markdownWriter,
        stdout
    );

    ExportRequest request = new ExportRequest(false, false, types);
    service.export(request);

    verify(stdout, never()).println(anyString());
    verify(markdownWriter, times(1)).write(eq("current-report"));
  }

  @Test
  void export_throws_unchecked_exception()
      throws IOException {
    IJacocoCoverageRepository jacocoRepository = mock(IJacocoCoverageRepository.class);
    IOwnCoveragesReadRepository readRepository = mock(IOwnCoveragesReadRepository.class);
    IOwnCoveragesWriteRepository writeRepository = mock(IOwnCoveragesWriteRepository.class);
    CoverageMarkdownReportService reportService = mock(CoverageMarkdownReportService.class);
    Writer markdownWriter = mock(Writer.class);
    PrintStream stdout = mock(PrintStream.class);

    CoverageTypes types = new CoverageTypes(Arrays.asList("foo"));
    Coverages jacoco = mock(Coverages.class);
    when(jacocoRepository.readAll()).thenReturn(jacoco);
    CoverageMarkdown md = mock(CoverageMarkdown.class);
    when(reportService.currentReport(eq(types), same(jacoco)))
        .thenReturn(md);
    when(md.toMarkdown()).thenReturn("current-report");
    doThrow(new IOException()).when(markdownWriter).write(anyString());

    CoverageExportService service = new CoverageExportService(
        jacocoRepository,
        readRepository,
        writeRepository,
        reportService,
        markdownWriter,
        stdout
    );

    ExportRequest request = new ExportRequest(false, false, types);
    Assertions.assertThatThrownBy(() -> service.export(request))
        .isInstanceOf(UncheckedIOException.class);
  }

}
