package example.spring.book.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.spring.book.domain.Book;
import example.spring.book.service.AnalyticsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
@Component
@Slf4j
@WebFilter(urlPatterns = "/book/*", filterName = "analyticsFilter")
public class AnalyticsFilter extends HttpFilter {

    private final AnalyticsClient analyticsClient;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var bookResponseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(request, bookResponseWrapper);

        var bookJson = new String(bookResponseWrapper.getContentAsByteArray());

        Book book = null;
        try {
            book = objectMapper.readValue(bookJson, Book.class);
            analyticsClient.updateAnalytics(book);
        } catch (JsonProcessingException e) {
            log.error("Error processing book json from response in HTTP filter");
        }

        bookResponseWrapper.copyBodyToResponse();
    }
}
