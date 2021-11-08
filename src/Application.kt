package com.alban.webapi.tests

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.delay
import java.io.File

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun String.isJsonFile() = this.toLowerCase().contains(".json")
fun getFile(name: String): File = File("./resources/$name")

suspend fun PipelineContext<*, ApplicationCall>.respondContent(
    param: String,
    delayStr: String?,
    statusCode: Int = 200
) {
    delayStr?.let {
        delay(it.toLong())
    }
    val statusCode = HttpStatusCode.fromValue(statusCode)
    if (param.isJsonFile()) {
        call.response.status(statusCode)
        call.respondFile(getFile(param))
    } else {
        call.respondText(param, ContentType.Application.Json, statusCode)
    }
}

fun logRoute(message: List<String>) {
    println("Route mapped -> ${message[0]}, Method=${message[1]}")
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val configuration = "routes.routesList"

    install(ContentNegotiation) {
        gson {
        }
    }

    val routes = environment.config.propertyOrNull(configuration)?.getList()

    routing {
        routes?.let {
            routes.forEach { route ->
                route.split(";").let { params ->
                    logRoute(params)
                    when (params[1]) {
                        HttpMethod.Get.value -> {
                            get(params[0]) {
                                respondContent(params[2], params.getOrNull(3), params.getOrNull(4)?.toInt() ?: 200)
                            }
                        }
                        HttpMethod.Post.value -> {
                            post(params[0]) {
                                respondContent(params[2], params.getOrNull(3), params.getOrNull(4)?.toInt() ?: 200)
                            }
                        }
                        HttpMethod.Put.value -> {
                            put(params[0]) {
                                respondContent(params[2], params.getOrNull(3), params.getOrNull(4)?.toInt() ?: 200)
                            }
                        }
                        HttpMethod.Delete.value -> {
                            delete(params[0]) {
                                respondContent(params[2], params.getOrNull(3), params.getOrNull(4)?.toInt() ?: 200)
                            }
                        }
                    }
                }
            }
        }
    }
}

