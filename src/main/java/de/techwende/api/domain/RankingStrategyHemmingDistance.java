package de.techwende.api.domain;

import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RankingStrategyHemmingDistance implements RankingStrategy {

    @Override
    public Ranking rank(List<Ranking> preferences) {
        Map<Ranking, Double> rankings = new HashMap<>();

        // if performance improvement is required, here would be a good place
        for (Ranking ranking1 : preferences) {
            for (Ranking ranking2 : preferences) {
                if (ranking1 == ranking2) {
                    rankings.put(ranking1, 0d);
                    continue;
                }

                double sumOld = rankings.getOrDefault(ranking1, 0d);
                sumOld += calculateDistance(ranking1, ranking2);
                rankings.put(ranking1, sumOld);
            }
        }

        return rankings.entrySet().stream().min(Comparator.comparingDouble(Entry::getValue)).get().getKey();
    }

    private double calculateDistance(Ranking ranking1, Ranking ranking2) {
        int diff = Sets.symmetricDifference(ranking1.getItems(), ranking2.getItems()).size();
        double distance = 0.5 * diff;

        List<AgendaItem> list1 = ranking1.getItemsOrdered();
        List<AgendaItem> list2 = ranking2.getItemsOrdered();

        for (int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            AgendaItem temp1 = list1.get(i);
            AgendaItem temp2 = list2.get(i);
            if (!temp1.equals(temp2)) {
                list2.set(list1.indexOf(temp1), temp2);
                list2.set(i, temp1);
                distance++;
            }
        }

        return distance;
    }
}
