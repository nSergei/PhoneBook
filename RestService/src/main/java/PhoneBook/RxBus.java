package PhoneBook;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;

public class RxBus {
    private static RxBus INSTANCE = new RxBus();

    static RxBus getInstance(){
        return INSTANCE;
    }
    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    private RxBus(){}

    void accept(Object o){
        bus.accept(o);
    }

    Observable<Object> get() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
