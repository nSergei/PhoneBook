package PhoneBook;

import PhoneBook.Persistence.Entities.User;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class LoginScene {

    private Scene scene;
    private Button signInButton;
    private Button signUpButton;
    private TextField loginField;
    private PasswordField passwordField;
    private Text errorText;

    public LoginScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Use \"admin\" for admin account");
        scenetitle.setId("welcome-text");
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Login:");
        grid.add(userName, 0, 1);

        loginField = new TextField();
        grid.add(loginField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        signInButton = new Button("Sign in");
        signUpButton = new Button("Sign up");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(signInButton, signUpButton);
        grid.add(hbBtn, 1, 4);

        errorText = new Text();
        grid.add(errorText, 1, 6);
        errorText.setId("errorText");

        scene = new Scene(grid, 300, 275);
    }

    public Scene getScene() {
        return scene;
    }
    Observable<Pair<UserAction, User>> getSigninAttempedObservable(){
        return JavaFxObservable.actionEventsOf(signInButton)
                .doOnEach(actionEventNotification -> {
                    errorText.setText("");
                    signInButton.setDisable(true);
                    signUpButton.setDisable(true);
                })
                .map(actionEvent -> new Pair<>(UserAction.SignIn,
                        new User(loginField.getText(), passwordField.getText(), "", -1)));
    }
    Observable<Pair<UserAction, User>> getSignupAttempedObservable(){
        return JavaFxObservable.actionEventsOf(signUpButton)
                .doOnEach(actionEventNotification -> {
                    errorText.setText("");
                    signInButton.setDisable(true);
                    signUpButton.setDisable(true);
                })
                .map(actionEvent -> new Pair<>(UserAction.SignUp,
                        new User(loginField.getText(), passwordField.getText(), "", -1)));
    }

    public void showLoginFailedMessage(String message) {
        errorText.setText(message);
        signInButton.setDisable(false);
        signUpButton.setDisable(false);
    }

    public void reset() {
        passwordField.setText("");
        signInButton.setDisable(false);
        signUpButton.setDisable(false);
    }
}
