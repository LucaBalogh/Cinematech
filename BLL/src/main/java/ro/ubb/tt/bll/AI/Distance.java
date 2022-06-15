package ro.ubb.tt.bll.AI;

import java.util.Map;

public interface Distance {
    double calculate(Map<String, Double> f1, Map<String, Double> f2);
}
