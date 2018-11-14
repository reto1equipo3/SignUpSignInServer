/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import dataaccess.DAO;
import dataaccess.DAOFactory;
import exceptions.BadPasswordException;
import exceptions.DatabaseException;
import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import exceptions.LoginNotExistingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageType;
import model.UserBean;

/**
 * This class for treating individual requests of the client. Extends from the
 * class Thread
 *
 * @author Imanol and Leticia
 */
public class ConnectionThread extends Thread {

	private static final Logger LOGGER = Logger.getLogger("signupsigninserver.businesslogic");
	private Socket socket;
	private DAOFactory daoFactory;
	private DAO dao;

	/**
	 * The server passes the socket
	 *
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * The run() with switch(message.getMessage()) observe which method have to
	 * be executed
	 */
	@Override
	public void run() {

		ObjectOutputStream oOutputStream = null;
		ObjectInputStream oInputStream = null;
		Message message = null;
		try {
			oInputStream = new ObjectInputStream(socket.getInputStream()); //Va a escribir lo que viene del socket.
			oOutputStream = new ObjectOutputStream(socket.getOutputStream()); //Va a a leer lo que viene del socket.
			message = (Message) oInputStream.readObject(); //  Obtenemos el mensaje

			switch (message.getMessage()) { //Miramos que tipo de mensaje tenemos
				case SIGN_IN:  //Si el mensaje es de tipo SIGN_IN:
					daoFactory = new DAOFactory(); //Conseguimos la factoria para saber la implementacion
					dao = daoFactory.createDAOImplementation(); //porque con la interfaz DAO no la tenemos. Sabe los metodo que tiene,
					//y asi sabe que lo que tiene que devolver es lo que queremos.
					UserBean user = dao.signIn((UserBean) message.getData()); //(SIGN_IN) tenemos que pasarle el usuario. Y lo guardamos en user.
					LOGGER.info("ConnectionThread::run: User signed in correctly.");
					oOutputStream.writeObject(new Message(MessageType.SIGN_IN, user)); //Y con ese user, le escribimos la vuelta al cliente (el SIGN_IN, y el usuario)
					break;
				case SIGN_UP:
					daoFactory = new DAOFactory();
					dao = daoFactory.createDAOImplementation();
					dao.signUp((UserBean) message.getData());
					oOutputStream.writeObject(new Message(MessageType.SIGN_UP, "OK"));
					break;
				case LOG_OUT:
					daoFactory = new DAOFactory();
					dao = daoFactory.createDAOImplementation();
					dao.logOut((UserBean) message.getData());
					oOutputStream.writeObject(new Message(MessageType.LOG_OUT, "OK"));
					break;
			}
		} catch (LoginNotExistingException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Login not found.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_LOGINNOTFOUND, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (BadPasswordException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Wrong password.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_BADPASSWORD, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (LoginExistingException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Login already exist.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_LOGINEXISTS, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (EmailNotUniqueException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Email already used.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_EMAILNOTUNIQUE, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (DatabaseException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Connection problems.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_ERROR, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (ClassNotFoundException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: User not found.",
				ex.getMessage());
			try {
				oOutputStream.writeObject(new Message(MessageType.E_LOGINNOTFOUND, ex.getMessage()));
			} catch (IOException ex1) {
				Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex1);
			}
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE,
				"ConnectionThread::run: Could not connect anything.",
				ex.getMessage());
		}
	}
}
