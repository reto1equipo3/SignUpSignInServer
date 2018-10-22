package exception;

/**
 * Exception associated to an error ocurred at signing in.
 * @author Imanol
 */
public class LoginNotExistingException extends Exception {

    /**
     * Creates a new instance of <code>LoginNotExistingException</code> without
     * detail message.
     */
    public LoginNotExistingException() {
    }

    /**
     * Constructs an instance of <code>LoginNotExistingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LoginNotExistingException(String msg) {
        super(msg);
    }
}
