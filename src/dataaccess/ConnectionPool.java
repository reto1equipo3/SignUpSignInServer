package dataaccess;

import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Connection pool interface encapsulating methods for {@link Connection}
 * collections.
 *
 * @author Imanol
 */
public interface ConnectionPool {

	/**
	 * Gets a {@link Connection} from the connection pool.
	 *
	 * @return Connection instance.
	 * @throws SQLException Database exception.
	 */
	public Connection getConnection() throws DatabaseException, SQLException;

	/**
	 * Release a {@link Connection} of the connection pool.
	 *
	 * @param connection The connection to release.
	 * @return True if the connection pool contained the connection.
	 */
	public boolean releaseConnection(Connection connection);

	/**
	 * Returns the connection pool with the unused connections.
	 *
	 * @return The connection pool
	 */
	public List<Connection> getConnectionPool();

	/**
	 * Returns the amount of used and unused connections.
	 *
	 * @return Size of the connection pool.
	 */
	public int getSize();

	/**
	 * Returns the url of the database the connection pool is connected to.
	 *
	 * @return The url of the database.
	 */
	public String getUrl();

	/**
	 * Returns the user used to connect to the database.
	 *
	 * @return The user.
	 */
	public String getUser();

	/**
	 * Returns the password of the user used to connect to the database.
	 *
	 * @return The password.
	 */
	public String getPassword();

	/**
	 * Shuts down the connections and the pool.
	 * @throws SQLException Database exception.
	 */
	public void shutdown() throws SQLException;
}
