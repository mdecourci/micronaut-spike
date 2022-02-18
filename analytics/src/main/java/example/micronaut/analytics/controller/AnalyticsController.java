package example.micronaut.analytics.controller;

import example.micronaut.analytics.domain.BookAnalytics;
import example.micronaut.analytics.service.AnalyticsService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/analytics")
public class AnalyticsController  {
    @Inject
    private AnalyticsService analyticsService;

    @Get
    public List<BookAnalytics> listAnalytics() {
        System.out.println("listAnalytics()");
        return analyticsService.listAnalytics();
    }
}
