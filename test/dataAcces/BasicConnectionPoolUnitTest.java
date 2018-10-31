/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAcces;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
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
	public static void setUpBasicConnectionPoolInstance() throws SQLException, ClassNotFoundException {
		connectionPool = BasicConnectionPool.create(
			"jdbc:mysql://macinf01.tartangalh.eus:3306/reto1_equipo3",
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
		//assertThat(connectionPool.releaseConnection(connection)).isTrue();
		assertTrue(connectionPool.releaseConnection(connection));
	}

	/**
	 * Test if the getter getUrl() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetUrl_thenCorrect() {
		//assertThat(connectionPool.getUrl()).isEqualTo("jdbc:mysql://macinf01.tartangalh.eus:3306/reto1_equipo3");
		assertTrue(connectionPool.getUrl().equals("jdbc:mysql://macinf01.tartangalh.eus:3306/reto1_equipo3"));
	}

	/**
	 * Test if the getter getUser() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetUser_thenCorrect() {
		//assertThat(connectionPool.getUser()).isEqualTo("root");
		assertTrue(connectionPool.getUser().equals("root"));
	}

	/**
	 * Test if the getter getPassword() returns the correct url.
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenCalledgetPassword_thenCorrect() {
		//assertThat(connectionPool.getPassword()).isEqualTo("abcd*1234");
		assertTrue(connectionPool.getPassword().equals("abcd*1234"));
	}

	/**
	 * Tests the behaviour when max amount of {@link Connection} is reached.
	 *
	 * @throws SQLException
	 */
	@Test
	public void givenBasicConnectionPoolInstance_whenAskedMoreThanMax_thenError() throws SQLException {
		ConnectionPool cp = BasicConnectionPool.create(
			"jdbc:mysql://macinf01.tartangalh.eus:3306/reto1_equipo3",
			"root",
			"abcd*1234");

		final int MAX_POOL_SIZE = 20;
		for (int i = 0; i < MAX_POOL_SIZE; i++) {
			cp.getConnection();
		}
		fail();
	}

	@Test
	public void givenBasicConnectionPoolInstance_whenShutdown_thenEmpty() throws SQLException {
		ConnectionPool cp = BasicConnectionPool.create(
			"jdbc:mysql://macinf01.tartangalh.eus:3306/reto1_equipo3",
			"root",
			"abcd*1234");
		//assertThat(((BasicConnectionPool)cp).getSize()).isEqualTo(10);
		assertTrue(((BasicConnectionPool) cp).getSize() == 6);

		((BasicConnectionPool) cp).shutdown();
		//assertThat(((BasicConnectionPool)cp).getSize()).isEqualTo(0);
		assertTrue(((BasicConnectionPool) cp).getSize() == 0);
	}
}
