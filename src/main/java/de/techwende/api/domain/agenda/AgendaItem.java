package de.techwende.api.domain.agenda;

import org.springframework.lang.Nullable;

public record AgendaItem(String name,
                         String description,
                         double estimatedDuration,
                         @Nullable AgendaItemConstraints constraints) {

}
