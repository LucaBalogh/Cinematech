package ro.uoradea.bll.validator;

import ro.uoradea.bll.exceptions.InvalidCredentialsException;

public interface Validator<T> {
    void validate(T entity) throws InvalidCredentialsException;
}
