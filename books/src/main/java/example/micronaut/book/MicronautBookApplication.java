package example.micronaut.book;

import io.micronaut.runtime.Micronaut;

import static io.micronaut.context.env.Environment.DEVELOPMENT;

public class MicronautBookApplication {

    public static void main(String[] args) {
        Micronaut.build(args).mainClass(MicronautBookApplication.class).defaultEnvironments(DEVELOPMENT).start();
    }
}
