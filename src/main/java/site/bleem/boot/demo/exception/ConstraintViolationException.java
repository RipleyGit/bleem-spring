package site.bleem.boot.demo.exception;

/**
 * @author 南京北路
 */
public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message) {
        super(message);
    }
}
