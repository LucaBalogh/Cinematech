package ro.uoradea.bll.AI;

import java.util.*;
import java.util.stream.Collectors;
import ro.uoradea.model.Movie;

public class KMeans {
    private static final Random random = new Random();

//    The k parameter determines the number of clusters, which we should provide in advance
//    Distance encapsulates the way we're going to calculate the difference between two features
//    K-Means terminates when the assignment stops changing for a few consecutive iterations.
//    In addition to this termination condition, we can place an upper bound for the number of iterations, too. The maxIterations argument determines that upper bound
//    When K-Means terminates, each centroid should have a few assigned features, hence we're using a Map<Centroid, List<Location>> as the return type.
//    Basically, each map entry corresponds to a cluster.
//    In each iteration, after assigning all records to their nearest centroid, first, we should compare the current assignments with the last iteration.
//    If the assignments were identical, then the algorithm terminates. Otherwise, before jumping to the next iteration, we should relocate the centroids.

    public static Map<Centroid, List<Movie>> fit(List<Movie> movies, int k, Distance distance, int maxIterations) {

        List<Centroid> centroids = randomCentroids(movies, k);
        Map<Centroid, List<Movie>> clusters = new HashMap<>();
        Map<Centroid, List<Movie>> lastState = new HashMap<>();

        // iteram pentru un numar definit de ori
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            // in each iteration we should find the nearest centroid for each record
            for (Movie loc : movies) {
                Centroid centroid = nearestCentroid(loc, centroids, distance);
                assignToCluster(clusters, loc, centroid);
            }

            // daca am ajuns la ultima iteratie sau nu s a mai modificat nimic algoritmul se termina
            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            // dupa fiecare iteratie facem relocarea centroizilor
            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }

//    Although each centroid can contain totally random coordinates, it's a good practice to generate random coordinates between the minimum and maximum possible values for each attribute.
//    Generating random centroids without considering the range of possible values would cause the algorithm to converge more slowly.
//    First, we should compute the minimum and maximum value for each attribute, and then, generate the random values between each pair of them.

    private static List<Centroid> randomCentroids(List<Movie> movies, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Movie loc : movies) {
            loc.getFeatures().forEach((key, value) -> {
                // compara valoarea cu maximul curent si il alege pe cel mai mare
                maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                // compara valoarea cu minimul curent si il alege pe cel mai mic
                mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
            });
        }

        Set<String> attributes = movies.stream()
                .flatMap(e -> e.getFeatures().keySet().stream())
                .collect(Collectors.toSet());
        for (int i = 0; i < k; i++) {
            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(coordinates));
        }

        return centroids;
    }

    // Functie care gaseste cel mai apropiat centroid pentru un film data
    private static Centroid nearestCentroid(Movie movie, List<Centroid> centroids, Distance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(movie.getFeatures(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    //    Assigneaza filmul data catre cel mai apropiat cluster
    private static void assignToCluster(Map<Centroid, List<Movie>> clusters, Movie movie, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(movie);
            return list;
        });
    }

    //    Daca dupa o iteratie un centroid nu a primit filme noi nu il realocam
    //    Altfel ar trebui sa realocam coordonatele centroidului catre filmul mediu din cele ale cluster-ului
    private static Centroid average(Centroid centroid, List<Movie> movies) {
        if (movies == null || movies.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getCoordinates();
        movies.stream().flatMap(e -> e.getFeatures().keySet().stream())
                .forEach(k -> average.put(k, 0.0));

        for (Movie loc : movies) {
            loc.getFeatures().forEach(
                    (k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue)
            );
        }

        average.forEach((k, v) -> average.put(k, v / movies.size()));

        return new Centroid(average);
    }

    //  Realoca centroizii
    private static List<Centroid> relocateCentroids(Map<Centroid, List<Movie>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    // Functie care genereaza top10 filme in functie de utilizatorul logat
    public static List<Movie> getTop10(List<Movie> movies, List<Movie> userMovies, int user_id){
        Map<Centroid, List<Movie>> clusters = KMeans.fit(movies, 10, new EuclideanDistance(), 200);

        Map<Centroid, List<Movie>> clustersS = clusters.
                entrySet()
                .stream()
                .sorted(Comparator.comparing(i -> Double.parseDouble(i.getKey().getCoordinate()), Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        Set<Movie> movieSet10 = new HashSet<>();
        List<Movie> allMovies = new ArrayList<>();

        // Printing the cluster configuration
        clustersS.forEach((key, value) -> {

            // Sorting the coordinates to see the most significant tags first.
            value.sort(Comparator.comparing(Movie::getRating));
            Collections.reverse(value);

            for(Movie movie : value){
                int ok = 0;
                if(movieSet10.size() < 10)
                    for(Movie mov : userMovies) {
                        if (movie.getName().equals(mov.getName()) || movie.getTip().equals(mov.getTip())) {
                            ok = 1;
                            break;
                        }
                    }
                if(ok == 1 && (movie.getUser().getId() != user_id))
                    movieSet10.add(movie);
            }

            allMovies.addAll(value);
        });

        for(Movie loc : allMovies)
            if(movieSet10.size() < 10 && (loc.getUser().getId() != user_id))
                movieSet10.add(loc);

        List<Movie> movieTop10 = new ArrayList<>(movieSet10);
        movieTop10.sort(Comparator.comparing(Movie::getRating));
        Collections.reverse(movieTop10);

        return movieTop10;
    }
}
