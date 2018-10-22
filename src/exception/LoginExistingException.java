package exception;

/**
 * Exception associated to an error ocurred at signing up.
 * @author Imanol
 */
public class LoginExistingException extends Exception {

    /**
     * Creates a new instance of <code>LoginExistingException</code> without
     * detail message.
     */
    public LoginExistingException() {
    }

    /**
     * Constructs an instance of <code>LoginExistingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginExistingException(String msg) {
        super(msg);
    }
}
