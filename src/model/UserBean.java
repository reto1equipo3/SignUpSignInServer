/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Image;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data Transfer Object used in UI and client side for representing User entity.
 *
 * @author Leticia
 */
public class UserBean implements Serializable {

    private Integer id;
    private String login;
    private String email;
    private String fullName;
    private UserStatus status;
    private UserPrivilege privilege;
    private String password;
    private Timestamp lastAcess;
    private Timestamp lastPasswordChange;
    private Image photo;

    //Getter and setters
    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getLastAcess() {
        return lastAcess;
    }

    public Timestamp getLastPasswordChange() {
        return lastPasswordChange;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastAcess(Timestamp lastAcess) {
        this.lastAcess = lastAcess;
    }

    public void setLastPasswordChange(Timestamp lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

//Constructor
    public UserBean(Integer id, String login, String email, String fullName, UserStatus status, UserPrivilege privilege, String password, Timestamp lastAcess, Timestamp lastPasswordChange, Image photo) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
        this.privilege = privilege;
        this.password = password;
        this.lastAcess = lastAcess;
        this.lastPasswordChange = lastPasswordChange;
        this.photo = photo;
    }

    public UserBean() {
    }

    //Methods
    /**
     * Method to validate the email is correct.
     *
     * @param String email to validate
     * @return True if the email format is the same tu Pattern
     * @author Leticia
     */
    public static boolean checkEmail(String email) {

        //Establecemos el patrón
        Pattern p = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@"
                + "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");
        //Asociamos el String del email al patron
        //El Matcher analiza si dicha cadena o las subcadenas que la componen pertenecen al lenguaje dado.     

        Matcher m = p.matcher(email);

        return m.matches();  //El método matches de String nos permite comprobar si un String cumple una expresión regular pasado como parámetro.
    }

}
