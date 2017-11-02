package PhoneBook.Persistence;

import PhoneBook.Persistence.Entities.Contact;
import PhoneBook.Persistence.Entities.User;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.sources.Flag;
import io.reactivex.rxjavafx.sources.ListChange;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

import static PhoneBook.Login.ACTIVE_USER;

public class DbHelper{
    private static DbHelper ourInstance = new DbHelper();
    public final ContactsObservable contactsObservable;
    public final UsersObservable usersObservable;

    private SessionFactory sessionFactory;

    public static DbHelper getInstance() {
        return ourInstance;
    }

    private DbHelper() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
        this.contactsObservable = new ContactsObservable();
        this.usersObservable = new UsersObservable();
    }
    public class UsersObservable implements Observer<ListChange<User>>{
        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(ListChange<User> userListChange) {
            User user = userListChange.getValue();
            Flag flag = userListChange.getFlag();
            if(flag.equals(Flag.REMOVED) || user.getLogin().equals("")){
                deleteUser(user.getId());
            } else {
                updateUser(user);
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }

        public void onDispose() {
            if(this.disposable != null) {
                this.disposable.dispose();
            }
        }
    }

    public class ContactsObservable implements Observer<ListChange<Contact>>{
        private Disposable disposable;

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(ListChange<Contact> contactListChange) {
            Contact contact = contactListChange.getValue();
            Flag flag = contactListChange.getFlag();
            if(flag.equals(Flag.REMOVED) || contact.getName().equals("")){
                deleteContact(contact);
            } else {
                if(flag.equals(Flag.ADDED)){
                    ACTIVE_USER.addContact(contact);
                }
                updateContact(contactListChange.getValue());
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }

        public void onDispose() {
            if(this.disposable != null) {
                this.disposable.dispose();
            }
        }
    }
    private int deleteUser(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete User where id = :id");
        query.setParameter("id", id);
        int result = query.executeUpdate();
        session.close();
        return result;
    }
    public int deleteUser(String login){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete User where login = :login");
        query.setParameter("login", login);
        int result = query.executeUpdate();
        session.close();
        return result;
    }
    public boolean addUser(User userToAdd){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(userToAdd);
        try {
            session.getTransaction().commit();
        } catch (javax.persistence.PersistenceException e){
            return false;
        }finally {
            session.close();
        }
        return true;
    }
    @SuppressWarnings({"unchecked"})
    public User getUser(String login){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User where login = :login");
        query.setParameter("login", login);
        List<User> user = (List<User>) query.list();
        session.close();
        return user.size() != 0? user.get(0): null;
    }

    @SuppressWarnings({"unchecked"})
    public List<User> getAllUsers(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> users =  (List<User>)session.createQuery("from User").list();
        session.getTransaction().commit();
        session.close();
        return users;
    }
    void addContact(Contact contact){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(contact);
        session.getTransaction().commit();
        session.close();
    }
    int deleteContact(Contact contact){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("delete Contact where id = :id");
        query.setParameter("id", contact.getId());
        int result = query.executeUpdate();
        session.close();
        return result;
    }
    public void closeSessionFactory(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<Contact> getAllContacts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Contact> contacts =  (List<Contact>)session.createQuery("from Contact").list();
        session.getTransaction().commit();
        session.close();
        return contacts;
    }

    void updateUser(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
    }
    void updateContact(Contact contact){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(contact);
        session.getTransaction().commit();
        session.close();
    }
}
