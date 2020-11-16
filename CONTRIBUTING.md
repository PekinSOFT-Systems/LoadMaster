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

Since we at PekinSOFT™ Systems consider quality as one of the most important features of the software we built, we protect our main codebase from regressions already during pull request reviews.

### Adding tests

Any contribution adding brand new UI components like menus, dialogs, settings etc. must be accompanied by corresponding functional test. All automated tests are located in `test/com/pekinsoft/loadmaster` directory under the project root. Every major UI component has its own operator class which exposes operations which real users can perform with the UI component. Such operators typically extend standard [Jemmy](https://github.com/openjdk/jemmy-v2) operators and implement their specific functionality which are then used by concrete [JUnit](https://junit.org/junit5/) functional tests. Test classes can contain multiple tests methods which must be annotated with `@Test` annotation. While Jemmy operators are stored in `operators` package, the functional tests belong to `tests` package.

If automated testing of some functionality is extremely difficult, it is necessary to write at least a manual test script in [LoadMaster project's Wiki](https://github.com/PekinSOFT-Systems/LoadMaster/wiki). Pull request creators then need to declare that they were able to perform these manual tests on their private branches in order to get their pull requests reviewed.

### Running tests

Running functional automated tests is very easy. In the NetBeans IDE it's enough to just invoke `Test` action from popup menu after right clicking the Load Master project node. In order to run tests from command prompt the necessary prerequisites are correctly configured JDK 12 or newer and Ant build system version 1.10.5 or newer. The execution then looks like this:

```bash
export JAVA_HOME=/home/jsmith/Software/Java/jdk-12/
cd /home/jsmith/Sources/LoadMaster
ant -f "Load Master" -Dnb.internal.action.name=test -Dignore.failing.tests=true test
```

## Code quality tools

Before opening any pull requests we strongly recommend potential code contributors to address all identified hints pointed out in `Action Items` window which can be opened from NetBeans main `Window` menu item. In addition to that we also ask everyone to invoke `Format` action from NetBeans editor context menu to apply NetBeans default formatting rules to the new source code.

## CI Information

Automatic checks run by Travis continuous integration server consist of WhiteSource Bolt security tests of the source code, compilation test and Jemmy based tests of the application behavior. No pull requests heading to master branch with failed checks will be accepted unless such pull request gets exceptional approval from the project team.

## Repo-specific PR guidelines

Anything not covered in the general guidelines linked above.
