package ctbiyi.com.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures. The error response follows RFC7807 - Problem Details for
 * HTTP APIs (https://tools.ietf.org/html/rfc7807)
 */
@RestControllerAdvice
public class ExceptionTranslator {
}

