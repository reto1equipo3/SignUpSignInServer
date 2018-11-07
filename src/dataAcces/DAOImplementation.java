package dataAcces;

import exception.BadPasswordException;
import exception.DatabaseException;
import exception.EmailNotUniqueException;
import exception.LoginExistingException;
import exception.LoginNotExistingException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import model.UserBean;

/**
 * Contains an implementation of the DAO interface.
 *
 * @author Imanol
 */
public class DAOImplementation implements DAO {

	private static final Logger LOGGER = Logger.getLogger("dataAccess");
	private static ConnectionPool connectionPool;
	private static final String DATABASE = ResourceBundle.getBundle("config.DatabaseParameters").getString("DATABASE");
	private static final String URL = ResourceBundle.getBundle("config.DatabaseParameters").getString("URL");
	private static final String USER = ResourceBundle.getBundle("config.DatabaseParameters").getString("USER");
	private static final String PASSWORD = ResourceBundle.getBundle("config.DatabaseParameters").getString("PASSWORD");

	public DAOImplementation() throws SQLException {
		if (connectionPool == null) {
			synchronized (BasicConnectionPool.class) {
				if (connectionPool == null) {
					LOGGER.info("DAOImplementation::constructor: Creating connection pool.");
					connectionPool = BasicConnectionPool.create(URL, USER, PASSWORD);
				}
			}
		}
	}

