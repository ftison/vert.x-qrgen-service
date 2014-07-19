package fr.tison.vertx.service;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.streams.Pump;
import org.vertx.java.platform.Verticle;

/**
 * Created by 20002299 on 19/07/2014.
 */
public class QrCodeVerticle extends Verticle {
    @Override
    public void start() {
        container.deployModule("fr.tison.vertx.module~qrcode-generator~1.0-SNAPSHOT");

/*        RouteMatcher routeMatcher = new RouteMatcher();

        routeMatcher.get("/:value", new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest req) {
                JsonObject in = new JsonObject().putString("value", req.params().get("value"));
                vertx.eventBus().send("fr.tison.qrcode.generator", in, new Handler<Message<Buffer>>() {
                    @Override
                    public void handle(Message<Buffer> reply) {
                        req.response().end(reply.body());
                    }
                });

            }
        });

        routeMatcher.noMatch(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                req.response().end("Nothing matched");
            }
        });
*/
        Handler<HttpServerRequest> handler = new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest req) {
                JsonObject in = new JsonObject().putString("value", req.resume().uri().substring(1)); // substring(1) to skip first char "/"
                vertx.eventBus().send("fr.tison.qrcode.generator", in, new Handler<Message<Buffer>>() {
                    @Override
                    public void handle(Message<Buffer> reply) {
                        req.response().end(reply.body());
                    }
                });

            }
        };

        vertx.createHttpServer().requestHandler(handler).listen(8888);

        container.logger().info("Webserver started, listening on port: 8888");
    }
}
