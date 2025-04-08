package com.barbas.www.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Config {
    private final String name = "Barba´s Vanilla";
    private final String shortName = "Barba´s";
    private final String logo = "/img/logo-alfa.png";
    private final String copyright = "&copy; Copyright 2025 Barba´s Vanilla";
    private final int cookieHoursAge = 24;
}