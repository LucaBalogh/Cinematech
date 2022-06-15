package ro.ubb.tt.bll.AI;

import java.util.Map;

// First, we calculate the sum of squared differences between corresponding entries.
// Then, by applying the sqrt function, we compute the actual Euclidean distance.

public class EuclideanDistance implements Distance {

    @Override
    public double calculate(Map<String, Double> f1, Map<String, Double> f2) {
        double sum = 0;
        for (String key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);

            if (v1 != null && v2 != null) {
                sum += Math.pow(v1 - v2, 2);
            }
        }

        return Math.sqrt(sum);
    }
}
