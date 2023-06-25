package com.example.starter

interface DataObj {
  var foo: String
}
class ExampleRepository {

  fun getData(): DataObj {
    return object: DataObj {
      override var foo: String = "bar"
    }
  }
}
