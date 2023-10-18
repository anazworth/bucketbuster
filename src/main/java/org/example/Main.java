package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;

import java.util.Random;

import static org.eclipse.jetty.util.StringUtil.toInt;

public class Main {
    private final static int RATELIMIT = 1000;
    private final static int RATELIMITPERIOD = 10; // seconds


    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final JedisPooled pool = new JedisPooled(System.getenv("REDIS_URL"), 6379);

    public static void main(String[] args) {


        var app = Javalin.create()
                // Send every request through the rate limit check *before* handling the request
                .before(Main::checkRateLimit)
                .get("/", ctx -> {
                    // Early return if the user has exceeded the rate limit
                    if (ctx.attribute("rateLimitExceeded") != null) {
                        ctx.header("Retry-After", String.valueOf(RATELIMITPERIOD));
                        ctx.status(429).result("Rate limit exceeded");
                        return;
                    }
                    // Simulate complicated processing
                    try {
                        String randomNumber = String.valueOf(new Random().nextInt(1000));
                        ctx.result(randomNumber);
                        log.info("Request from {}, returned {}", ctx.ip(), randomNumber);
                        if (Integer.parseInt(randomNumber) % 100 == 0) {
                            log.warn("Suspicious request from {}", ctx.ip());
                        }
                    } catch (Exception e) {
                        log.error("Error processing request", e);
                        ctx.status(500).result("Error processing request");
                    }
                })
                .start(8080);
    }

    public static void checkRateLimit(Context ctx) {
        String userIp = ctx.ip();

        String bucket = pool.get(userIp);

        // Null bucket indicates that this is the first request from this IP
        if (bucket == null) {
            pool.set(userIp, String.valueOf(RATELIMIT - 1));
            pool.expire(userIp, RATELIMITPERIOD);
            return;
        }
        if (Integer.parseInt(bucket) > 0) {
            pool.decr(userIp);
            return;
        }
        ctx.attribute("rateLimitExceeded", true);
        log.error("Rate limit exceeded for {}", userIp);
    }
}