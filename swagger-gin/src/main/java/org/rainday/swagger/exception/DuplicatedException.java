package org.rainday.swagger.exception;

/**
 * Created by admin on 2021/1/13 16:29:22.
 */
public class DuplicatedException extends RuntimeException {
    
    public DuplicatedException(String message) {
        super(message);
    }
}
