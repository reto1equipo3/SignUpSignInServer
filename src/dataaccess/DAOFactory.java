/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import exceptions.DatabaseException;

/**
 * This class creates a DAOImplementation instance and returns it.
 * @author Gaizka
 */
public class DAOFactory {
	public DAO createDAOImplementation() throws DatabaseException{
		return new DAOImplementation();
	}
}