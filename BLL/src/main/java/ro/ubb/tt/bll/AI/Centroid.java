package ro.ubb.tt.bll.AI;

import java.util.Map;
import java.util.Objects;

public class Centroid {

    private final Map<String, Double> coordinates;

    public Centroid(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Map<String, Double> getCoordinates() {
        return coordinates;
    }

    public String getCoordinate(){
        String val =  getCoordinates().values().toString();
        val = val.replaceAll("\\[", "").replaceAll("\\]","");
        return val;
    }

    @Override
    public String toString() {
        return "Centroid{" +
                "coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centroid centroids = (Centroid) o;
        return Objects.equals(coordinates, centroids.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }
}
