package de.techwende.api.domain.ranking;

import de.techwende.exception.RankingFailedException;

import java.util.List;

public interface RankingStrategy {

    Ranking rank(List<Ranking> preferences) throws RankingFailedException;

}
