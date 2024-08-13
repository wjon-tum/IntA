package de.techwende.api.service;

import de.techwende.api.domain.AgendaItem;
import de.techwende.api.domain.Ranking;
import de.techwende.api.domain.RankingEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class RankService {

    private static final double WEIGHT_FACTOR = 0.5;

    public static Ranking rank(List<Ranking> preferences) {
        Set<AgendaItem> items = preferences.stream().flatMap(r -> r.getItems().stream()).collect(Collectors.toSet());
        Ranking rankingResult = new Ranking();

        for (AgendaItem item : items) {
            Set<RankingEntry> prefsSet =
                    preferences.stream().map(r -> r.getRank(item)).filter(Optional::isPresent).map(Optional::get)
                            .collect(Collectors.toSet());
            double avgRank = prefsSet.stream().mapToDouble(RankingEntry::rank).average().orElse(0);
            double avgWeight = prefsSet.stream().mapToDouble(RankingEntry::weight).average().orElse(0);
            rankingResult.setItem(item, avgRank, avgWeight);
        }

        return rankingResult;
    }

    public static List<AgendaItem> resultListOrdered(Ranking result) {
        return result.getItems().stream().sorted(
                        Comparator.comparingDouble(
                                r -> calculateItemScore(result.getRank(r).orElse(new RankingEntry(-1, -1)))))
                .collect(Collectors.toList());
    }

    private static double calculateItemScore(RankingEntry entry) {
        return entry.rank() / (WEIGHT_FACTOR * entry.weight());
    }

}
