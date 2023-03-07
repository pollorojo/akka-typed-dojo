package com.dojo

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

import scala.annotation.tailrec
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.io.StdIn

object Main {
  // Paso 1-1: inicializar un sistema de actores con comportamiento vacio
  val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "PolloSystem")

  // akka://PolloSystem@127.0.0.1:8888/user/my-actor

  // Paso 3: Hacer spawn del actor creaado en el paso 2
  val actor: ActorRef[String] = system.systemActorOf(MyActor(), "my-actor")

  @tailrec
  // Paso 4-1: recibir por parametro la referencia del actor
  def commandLoop(actorRef: ActorRef[String]): Unit =
    StdIn.readLine("Ingrese un mensaje: \n") match {
      // Paso 1-2: Cuando se recibe "q" usar el metodo terminate para finalizar el sistema de actores
      case "q" =>
        system.terminate()
      case str: String =>
        // Paso 4-2: enviar al actor el mensaje recibido
        actorRef ! str
        commandLoop(actorRef)
      case _ =>
        println("Algo malió sal")
    }

  def main(args: Array[String]): Unit = {
    commandLoop(actor)
    // Paso 1-3: descomentar esta linea
    Await.ready(system.whenTerminated, Duration.Inf)
  }
}

// Paso 2: crear un objeto que defina el comportamiento de un actor para recibir e imprimir los mensajes recibidos
object MyActor {
  def apply(): Behavior[String] = Behaviors.receiveMessage{
    message => {
      println(message)
      Behaviors.same
    }
  }
}