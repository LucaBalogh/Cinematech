package ro.ubb.tt.bll.AI;

import ro.ubb.tt.model.Location;

import java.util.*;
import java.util.stream.Collectors;

public class KMeans {

    private static final Random random = new Random();

//    The dataset is a set of feature vectors. Since each feature vector is a Location, then the dataset type is List<Location>
//    The k parameter determines the number of clusters, which we should provide in advance
//    distance encapsulates the way we're going to calculate the difference between two features
//    K-Means terminates when the assignment stops changing for a few consecutive iterations.
//    In addition to this termination condition, we can place an upper bound for the number of iterations, too. The maxIterations argument determines that upper bound
//    When K-Means terminates, each centroid should have a few assigned features, hence we're using a Map<Centroid, List<Location>> as the return type.
//    Basically, each map entry corresponds to a cluster.
//    In each iteration, after assigning all records to their nearest centroid, first, we should compare the current assignments with the last iteration.
//    If the assignments were identical, then the algorithm terminates. Otherwise, before jumping to the next iteration, we should relocate the centroids.

    public static Map<Centroid, List<Location>> fit(List<Location> locations, int k, Distance distance, int maxIterations) {

        List<Centroid> centroids = randomCentroids(locations, k);
        Map<Centroid, List<Location>> clusters = new HashMap<>();
        Map<Centroid, List<Location>> lastState = new HashMap<>();

        // iteram pentru un numar definit de ori
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            // in each iteration we should find the nearest centroid for each record
            for (Location loc : locations) {
                Centroid centroid = nearestCentroid(loc, centroids, distance);
                assignToCluster(clusters, loc, centroid);
            }

            // daca nu se mai modifica nimic algoritmul se termina
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

    private static List<Centroid> randomCentroids(List<Location> locations, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Location loc : locations) {
            loc.getFeatures().forEach((key, value) -> {
                // compara valoarea cu maximul curent si il alege pe cel mai mare
                maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                // compara valoarea cu minimul curent si il alege pe cel mai mare
                mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
            });
        }

        Set<String> attributes = locations.stream()
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

    // Functie care gaseste cel mai apropiat centroid pentru o locatie data
    private static Centroid nearestCentroid(Location location, List<Centroid> centroids, Distance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(location.getFeatures(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    //    Assigneaza locatia data catre cel mai apropiat cluster
    private static void assignToCluster(Map<Centroid, List<Location>> clusters, Location location, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(location);
            return list;
        });
    }

    //    Daca dupa o iteratie un centroizi nu a primit locatii noi nu il realocam
    //    Altfel ar trebui sa realocam coordonatele centroidului catre locatia medie din cele ale cluster-ului
    private static Centroid average(Centroid centroid, List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getCoordinates();
        locations.stream().flatMap(e -> e.getFeatures().keySet().stream())
                .forEach(k -> average.put(k, 0.0));

        for (Location loc : locations) {
            loc.getFeatures().forEach(
                    (k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue)
            );
        }

        average.forEach((k, v) -> average.put(k, v / locations.size()));

        return new Centroid(average);
    }

    //  Realoca centroizii
    private static List<Centroid> relocateCentroids(Map<Centroid, List<Location>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public static List<Location> getTop10(List<Location> locations, List<Location> userLocations, int user_id){
        Map<Centroid, List<Location>> clusters = KMeans.fit(locations, 10, new EuclideanDistance(), 200);

        Map<Centroid, List<Location>> clustersS = clusters.
                entrySet()
                .stream()
                .sorted(Comparator.comparing(i -> Double.parseDouble(i.getKey().getCoordinate()), Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        Set<Location> locationSet10 = new HashSet();
        List<Location> allLocations = new ArrayList<>();

        // Printing the cluster configuration
        clustersS.forEach((key, value) -> {
            System.out.println("-------------------------- CLUSTER ----------------------------");

            // Sorting the coordinates to see the most significant tags first.
            System.out.println("Cluster number " + key.getCoordinates().values().toString());
            Collections.sort(value, Comparator.comparing(Location::getRating));
            Collections.reverse(value);

            for(Location loc : value)
                System.out.println(loc);

            for(Location loc : value){
                int ok = 0;
                if(locationSet10.size() < 10)
                    for(Location locc : userLocations) {
                        if (loc.getCountry().equals(locc.getCountry()) || loc.getTip().equals(locc.getTip())) {
                            ok = 1;
                            break;
                        }
                    }
                if(ok == 1 && (loc.getUser().getId() != user_id))
                    locationSet10.add(loc);
            }

            allLocations.addAll(value);

            System.out.println();
            System.out.println();
        });

        for(Location loc : allLocations)
            if(locationSet10.size() < 10 && (loc.getUser().getId() != user_id))
                locationSet10.add(loc);

        List<Location> locationTop10 = new ArrayList<>(locationSet10);
        Collections.sort(locationTop10, Comparator.comparing(Location::getRating));
        Collections.reverse(locationTop10);

        return locationTop10;
    }
}
