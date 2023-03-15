package com.dojo

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object DeveloperActor {
  sealed trait Command
  case class ResolverTarea(name: String, replyTo: ActorRef[ManagerActor.Command]) extends Command
  case class TomarClona(tarea: String, replyTo: ActorRef[ManagerActor.Command]) extends Command
  case class ProgramadorEstresado(replyTo: ActorRef[ManagerActor.Command]) extends IllegalStateException("Timeout")

  def apply(): Behavior[Command] = working(0)

  def working(count: Int): Behavior[Command] = Behaviors.receiveMessage {
    case ResolverTarea(name, replyTo) => {
      val newCount = count + 1
      if (count >= 4) {
        println("Me estresé")
        stressed
      } else {
        replyTo ! ManagerActor.CerrarTarea(name)
        working(newCount)
      }
    }
    case _ => Behaviors.same
  }

  def stressed: Behavior[Command] = Behaviors.receiveMessage {
    case ResolverTarea(_, replyTo) =>
      replyTo ! ManagerActor.Mensaje("Me estresé")
      Behaviors.same
    case TomarClona(tarea, replyTo) =>
      replyTo ! ManagerActor.RecuperarDevelop(tarea)
      working(0)
    case _ => Behaviors.same
  }
}
