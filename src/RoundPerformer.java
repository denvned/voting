import java.util.Collections;
import java.util.List;

public class RoundPerformer {
    public static ScoresTable performRound(ScoresTable scoresTable) {
        List<String> candidates = Collections.emptyList();

        ScoresTable tempScores = new ScoresTable(scoresTable);

        while (tempScores.getCandidatesCount() > 0) {
            NormalizedScoresTable normalizedScores = new NormalizedScoresTable(tempScores);
            candidates = normalizedScores.getCandidatesWithMaximalScores();
            tempScores.removeCandidates(candidates);
        }

        tempScores = new ScoresTable(scoresTable);
        tempScores.removeCandidates(candidates);
        return tempScores;
    }
}
