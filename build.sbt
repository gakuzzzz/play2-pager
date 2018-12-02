scalaVersion := "2.12.7"

val _version = "0.2.0"

val _crossScalaVersions = Seq("2.12.7", "2.11.12")

val _org = "jp.t2v"

lazy val _publishMavenStyle = true

lazy val _publishArtifactInTest = false

lazy val _pomIncludeRepository = { _: MavenRepository => false }

lazy val _publishTo = { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

lazy val _pomExtra = {
  <url>https://github.com/gakuzzzz/play2-pager</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:gakuzzzz/play2-pager.git</url>
      <connection>scm:git:git@github.com:gakuzzzz/play2-pager.git</connection>
    </scm>
    <developers>
      <developer>
        <id>gakuzzzz</id>
        <name>gakuzzzz</name>
        <url>https://github.com/gakuzzzz</url>
      </developer>
    </developers>
}

lazy val _playVersion = play.core.PlayVersion.current

lazy val root = (project in file(".")).
  aggregate(core, scalikejdbc, sample).
  settings(
    aggregate in update := false,
    crossScalaVersions  := _crossScalaVersions,
    publish             := { },
    publishArtifact     := false,
    packagedArtifacts   := Map.empty,
    publishTo           := _publishTo(_version),
    pomExtra            := _pomExtra
  )

lazy val commonSettings = Seq(
  organization := _org,
  name := "play2-pager",
  version := _version,
  scalaVersion := "2.12.7",
  crossScalaVersions := _crossScalaVersions,
  publishMavenStyle       := _publishMavenStyle,
  publishArtifact in Test := _publishArtifactInTest,
  pomIncludeRepository    := _pomIncludeRepository,
  publishTo               := _publishTo(_version),
  pomExtra                := _pomExtra
)

lazy val core = (project in file("core")).
  enablePlugins(SbtTwirl).
  settings(
    commonSettings,
    name := "play2-pager",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % _playVersion  % "provided"
    ),
    TwirlKeys.templateImports ++= Seq(
      "jp.t2v.lab.play2.pager._"
    )
  )

lazy val scalikejdbc = (project in file("scalikejdbc")).
  dependsOn(core).
  settings(
    commonSettings,
    name := "play2-pager-scalikejdbc",
    libraryDependencies ++= Seq(
      "org.scalikejdbc"  %% "scalikejdbc"  % "3.1.+"  % "provided"
    )
  )

lazy val sample = (project in file("sample")).
  dependsOn(core, scalikejdbc).
  enablePlugins(PlayScala).
  settings(
    commonSettings,
    name := "play2-pager-sample",
    libraryDependencies ++= Seq(
      "com.h2database"           %  "h2"                                  % "1.4.+",
      "ch.qos.logback"           %  "logback-classic"                     % "1.2.+",
      "org.scalikejdbc"          %% "scalikejdbc"                         % "3.1.+",
      "org.scalikejdbc"          %% "scalikejdbc-config"                  % "3.1.+",
      "org.scalikejdbc"          %% "scalikejdbc-play-initializer"        % "2.6.0-scalikejdbc-3.1",
      "org.scalikejdbc"          %% "scalikejdbc-syntax-support-macro"    % "3.1.+",
      guice,
      "org.flywaydb"             %% "flyway-play"                         % "4.0.0",
      "net.sourceforge.htmlunit" %  "htmlunit"                            % "2.14"     % "test",
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
    ),
    routesImport ++= Seq(
      "jp.t2v.lab.play2.pager.Pager",
      "jp.t2v.lab.play2.pager.Bindables._",
      "models.account._"
    ),
    TwirlKeys.templateImports ++= Seq(
      "jp.t2v.lab.play2.pager._",
      "models.account._"
    ),
    publish             := { },
    publishArtifact     := false,
    packagedArtifacts   := Map.empty,
    publishTo           := _publishTo(_version),
    pomExtra            := _pomExtra
  )
