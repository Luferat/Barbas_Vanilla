package com.barbas.core.util;

import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonResponse {

    public static ResponseEntity<Map<String, Object>> success(int httpStatus, String message) {
        return build(httpStatus, "success", message, null);
    }

    public static ResponseEntity<Map<String, Object>> success(int httpStatus, String message, Object data) {
        return build(httpStatus, "success", message, data);
    }

    public static ResponseEntity<Map<String, Object>> error(int httpStatus, String message) {
        return build(httpStatus, "error", message, null);
    }

    public static ResponseEntity<Map<String, Object>> error(int httpStatus, String message, Object data) {
        return build(httpStatus, "error", message, data);
    }

    private static ResponseEntity<Map<String, Object>> build(int httpStatus, String statusStr, String message, Object data) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", httpStatus);
        body.put("status", statusStr);
        body.put("message", message);
        if (data != null) {
            body.put("data", data);
        }
        return ResponseEntity.status(httpStatus).body(body);
    }
}
