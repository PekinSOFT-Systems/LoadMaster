# Contributing to PekinSOFT™ Systems Projects

Welcome and thank you for considering contributing to PekinSOFT™ Systems open source projects! It is people like you that make software design and availability a reality for millions of people around the world.

Please read and follow these guidelines to assist us in making the contribution process easy and effective for everyone involved. By doing so, it also lets us know that you agree to respect the time of the developers managing and developing these open source projects. In return, we will always reciprocate that respect by addressing your issue, taking your advice on potential changes, and helping you to finalize your pull requests (PRs).

Once again, we would like to thank you for considering contributing to our projects and give you a ***big, warm WELCOME***!

## Documentation

- Wiki: For flow and class diagrams for the Project, go to the [Wiki](https://github.com/PekinSOFT-Systems/LoadMaster/wiki) for the repository.

## Assets compilation

Information about compiling CSS, JS, SVG, etc.

## Environment setup

When setting up your environment to contribute to PekinSOFT™ Systems projects, we ask that you *fork* the repository for the project in which you are interested. We strive to make sure that any required libraries are included in the repository, typically at the root level. If you have any issues running the project after you have forked it, please let us know by submitting an [Issue](https://github.com/PekinSOFT-Systems/LoadMaster/issues/new?labels=won%27t+run&template=forked_issue.md&title=%5BRunning+Issue%5D) and we will try to assist with the configuration.

## Testing

Since we at PekinSOFT™ Systems consider quality as one of the most important features of the software we built, we protect our main codebase from regressions already during pull request reviews. Automatic checks consist of WhiteSource Bolt security tests of the source code and Selenium based tests of the application behavior. No pull requests heading to master branch with failed checks will be accepted unless the such pull request gets exceptional approval from the project team. Any contribution adding brand new UI components like menus, dialogs, settings etc. must be accompanied by corresponding functional test.

### Adding tests

General information about the test suite and how to format and structure tests.

All automated tests are located in `test/com/pekinsoft/loadmaster` directory under the project root. Every major UI component has its own operator class which exposes operations which real users can perform with the UI component. Such operators typically extend standard [Jemmy](https://github.com/openjdk/jemmy-v2) operators and implement their specific functionality which are then used by concrete [JUnit](https://junit.org/junit5/) functional tests. Test classes can contain multiple tests methods which must be annotated with @Test annotation. While Jemmy operators are stored in `operators` package, the functional tests belong to `tests` package. 

### Running tests

Any additional information needed to run the test suite. Include `bash`-formatted commands like:

```bash
composer test
bundle exec rake test
```

Also include any information about essential manual tests.

## Code quality tools

Information about scripts to run before committing.

## CI Information

What CI checks for and how to pass.

## Repo-specific PR guidelines

Anything not covered in the general guidelines linked above.