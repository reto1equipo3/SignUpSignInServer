/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import exceptions.BadPasswordException;
import exceptions.DatabaseException;
import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import java.awt.Image;
import java.sql.SQLException;
import java.util.logging.Logger;
import model.UserBean;
import model.UserPrivilege;
import model.UserStatus;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Tests the DAOImplementation class.
 *
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOImplementationTest {

	/**
	 * {@link Logger} object used to log messages for the class.
	 */
	private static final Logger LOGGER = Logger.getLogger("dataaccess");
	/**
	 * Static DAO instance.
	 */
	private static DAO dao;

	/**
	 * Default constructor.
	 */
	public DAOImplementationTest() {
	}

	/**
	 * Get the {@link DAOImplementation} object before executing the class.
	 *
	 * @throws Exception
	 */
	@BeforeClass
	public static void getDAO() throws Exception {
		LOGGER.info("DAOImplementationTest::getDAO: Beginning getting DAO.");
		DAOFactory daoFactory = new DAOFactory();
		dao = daoFactory.createDAOImplementation();
	}

	/**
	 * Test of signIn method, of class DAOImplementation.
	 *
	 * @throws Exception
	 */
	@Test
	public void test00_SignIn_valid() throws Exception {
		LOGGER.info("DAOImplementationTest::test00: Beginning valid signing in test.");
		UserBean user = new UserBean();
		user.setLogin("Leticia");
		user.setPassword("Abcd*123");

		UserBean resultUser = dao.signIn(user);
		assertEquals(resultUser.getFullName().trim().toLowerCase(), "leticia garcia zarragoitia");
		assertEquals(resultUser.getEmail().trim().toLowerCase(), "letogz@gmail.com");
	}

	/**
	 * Test of signIn method, using wrong login.
	 * @throws Exception 
	 */
	@Test(expected = LoginNotExistingException.class)
	public void test01_signIn_wrongLogin() throws Exception {
		LOGGER.info("DAOImplementationTest::test01: Beginning wrong login signing in test.");
		UserBean user = new UserBean();
		user.setLogin("asdsadf");
		user.setPassword("Abcd*123");

		dao.signIn(user);
		fail();
	}

	/**
	 * Test of signIn method, using wrong password.
	 * @throws Exception 
	 */
	@Test(expected = BadPasswordException.class)
	public void test02_signIn_wrongPassword() throws Exception {
		LOGGER.info("DAOImplementationTest::test02: Beginning wrong password signing in test.");
		UserBean user = new UserBean();
		user.setLogin("Leticia");
		user.setPassword("asdasdf");

		dao.signIn(user);
		fail();
	}

	/**
	 * Test of signIn method, using null login.
	 * @throws Exception 
	 */
	@Test(expected = LoginNotExistingException.class)
	public void test03_signIn_nullLogin() throws Exception {
		LOGGER.info("DAOImplementationTest::test03: Beginning null login signing in test.");
		UserBean user = new UserBean();

		dao.signIn(user);
		fail();
	}

	/**
	 * Test of signIn method, using null password.
	 * @throws Exception 
	 */
	@Test(expected = BadPasswordException.class)
	public void test04_signIn_nullPassword() throws Exception {
		LOGGER.info("DAOImplementationTest::test04: Beginning null password signing in test.");
		UserBean user = new UserBean();
		user.setLogin("Leticia");

		dao.signIn(user);
		fail();
	}

	/**
	 * Test of signUp method, of class DAOImplementation.
	 * @throws java.lang.Exception
	 */
	@Test
	public void test05_SignUp() throws Exception {
		LOGGER.info("DAOImplementationTest::test05: Beginning signing up test.");
		UserBean user = new UserBean(null, "PacoPico12", "pacopico12@gmail.com", "Paco Pico", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
	}

	/**
	 * Test of signUp method, using existing login.
	 * @throws Exception 
	 */
	@Test(expected = LoginExistingException.class)
	public void test06_signUp_repeatedLogin() throws Exception {
		LOGGER.info("DAOImplementationTest::test06: Beginning repeated login signing up test.");
		UserBean user = new UserBean(null, "Leticia", "letogz@gmail.com", "Leticia", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
		fail();
	}

	/**
	 * Test of signUp method, using existing email.
	 * @throws Exception 
	 */
	@Test(expected = EmailNotUniqueException.class)
	public void test07_signUp_repeatedEmail() throws Exception {
		LOGGER.info("DAOImplementationTest::test07: Beginning repeated email signing up test.");
		UserBean user = new UserBean(null, "aaaaa", "letogz@gmail.com", "Leticia", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
		fail();
	}

	/**
	 * Test of logOut method, of class DAOImplementation.
	 * @throws java.lang.Exception
	 */
	@Test
	public void test08_LogOut() throws Exception {
		LOGGER.info("DAOImplementationTest::test08: Beginning log out test.");
		UserBean user = new UserBean();
		user.setFullName("Leticia Garcia");
		user.setLogin("Leticia");
		user.setEmail("leti@gmail.com");
		user.setPassword("Abcd*1234");

		dao.logOut(user);
	}
}
