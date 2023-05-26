package com.nnk.springboot.exception;

public class UsernameAlreadyExistException extends RuntimeException {
    public UsernameAlreadyExistException(String username) { super("An account already exist with the username = " + username); }
}
