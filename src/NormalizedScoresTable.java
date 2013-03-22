import java.math.BigInteger;
import java.util.*;

public class NormalizedScoresTable {
    private final Map<String, CandidateScores> candidatesScores = new HashMap<>();
    private final long[] votersScoresTotals;

    public NormalizedScoresTable(ScoresTable scoresTable) {
        for (String candidate : scoresTable.getCandidates()) {
            candidatesScores.put(candidate, new CandidateScores(scoresTable.getCandidateRawScores(candidate)));
        }

        votersScoresTotals = new long[scoresTable.getVotersCount()];

        calculateScoresSums();
    }

    public int getVotersCount() {
        return votersScoresTotals.length;
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

        CandidateScores candidateScores = candidatesScores.get(name);

        if (candidateScores == null) {
            throw new IllegalArgumentException();
        }

        return candidateScores.rawScores.clone();
    }

    public BigRational[] getCandidateNormalizedScores(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        CandidateScores candidateScores = candidatesScores.get(name);

        if (candidateScores == null) {
            throw new IllegalArgumentException();
        }

        return candidateScores.normalizedScores.clone();
    }

    public BigRational getCandidateScoresSum(String name) {
        if (name == null) {
            throw new NullPointerException();
        }

        CandidateScores candidateScores = candidatesScores.get(name);

        if (candidateScores == null) {
            throw new IllegalArgumentException();
        }

        return candidateScores.scoresSum;
    }

    public List<String> getCandidatesWithMaximalScores() {
        List<String> result = new ArrayList<>();
        BigRational maxScore = BigRational.ZERO;

        for (Map.Entry<String, CandidateScores> candidateScores : candidatesScores.entrySet()) {
            BigRational score = candidateScores.getValue().scoresSum;

            int cmp = score.compareTo(maxScore);
            if (cmp >= 0) {
                if (cmp > 0) {
                    result.clear();
                    maxScore = score;
                }

                result.add(candidateScores.getKey());
            }
        }

        return result;
    }

    public long[] getVotersScoresTotals() {
        return votersScoresTotals.clone();
    }

    private void calculateVotersScoresTotals() {
        for (int i = 0; i < votersScoresTotals.length; ++i) {
            long total = 0;

            for (CandidateScores candidate : candidatesScores.values()) {
                int rawScore = candidate.rawScores[i];
                assert rawScore >= 0;
                total += rawScore;
            }

            assert total >= 0;

            votersScoresTotals[i] = total;
        }
    }

    private void calculateScoresSums() {
        calculateVotersScoresTotals();

        for (CandidateScores candidate : candidatesScores.values()) {
            candidate.calculateScoresSum();
        }

        assert checkScoresSumsValidity();
    }

    protected boolean checkScoresSumsValidity() {
        BigRational totalsSum = BigRational.ZERO;

        for (int i = 0; i < votersScoresTotals.length; ++i) {
            BigRational total = BigRational.ZERO;

            for (CandidateScores candidate : candidatesScores.values()) {
                total = total.add(candidate.normalizedScores[i]);
            }

            if (votersScoresTotals[i] != 0) {
                if (total.signum() != 0) {
                    return false;
                }
            }
            else if (!total.equals(BigRational.ONE)) {
                return false;
            }

            totalsSum = totalsSum.add(total);
        }

        BigRational sumsTotal = BigRational.ZERO;

        for (CandidateScores candidate : candidatesScores.values()) {
            sumsTotal = sumsTotal.add(candidate.scoresSum);
        }

        return totalsSum.equals(sumsTotal);
    }

    private class CandidateScores {
        final int[] rawScores;
        final BigRational[] normalizedScores;
        BigRational scoresSum;

        CandidateScores(int[] scores) {
            assert scores.length == votersScoresTotals.length;

            rawScores = scores.clone();
            normalizedScores = new BigRational[scores.length];
        }

        void calculateScoresSum() {
            assert votersScoresTotals.length == rawScores.length;
            assert rawScores.length == normalizedScores.length;

            scoresSum = BigRational.ZERO;

            for (int i = 0; i < votersScoresTotals.length; ++i) {
                BigRational normalizedScore;

                long total = votersScoresTotals[i];

                if (total == 0) {
                    normalizedScore = BigRational.ZERO;
                }
                else {
                    assert total > 0;

                    int rawScore = rawScores[i];

                    assert rawScore >= 0;

                    normalizedScore = new BigRational(BigInteger.valueOf(rawScore), BigInteger.valueOf(total));
                }

                normalizedScores[i] = normalizedScore;

                scoresSum = scoresSum.add(normalizedScore);
            }
        }
    }
}
