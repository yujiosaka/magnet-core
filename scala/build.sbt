name := "magnet-core"

version := "1.0"

libraryDependencies ++= Seq(
  "org.mongodb" %% "casbah" % "2.7.0-RC0",
  "org.apache.commons" % "commons-lang3" % "3.2.1",
  "org.apache.httpcomponents" % "httpclient" % "4.3.3",
  "org.slf4j" % "slf4j-nop" % "1.7.6",
  "commons-configuration" % "commons-configuration" % "1.7",
  "com.amazonaws" % "aws-java-sdk" % "1.7.3"
)
