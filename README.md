# concordion-status-info-extension

This [Concordion](https://concordion.org/) extension displays a Note and a Reason in the  output, when the `status`  of specific examples in a specification is [set as](https://concordion.org/instrumenting/java/markdown/#implementation-status) `Ignored`, `Unimplemented` or `ExpectedToFail`.

The [demo project](https://github.com/concordion/concordion-status-info-extension-demo) demonstrates this extension.

## Introduction

This extension was brought about by a need to display why a particular test was marked as "expected to fail" on the Concordion storyboard. This extension enables the user to know exactly why a test was so marked. Other information could also be added such as date by which it was expected to be resolved and who to communicate within the team with regards to that failing feature if further information was required.

## Acknowledgements

This extension was originally developed as an extension to the [cubano](https://github.com/concordion/cubano) framework.