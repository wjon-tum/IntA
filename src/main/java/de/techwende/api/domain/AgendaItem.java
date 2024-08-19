package de.techwende.api.domain;

import org.springframework.lang.Nullable;

public record AgendaItem(String name,
                         String description,
                         double estimatedDuration,
                         @Nullable AgendaItemConstraints constraints) {

}
