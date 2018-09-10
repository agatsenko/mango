lazy val mango = (project in file(".")).
  settings(build.scalaCommonSettings: _*).
  settings(
    name := "mango",
  )
