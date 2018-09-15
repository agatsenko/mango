lazy val mangoCommon = (project in file("./modules/mango-common/")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango-common",
    libraryDependencies ++= Seq(
      build.depends.scalamock % Test,
    )
  )

lazy val mangoSql = (project in file("./modules/mango-sql/")).
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


lazy val mango = (project in file(".")).
  settings(build.commonSettings: _*).
  settings(
    name := "mango",
  ).
  aggregate(
    mangoCommon,
    mangoSql,
  )
