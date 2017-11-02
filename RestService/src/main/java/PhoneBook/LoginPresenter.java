package PhoneBook;

import PhoneBook.Events.OnLoginAttemptedEvent;
import PhoneBook.Persistence.DbHelper;
import PhoneBook.Persistence.Entities.User;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.util.Pair;

public class LoginPresenter implements Observer<Pair<UserAction, User>> {
    private static LoginPresenter INSTANCE = new LoginPresenter();
    private static RxBus bus = RxBus.getInstance();
    private static DbHelper dbHelper = DbHelper.getInstance();

    public static LoginPresenter getInstance() {
        return INSTANCE;
    }
    private LoginPresenter(){
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Pair<UserAction, User> pair) {
        OnLoginAttemptedEvent event;
        User user = pair.getValue();
        UserAction userAction = pair.getKey();
        User registeredUser = dbHelper.getUser(user.getLogin());
        if(userAction.equals(UserAction.SignUp)){
            if(registeredUser != null){
                event = new OnLoginAttemptedEvent(null, "Login is taken!");
            } else if(user.getPassword().equals("")){
                event = new OnLoginAttemptedEvent(null, "The password field must be filled!");
            } else {
                if(user.getLogin().equals("admin")){
                    user.setRole(User.ROLE_ADMIN);
                } else {
                    user.setRole(User.ROLE_USER);
                }
                dbHelper.addUser(user);
                event = new OnLoginAttemptedEvent(user, "");
            }
        } else {
            if (registeredUser == null || !registeredUser.getPassword().equals(user.getPassword())) {
                event = new OnLoginAttemptedEvent(null, "Invalid login/password");
            } else {
                event = new OnLoginAttemptedEvent(registeredUser, "");
            }
        }
        bus.accept(event);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
