# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Gradle plugin (`com.github.sakata1222.jacoco-markdown`) that parses JaCoCo XML coverage reports and generates markdown summaries. The plugin creates tasks that automatically run after JaCoCo reports to produce human-readable coverage tables and class lists.

## Build Commands

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run functional tests  
./gradlew functionalTest

# Run all checks (includes test, functionalTest, checkstyle, spotbugs)
./gradlew check

# Generate coverage report
./gradlew jacocoTestReport

# Generate coverage markdown (automatically runs after jacocoTestReport)
./gradlew jacocoTestReportMarkdown
```

## Architecture

The plugin follows a layered architecture:

- **Plugin Layer**: `JacocoMarkdownPlugin` - Entry point that registers tasks and extensions
- **Application Layer**: `CoverageExportService` - Orchestrates the coverage export process
- **Domain Layer**: 
  - `coverage.model.*` - Core domain objects (Coverage, CoverageReport, ClassCoverage, etc.)
  - `md.model.*` - Markdown table generation models
  - `md.service.*` - Markdown report generation services
- **Infrastructure Layer**: 
  - `JacocoCoveragesXmlRepository` - Parses JaCoCo XML reports
  - `CoverageJsonRepository` - Handles JSON serialization for diff comparisons

### Key Components

- `JacocoMarkdownTask` - Main task that processes JaCoCo XML and generates markdown
- `JacocoMarkdownExtension` - Plugin configuration (diffEnabled, stdout, classListEnabled, etc.)
- `CoverageReport` - Contains coverage summary and per-class coverage data
- `ClassCoverageMarkdownTable` / `CoverageSummaryMarkdownTable` - Generate markdown tables

The plugin automatically creates tasks named `<jacocoReportTaskName>Markdown` for each JacocoReport task.

## Test Structure

- Unit tests: `plugin/src/test/java/` - Tests individual components
- Functional tests: `plugin/src/functionalTest/java/` - End-to-end plugin behavior tests
- Test data: `plugin/testData/` and test resources directories contain sample XML/JSON files
- Custom assertions: `test.assertions.*` - Domain-specific assertion helpers

## Quality Tools

- **Checkstyle**: Code style enforcement (config in `config/checkstyle/`)  
- **SpotBugs**: Static analysis (exclusions in `config/spotbugs/exclude.xml`)
- **JaCoCo**: Code coverage with markdown reporting (uses own plugin)
- **SonarQube**: Code quality analysis