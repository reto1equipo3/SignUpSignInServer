package exception;

/**
 * Exception associated to an error ocurred at signing in.
 *
 * @author Imanol
 */
public class BadPasswordException extends Exception {

    /**
     * Creates a new instance of <code>BadPasswordException</code> without
     * detail message.
     */
    public BadPasswordException() {
    }

    /**
     * Constructs an instance of <code>BadPasswordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BadPasswordException(String msg) {
        super(msg);
    }
}
