package de.techwende.api.domain.ranking;

public record RankingEntry(int rank, double weight) implements Comparable<RankingEntry> {

    @Override
    public int compareTo(RankingEntry r) {
        if (rank == r.rank) {
            if (weight == r.weight) {
                return 0;
            }
            return weight < r.weight ? -1 : 1;
        }
        return rank < r.rank ? -1 : 1;
    }
}
