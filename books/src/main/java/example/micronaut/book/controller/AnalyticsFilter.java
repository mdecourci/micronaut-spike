package example.micronaut.book.controller;

import example.micronaut.book.domain.Book;
import example.micronaut.book.service.AnalyticsClient;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Filter("/book/?*")
public class AnalyticsFilter implements HttpServerFilter {

    private final AnalyticsClient analyticsClient;

    public AnalyticsFilter(AnalyticsClient analyticsClient) {
        this.analyticsClient = analyticsClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return Flux.from(chain.proceed(request))
                .flatMap(response -> {
                    Book book = response.getBody(Book.class).orElse(null);
                    if (book != null) {
                        return Flux.from(analyticsClient.updateAnalytics("books", book)).map(b -> response);
                    } else {
                        return Flux.just(response);
                    }
                });
    }
}
