package src.PPClean.Similarity;

/**
 * Levenshtein String similarity
 */
public class Levenshtein implements StringSimilarity {
    public Levenshtein() {
    }

    /**
     * Calculates Levenshtein String similarity for x and y
     * @param x
     * @param y
     * @return Similarity score in range [0,1]
     */
    @Override
    public double compare(String x, String y) {
        double res = 0;
        int m = x.length();
        int n = y.length();
        // BEGIN SOLUTION
//        res = 1 - (double) LD(x, y) / Math.max(m, n);
        // ---------------------------------
        if (m == 0) return n == 0 ? 1.0 : 0.0;
        if (n == 0) return 0.0;

        int[][] matrix = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            matrix[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = x.charAt(i - 1) == y.charAt(j - 1) ? 0 : 1;
                matrix[i][j] = Math.min(
                        Math.min(
                                matrix[i - 1][j] + 1, // deletion
                                matrix[i][j - 1] + 1 // insertion
                        ),
                        matrix[i - 1][j - 1] + cost // substitution
                );
            }
        }

        res = 1 - (double) matrix[m][n] / Math.max(m, n);
        // END SOLUTION
        return res;
    }

    private int LD(String x, String y) {
        int m = x.length() - 1;
        int n = y.length() - 1;
        if (m == -1) {
            return y.length();
        }
        if (n == -1) {
            return x.length();
        }

        if (x.charAt(m) == y.charAt(n)) {
            return LD(x.substring(0 , m), y.substring(0, n));
        }

        return 1 + Math.min(
                LD(x.substring(0, m), y.substring(0, n)),
                Math.min(
                        LD(x.substring(0, m), y),
                        LD(x, y.substring(0, n))
                )
        );
    }
}
