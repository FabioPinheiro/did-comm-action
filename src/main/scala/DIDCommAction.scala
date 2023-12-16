// //> using scala 3.3
// //> using dep app.fmgp::did::0.1.0-M16

import zio._
import zio.json._

import fmgp.crypto.*
import fmgp.did.*
import fmgp.did.comm.*
import fmgp.did.method.peer.*
import fmgp.did.comm.protocol.basicmessage2.BasicMessage
import fmgp.crypto.error.DIDSubjectNotSupported

val alice = DIDPeer2.makeAgent(
  Seq(
    OKPPrivateKey( // keyAgreement
      kty = KTY.OKP,
      crv = Curve.X25519,
      d = "Z6D8LduZgZ6LnrOHPrMTS6uU2u5Btsrk1SGs4fn8M7c",
      x = "Sr4SkIskjN_VdKTn0zkjYbhGTWArdUNE4j_DmUpnQGw",
      kid = None
    ),
    OKPPrivateKey( // keyAuthentication
      kty = KTY.OKP,
      crv = Curve.Ed25519,
      d = "INXCnxFEl0atLIIQYruHzGd5sUivMRyQOzu87qVerug",
      x = "MBjnXZxkMcoQVVL21hahWAw43RuAG-i64ipbeKKqwoA",
      kid = None
    )
  )
  // Seq(DIDPeerServiceEncoded("https://alice.did.fmgp.app/"))
)
//[{"kty":"OKP","crv":"X25519","d":"Z6D8LduZgZ6LnrOHPrMTS6uU2u5Btsrk1SGs4fn8M7c","x":"Sr4SkIskjN_VdKTn0zkjYbhGTWArdUNE4j_DmUpnQGw"},{"kty":"OKP","crv":"Ed25519","d":"INXCnxFEl0atLIIQYruHzGd5sUivMRyQOzu87qVerug","x":"MBjnXZxkMcoQVVL21hahWAw43RuAG-i64ipbeKKqwoA"}]

/** scala-cli run DIDCommAction.scala --dependency app.fmgp::did::0.1.0-M16 -S 3.3.1
  */
object DIDCommAction extends ZIOAppDefault {
  override val run = {
    for {
      _ <- Console.printLine(
        """█▀▄ █ █▀▄   █▀▀ █▀█ █▀▄▀█ █▀▄▀█   ▄▀█ █▀▀ ▀█▀ █ █▀█ █▄░█
          |█▄▀ █ █▄▀   █▄▄ █▄█ █░▀░█ █░▀░█   █▀█ █▄▄ ░█░ █ █▄█ █░▀█
          |Github action to send message over DID Comm v2
          |Vist: https://github.com/FabioPinheiro/did-comm-action""".stripMargin
      )
      // args <- getArgs
      // _ <- Console.printLine(args)
      // sender <- System.env("SENDER_DID").orDie
      mAgent <- System.env("SENDER_KEYS").map {
        case None => Left("Missing Env SENDER_KEYS")
        case Some(data) =>
          data.fromJson[Seq[PrivateKey]] match
            case Left(value)  => Left("Fail to parse SENDER_KEYS")
            case Right(value) => Right(DIDPeer2.makeAgent(value))
      }
      sender <- ZIO.fromEither(mAgent)

      mRecipientDID <- System.env("RECIPIENT_DID").map {
        case None        => Left("Missing Env RECIPIENT_DID")
        case Some(value) => DIDSubject.either(value)
      }
      recipientDID <- ZIO.fromEither(mRecipientDID)

      msgText <- System.env("MSG").map(_.getOrElse("empty"))
      _ <- Console.printLine(msgText)
      _ <- Console.printLine(alice.id.asDIDSubject)

      basicMessage = BasicMessage(
        from = Some(alice.id),
        to = Set(recipientDID.asTO),
        content = msgText
      )
      msg <- Operations
        .sign(basicMessage.toPlaintextMessage)
        .provideSomeLayer(
          ZLayer.succeed(alice)
        )
      // TODO SEND
      _ <- Console.printLine(msg.toJsonPretty)
    } yield ()
  }.provide(
    Operations.layerDefault ++ DidPeerResolver.layer
  )

}
