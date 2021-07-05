package com.example.simplescalabackend

import cats.effect.{ExitCode => CatsExitCode}
import com.example.simplescalabackend.configuration.{Configuration, apiConfig}
import com.example.simplescalabackend.http.Api
import com.example.simplescalabackend.persistence.{DBTransactor, UserPersistence, UserPersistenceService}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.putStrLn
import zio.interop.catz._


object Main extends App {

  type AppEnvironment = Configuration with Clock with DBTransactor with UserPersistence

  type AppTask[A] = RIO[AppEnvironment, A]

  val appEnvironment: ZLayer[Any, Throwable, Configuration with Blocking with DBTransactor with UserPersistence] =
    Configuration.live >+> Blocking.live >+> UserPersistenceService.transactorLive >+> UserPersistenceService.live

  override def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    val program: ZIO[AppEnvironment, Throwable, Unit] =
      for {
        _ <- UserPersistenceService.createUserTable
        api <- apiConfig
        httpApp = Router[AppTask](
          "/users" -> Api(s"${api.endpoint}/users").route
        ).orNotFound

        server <- ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
          BlazeServerBuilder[AppTask]
            .bindHttp(api.port, api.endpoint)
            .withHttpApp(CORS(httpApp))
            .serve
            .compile[AppTask, AppTask, CatsExitCode]
            .drain
        }
      } yield server

    program
      .provideSomeLayer[ZEnv](appEnvironment)
      .tapError(err => putStrLn(s"Execution failed with: $err"))
      .exitCode
  }
}
