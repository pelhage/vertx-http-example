package com.example.starter

import io.vertx.core.AbstractVerticle
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.RoutingContext
import com.example.starter.getFoo


class MainVerticle : AbstractVerticle() {

  override fun start() {
    val logger =  LoggerHandler.create()
    val router = Router.router(vertx)
    val exampleRepository = ExampleRepository()
    router.route().handler(logger);

    // mount the handler for all incoming requests at every path and HTTP method
    router.route().handler { context: RoutingContext ->
      // Get the address of the request
      val address = context.request().connection().remoteAddress()
      // get the query parameter 'name'
      val queryParams = context.queryParams()
      val name = queryParams.get("name") ?: "unknown"

      context.json(
        json {
          obj(
            "name" to name,
            "address" to address,
            "message" to "Hello $name connected from $address",
            "foo" to getFoo(repository = exampleRepository)
          )
        }
      )
    }

    // create the HTTP server
    vertx
      // Handle every request using the router
      .createHttpServer()
      .requestHandler(router)
      .listen(8888)
      .onSuccess { server ->
        println("HTTP server started on port " + server.actualPort())
      }
  }
}
