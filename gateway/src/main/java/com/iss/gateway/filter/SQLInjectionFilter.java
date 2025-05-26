package com.iss.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SQLInjectionFilter extends OncePerRequestFilter {

    private static final List<String> KEYWORDS = Arrays.asList(
            "select", "insert", "update", "delete", "drop", "truncate",
            "exec", "information_schema", "union", "sleep", "benchmark",
            "--", ";", "/*", "*/", "@@", "char(", "xp_"
    );

    private static final Pattern SUSPECT_PATTERN =
            Pattern.compile("(['\"`]).*?\\1\\s*(or|and)\\s+\\d+=\\d+", Pattern.CASE_INSENSITIVE);


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest sanitizedRequest = new SanitizedRequest(request);

        if (isMalicious(sanitizedRequest)) {
            log.warn("⛔ SQL Injection suspected! ip={}, uri={}, params={}",
                    request.getRemoteAddr(), request.getRequestURI(),
                    sanitizedRequest.getParameterMap());

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Bad request parameter");
            return;
        }

        filterChain.doFilter(sanitizedRequest, response);
    }

    private boolean isMalicious(HttpServletRequest req) {
        for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            for (String value : entry.getValue()) {
                if (value == null) continue;
                String lower = value.toLowerCase(Locale.ROOT);

                if (SUSPECT_PATTERN.matcher(lower).find()) {
                    return true;
                }
                for (String kw : KEYWORDS) {
                    if (lower.contains(kw)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String cleanValue(String raw) {
        return raw == null ? null : raw.replace("'", "''");
    }

    private static class SanitizedRequest extends HttpServletRequestWrapper {
        private final Map<String, String[]> sanitized = new HashMap<>();

        SanitizedRequest(HttpServletRequest request) {
            super(request);
            request.getParameterMap().forEach((k, v) -> sanitized.put(
                    k,
                    Arrays.stream(v)
                            .map(SQLInjectionFilter::cleanValue)  // 调用外部静态方法
                            .toArray(String[]::new)
            ));
        }

        @Override
        public String getParameter(String name) {
            return Optional.ofNullable(sanitized.get(name))
                    .map(arr -> arr.length > 0 ? arr[0] : null)
                    .orElse(null);
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return Collections.enumeration(sanitized.keySet());
        }

        @Override
        public String[] getParameterValues(String name) {
            return sanitized.get(name);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return Collections.unmodifiableMap(sanitized);
        }
    }
}
