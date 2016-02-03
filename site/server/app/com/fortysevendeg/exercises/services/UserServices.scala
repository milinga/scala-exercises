package com.fortysevendeg.exercises.services

import scala.language.implicitConversions

import cats.data.Xor
import doobie.imports._
import scalaz._, Scalaz._
import scalaz.concurrent.Task
import scala.concurrent.{ Future, ExecutionContext }
import com.fortysevendeg.exercises.models.{ UserDoobieStore, NewUser }
import shared.User

trait UserServices {
  def getUserOrCreate(
    login:      String,
    name:       String,
    githubId:   String,
    pictureUrl: String,
    githubUrl:  String,
    email:      String
  ): Task[Option[User]]
}

class UserServiceImpl(
    implicit
    transactor: Transactor[Task]
) extends UserServices {
  def getUserOrCreate(
    login:      String,
    name:       String,
    githubId:   String,
    pictureUrl: String,
    githubUrl:  String,
    email:      String
  ): Task[Option[User]] = {
    (for {
      maybeUser ← UserDoobieStore.getByLogin(login)
      theUser ← if (maybeUser.isDefined) maybeUser.point[ConnectionIO] else UserDoobieStore.create(
        NewUser(
          login,
          name,
          githubId,
          pictureUrl,
          githubUrl,
          email
        )
      )
    } yield theUser).transact(transactor)
  }
}

object UserServices {
  implicit def instance(
    implicit
    transactor: Transactor[Task]
  ): UserServices = new UserServiceImpl
}
