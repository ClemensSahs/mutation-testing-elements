package mutationtesting

import io.circe._
import mutationtesting.CodecOps._

/** Circe codecs for encoding and decoding `mutationtesting` report values
  */
object circe {

  implicit lazy val mutationTestResultCodec: Codec[MutationTestResult] =
    Codec
      .forProduct9(
        "$schema",
        "schemaVersion",
        "thresholds",
        "projectRoot",
        "files",
        "testFiles",
        "performance",
        "framework",
        "system"
      )(MutationTestResult.apply)(m =>
        (
          m.`$schema`,
          m.schemaVersion,
          m.thresholds,
          m.projectRoot,
          m.files,
          m.testFiles,
          m.performance,
          m.framework,
          m.system
        )
      )
      .mapEncoder(_.mapJson(_.deepDropNullValues))

  implicit lazy val fileResultCodec: Codec[FileResult] =
    Codec.forProduct3("source", "mutants", "language")(FileResult.apply)(f => (f.source, f.mutants, f.language))

  implicit lazy val mutantResultCodec: Codec[MutantResult] =
    Codec.forProduct10(
      "id",
      "mutatorName",
      "replacement",
      "location",
      "status",
      "description",
      "coveredBy",
      "killedBy",
      "testsCompleted",
      "static"
    )(MutantResult.apply)(m =>
      (
        m.id,
        m.mutatorName,
        m.replacement,
        m.location,
        m.status,
        m.description,
        m.coveredBy,
        m.killedBy,
        m.testsCompleted,
        m.static
      )
    )

  implicit lazy val thresholdsCodec: Codec[Thresholds] = Codec
    .forProduct2("high", "low")(Thresholds.apply)(t => (t.high, t.low))
    .mapDecoder(_.emap(t => Thresholds.create(high = t.high, low = t.low)))

  implicit lazy val testFileCodec: Codec[TestFile] = Codec.forProduct1("tests")(TestFile.apply)(t => t.tests)

  implicit lazy val mutantStatusCodec: Codec[MutantStatus] =
    Codec
      .from(Decoder[String], Encoder[String])
      .mapCodec[MutantStatus](_.emap {
        case "Killed"       => Right(MutantStatus.Killed)
        case "Survived"     => Right(MutantStatus.Survived)
        case "NoCoverage"   => Right(MutantStatus.NoCoverage)
        case "Timeout"      => Right(MutantStatus.Timeout)
        case "CompileError" => Right(MutantStatus.CompileError)
        case "RuntimeError" => Right(MutantStatus.RuntimeError)
        case "Ignored"      => Right(MutantStatus.Ignored)
        case other          => Left(s"Invalid status '$other'")
      })(_.contramap {
        case MutantStatus.Killed       => "Killed"
        case MutantStatus.Survived     => "Survived"
        case MutantStatus.NoCoverage   => "NoCoverage"
        case MutantStatus.Timeout      => "Timeout"
        case MutantStatus.CompileError => "CompileError"
        case MutantStatus.RuntimeError => "RuntimeError"
        case MutantStatus.Ignored      => "Ignored"
      })

  implicit lazy val testDefinitionCodec: Codec[TestDefinition] =
    Codec.forProduct3("id", "name", "location")(TestDefinition.apply)(t => (t.id, t.name, t.location))

  implicit lazy val locationCodec: Codec[Location] =
    Codec.forProduct2("start", "end")(Location.apply)(o => (o.start, o.end))

  implicit lazy val openEndLocationCodec: Codec[OpenEndLocation] =
    Codec.forProduct2("start", "end")(OpenEndLocation.apply)(o => (o.start, o.end))

  implicit lazy val positionCodec: Codec[Position] =
    Codec.forProduct2("line", "column")(Position.apply)(p => (p.line, p.column))

  implicit lazy val performanceStatisticsCodec: Codec[PerformanceStatistics] =
    Codec.forProduct3("setup", "initialRun", "mutation")(PerformanceStatistics.apply)(p =>
      (p.setup, p.initialRun, p.mutation)
    )

  implicit lazy val frameworkInformationCodec: Codec[FrameworkInformation] =
    Codec.forProduct4("name", "version", "branding", "dependencies")(FrameworkInformation.apply)(p =>
      (p.name, p.version, p.branding, p.dependencies)
    )

  implicit lazy val brandingInformationCodec: Codec[BrandingInformation] =
    Codec.forProduct2("homepageUrl", "imageUrl")(BrandingInformation.apply)(b => (b.homepageUrl, b.imageUrl))

  implicit lazy val systemInformationCodec: Codec[SystemInformation] =
    Codec.forProduct4("ci", "os", "cpu", "ram")(SystemInformation.apply)(s => (s.ci, s.os, s.cpu, s.ram))

  implicit lazy val osInformationCodec: Codec[OSInformation] =
    Codec.forProduct3("platform", "description", "version")(OSInformation.apply)(o =>
      (o.platform, o.description, o.version)
    )

  implicit lazy val cpuInformationCodec: Codec[CpuInformation] =
    Codec.forProduct3("logicalCores", "baseClock", "model")(CpuInformation.apply)(c =>
      (c.logicalCores, c.baseClock, c.model)
    )

  implicit lazy val ramInformation: Codec[RamInformation] =
    Codec.forProduct1("total")(RamInformation.apply)(r => r.total)

}