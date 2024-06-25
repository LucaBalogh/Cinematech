package ro.uoradea.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import ro.uoradea.model.enums.Type;

@javax.persistence.Entity
@Table(name = "movie")
public class Movie extends Entity implements Serializable{

    @NotNull
    @Column(name = "name")
    private String name;

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
    @JsonIgnoreProperties(value = {"movie", "hibernateLazyInitializer"})
    private User user;

    public Movie() {}

    public Movie(@NotNull String name, @NotNull Type tip, @NotNull Float rating, User user) {
        this.name = name;
        this.tip = tip;
        this.rating = rating;
        this.user = user;
    }

    public Movie(@NotNull String name, @NotNull Type tip, User user) {
        this.name = name;
        this.tip = tip;
        this.rating = 0f;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Map<String, Double> features = new HashMap<>();
        features.put("rating", Double.parseDouble(rating.toString()));

        return features;
    }

    @Override
    public String toString() {
        return "Movie {" +
                "name = " + name +
                " tip = " + tip +
                " rating = " + rating +
                " user_id = " + user.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Movie))
            return false;
        Movie other = (Movie)o;
        return (other.name.equals(this.name) && other.tip == this.tip && Objects.equals(other.rating, this.rating) && other.user == this.user );
    }

    @Override
    public int hashCode()
    {
        return this.user.getId();
    }
}
