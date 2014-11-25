val _version = "0.1.0-SNAPSHOT"

val _crossScalaVersions = Seq("2.11.2", "2.10.4")

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


lazy val root = (project in file(".")).
  aggregate(core, scalikejdbc, sample).
  settings(
    aggregate in update := false,
    crossScalaVersions  := _crossScalaVersions,
    publish             := { },
    publishArtifact     := false,
    packagedArtifacts   := Map.empty,
    publishTo           <<=(version)(_publishTo),
    pomExtra            := _pomExtra
  )

lazy val core = (project in file("core")).
  enablePlugins(SbtTwirl).
  settings(
    organization := _org,
    name := "play2-pager",
    version := _version,
    crossScalaVersions := _crossScalaVersions,
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      "com.typesafe.play"  %%   "play"   %  "2.3.4"  %   "provided"
    ),
    TwirlKeys.templateImports ++= Seq(
      "jp.t2v.lab.play2.pager._"
    ),
    publishMavenStyle       := _publishMavenStyle,
    publishArtifact in Test := _publishArtifactInTest,
    pomIncludeRepository    := _pomIncludeRepository,
    publishTo               <<=(version)(_publishTo),
    pomExtra                := _pomExtra
  )

lazy val scalikejdbc = (project in file("scalikejdbc")).
  dependsOn(core).
  settings(
    organization := _org,
    name := "play2-pager-scalikejdbc",
    version := _version,
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++= Seq(
      "org.scalikejdbc"  %% "scalikejdbc"  % "2.2.+"  % "provided"
    ),
    publishMavenStyle       := _publishMavenStyle,
    publishArtifact in Test := _publishArtifactInTest,
    pomIncludeRepository    := _pomIncludeRepository,
    publishTo               <<=(version)(_publishTo),
    pomExtra                := _pomExtra
  )

lazy val sample = (project in file("sample")).
  dependsOn(core, scalikejdbc).
  enablePlugins(PlayScala).
  settings(
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++= Seq(
      "com.h2database"           %  "h2"                                  % "1.4.+",
      "ch.qos.logback"           %  "logback-classic"                     % "1.1.+",
      "org.scalikejdbc"          %% "scalikejdbc"                         % "2.2.+",
      "org.scalikejdbc"          %% "scalikejdbc-play-plugin"             % "2.3.+",
      "org.scalikejdbc"          %% "scalikejdbc-syntax-support-macro"    % "2.2.+",
      "com.github.tototoshi"     %% "play-flyway"                         % "1.1.0",
      "net.sourceforge.htmlunit" %  "htmlunit"                            % "2.14"     % "test"
    ),
    play.PlayImport.PlayKeys.routesImport ++= Seq(
      "jp.t2v.lab.play2.pager.Pager",
      "jp.t2v.lab.play2.pager.Bindables._",
      "models.account._"
    ),
    TwirlKeys.templateImports ++= Seq(
      "jp.t2v.lab.play2.pager._",
      "models.account._"
    ),
    publish           := { },
    publishArtifact   := false,
    packagedArtifacts := Map.empty,
    publishTo         <<=(version)(_publishTo),
    pomExtra          := _pomExtra
  )
