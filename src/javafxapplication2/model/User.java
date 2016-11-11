
package javafxapplication2.model;

import java.util.Map;


public class User {
    
    Map<String,String> cookies;
    String username;
    
    
    /**
     * Constructor
     * 
     * Set's both the username and the cookies of the user;
     * 
     * @param username user's username
     * @param cookies user's login cookies
     */
    public User(String username, Map cookies) {
        this.username = username;
        this.cookies = cookies;
    }
    
    
    /**
     * Get the user's login cookies
     * 
     * @return FAKKU cookies used to identify the user after login.
     */
    public Map<String,String> getCookies() {
        return cookies;
    }
    
    /**
     * Set the login cookies for this user
     * 
     * @param cookies user's login cookies
     * 
     */
    public void setCookies(Map cookies) {
        this.cookies = cookies;
    }
    
    /** 
     * Set the user's username. 
     * 
     * @param username username of the user
     */
    public void setUserName(String username) {
        this.username = username;
    }
    
    /**
     * Get the user's username
     * 
     * @return returns the username of the user
     */
    public String getUserName() {
        return username;
    }
}
