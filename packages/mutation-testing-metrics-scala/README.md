[![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fstryker-mutator%2Fmutation-testing-elements%2Fmaster%3Fmodule%3Dmetrics-scala)](https://badge-api.stryker-mutator.io/github.com/stryker-mutator/mutation-testing-elements/master?module=metrics-scala)
[![Build Status](https://github.com/stryker-mutator/mutation-testing-elements/workflows/CI/badge.svg)](https://github.com/stryker-mutator/mutation-testing-elements/actions?query=workflow%3ACI+branch%3Amaster)

# Mutation testing metrics (Scala)

Zero-dependency library to calculate mutation testing metrics in Scala.

See [mutant states and metrics in the Stryker handbook](https://github.com/stryker-mutator/stryker-handbook/blob/master/mutant-states-and-metrics.md#readme) for more details about mutation testing metrics.

Cross-compiled for Scala 2.12, 2.13, [Dotty](https://dotty.epfl.ch/), [Scala.js 0.6.x](http://www.scala-js.org/) and [Scala Native 0.3.x](https://www.scala-native.org/).

## Usage example

Add the dependency to your project [![Maven Central](https://img.shields.io/maven-central/v/io.stryker-mutator/mutation-testing-metrics_2.13.svg?label=Maven%20Central&colorB=brightgreen)](https://search.maven.org/artifact/io.stryker-mutator/mutation-testing-metrics_2.13):

```scala
libraryDependencies += "io.stryker-mutator" %% "mutation-testing-metrics" % version
```

If you use Scala.js or Scala Native, use `%%%` instead after the groupId.

First create the mutation test report:

```scala
import mutationtesting._
val report = MutationTestReport(thresholds = Thresholds(high = 80, low = 10),
  files = Map(
    "src/stryker4s/Stryker4s.scala" -> MutationTestResult(
      source = "case class Stryker4s(foo: String)",
      mutants = Seq(
        MutantResult("1", "BinaryOperator", "-", Location(Position(1, 2), Position(2, 3)), status = MutantStatus.Killed)
      )
    )
  )
)
```

The `MutationTestReport` case classes generate a JSON compliant with the [mutation-testing JSON schema](https://github.com/stryker-mutator/mutation-testing-elements/blob/master/packages/mutation-testing-report-schema/src/mutation-testing-report-schema.json).


Then calculate and use metrics from that report:

```scala
val metrics: MetricsResult = Metrics.calculateMetrics(report)
```

That report will have all the metrics you need:

```scala
val mutationScore = metrics.mutationScore
// mutationScore: Double = 70.12987012987013
val killed = metrics.killed
// killed: Int = 162
val survived = metrics.survived
// survived: Int = 69
```

## mutation-testing-metrics-circe

Circe transcodings are provided by the `mutation-testing-metrics-circe` library to work with JSON if you don't want the extra dependency on `circe-generic`. It has two dependencies: `circe-core` and `circe-parser`.

```scala
libraryDependencies += "io.stryker-mutator" %% "mutation-testing-metrics-circe" % version
```

### Encoding

Import the encoder:

```scala
import io.circe.syntax._
import mutationtesting.MutationReportEncoder._

val encoded = report.asJson
```

### Decoding

Import the decoder:

```scala
import io.circe.parser.decode
import mutationtesting.MutationReportDecoder._

val decoded = decode[MutationTestReport](json)
```

## API reference

### `MetricsResult`

A `MetricsResult` has the following properties, as described in [the handbook](https://github.com/stryker-mutator/stryker-handbook/blob/master/mutant-states-and-metrics.md): 
 
```scala
metrics.killed
// res1: Int = 162
metrics.survived
// res2: Int = 69
metrics.timeout
// res3: Int = 0
metrics.noCoverage
// res4: Int = 0
metrics.compileErrors
// res5: Int = 0
metrics.totalDetected
// res6: Int = 162
metrics.totalUndetected
// res7: Int = 69
metrics.totalCovered
// res8: Int = 231
metrics.totalValid
// res9: Int = 231
metrics.totalInvalid
// res10: Int = 0
metrics.totalMutants
// res11: Int = 231
metrics.mutationScore
// res12: Double = 70.12987012987013
metrics.mutationScoreBasedOnCoveredCode
// res13: Double = 70.12987012987013
```

- `MetricsResult` is a trait with three implementations:
  - `MetricsResultRoot`: The root of a `MetricsResult`, contains zero or more `MetricsResult`'s
  - `MetricsDirectory`: Representation of a directory. Has a directory name and zero or more `MetricsResult`'s
  - `MetricsFile`: Representation of a file with mutated code. Has a filename and zero or more `MetricMutant`'s
- `MetricMutant`: Contains a [`MutantStatus`](https://github.com/stryker-mutator/stryker-handbook/blob/master/mutant-states-and-metrics.md#mutant-states)

## Contributing

To use this project, you will need sbt. The recommended way on macOS/Linux is with [sbt-extras](https://github.com/paulp/sbt-extras). On Windows, you can install sbt using the [official .msi](https://www.scala-sbt.org/download.html).

This project uses the [sbt-crossproject](https://github.com/portable-scala/sbt-crossproject) plugin for multiple build targets. You can compile code with `sbt compile` and run tests
with `sbt test`. Running `sbt +test` will compile and test all targets. 

In CI, JS and Native will only be compiled, while tests are run on the JVM project to provide faster CI builds. Publishing is done on all targets. For more information on
cross-compilation in sbt, see <https://www.scala-sbt.org/1.x/docs/Cross-Build.html>.

This readme uses [mdoc](https://scalameta.org/mdoc/). To edit it, please edit the readme in docs and call `sbt docs/mdoc` to compile the readme in the root of the project.