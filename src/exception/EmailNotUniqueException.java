package exception;

/**
 * Exception associated to an error ocurred at signing up.
 *
 * @author Imanol
 */
public class EmailNotUniqueException extends Exception {

    /**
     * Creates a new instance of <code>EmailNotUniqueException</code> without
     * detail message.
     */
    public EmailNotUniqueException() {
    }

    /**
     * Constructs an instance of <code>EmailNotUniqueException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmailNotUniqueException(String msg) {
        super(msg);
    }
}
