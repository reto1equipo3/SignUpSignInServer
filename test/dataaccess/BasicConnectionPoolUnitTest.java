package dataaccess;

import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Tests the BasicConnectionPool class.
 *
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicConnectionPoolUnitTest {

	/**
	 * {@link Logger} object used to log messages for the class.
	 */
	private static final Logger LOGGER = Logger.getLogger("dataaccess");
	/**
	 * Static pool of {@link Connection} to connect to database.
	 */
	private static ConnectionPool connectionPool;
	/**
	 * Database name.
	 */
	private static final String DATABASE = ResourceBundle.getBundle("config.DatabaseParameters").getString("DATABASE");
	/**
	 * Url of the database.
	 */
	private static final String URL = ResourceBundle.getBundle("config.DatabaseParameters").getString("URL");
	/**
	 * User of the database.
	 */
	private static final String USER = ResourceBundle.getBundle("config.DatabaseParameters").getString("USER");
	/**
	 * Password of the database.
	 */
	private static final String PASSWORD = ResourceBundle.getBundle("config.DatabaseParameters").getString("PASSWORD");
	private static final int MAX_CONNECTIONS = Integer.parseInt(ResourceBundle.getBundle("config.DatabaseParameters").getString("MAX_CONNECTIONS"));

	/**
	 * Creates a {@link Connection} pool before the tests are executed.
	 *
	 * @throws SQLException
	 */
	@BeforeClass
	public static void setUpBasicConnectionPoolInstance() throws SQLException {
		LOGGER.info("BasicConnectionPoolUnitTest::setUpBasicConnectionPoolInstance: Beginng setting connection pool instance.");
		connectionPool = BasicConnectionPool.create(URL, USER, PASSWORD, MAX_CONNECTIONS);
	}

	/**
	 * Tests if the method getConnection returns a valid {@link Connection}.
	 *
	 * @throws SQLException
	 */
	@Test
	public void test1_givenBasicConnectionPoolInstance_whenCalledgetConnection_thenCorrect() throws SQLException, DatabaseException {
		LOGGER.info("BasicConnectionPoolUnitTest::test1: Beginng get connection test.");
		assertTrue(connectionPool.getConnection().isValid(1));
	}

	/**
	 * Test if the method release() releases correctly a {@link Connection}.
	 *
	 * @throws SQLException
	 */
	@Test
	public void test2_givenBasicConnectionPoolInstance_whenCalledreleaseConnection_thenCorrect() throws SQLException, DatabaseException {
		LOGGER.info("BasicConnectionPoolUnitTest::test2: Beginng release connection test.");
		Connection connection = connectionPool.getConnection();
		assertTrue(connectionPool.releaseConnection(connection));
	}

	/**
	 * Test if the getter getUrl() returns the correct url.
	 */
	@Test
	public void test3_givenBasicConnectionPoolInstance_whenCalledgetUrl_thenCorrect() {
		LOGGER.info("BasicConnectionPoolUnitTest::test3: Beginng get url test.");
		assertTrue(connectionPool.getUrl().equals(URL));
	}

	/**
	 * Test if the getter getUser() returns the correct url.
	 */
	@Test
	public void test4_givenBasicConnectionPoolInstance_whenCalledgetUser_thenCorrect() {
		LOGGER.info("BasicConnectionPoolUnitTest::test4: Beginng get user test.");
		assertTrue(connectionPool.getUser().equals(USER));
	}

	/**
	 * Test if the getter getPassword() returns the correct url.
	 */
	@Test
	public void tes5_givenBasicConnectionPoolInstance_whenCalledgetPassword_thenCorrect() {
		LOGGER.info("BasicConnectionPoolUnitTest::test5: Beginng get password test.");
		assertTrue(connectionPool.getPassword().equals(PASSWORD));
	}

	/**
	 * Tests the behaviour when max amount of {@link Connection} is reached.
	 *
	 * @throws SQLException
	 */
	@Test(expected = DatabaseException.class)
	public void test6_givenBasicConnectionPoolInstance_whenAskedMoreThanMax_thenError() throws SQLException, DatabaseException {
		LOGGER.info("BasicConnectionPoolUnitTest::test6: Beginng asked more than max test.");
		ConnectionPool cp = BasicConnectionPool.create(URL, USER, PASSWORD, MAX_CONNECTIONS);

		final int MAX_POOL_SIZE = 20;
		for (int i = 0; i < MAX_POOL_SIZE; i++) {
			cp.getConnection();
		}
		fail();
	}

	/**
	 * Test that the shutdown() method releases all the connections.
	 *
	 * @throws SQLException
	 */
	@Test
	public void tes7_givenBasicConnectionPoolInstance_whenShutdown_thenEmpty() throws SQLException {
		LOGGER.info("BasicConnectionPoolUnitTest::test7: Beginng shutdown test.");
		ConnectionPool cp = BasicConnectionPool.create(URL, USER, PASSWORD, MAX_CONNECTIONS);
		assertTrue(cp.getSize() == MAX_CONNECTIONS/2);
		
		cp.shutdown();
		assertTrue(cp.getSize() == 0);
	}
}
