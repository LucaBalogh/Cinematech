package ro.uoradea.bll.AI;

import java.sql.*;
import java.util.*;
import ro.uoradea.model.Movie;

public class KmeansRunner {

    private static final String URL = "jdbc:postgresql://localhost:5050/Cinematech";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        List<Movie> movies = loadMovies();

        // Normalizare datelor dacă este necesar
        normalizeFeatures(movies);

        // Determinarea valorii optime pentru k folosind metoda "Elbow"
        int optimalK = determineOptimalK(movies, 1, 10);
        System.out.println("Optimal K: " + optimalK);

        // Determinarea numărului optim de iterații
        int optimalIterations = determineOptimalIterations(movies, optimalK, 500);
        System.out.println("Optimal Iterations: " + optimalIterations);

        // Apelarea funcției fit cu parametri determinați
        Map<Centroid, List<Movie>> clusters = KMeans.fit(movies, optimalK, new EuclideanDistance(), optimalIterations);

        // Evaluarea clusteringului
        evaluateClustering(clusters);
    }

    //    Aceasta functie determina numarul optim de clustere (k) folosind metoda "Elbow"
    //    Se creeaza un vector inertias pentru a stoca inertia pentru fiecare k intre minK și maxK.
    //    Pentru fiecare valoare a lui k, se realizeaza clustering, se calculeaza inertia și se stocheaza rezultatul
    //    Se identifica valoarea optima a lui k folosind metoda "Elbow", unde se găseste punctul in care gradientul scaderii inertei este cel mai mic.
    private static int determineOptimalK(List<Movie> movies, int minK, int maxK) {
        double[] inertias = new double[maxK - minK + 1];
        for (int k = minK; k <= maxK; k++) {
            Map<Centroid, List<Movie>> clusters = KMeans.fit(movies, k, new EuclideanDistance(), 500);
            double inertia = calculateInertia(clusters);
            inertias[k - minK] = inertia;
        }

        int optimalK = minK;
        double minGradient = Double.MAX_VALUE;

        for (int i = 1; i < inertias.length - 1; i++) {
            double gradient = inertias[i] - inertias[i - 1];
            if (gradient < minGradient) {
                minGradient = gradient;
                optimalK = i + minK;
            }
        }

        return optimalK;
    }

    //    Această funcție determină numărul optim de iterații pentru algoritmul K-means
    //    Procesul este la fel ca la functia de dinainte.
    private static int determineOptimalIterations(List<Movie> movies, int k, int maxIterations) {
        double[] inertias = new double[maxIterations];
        for (int iterations = 1; iterations <= maxIterations; iterations++) {
            Map<Centroid, List<Movie>> clusters = KMeans.fit(movies, k, new EuclideanDistance(), iterations);
            double inertia = calculateInertia(clusters);
            inertias[iterations - 1] = inertia;
        }

        int optimalIterations = 1;
        double minInertia = Double.MAX_VALUE;

        for (int i = 0; i < inertias.length; i++) {
            if (inertias[i] < minInertia) {
                minInertia = inertias[i];
                optimalIterations = i + 1;
            }
        }

        return optimalIterations;
    }

    //    Aceasta functie calculează inertua (suma patratelor distantelor) pentru un set de clustere dat
    //    Pentru fiecare cluster, se calculeaza distanta intre fiecare obiect Movie și centroidul sau.
    //    Se calculeaza suma patratelor acestor distante pentru a obtine inertia.
    private static double calculateInertia(Map<Centroid, List<Movie>> clusters) {
        double totalInertia = 0.0;
        for (Map.Entry<Centroid, List<Movie>> entry : clusters.entrySet()) {
            Centroid centroid = entry.getKey();
            List<Movie> movies = entry.getValue();
            for (Movie movie : movies) {
                totalInertia += calculateDistance(movie.getFeatures(), centroid.getCoordinates());
            }
        }
        return totalInertia;
    }


    //    Aceasta functie calculeaza distanta Euclidiana intre doua seturi de caracteristici.
    private static double calculateDistance(Map<String, Double> features1, Map<String, Double> features2) {
        double sum = 0.0;
        for (String key : features1.keySet()) {
            double diff = features1.get(key) - features2.getOrDefault(key, 0.0);
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    //    Aceasta functie normalizeaza caracteristicile obiectelorlor Movie pentru a asigura ca fiecare caracteristica contribuie in mod egal la calculul distantelor în algoritmul K-means
    //    Se calculeaza valorile minime si maxime pentru fiecare caracteristica in randul tuturor obiectelor Movie.
    //    Se normalizeaza caracteristicile fiecarui obiect Movie utilizand formulele standard de normalizare (value - min) / (max - min)
    private static void normalizeFeatures(List<Movie> movies) {
        Map<String, Double> minValues = new HashMap<>();
        Map<String, Double> maxValues = new HashMap<>();

        for (Movie movie : movies) {
            movie.getFeatures().forEach((key, value) -> {
                minValues.compute(key, (k, v) -> (v == null || value < v) ? value : v);
                maxValues.compute(key, (k, v) -> (v == null || value > v) ? value : v);
            });
        }

        for (Movie movie : movies) {
            Map<String, Double> normalizedFeatures = new HashMap<>();
            movie.getFeatures().forEach((key, value) -> {
                double min = minValues.get(key);
                double max = maxValues.get(key);
                normalizedFeatures.put(key, (value - min) / (max - min));
            });
            movie.setFeatures(normalizedFeatures);
        }
    }

    //    Aceasta functie evalueaza performanta clusteringului afisand clusterele gasite de algoritm
    private static void evaluateClustering(Map<Centroid, List<Movie>> clusters) {
        for (Map.Entry<Centroid, List<Movie>> entry : clusters.entrySet()) {
            Centroid centroid = entry.getKey();
            List<Movie> movies = entry.getValue();
            System.out.println("Centroid: " + centroid.getCoordinates());
            System.out.println("Movies in this cluster: " + movies.size());
            for (Movie movie : movies) {
                System.out.println("\t" + movie.getName() + " - Rating: " + movie.getRating());
            }
        }
    }

    // Ia toate filmele din baza de date
    private static List<Movie> loadMovies() {
        List<Movie> movies = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM movie WHERE id BETWEEN 1 AND 1000";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getInt("id"));
                movie.setName(resultSet.getString("name"));
                movie.setRating(resultSet.getFloat("rating"));

                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return movies;
    }
}
