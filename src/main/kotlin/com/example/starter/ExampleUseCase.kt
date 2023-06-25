package com.example.starter
import com.example.starter.ExampleRepository
fun getFoo(repository: ExampleRepository): String  {
  val someData = repository.getData()

  return someData.foo
}
