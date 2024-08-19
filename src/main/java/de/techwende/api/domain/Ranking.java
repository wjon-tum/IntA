package de.techwende.api.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

public class Ranking {

    private final Map<AgendaItem, RankingEntry> rankings;

    public Ranking() {
        rankings = new HashMap<>();
    }

    public void setItem(AgendaItem item, int rank, double weight) {
        rankings.put(item, new RankingEntry(rank, weight));
    }

    public Optional<RankingEntry> getRank(AgendaItem item) {
        return Optional.ofNullable(rankings.get(item));
    }

    public void deleteItem(AgendaItem item) {
        rankings.remove(item);
    }

    public List<AgendaItem> getItemsOrdered() {
        return rankings.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().rank()))
                .map(Entry::getKey)
                .toList();
    }

    public Set<AgendaItem> getItems() {
        return rankings.keySet();
    }
}
