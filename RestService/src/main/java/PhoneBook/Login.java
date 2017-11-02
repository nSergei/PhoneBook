package PhoneBook;

import PhoneBook.Events.OnLoginAttemptedEvent;
import PhoneBook.Persistence.DbHelper;
import PhoneBook.Persistence.Entities.User;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.stage.Stage;

public class Login extends Application implements Observer<UserAction> {

    private static DbHelper dbHelper = DbHelper.getInstance();
    private static LoginPresenter loginPresenter = LoginPresenter.getInstance();
    public static User ACTIVE_USER;

    private CompositeDisposable compositeDisposable;
    private RxBus bus;
    private Stage pStage;
    private LoginScene loginScene;
    private MainScene mainScene;
    private UsersScene userScene;
    private Disposable disposable;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;
        compositeDisposable = new CompositeDisposable();
        bus = RxBus.getInstance();
        compositeDisposable.add(bus.get().subscribe(o -> {
                    if (o instanceof OnLoginAttemptedEvent) {
                        handleOnLoginAttemptedEvent((OnLoginAttemptedEvent) o);
                    }
                })
        );
        pStage.setWidth(300);
        pStage.setHeight(550);
        pStage.setTitle("PhoneBook");
        loginScene = new LoginScene();
        loginScene.getSigninAttempedObservable()
                .subscribe(loginPresenter);
        loginScene.getSignupAttempedObservable()
                .subscribe(loginPresenter);
        pStage.setScene(loginScene.getScene());
        pStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        dbHelper.closeSessionFactory();
        System.out.println("closed db connection");
    }

    private void handleOnLoginAttemptedEvent(OnLoginAttemptedEvent event) {
        if(event.getUser() == null){
            loginScene.showLoginFailedMessage(event.getInfoMessage());
        } else {
            loginScene.reset();
            ACTIVE_USER = event.getUser();
            if (mainScene == null) {
                mainScene = new MainScene();
            }
            mainScene.setData(ACTIVE_USER.getContacts());
            mainScene.getContactsObservable()
                    .observeOn(Schedulers.io())
                    .subscribe(dbHelper.contactsObservable);
            mainScene.getMenuObservable()
                    .subscribe(this);
            pStage.setScene(mainScene.getScene());
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(UserAction userAction) {
        switch (userAction) {
            case SignOut:
                pStage.setScene(loginScene.getScene());
                mainScene.reset();
                dbHelper.contactsObservable.onDispose();
                dbHelper.usersObservable.onDispose();
                this.onDispose();
                ACTIVE_USER = null;
                break;
            case DeleteAccount:
                pStage.setScene(loginScene.getScene());
                mainScene.reset();
                dbHelper.deleteUser(ACTIVE_USER.getLogin());
                dbHelper.contactsObservable.onDispose();
                dbHelper.usersObservable.onDispose();
                this.onDispose();
                ACTIVE_USER = null;
                break;
            case ViewContacts:
                if(pStage.getScene() == mainScene.getScene()){
                    break;
                }
                dbHelper.contactsObservable.onDispose();
                dbHelper.usersObservable.onDispose();
                this.onDispose();
                userScene.reset();
                mainScene.setData(ACTIVE_USER.getContacts());
                mainScene.getContactsObservable()
                        .observeOn(Schedulers.io())
                        .subscribe(dbHelper.contactsObservable);
                mainScene.getMenuObservable()
                        .subscribe(this);
                pStage.setScene(mainScene.getScene());
                break;
            case ViewUsers:
                if(userScene == null){
                    userScene = new UsersScene();
                }else if(pStage.getScene() == userScene.getScene()){
                    break;
                }
                dbHelper.contactsObservable.onDispose();
                dbHelper.usersObservable.onDispose();
                this.onDispose();
                mainScene.reset();
                if (userScene == null) {
                    userScene = new UsersScene();
                }
                userScene.setData(dbHelper.getAllUsers());
                userScene.getUsersObservable()
                        .observeOn(Schedulers.io())
                        .subscribe(dbHelper.usersObservable);
                userScene.getMenuObservable()
                        .subscribe(this);
                pStage.setScene(userScene.getScene());
                break;
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
    private void onDispose(){
        if(this.disposable != null) {
            this.disposable.dispose();
        }
    }
}