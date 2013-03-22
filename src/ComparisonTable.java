import java.io.PrintStream;
import java.math.BigInteger;
import java.util.*;

public class ComparisonTable {
    protected final Map<String, Map<String, BigRational>> comparisonTable = new HashMap<>();
    protected final Map<String, BigRational> averageScores = new HashMap<>();

    public ComparisonTable(ScoresTable scoresTable) {
        Set<String> candidates = scoresTable.getCandidates();

        int numCandidates = candidates.size();

        for (String candidate : candidates) {
            comparisonTable.put(candidate, new HashMap<String, BigRational>(numCandidates - 1));
            averageScores.put(candidate, BigRational.ZERO);
        }

        ScoresTable compare = new ScoresTable(scoresTable.getVotersCount());

        for (String candidate1 : candidates) {
            int[] rawScores1 = scoresTable.getCandidateRawScores(candidate1);

            for (String candidate2 : candidates) {
                if (candidate1.compareTo(candidate2) < 0) {
                    int[] rawScores2 = scoresTable.getCandidateRawScores(candidate2);

                    compare.removeAllCandidates();
                    compare.addCandidate(candidate1, rawScores1);
                    compare.addCandidate(candidate2, rawScores2);

                    NormalizedScoresTable normalizedScores = new NormalizedScoresTable(compare);

                    BigRational scoresSum1 = normalizedScores.getCandidateScoresSum(candidate1);
                    BigRational scoresSum2 = normalizedScores.getCandidateScoresSum(candidate2);

                    comparisonTable.get(candidate1).put(candidate2, scoresSum1);
                    comparisonTable.get(candidate2).put(candidate1, scoresSum2);

                    averageScores.put(candidate1, averageScores.get(candidate1).add(scoresSum1));
                    averageScores.put(candidate2, averageScores.get(candidate2).add(scoresSum2));
                }
            }
        }

        BigRational numOpponentsInv = new BigRational(BigInteger.ONE, BigInteger.valueOf(numCandidates - 1));

        for (String candidate : candidates) {
            BigRational averageScore = averageScores.get(candidate);
            averageScores.put(candidate, averageScore.multiply(numOpponentsInv));
        }
    }

    public void print(PrintStream out) {
        String[] candidates = new String[comparisonTable.size()];
        candidates = comparisonTable.keySet().toArray(candidates);

        Arrays.sort(candidates, new Comparator<String>() {
            @Override
            public int compare(String candidate1, String candidate2) {
                BigRational averageScore1 = averageScores.get(candidate1);
                BigRational averageScore2 = averageScores.get(candidate2);
                int cmp = averageScore2.compareTo(averageScore1);
                return cmp == 0 ? candidate1.compareTo(candidate2) : cmp;
            }
        });

        out.print('\t');

        for (String candidate : candidates) {
            out.print(candidate);
            out.print('\t');
        }

        out.println();

        for (String candidate1 : candidates) {
            out.print(candidate1);
            out.print('\t');

            Map<String, BigRational> comparisons = comparisonTable.get(candidate1);

            for (String candidate2 : candidates) {
                if (candidate1.equals(candidate2)) {
                    out.print("-\t");
                }
                else {
                    out.print(comparisons.get(candidate2));
                    out.print('\t');
                }
            }

            out.println(averageScores.get(candidate1));
        }
    }
}
