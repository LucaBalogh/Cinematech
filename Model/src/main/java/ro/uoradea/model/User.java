package ro.uoradea.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@javax.persistence.Entity
@Table(name = "users")
public class User extends Entity {

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Email
    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "password")
    private String password;

    public User(){}

    public User(String lastName, String firstName, String email, String password){
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
