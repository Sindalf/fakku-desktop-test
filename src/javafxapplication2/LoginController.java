/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication2;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class LoginController implements Initializable {

    @FXML
    private Label user, result;
    @FXML
    private Button loginButton;
    @FXML
    private TextField userField, passwordField;

    Main application;

    public void setApp(Main application) {
        
        this.application = application;
    }

    @FXML
    private void handleLoginButton(ActionEvent event) throws IOException {
       // loginButton.setDisable(true); // makes button unclickable while processing data
        String username = userField.getText();
        String password = passwordField.getText();
        Map<String, String> cookies = login(username, password);
        
        System.out.println(username);
        if (cookies != null) {
            result.setText("Login successful!");
            System.out.println("trying to create user");
            System.out.println(application);
            application.createUser(username, cookies);
            application.gotoFrontPage();
        } else {
            result.setText("Login failed!");
           // loginButton.setDisable(false); // makes button clickable again
        }
    }

    public Map login(String username, String password) throws IOException {
        Connection.Response res = Jsoup.connect("https://www.fakku.net/login/submit")
                .data("username", username, "password", password).timeout(10000)
                .method(Connection.Method.POST).execute();
        System.out.println(res.statusMessage());

        Map cookies;

        if (res.url().toString().equals("https://www.fakku.net/login/submit")) {
            return null;
        } else {
            cookies = res.cookies();
            System.out.println(cookies);

            //Document doc2 = Jsoup.connect("https://www.fakku.net/hentai/drunk-communication-english/read").cookies(cookies).get();
            return cookies;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
