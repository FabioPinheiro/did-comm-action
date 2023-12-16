val scala3Version = "3.3.1"

val scalaDidVersion = "0.1.0-M16"

lazy val root = project
  .in(file("."))
  .settings(
    name := "did-comm-action",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "app.fmgp" %% "did" % scalaDidVersion,
    libraryDependencies += "app.fmgp" %% "did-imp" % scalaDidVersion,
    libraryDependencies += "app.fmgp" %% "did-method-peer" % scalaDidVersion,
    fork := true,
    envVars := Map(
      "SENDER_KEYS" -> """[{"kty":"OKP","crv":"X25519","d":"Z6D8LduZgZ6LnrOHPrMTS6uU2u5Btsrk1SGs4fn8M7c","x":"Sr4SkIskjN_VdKTn0zkjYbhGTWArdUNE4j_DmUpnQGw"},{"kty":"OKP","crv":"Ed25519","d":"INXCnxFEl0atLIIQYruHzGd5sUivMRyQOzu87qVerug","x":"MBjnXZxkMcoQVVL21hahWAw43RuAG-i64ipbeKKqwoA"}]""",
      "RECIPIENT_DID" -> "did:peer:2.Ez6LSghwSE437wnDE1pt3X6hVDUQzSjsHzinpX3XFvMjRAm7y.Vz6Mkhh1e5CEYYq6JBUcTZ6Cp2ranCWRrv7Yax3Le4N59R6dd.SeyJ0IjoiZG0iLCJzIjoiaHR0cHM6Ly9hbGljZS5kaWQuZm1ncC5hcHAvIiwiciI6W10sImEiOlsiZGlkY29tbS92MiJdfQ",
      "MSG" -> "Test Message",
    ),
  )
