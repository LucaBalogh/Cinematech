package ro.uoradea.bll.AI;

import java.util.*;
import java.util.stream.Collectors;
import ro.uoradea.model.Movie;

public class KMeans {
    private static final Random random = new Random();

    //    Aceasta functie implementeaza algoritmul k-means clustering pentru a grupa filmele în k clustere, folosind o metrica de distanta specificata si un numar maxim de iterari.
    //    Genereaza k centroizi aleatori folosind functia randomCentroids.
    //    Inițializează doua dictionare: clusters pentru a stoca clusterele curente și lastState pentru a stoca starea anterioară a clusterelor.
    //    Se itereaza pana la maxIterations sau pana cand nu se mai modifica clusterii:
    //    Cu isLastIteration se determina daca e ultima iterare sau nu
    //    Pentru fiecare film din lista, gaseste cel mai apropiat centroid folosind functia nearestCentroid si il atribuie folosing functia assignToCluster.
    //    Dacă este ultima iterare sau dacă clusterele nu s-au schimbat față de starea anterioară (lastState), algoritmul se oprește.
    //    Dupa fiecare iterare, recalculeaza pozitiile centroizilor pe baza filmelor din clustere folosind funcția relocateCentroids, iar apoi reseteaza dictionarul clusters pentru următoarea iterare
    public static Map<Centroid, List<Movie>> fit(List<Movie> movies, int k, Distance distance, int maxIterations) {
        List<Centroid> centroids = randomCentroids(movies, k);
        Map<Centroid, List<Movie>> clusters = new HashMap<>();
        Map<Centroid, List<Movie>> lastState = new HashMap<>();

        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            for (Movie loc : movies) {
                Centroid centroid = nearestCentroid(loc, centroids, distance);
                assignToCluster(clusters, loc, centroid);
            }

            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }

    //    Genereaza k centroizi aleatori pe baza caracteristicilor din lista de filme
    //    Initializeaza liste goale pentru centroizi, valorile maxime si minime ale caracteristicilor
    //    Parcurge fiecare film și actualizeaza valorile maxime si minime pentru fiecare caracteristica
    //    Pentru fiecare centroid care trebuie creat:
    //    Genereaza coordonate aleatorii pentru fiecare atribut intre valorile sale minime si maxime.
    //    Creeaza un nou Centroid cu aceste coordonate si il adauga în lista de centroizi.
    private static List<Centroid> randomCentroids(List<Movie> movies, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Movie loc : movies) {
            loc.getFeatures().forEach((key, value) -> {
                maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);
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

    //    Aceasta functie gaseste centroidul cel mai apropiat de un film dat pe baza metricii de distanta furnizate
    //    Initializeaza variabile pentru a urmari distanta minima si centroidul cel mai apropiat.
    //    Parcurge fiecare centroid si calculează distanta pana la film.
    //    Actualizeaza centroidul cel mai apropiat daca distanta curenta este mai mica decat distanta minima.
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

    //    Aceasta functie atribuie un film celui mai apropiat cluster (centroid)
    //    Foloseste compute pentru a actualiza lista de filme pentru centroidul dat.
    //    Daca lista este null, initializeaz-o, iar apoi adauga filmul in ea.
    private static void assignToCluster(Map<Centroid, List<Movie>> clusters, Movie movie, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(movie);
            return list;
        });
    }

  //    Aceasta functie recalculeaza coordonatele centroidului pe baza mediei caracteristicilor filmelor din clusterul sau.
  //    Daca lista de filme este null sau goala, returnează centroidul curent.
  //    Initializeaza un dictionar cu zero pentru fiecare caracteristica.
  //    Face media fiecarei caracteristici din toate filmele si returneaza un nou Centroid cu coordonatele mediate.
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

    //    Aceasta functie realoca toti centroizii prin medierea coordonatelor filmelor din clusterul lor respectiv.
    private static List<Centroid> relocateCentroids(Map<Centroid, List<Movie>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    //    Aceasta functie genereaza un top 10 filme recomandate pentru un utilizator specific, pe baza filmelor pe care le-a vizionat anterior si a filmelor disponibile
    //    Filmele sunt grupate în 10 clustere folosind algoritmul k-means (KMeans.fit) cu distanța Euclidiana și maxim 200 de iterari.
    //    Clusters conține dictionarul cu centroizii și listele de filme asociate fiecărui centroid.
    //    Clusterele sunt sortate în functie de coordonatele centroidului, intr-o ordine descrescatoare si este stocată în clustersS.
    //    Filmele din cluster sunt sortate descrescator după rating.
    //    Se verifică dacă setul movieSet10 are mai puțin de 10 filme.
    //    Se verifică dacă filmul nu este deja vizionat de utilizator (în funcție de nume sau tip).
    //    Daca filmul indeplineste conditiile si nu apartine utilizatorului logat, este adaugat în movieSet10.
    //    Dupa parcurgerea clusterelor, dacă movieSet10 contine mai puțin de 10 filme, se adauga filme din allMovies care nu apartin utilizatorului logat.
    //    Filmele din movieSet10 sunt sortate descrescator după rating, iar apoi lista de filme e returnata
    public static List<Movie> getTop10(List<Movie> movies, List<Movie> userMovies, int user_id){
        Map<Centroid, List<Movie>> clusters = KMeans.fit(movies, 10, new EuclideanDistance(), 1000);

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

        clustersS.forEach((key, value) -> {

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
