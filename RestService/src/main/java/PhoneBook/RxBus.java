package PhoneBook;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;

public class RxBus {
    private static RxBus INSTANCE = new RxBus();

    public static RxBus getInstance(){
        return INSTANCE;
    }
    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    private RxBus(){}

    public void accept(Object o){
        bus.accept(o);
    }

    public Observable<Object> get() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
