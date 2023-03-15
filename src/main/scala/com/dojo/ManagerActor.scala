package com.dojo

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

object ManagerActor {
  sealed trait Command
  case class DesarrollarFeature(tarea: String) extends Command
  case class CerrarTarea(name: String) extends Command
  case class RecuperarDevelop(tarea: String) extends Command
  case class Mensaje(mensaje: String) extends Command

  def apply(): Behavior[Command] = Behaviors.setup {
    context => working(context)
  }

  def working(context: ActorContext[Command]): Behavior[Command] = Behaviors.receiveMessage {
    case DesarrollarFeature(name) =>
      developer(context) ! DeveloperActor.ResolverTarea(name, context.self)
      Behaviors.same
    case CerrarTarea(name) =>
      println(s"Cerramos la tarea $name!")
      Behaviors.same
    case RecuperarDevelop(tarea) =>
      println("Recuperamos un dev!")
      developer(context) ! DeveloperActor.ResolverTarea(tarea, context.self)
      Behaviors.same
    case Mensaje(mensaje) =>
      println(s"El dev dice: $mensaje")
      developer(context) ! DeveloperActor.TomarClona("leer doc", context.self)
      Behaviors.same
    case _ => Behaviors.same
  }

  private def developer(context: ActorContext[_]) = {
    context.child("el-esclavito")
      .getOrElse(context.spawn(DeveloperActor(), "el-esclavito"))
      .asInstanceOf[ActorRef[DeveloperActor.Command]]
  }
}