/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAcces;

/**
 * This class is used to create an intermediary between the Logic tier and the Data Access tier
 * 
 * @author Gaizka
 */
public class DAOFactory {
    
/**
* This method is used to return an object of type DAO
* 
* @return the object of type DAO that will be the intermediary
*/
    public DAO createDAOImplementation() {
        return new DAOImplementation();
    }
    
}
