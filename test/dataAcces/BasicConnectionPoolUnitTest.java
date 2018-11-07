package dataAcces;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the BasicConnectionPool class.
 *
 * @author Imanol
 */
public class BasicConnectionPoolUnitTest {

	private static ConnectionPool connectionPool;

	/**
	 * Creates a {@link Connection} pool before the tests are executed.
	 *
	 * @throws SQLException
	 */
	@BeforeClass
	public static void setUpBasicConnectionPoolInstance() throws SQLException {
		connectionPool = BasicConnectionPool.create(
			"jdbc:mysql://lapinf04.tartangalh.eus:3306/reto1_equipo3",
			"root",
			"abcd*1234");
	}

	/**
	 * Tests if the method getConnection returns a valid {@link Connection}.
	 *
	 * @throws SQLException
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetConnection_thenCorrect() throws SQLException {
		assertTrue(connectionPool.getConnection().isValid(1));
	}

	/**
	 * Test if the method release() releases correctly a {@link Connection}.
	 *
	 * @throws SQLException
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledreleaseConnection_thenCorrect() throws SQLException {
		Connection connection = connectionPool.getConnection();
		assertTrue(connectionPool.releaseConnection(connection));
	}

	/**
	 * Test if the getter getUrl() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetUrl_thenCorrect() {
		assertTrue(connectionPool.getUrl().equals("jdbc:mysql://lapinf04.tartangalh.eus:3306/reto1_equipo3"));
	}

	/**
	 * Test if the getter getUser() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetUser_thenCorrect() {
		assertTrue(connectionPool.getUser().equals("root"));
	}

	/**
	 * Test if the getter getPassword() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetPassword_thenCorrect() {
		assertTrue(connectionPool.getPassword().equals("abcd*1234"));
	}

	/**
	 * Tests the behaviour when max amount of {@link Connection} is reached.
	 *
	 * @throws SQLException
	 */
	@Test(expected = RuntimeException.class)
	public void givenBasicConnectionPoolInstance_whenAskedMoreThanMax_thenError() throws SQLException {
		ConnectionPool cp = BasicConnectionPool.create(
			"jdbc:mysql://lapinf04.tartangalh.eus:3306/reto1_equipo3",
			"root",
			"abcd*1234");

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
	public void givenBasicConnectionPoolInstance_whenShutdown_thenEmpty() throws SQLException {
		ConnectionPool cp = BasicConnectionPool.create(
			"jdbc:mysql://lapinf04.tartangalh.eus:3306/reto1_equipo3",
			"root",
			"abcd*1234");
		assertTrue(((BasicConnectionPool) cp).getSize() == 6);

		((BasicConnectionPool) cp).shutdown();
		assertTrue(((BasicConnectionPool) cp).getSize() == 0);
	}
}