	@Override
	public UserBean signIn(UserBean user) throws LoginNotExistingException, BadPasswordException, DatabaseException {
		LOGGER.info("DAOImplementation::signIn: Signing in user.");

		if (user.getLogin() != null) {
			if (user.getPassword() != null) {
				Connection con = null;
				PreparedStatement stmt = null;
				ResultSet resultSet = null;
				try {
					// Get user from database
					con = connectionPool.getConnection();
					stmt = con.prepareStatement("select * from " + DATABASE + ".USERS where lower(LOGIN) like ?");
					stmt.setString(1, user.getLogin().trim().toLowerCase());
					resultSet = stmt.executeQuery();
					if (resultSet.next()) {
						if (user.getPassword().trim().equals(resultSet.getString("password").trim())) {
							user.setFullName(resultSet.getString("fullName"));
							user.setEmail(resultSet.getString("email"));
							// Set photo
							//user.setPhoto(resultSet.getBlob("Photo"));

							// Update last access
							stmt = con.prepareStatement("update " + DATABASE + ".USERS " + " set LASTACCESS = ? " + " where LOWER(LOGIN) = ?");
							stmt.setDate(1, Date.valueOf(LocalDate.now()));
							stmt.setString(2, user.getLogin().trim().toLowerCase());
							if (stmt.executeUpdate() == 0) {
								LOGGER.log(Level.SEVERE,
									"DAOImplementation::signIn: Could not update last access.");
							}

						} else {
							LOGGER.log(Level.SEVERE,
								"DAOImplementation::signIn: Password is wrong.");
							throw new BadPasswordException("Password is wrong!");
						}
					} else {
						LOGGER.log(Level.SEVERE,
							"DAOImplementation::signIn: User was not found.");
						throw new LoginNotExistingException("Login was not found!");
					}

				} catch (SQLException ex) {
					LOGGER.log(Level.SEVERE,
						"DAOImplementation::signIn: Exception connecting to database",
						ex.getMessage());
					throw new DatabaseException("Problem signing up user: " + user.getLogin());
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
						}
						if (stmt != null) {
							stmt.close();
						}
						if (con != null) {
							connectionPool.releaseConnection(con);
						}
					} catch (SQLException ex) {
						LOGGER.log(Level.SEVERE,
							"DAOImpentation::signIn: Exception closing the connection to database.",
							ex.getMessage());
					}
				}
			} else {
				LOGGER.log(Level.SEVERE,
					"DAOImplementation::signIn: Password is null.");
				throw new BadPasswordException("Password is wrong!");
			}
		} else {
			LOGGER.log(Level.SEVERE,
				"DAOImplementation::signIn: Login is null.");
			throw new LoginNotExistingException("Login was not found!");
		}
		return user;
	}

	@Override
	public void signUp(UserBean user) throws LoginExistingException, EmailNotUniqueException, DatabaseException {
		LOGGER.info("DAOImplementation::signUp: Signing up user.");
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			con = connectionPool.getConnection();
			// Check the login and email are not used
			stmt = con.prepareStatement("select count(*) as loginCount from " + DATABASE + ".USERS where lower(LOGIN) like ?");
			stmt.setString(1, user.getLogin().trim().toLowerCase());
			resultSet = stmt.executeQuery();

			resultSet.next();

			// If the login exists throw exception else check email
			if (resultSet.getInt(1) == 0) {
				stmt = con.prepareStatement("select count(EMAIL) as emailCount from " + DATABASE + ".USERS where lower(EMAIL) like ?");
				stmt.setString(1, user.getEmail().trim().toLowerCase());
				resultSet = stmt.executeQuery();
				resultSet.next();

				// If the email exists throw exception else insert it
				if (resultSet.getInt("emailCount") == 0) {
					// Insert new user
					stmt = con.prepareStatement("insert into users(login,fullName,email,status,privilege,password,lastAccess,lastPasswordChange) values(?,?,?,?,?,?,?,?)");

					stmt.setString(1, user.getLogin().trim().toLowerCase());
					stmt.setString(2, user.getFullName().trim().toLowerCase());
					stmt.setString(3, user.getEmail().trim().toLowerCase());
					// status
					stmt.setInt(4, user.getStatus().ordinal() + 1);
					// privilege
					stmt.setInt(5, user.getPrivilege().ordinal() + 1);
					stmt.setString(6, user.getPassword().trim());
					stmt.setDate(7, Date.valueOf(LocalDate.now()));
					stmt.setDate(8, Date.valueOf(LocalDate.now()));

					stmt.executeUpdate();
				} else {
					LOGGER.log(Level.SEVERE,
						"DAOImplementation::signUp: Email is already used");
					throw new EmailNotUniqueException("Email already used!");
				}

			} else {
				LOGGER.log(Level.SEVERE,
					"DAOImplementation::signUp: Login is already used");
				throw new LoginExistingException("Login already used!");
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE,
				"DAOImplementation::signUp: Exception connecting to database",
				ex.getMessage());
			throw new DatabaseException("Problem signing up user");
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					connectionPool.releaseConnection(con);
				}
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE,
					"DAOImpentation::signUp: Exception closing the connection to database.",
					e.getMessage());
			}
		}
	}

	private ByteArrayOutputStream convertImageToByteArray(Image image) throws IOException {
		// Get a BufferedImage
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		bufferedImage.getGraphics().drawImage(image, 0, 0, null);
		// Get a byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", baos);
		return baos;
	}

	/**
	 * Updates the last access of the user.
	 *
	 * @param user The user to update
	 * @throws DatabaseException Problems with the database
	 * @throws LoginNotExistingException User was not found
	 */
	@Override
	public void logOut(UserBean user) throws DatabaseException, LoginNotExistingException {
		LOGGER.info("DAOImplementation::logOut: Logging out user.");
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = connectionPool.getConnection();
			stmt = con.prepareStatement("update " + DATABASE + ".USERS " + " set LASTACCESS = ? " + " where LOWER(LOGIN) = ?");
			stmt.setDate(1, Date.valueOf(LocalDate.now()));
			stmt.setString(2, user.getLogin().trim().toLowerCase());
			if (stmt.executeUpdate() == 0) {
				throw new LoginNotExistingException();
			}
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE,
				"DAOImplementation::logOut: Exception connecting to database",
				ex.getMessage());
			throw new DatabaseException("Problem logging out user: " + user.getLogin());
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					connectionPool.releaseConnection(con);
				}
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE,
					"DAOImpentation::logOut: Exception closing the connection to database.",
					ex.getMessage());
			}
		}
	}
}
