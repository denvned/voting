import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Voting {
    public static final int NUM_WINNERS = 5;

    public static void main(String[] args) throws IOException {
        ScoresTable scoresTable = ScoresTableParser.parse(new BufferedReader(new InputStreamReader(System.in)));

        ComparisonTable comparisonTable = new ComparisonTable(scoresTable);
        System.out.println("=================================================");
        comparisonTable.print(System.out);
        System.out.println("=================================================");
        System.out.println("=================================================");

        while (true) {
            NormalizedScoresTable normalizedScores = new NormalizedScoresTable(scoresTable);
            NormalizedScoresTablePrinter.print(System.out, normalizedScores);
            System.out.println();
            System.out.println("=================================================");
            System.out.println();

            ScoresTable newScoresTable = RoundPerformer.performRound(scoresTable);

            if (newScoresTable.getCandidatesCount() < NUM_WINNERS) {
                break;
            }

            scoresTable = newScoresTable;
        }
    }
}
