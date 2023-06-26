package com.example.starter

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj


class MainVerticle : AbstractVerticle() {

  override fun start() {
    val logger =  LoggerHandler.create()
    val router = Router.router(vertx)
    val exampleRepository = ExampleRepository()

    // logging
    router.route().handler(logger);

    // hello route
    router["/hello/*"]
      .respond { ctx: RoutingContext ->
        val path = ctx.request().path()
        ctx
          .response()
          .putHeader("Content-Type", "text/plain")
          .end("hello world! $path")
      }

    router.route(HttpMethod.GET, "/authors/:authorId")
      .handler { ctx: RoutingContext ->
        val authorId = ctx.pathParam("authorId")
        ctx.response()
          .putHeader("Content-Type", "text/plain")
          .end("hello $authorId")
    }

    router.route().method(HttpMethod.POST)

    // catch-all: mount the handler for all incoming requests at every path and HTTP method
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
