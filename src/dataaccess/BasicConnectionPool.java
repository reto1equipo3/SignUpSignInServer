package dataaccess;

import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic implementation of {@link ConnectionPool}.
 *
 * @author Imanol
 */
public class BasicConnectionPool implements ConnectionPool {

	/**
	 * Instance of the BasicConnectionPool class
	 */
	private static BasicConnectionPool instance;
	/**
	 * {@link Logger} object used to log messages for the class.
	 */
	private static final Logger LOGGER = Logger.getLogger("dataaccess");
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
	 * Maximun amount of connections allowed.
	 */
	private static int MAX_POOL_SIZE;

	/**
	 * Private constructor using params.
	 *
	 * @param url The database url.
	 * @param user User of the database.
	 * @param password Password of the user.
	 * @param pool Connection pool.
	 */
	private BasicConnectionPool(String url, String user, String password, List<Connection> connectionPool) {
		LOGGER.info("BasicConnectionPool::constructor: Beginning basic connection pool constructor.");
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
	 * @param max_size Max connection amount.
	 * @return New connection pool.
	 * @throws SQLException Database exception.
	 */
	public static BasicConnectionPool create(String url, String user, String password, int max_size) throws SQLException {
		LOGGER.info("BasicConnectionPool::create: Beginning connection pool creation.");

		if (instance == null) {
			synchronized (BasicConnectionPool.class) {
				if (instance == null) {
					MAX_POOL_SIZE = max_size;
					List<Connection> pool = new ArrayList<>(max_size/2);
					for (int i = 0; i < max_size/2; i++) {
						pool.add(createConnection(url, user, password));
					}
					instance = new BasicConnectionPool(url, user, password, pool);
				}
			}
		}

		return instance;
	}

	/**
	 * Gets a {@link Connection} from the connection pool.
	 *
	 * @return Connection instance.
	 * @throws exceptions.DatabaseException
	 * @throws java.sql.SQLException
	 */
	@Override
	public Connection getConnection() throws DatabaseException, SQLException {
		LOGGER.info("BasicConnectionPool::getConnection: Beginning getting connection.");

		if (connectionPool.isEmpty()) {
			if (usedConnections.size() < MAX_POOL_SIZE) {
				connectionPool.add(createConnection(url, user, password));
			} else {
				LOGGER.log(Level.SEVERE,
					"BasicConnectionPool::getConnection: Exception max connection pool size reached.");
				throw new DatabaseException("Maximum pool size reached, no available connections!");
			}
		}
		// Get a connection from the pool and save it as used
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
		LOGGER.info("BasicConnectionPool::releaseConnection: Beginning releasing a connection.");
		boolean releasedCorrectly = usedConnections.remove(connection);
		connectionPool.add(connection);
		return releasedCorrectly;
	}

	/**
	 * Creates a new connection to the database using the input user.
	 *
	 * @return Connection to the database.
	 * @throws SQLException Problems connecting to database.
	 */
	private static Connection createConnection(String url, String user, String password) throws SQLException {
		LOGGER.info("BasicConnectionPool::createConnection: Beginning creating a connection.");
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ex) {
			LOGGER.log(Level.SEVERE,
				"BasicConnectionPool::createConnection: Exception with Driver name",
				ex.getMessage());
		}
		return connection;
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
	 *
	 * @throws SQLException Database exception.
	 */
	@Override
	public void shutdown() throws SQLException {
		LOGGER.info("BasicConnectionPool::shutdown: Beginning shutting down connection pool.");
		usedConnections.forEach(this::releaseConnection);
		for (Connection connection : connectionPool) {
			connection.close();
		}
		connectionPool.clear();
	}
}
