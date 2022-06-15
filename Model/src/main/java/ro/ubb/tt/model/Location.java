package ro.ubb.tt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ro.ubb.tt.model.enums.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@javax.persistence.Entity
@Table(name = "locations")
public class Location extends Entity implements Serializable{

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "country")
    private String country;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tip")
    private Type tip;

    @NotNull
    @Positive
    @Column(name = "rating")
    private Float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"locations", "hibernateLazyInitializer"})
    private User user;

    public Location() {}

    public Location(@NotNull String city, @NotNull String country, @NotNull Type tip, @NotNull Float rating, User user) {
        this.city = city;
        this.country = country;
        this.tip = tip;
        this.rating = rating;
        this.user = user;
    }

    public Location(@NotNull String city, @NotNull String country, @NotNull Type tip, User user) {
        this.city = city;
        this.country = country;
        this.tip = tip;
        this.rating = 0f;
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Type getTip() {
        return tip;
    }

    public void setTip(Type tip) {
        this.tip = tip;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Double> getFeatures() {
        Map<String, Double> features = new HashMap();
        features.put("rating", Double.parseDouble(rating.toString()));
        return features;
    }

    @Override
    public String toString() {
        return "Location {" +
                "city = " + city +
                " country = " + country +
                " tip = " + tip +
                " rating = " + rating +
                " user_id = " + user.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Location))
            return false;
        Location other = (Location)o;
        return (other.city.equals(this.city) && other.country.equals(this.country) && other.tip == this.tip && other.rating == this.rating && other.user == this.user );
    }

    @Override
    public int hashCode()
    {
        return this.user.getId();
    }
}
