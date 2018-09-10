lazy val `mango-common` = (project in file("./modules/mango-common/")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango-common",
  )

lazy val `mango-sql` = (project in file("./modules/mango-sql/")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango-sql",
    libraryDependencies ++= Seq(
      build.depends.scalamock % Test,
    )
  ).
  dependsOn(
    `mango-common`,
  )


lazy val mango = (project in file(".")).
  settings(build.commonSettings: _*).
  settings(
    name := "mango",
  ).
  aggregate(
    `mango-common`,
    `mango-sql`,
  )
