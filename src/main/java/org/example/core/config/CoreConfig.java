package org.example.core.config;

import org.example.core.CoreScanMarker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CoreScanMarker.class)
public class CoreConfig {
}
