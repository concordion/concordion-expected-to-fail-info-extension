[![Build and Test](https://github.com/concordion/concordion-status-info-extension/actions/workflows/ci.yml/badge.svg)](https://github.com/concordion/concordion-status-info-extension/actions/workflows/ci.yml)

This [Concordion](https://concordion.org/) extension allows for the displaying of a Note and Reason on the result report, 
when the `status` of specific examples in a specification is [set as](https://concordion.org/instrumenting/java/markdown/#implementation-status) 
`Ignored`, `Unimplemented` or `ExpectedToFail`.

The [demo project](https://github.com/concordion/concordion-status-info-extension-demo) demonstrates this extension.

## Introduction

This extension was brought about by a want to display why a particular test was marked as "expected to fail" on the Concordion 
storyboard. This extension enables the user to know exactly why a test was so marked. Other information could also be added 
such as date by which it was expected to be resolved and who to communicate within the team with regards to that failing 
feature if further information was required. This has been extended to encompass all of the status's.

## Acknowledgements

This extension was originally developed as an extension to the [cubano](https://github.com/concordion/cubano) framework.
