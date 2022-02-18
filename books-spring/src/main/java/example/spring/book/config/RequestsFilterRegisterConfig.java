package example.spring.book.config;

import example.spring.book.controller.AnalyticsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

//@Configuration
public class RequestsFilterRegisterConfig {

    //    @Bean
    public FilterRegistrationBean<AnalyticsFilter> registerPostCommentsRateLimiter(AnalyticsFilter analyticsFilter) {
        FilterRegistrationBean<AnalyticsFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(analyticsFilter);
        registrationBean.addUrlPatterns("/book/?*");

        return registrationBean;
    }
}
