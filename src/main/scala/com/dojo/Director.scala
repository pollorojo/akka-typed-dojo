package com.dojo

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.Behaviors

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

object Director {
  val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Dojo")
  val actorRef: ActorRef[ManagerActor.Command] = system.systemActorOf(ManagerActor(), "manager")

  @tailrec
  def commandLoop(): Unit =
    StdIn.readLine("comando: \n") match {
      case "q" =>
        system.terminate()
      case str: String =>
        actorRef ! ManagerActor.DesarrollarFeature(str)
        commandLoop()
    }

  def main(args: Array[String]): Unit = {
    commandLoop()
    Await.ready(system.whenTerminated, Duration.Inf)
  }
}