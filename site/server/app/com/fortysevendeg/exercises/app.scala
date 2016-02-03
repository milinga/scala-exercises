package com.fortysevendeg.exercises

import cats.data.Coproduct

import com.fortysevendeg.shared.free.ExerciseOp
import com.fortysevendeg.exercises.services.free.UserOp
import com.fortysevendeg.exercises.services.free.DBResult

object app {
  type ExercisesAndUserOps[A] = Coproduct[ExerciseOp, UserOp, A]
  type ExercisesApp[A] = Coproduct[ExercisesAndUserOps, DBResult, A]
}

