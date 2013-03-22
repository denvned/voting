import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

public class NormalizedScoresTablePrinter {
    public static void print(PrintStream out, final NormalizedScoresTable table) {
        String[] candidates = new String[table.getCandidatesCount()];
        candidates = table.getCandidates().toArray(candidates);

        Arrays.sort(candidates, new Comparator<String>() {
            @Override
            public int compare(String candidate1, String candidate2) {
                BigRational scores1 = table.getCandidateScoresSum(candidate1);
                BigRational scores2 = table.getCandidateScoresSum(candidate2);
                int comp = scores2.compareTo(scores1);
                return comp == 0 ? candidate1.compareTo(candidate2) : comp;
            }
        });

        for (String candidate : candidates) {
            printCandidate(out, table, candidate);
        }

        out.print("Totals:\t");

        for (long total : table.getVotersScoresTotals()) {
            out.print(total);
            out.print('\t');
        }

        out.println();
    }

    private static void printCandidate(PrintStream out, NormalizedScoresTable table, String candidate) {
        out.print(candidate);
        out.print('\t');

        int[] rawScores = table.getCandidateRawScores(candidate);
        BigRational[] normalizedScores = table.getCandidateNormalizedScores(candidate);

        for (int i = 0; i < normalizedScores.length; ++i) {
            BigRational normalizedScore = normalizedScores[i];

            int rawScore = rawScores[i];

            out.print(normalizedScore);
            out.print(" (");
            out.print(rawScore);
            out.print(")\t");
        }

        out.println(table.getCandidateScoresSum(candidate));
    }
}
