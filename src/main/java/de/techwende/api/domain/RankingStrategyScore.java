package de.techwende.api.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

record AvgRankingEntry(AgendaItem item, double rank, double weight) implements Comparable<AvgRankingEntry> {
    @Override
    public int compareTo(AvgRankingEntry r) {
        if (rank == r.rank) {
            if (weight == r.weight) {
                return 0;
            }
            return weight < r.weight ? -1 : 1;
        }
        return rank < r.rank ? -1 : 1;
    }
}


public class RankingStrategyScore implements RankingStrategy {
    private static final double WEIGHT_FACTOR = 0.5;

    @Override
    public Ranking rank(List<Ranking> preferences) {
        Set<AgendaItem> items = preferences.stream().flatMap(r -> r.getItems().stream()).collect(Collectors.toSet());
        List<AvgRankingEntry> rankingResult = new ArrayList<>();

        for (AgendaItem item : items) {
            Set<RankingEntry> prefsSet =
                    preferences.stream().map(r -> r.getRank(item)).filter(Optional::isPresent).map(Optional::get)
                            .collect(Collectors.toSet());
            double avgRank = prefsSet.stream().mapToDouble(RankingEntry::rank).average().orElse(0);
            double avgWeight = prefsSet.stream().mapToDouble(RankingEntry::weight).average().orElse(0);
            rankingResult.add(new AvgRankingEntry(item, avgRank, avgWeight));
        }

        return resultOrdered(rankingResult);
    }

    private static Ranking resultOrdered(List<AvgRankingEntry> ranking) {
        List<AvgRankingEntry> rankingsSorted = ranking.stream().sorted(
                Comparator.comparingDouble(
                        r -> calculateItemScore(r.rank(), r.weight()))).toList();

        Ranking result = new Ranking();
        for (AvgRankingEntry entry : rankingsSorted) {
            result.setItem(entry.item(), rankingsSorted.indexOf(entry),
                    calculateItemScore(entry.rank(), entry.weight()));
        }

        return result;
    }

    private static double calculateItemScore(double rank, double weight) {
        return rank / (WEIGHT_FACTOR * weight);
    }
}
