package de.techwende.api.service;

import de.techwende.api.domain.AgendaItem;
import de.techwende.api.domain.Ranking;
import de.techwende.api.domain.RankingEntry;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankService {
    private static final RankingEntry NO_ENTRY = new RankingEntry(Integer.MAX_VALUE, 0);

    private static List<AgendaItem> resultOrdered(Ranking result) {
        return result.getItems().stream()
                .sorted(Comparator.comparingInt(r -> result.getRank(r).orElse(NO_ENTRY).rank()))
                .collect(Collectors.toList());
    }

}
