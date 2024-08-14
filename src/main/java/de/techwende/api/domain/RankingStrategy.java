package de.techwende.api.domain;

import java.util.List;

public interface RankingStrategy {

    Ranking rank(List<Ranking> preferences);

}
