/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAcces;

import exception.BadPasswordException;
import exception.DatabaseException;
import exception.EmailNotUniqueException;
import exception.LoginExistingException;
import exception.LoginNotExistingException;
import java.awt.Image;
import java.sql.SQLException;
import model.UserBean;
import model.UserPrivilege;
import model.UserStatus;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DAOImplementationTest {

	private static DAO dao;

	public DAOImplementationTest() {
	}

	@BeforeClass
	public static void getDAO() throws Exception {
		DAOFactory daoFactory = new DAOFactory();
		dao = daoFactory.createDAOImplementation();
	}

	/**
	 * Test of signIn method, of class DAOImplementation.
	 */
	@Test
	public void test0_SignIn() throws Exception {
		UserBean user = new UserBean();
		user.setLogin("Leticia");
		user.setPassword("Abcd*123");

		UserBean resultUser = dao.signIn(user);
		assertEquals(resultUser.getFullName().trim().toLowerCase(), "leticia garcia zarragoitia");
		assertEquals(resultUser.getEmail().trim().toLowerCase(), "letogz@gmail.com");
	}

	@Test(expected = LoginNotExistingException.class)
	public void test01_signIn_wrongLogin() throws Exception {
		UserBean user = new UserBean();
		user.setLogin("asdsadf");
		user.setPassword("Abcd*123");

		dao.signIn(user);
		fail();
	}

	@Test(expected = BadPasswordException.class)
	public void test02_signIn_wrongPassword() throws Exception {
		UserBean user = new UserBean();
		user.setLogin("Leticia");
		user.setPassword("asdasdf");

		dao.signIn(user);
		fail();
	}
	@Test(expected=LoginNotExistingException.class)
	public void test03_signIn_nullLogin() throws Exception{
		UserBean user = new UserBean();

		dao.signIn(user);
		fail();
	}

	@Test(expected=BadPasswordException.class)
	public void test04_signIn_nullPassword() throws Exception{
		UserBean user = new UserBean();
		user.setLogin("asdasdsadas");

		dao.signIn(user);
		fail();
	}
	/**
	 * Test of signUp method, of class DAOImplementation.
	 */
	@Test
	public void test11_SignUp() throws Exception {
		UserBean user = new UserBean(null,"PacoPico10", "pacopico10@gmail.com", "Paco Pico", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
	}

	@Test(expected = LoginExistingException.class)
	public void test12_signUp_repeatedLogin() throws Exception{
		UserBean user = new UserBean(null,"Leticia", "letogz@gmail.com", "Leticia", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
		fail();
	}
	@Test(expected=EmailNotUniqueException.class)
	public void test13_signUp_repeatedEmail() throws Exception{
		UserBean user = new UserBean(null,"aaaaa", "letogz@gmail.com", "Leticia", UserStatus.ENABLED, UserPrivilege.USER, "Abcd*1234", null, null, null);
		dao.signUp(user);
		fail();
	}

	/**
	 * Test of logOut method, of class DAOImplementation.
	 */
	@Test
	public void test2_LogOut() throws Exception {
		UserBean user = new UserBean();
		user.setFullName("Leticia Garcia");
		user.setLogin("Leticia");
		user.setEmail("leti@gmail.com");
		user.setPassword("Abcd*1234");

		dao.logOut(user);
	}


}
