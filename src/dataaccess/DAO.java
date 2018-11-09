package dataaccess;

import exceptions.BadPasswordException;
import exceptions.DatabaseException;
import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
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
	 * @throws DatabaseException If something goes wrong connecting to database.
	 */
	public UserBean signIn(UserBean user) throws LoginNotExistingException, BadPasswordException, DatabaseException;

	/**
	 * This method signs up a user.
	 *
	 * @param user The {@link UserBean} to be signed up.
	 * @throws LoginExistingException If login already exists.
	 * @throws EmailNotUniqueException If email is already used.
	 * @throws DatabaseException If something goes wrong connecting to database.
	 */
	public void signUp(UserBean user) throws LoginExistingException, EmailNotUniqueException, DatabaseException;

	/**
	 * This method logs out a user.
	 *
	 * @param user The {@link UserBean} to be logged out.
	 * @throws DatabaseException If something goes wrong connecting to database.
	 * @throws LoginNotExistingException Is user is not found.
	 */
	public void logOut(UserBean user) throws DatabaseException, LoginNotExistingException;
}
