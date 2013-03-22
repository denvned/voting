import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ScoresTableParser {
    public static ScoresTable parse(BufferedReader reader) throws IOException {
        int votersCount = Integer.parseInt(reader.readLine());

        if (votersCount < 0) {
            throw new IOException();
        }

        int candidatesCount = Integer.parseInt(reader.readLine());

        if (candidatesCount < 0) {
            throw new IOException();
        }

        ScoresTable table = new ScoresTable(votersCount);

        for (int i = 0; i < candidatesCount; ++i) {
            StringTokenizer tokenizer = new StringTokenizer(reader.readLine(), "\t");

            String name = tokenizer.nextToken();

            if (table.getCandidates().contains(name)) {
                throw new IOException();
            }

            int[] scores = new int[votersCount];

            for (int j = 0; j < votersCount; ++j) {
                int score = Integer.parseInt(tokenizer.nextToken());

                if (score < 0) {
                    throw new IOException();
                }

                scores[j] = score;
            }

            table.addCandidate(name, scores);
        }

        return table;
    }
}
