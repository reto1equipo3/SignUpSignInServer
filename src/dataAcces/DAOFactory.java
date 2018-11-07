/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAcces;

import java.sql.SQLException;

/**
 *
 * @author 2dam
 */
class DAOFactory {
	public DAO createDAOImplementation() throws SQLException{
		return new DAOImplementation();
	}
}
