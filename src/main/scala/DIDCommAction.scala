// //> using scala 3.3
// //> using dep app.fmgp::did::0.1.0-M16

import zio._
import zio.json._
import zio.http._

import fmgp.crypto.*
import fmgp.crypto.error.*
import fmgp.did.*
import fmgp.did.comm.*
import fmgp.did.method.peer.*
import fmgp.did.comm.protocol.basicmessage2.BasicMessage
import fmgp.did.framework.*

object DIDCommAction extends ZIOAppDefault {

  private val transportDispatcherLayer = ZLayer.fromZIO({
    for {
      transportFactory <- ZIO.service[TransportFactory]
      transportDispatcher = new TransportDispatcher {
        def send(
            to: comm.TO,
            msg: comm.SignedMessage | comm.EncryptedMessage,
            thid: Option[comm.MsgID],
            pthid: Option[comm.MsgID]
        ): zio.ZIO[Resolver & Agent & comm.Operations, DidFail, Unit] =
          super.sendViaDIDCommMessagingService(to, msg).unit

        override def openTransport(uri: String): UIO[TransportDIDComm[Any]] =
          transportFactory.openTransport(uri)
      }
    } yield transportDispatcher
  }) // .provideLayer(transportFactoryLayer))

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
      senderAgent: Agent <- ZIO.fromEither(mAgent)

      mRecipientDID <- System.env("RECIPIENT_DID").map {
        case None        => Left("Missing Env RECIPIENT_DID")
        case Some(value) => DIDSubject.either(value)
      }
      recipient <- ZIO.fromEither(mRecipientDID).map(_.asTO)

      msgText <- System.env("MSG").map(_.getOrElse("empty"))
      _ <- Console.printLine(msgText)
      _ <- Console.printLine(senderAgent.id.asDIDSubject)

      encrypted <- System.env("ENCRYPTED").map {
        case None => false
        case Some(value) =>
          value.toBooleanOption match
            case None        => false
            case Some(value) => value
      }
      basicMessage = BasicMessage(
        from = Some(senderAgent.id),
        to = Set(recipient),
        content = msgText
      )
      msg <-
        if (encrypted) {
          Operations
            .sign(basicMessage.toPlaintextMessage)
            .provideSomeLayer(
              ZLayer.succeed(senderAgent)
            )
        } else {
          Operations
            .encrypt(basicMessage.toPlaintextMessage)
            .provideSomeLayer(
              ZLayer.succeed(senderAgent)
            )
        }
      transportDispatcher <- ZIO.service[TransportDispatcher]
      _ <- transportDispatcher
        .send(recipient, msg, None, None)
        .provideSomeEnvironment((env: ZEnvironment[Resolver & Operations]) => env ++ ZEnvironment(senderAgent))
      _ <- Console.printLine(msg.toJson) // msg.toJsonPretty
    } yield ()
  }.provide(
    Scope.default,
    Client.default,
    TransportFactoryImp.layer,
    Operations.layerDefault,
    DidPeerResolver.layer,
    transportDispatcherLayer
  )

}
