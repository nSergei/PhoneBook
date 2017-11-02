package PhoneBook;

import PhoneBook.Persistence.DbHelper;
import PhoneBook.Persistence.Entities.Contact;
import PhoneBook.Persistence.Entities.User;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.sources.Flag;
import io.reactivex.rxjavafx.sources.ListChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

import static PhoneBook.Login.ACTIVE_USER;

public class UsersScene {

    private Scene scene;
    private ObservableList<User> data;
    private final VBox vbox;
    private TableView table;
    private Observable<UserAction> menuObservable;

    public UsersScene() {
        table = new TableView();
        scene = new Scene(new Group());

        final Label label = new Label("Address Book");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);
        TableColumn loginCol = new TableColumn("Login");
        loginCol.setMinWidth(280);
        loginCol.setCellValueFactory(
                new PropertyValueFactory<User, String>("login"));
        loginCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<User, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<User, String> t) {
                        User user = (User) t.getTableView().getItems().get(
                                t.getTablePosition().getRow());
                        user.setLogin(t.getNewValue());
                        if(t.getNewValue().equals("")) {
                            data.remove(user);
                        } else {
                            DbHelper.getInstance().usersObservable.onNext(ListChange.of((User) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow()), Flag.UPDATED));
                        }
                    }
                }
        );
        table.getColumns().add(loginCol);
        MenuBar menuBar = createMenu();

        vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(menuBar, label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
    }

    Observable<ListChange<User>> getUsersObservable() {
        return JavaFxObservable.changesOf(data);
    }
    private MenuBar createMenu(){
        MenuBar menuBar = new MenuBar();
        menuObservable = Observable.empty();
        switch (ACTIVE_USER.getRole()){
            case User.ROLE_ADMIN:
                // --- Menu View
                Menu menuView = new Menu("View");
                MenuItem usersView = new MenuItem("Users");
                menuObservable = menuObservable.mergeWith(JavaFxObservable.actionEventsOf(usersView)
                        .map(actionEvent -> UserAction.ViewUsers));
                MenuItem contactsView = new MenuItem("Contacts");
                menuObservable = menuObservable.mergeWith(JavaFxObservable.actionEventsOf(contactsView)
                        .map(actionEvent -> UserAction.ViewContacts));
                menuView.getItems().addAll(usersView, contactsView);
                menuBar.getMenus().add(menuView);
            case User.ROLE_USER:
                Menu optionsMenu = new Menu("Options");
                MenuItem logout = new MenuItem("SignOut");
                menuObservable = menuObservable.mergeWith(JavaFxObservable.actionEventsOf(logout)
                        .map(actionEvent -> UserAction.SignOut));
                MenuItem deleteAccount = new MenuItem("Delete account");
                menuObservable = menuObservable.mergeWith(JavaFxObservable.actionEventsOf(deleteAccount)
                        .map(actionEvent -> UserAction.DeleteAccount));
                optionsMenu.getItems().addAll(logout, deleteAccount);
                menuBar.getMenus().add(optionsMenu);
                break;
        }
        return menuBar;
    }
    public Scene getScene() {
        return scene;
    }

    public Observable<UserAction> getMenuObservable() {
        return menuObservable;
    }

    public void reset() {
        this.data.clear();
    }
    public void setData(List<User> users){
        if(users != null) {
            this.data = FXCollections.observableArrayList(users);
        } else {
            this.data = FXCollections.observableArrayList();
        }
        table.setItems(data);
        vbox.getChildren().remove(0);
        vbox.getChildren().add(0, createMenu());
    }
}
