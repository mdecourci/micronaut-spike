package example.micronaut.analytics;

import io.micronaut.runtime.Micronaut;

import static io.micronaut.context.env.Environment.DEVELOPMENT;

public class AnalyticsApplication {

    public static void main(String[] args) {
        Micronaut.build(args)
                .mainClass(AnalyticsApplication.class)
                .defaultEnvironments(DEVELOPMENT)
                .start();
    }
}
