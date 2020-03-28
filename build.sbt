lazy val mangoCommon = (project in file("./modules/mango-common")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango-common",
    libraryDependencies ++= Seq(
      build.depends.scalamock % Test,
    )
  )

lazy val mangoSql = (project in file("./modules/mango-sql")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango-sql",
    libraryDependencies ++= Seq(
      build.depends.scalamock % Test,
      build.depends.h2database % Test,
      build.depends.hikariCp % Test,
    )
  ).
  dependsOn(
    mangoCommon,
  )

lazy val mangoServices = (project in file("./modules/mango-services")).
    settings(build.scalaCommonSettings: _*).
    settings(
      name := "mango-services",
      libraryDependencies ++= Seq(
        build.depends.scalamock % Test,
      )
    ).
    dependsOn(
      mangoCommon,
    )

lazy val mangoServicesMacwire = (project in file("./modules/mango-services-macwire")).
    settings(build.scalaCommonSettings: _*).
    settings(
      name := "mango-services-macwire",
      libraryDependencies ++= Seq(
        build.depends.macwireMacros,
        build.depends.macwireUtil,
      )
    ).
    dependsOn(
      mangoCommon,
      mangoServices,
    )

lazy val mangoSamples = (project in file("./modules/mango-samples")).
    settings(build.scalaCommonSettings: _*).
    settings(
      name := "mango-samples",
      libraryDependencies ++= Seq(
        build.depends.logbackClassic,
        build.depends.scalaLogging,
        build.depends.h2database,
        build.depends.hikariCp,
      )
    ).
    dependsOn(
      mangoCommon,
      mangoSql,
    )

lazy val mango = (project in file(".")).
  settings(build.commonSettings: _*).
  settings(
    name := "mango",
  ).
  aggregate(
    mangoCommon,
    mangoSql,
    mangoServices,
    mangoServicesMacwire,
    mangoSamples,
  )
