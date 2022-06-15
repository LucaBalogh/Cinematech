package ro.ubb.tt.bll.validator;

import ro.ubb.tt.bll.exceptions.InvalidCredentialsException;

public interface Validator<T> {
    void validate(T entity) throws InvalidCredentialsException;
}
