package de.techwende.api.service;

import de.techwende.api.domain.agenda.AgendaItem;
import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.ranking.RankingEntry;
import de.techwende.api.domain.ranking.RankingStrategy;
import de.techwende.api.domain.ranking.RankingStrategyHemmingDistance;
import de.techwende.api.domain.ranking.RankingStrategyScore;
import de.techwende.exception.RankingFailedException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankingService {
    private static final RankingEntry NO_ENTRY = new RankingEntry(Integer.MAX_VALUE, 0);
    private final RankingStrategyScore rankingStrategyScore;
    private final RankingStrategyHemmingDistance rankingStrategyHemmingDistance;

    public RankingService(RankingStrategyScore rankingStrategyScore,
            RankingStrategyHemmingDistance rankingStrategyHemmingDistance) {
        this.rankingStrategyScore = rankingStrategyScore;
        this.rankingStrategyHemmingDistance = rankingStrategyHemmingDistance;
    }

    public List<AgendaItem> computeRankingResultList(List<Ranking> preferences, boolean resultOfExisting)
            throws RankingFailedException {
        RankingStrategy chosenRankingStrategy;
        if (resultOfExisting) {
            chosenRankingStrategy = rankingStrategyScore;
        } else {
            chosenRankingStrategy = rankingStrategyHemmingDistance;
        }

        Ranking result = chosenRankingStrategy.rank(preferences);
        return resultOrdered(result);
    }

    private List<AgendaItem> resultOrdered(Ranking result) {
        return result.getItems().stream()
                .sorted(Comparator.comparingInt(r -> result.getRank(r).orElse(NO_ENTRY).rank()))
                .collect(Collectors.toList());
    }

}
