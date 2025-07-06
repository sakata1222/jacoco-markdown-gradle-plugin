# Changelog

## [1.5.0] - Not released

### Added

- Support Gradle 9.0

### Breaking Changes

- Versions earlier than 7.3 are no longer supported.

### Refactor

- [#74](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/pull/74) Do not use
  `org.gradle.util.ConfigureUtil.configureSelf` which is deprecated.

## [1.4.0] - 2023-03-12

## Added

- Support Gradle New Versions:
    - 8.0.x

## [1.3.0] - 2021-10-03

## Added

- Support Gradle New Versions:
    - 7.3.x
    - 7.4.x
    - 7.5.x
    - 7.6.x

## Fixed

- Deprecation warning occurs in Gradle 7.0
- Add new line between a text and a table. Thank you for
  the [contribution](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/pull/49).

## Removed

- Gradle 5.x and 6.0.x is no longer supported

## [1.2.0] - 2021-04-10

## Added

- Support Gradle 7.0

## [1.1.2] - 2021-03-15

### Added

- Exclude filters for class list
- Branch coverage threshold

### Fix

- The separator of class names should be `.` not `/'

## [1.1.1] - 2021-03-14

### Fix

- Branch coverage is not displayed in the class list.

## [1.1.0] - 2021-03-14

### Added

- Output class list with less coverage.

## [1.0.0] - 2021-01-17

### Added

- Output total coverage as a markdown.
- Output coverage difference between the current build and the previous build.
