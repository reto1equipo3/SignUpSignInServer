package dataAcces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Imanol
 */
public class BasicConnectionPool implements ConnectionPool {

	/**
	 * Database URL
	 */
	private final String url;
	/**
	 * Database user
	 */
	private final String user;
	/**
	 * Database password
	 */
	private final String password;
	/**
	 * Collection of all the available connections.
	 */
	private final List<Connection> connectionPool;
	/**
	 * Collection of all the connections that are being used.
	 */
	private final List<Connection> usedConnections = new ArrayList<>();
	/**
	 * Initial amount of connections.
	 */
	private static final int INITIAL_POOL_SIZE = 6;
	/**
	 * Maximun amount of connections allowed.
	 */
	private final int MAX_POOL_SIZE = 6;

	/**
	 * Constructor using params.
	 *
	 * @param url The database url.
	 * @param user User of the database.
	 * @param password Password of the user.
	 * @param pool Connection pool.
	 */
	private BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
		this.url = url;
		this.user = user;
		this.password = password;
		this.connectionPool = connectionPool;
	}

	/**
	 * Creates a new connection pool to the database using the user credentials.
	 *
	 * @param url The database url to connect to.
	 * @param user User to connect with.
	 * @param password Password to connect with.
	 * @return New connection pool.
	 * @throws SQLException Database exception.
	 */
	public static BasicConnectionPool create(String url, String user, String password) throws SQLException {
		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url, user, password));
		}
		return new BasicConnectionPool(url, user, password, pool);
	}

	/**
	 * Gets a {@link Connection} from the connection pool.
	 *
	 * @return Connection instance.
	 * @throws java.sql.SQLException
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if (connectionPool.isEmpty()) {
			if (usedConnections.size()<MAX_POOL_SIZE) {
				connectionPool.add(createConnection(url, user, password));
			} else {
				throw new RuntimeException("Maximum pool size reached, no available connections!");
			}
	
		}
		Connection connection = connectionPool.remove(connectionPool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}

	/**
	 * Release a {@link Connection} of the connection pool.
	 *
	 * @param connection The connection to release.
	 * @return True if the connection pool contained the connection.
	 */
	@Override
	public boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}

	/**
	 * Creates a new connection to the database using the input user.
	 *
	 * @return Connection to the database.
	 * @throws SQLException
	 */
	private static Connection createConnection(String url, String user, String password) throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	/**
	 * Returns the connection pool with the unused connections.
	 *
	 * @return The connection pool
	 */
	@Override
	public List<Connection> getConnectionPool() {
		return connectionPool;
	}

	/**
	 * Returns the amount of used and unused connections.
	 *
	 * @return Size of the connection pool.
	 */
	@Override
	public int getSize() {
		return connectionPool.size() + usedConnections.size();
	}

	/**
	 * Returns the url of the database the connection pool is connected to.
	 *
	 * @return The url of the database.
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the user used to connect to the database.
	 *
	 * @return The user.
	 */
	@Override
	public String getUser() {
		return user;
	}

	/**
	 * Returns the password of the user used to connect to the database.
	 *
	 * @return The password.
	 */
	@Override
	public String getPassword() {
		return password;
	}
	/**
	 * Shuts down the connections and the pool.
	 * @throws SQLException Database exception.
	 */
	@Override
	public void shutdown() throws SQLException{
		usedConnections.forEach(this::releaseConnection);
		for (Connection connection : connectionPool) {
			connection.close();
		}
		connectionPool.clear();
	}
}
