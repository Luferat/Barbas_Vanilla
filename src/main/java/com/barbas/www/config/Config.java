package com.barbas.www.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Component
public class Config {
    private final String name = "Barba´s Vanilla";
    private final String shortName = "Barba´s";
    private final String logo = "/img/logo-alfa.png";
    private final String copyright = "&copy; Copyright 2025 Barba´s Vanilla";
    private final int cookieHoursAge = 24;

    public Map<String, String> getAll() {
        return Map.ofEntries(
                Map.entry("name", name),
                Map.entry("shortName", shortName),
                Map.entry("logo", logo),
                Map.entry("copyright", copyright),
                Map.entry("cookieHoursAge", String.valueOf(cookieHoursAge))
        );
    }
}