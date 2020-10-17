package models

import java.time.LocalDate

case class Person(id: Long,
                  name: String,
                  age: Int) extends HasId

case class PersonForm(name: String, age: Int)

// participants is an ordered list
case class Standup(id: Long,
                   date: LocalDate,
                   participants: List[Person])
