import java.util.*;

public class ScoresTable {
    protected final int votersCount;
    protected final Map<String, int[]> candidatesScores = new HashMap<>();

    public ScoresTable(int votersCount) {
        if (votersCount < 0) {
            throw new IllegalArgumentException();
        }

        this.votersCount = votersCount;
    }

    public ScoresTable(ScoresTable other) {
        votersCount = other.votersCount;

        for (Map.Entry<String, int[]> entry : other.candidatesScores.entrySet()) {
            candidatesScores.put(entry.getKey(), entry.getValue().clone());
        }
    }

    public int getVotersCount() {
        return votersCount;
    }

    public int getCandidatesCount() {
        return candidatesScores.size();
    }

    public Set<String> getCandidates() {
        return Collections.unmodifiableSet(candidatesScores.keySet());
    }

    public int[] getCandidateRawScores(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        int[] candidateScores = candidatesScores.get(name);

        if (candidateScores == null) {
            throw new IllegalArgumentException();
        }

        return candidateScores;
    }

    public void addCandidate(String name, int[] scores) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (candidatesScores.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        if (scores.length != votersCount) {
            throw new IllegalArgumentException();
        }
        for (int score : scores) {
            if (score < 0) {
                throw new IllegalArgumentException();
            }
        }

        candidatesScores.put(name, scores.clone());
    }

    public void removeCandidate(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        if (candidatesScores.remove(name) == null) {
            throw new IllegalArgumentException();
        }
    }

    public void removeCandidates(List<String> candidates) {
        for (String candidate : candidates) {
            removeCandidate(candidate);
        }
    }

    public void removeAllCandidates() {
        candidatesScores.clear();
    }
}
