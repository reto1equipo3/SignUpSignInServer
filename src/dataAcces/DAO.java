package dataAcces;

import exception.BadPasswordException;
import exception.EmailNotUniqueException;
import exception.LoginExistingException;
import exception.LoginNotExistingException;
import model.UserBean;

/**
 * DAO interface encapsulating DAO methods for data access object.
 *
 * @author Imanol
 */
public interface DAO {

    /**
     * This method signs in a user.
     *
     * @param user The {@link UserBean} to be signed in.
     * @return The user's whole data.
     * @throws LoginNotExistingException If login does not exist.
     * @throws BadPasswordException If password is wrong.
     * @throws Exception If something else goes wrong.
     */
    public UserBean signIn(UserBean user) throws LoginNotExistingException, BadPasswordException, Exception;

    /**
     * This method signs up a user.
     *
     * @param user The {@link UserBean} to be signed up.
     * @throws LoginExistingException If login already exists.
     * @throws EmailNotUniqueException If email is already used.
     * @throws Exception If something else goes wrong
     */
    public void signUp(UserBean user) throws LoginExistingException, EmailNotUniqueException, Exception;

    /**
     * This method logs out a user.
     *
     * @param user The {@link UserBean} to be logged out.
     * @throws Exception If something goes wrong.
     */
    public void logOut(UserBean user) throws Exception;
}
