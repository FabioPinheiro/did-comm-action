val scala3Version = "3.3.1"

val scalaDidVersion = "0.1.0-M18"

lazy val root = project
  .in(file("."))
  .settings(
    name := "did-comm-action",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "app.fmgp" %% "did" % scalaDidVersion,
    libraryDependencies += "app.fmgp" %% "did-imp" % scalaDidVersion,
    libraryDependencies += "app.fmgp" %% "did-method-peer" % scalaDidVersion,
    libraryDependencies += "app.fmgp" %% "did-framework" % scalaDidVersion,
    fork := true,
    envVars := Map(
      "SENDER_KEYS" -> """[{"kty":"OKP","crv":"X25519","d":"Z6D8LduZgZ6LnrOHPrMTS6uU2u5Btsrk1SGs4fn8M7c","x":"Sr4SkIskjN_VdKTn0zkjYbhGTWArdUNE4j_DmUpnQGw"},{"kty":"OKP","crv":"Ed25519","d":"INXCnxFEl0atLIIQYruHzGd5sUivMRyQOzu87qVerug","x":"MBjnXZxkMcoQVVL21hahWAw43RuAG-i64ipbeKKqwoA"}]""",
      // "RECIPIENT_DID" -> "did:peer:2.Ez6LSghwSE437wnDE1pt3X6hVDUQzSjsHzinpX3XFvMjRAm7y.Vz6Mkhh1e5CEYYq6JBUcTZ6Cp2ranCWRrv7Yax3Le4N59R6dd.SeyJ0IjoiZG0iLCJzIjoiaHR0cHM6Ly9hbGljZS5kaWQuZm1ncC5hcHAvIiwiciI6W10sImEiOlsiZGlkY29tbS92MiJdfQ",
      // "RECIPIENT_DID" -> "did:peer:2.Ez6LSghwSE437wnDE1pt3X6hVDUQzSjsHzinpX3XFvMjRAm7y.Vz6Mkhh1e5CEYYq6JBUcTZ6Cp2ranCWRrv7Yax3Le4N59R6dd.SeyJ0IjoiZG0iLCJzIjoiaHR0cHM6Ly9zaXQtcHJpc20tbWVkaWF0b3IuYXRhbGFwcmlzbS5pbyIsInIiOltdLCJhIjpbImRpZGNvbW0vdjIiXX0.SeyJ0IjoiZG0iLCJzIjoid3NzOi8vc2l0LXByaXNtLW1lZGlhdG9yLmF0YWxhcHJpc20uaW8vd3MiLCJyIjpbXSwiYSI6WyJkaWRjb21tL3YyIl19",
      "RECIPIENT_DID" -> "did:peer:2.Ez6LSjrRmrSaHcrdThyW5qoWzcDxiJwJsmMdJumfFJb2HDDqu.Vz6MkwQxWcfEHkiyuguxVN44g1AuMQQn94nATHKcMyriee3wZ.SeyJ0IjoiZG0iLCJzIjoiZGlkOnBlZXI6Mi5FejZMU2dod1NFNDM3d25ERTFwdDNYNmhWRFVRelNqc0h6aW5wWDNYRnZNalJBbTd5LlZ6Nk1raGgxZTVDRVlZcTZKQlVjVFo2Q3AycmFuQ1dScnY3WWF4M0xlNE41OVI2ZGQuU2V5SjBJam9pWkcwaUxDSnpJam9pYUhSMGNITTZMeTl6YVhRdGNISnBjMjB0YldWa2FXRjBiM0l1WVhSaGJHRndjbWx6YlM1cGJ5SXNJbklpT2x0ZExDSmhJanBiSW1ScFpHTnZiVzB2ZGpJaVhYMC5TZXlKMElqb2laRzBpTENKeklqb2lkM056T2k4dmMybDBMWEJ5YVhOdExXMWxaR2xoZEc5eUxtRjBZV3hoY0hKcGMyMHVhVzh2ZDNNaUxDSnlJanBiWFN3aVlTSTZXeUprYVdSamIyMXRMM1l5SWwxOSIsInIiOltdLCJhIjpbImRpZGNvbW0vdjIiXX0",
      "MSG" -> "Test Message",
    ),
  )
