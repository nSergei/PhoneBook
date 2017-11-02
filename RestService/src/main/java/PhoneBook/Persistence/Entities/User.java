package PhoneBook.Persistence.Entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "User")
public class User {
    @Transient
    public final static int ROLE_ADMIN = 1;
    @Transient
    public final static int ROLE_USER = 2;

    private int id;

    //Not using login as a primary key - it can change

    private String login;
    private String password;
    private String name;
    private int role;

    private List<Contact> contacts = new ArrayList<>();

    public User() {

    }

    public User(String login, String password, String name, int role) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Column(unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setOwner(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setOwner(null);
    }
    public void removeContactById(int id){
        for(int i = 0; i < this.contacts.size(); i++){
            if(this.contacts.get(i).getId() == id){
                this.contacts.remove(i);
                break;
            }
        }
    }

    public void removeAllContacts() {
        this.contacts.forEach(contact -> contact.setOwner(null));
        this.contacts.clear();
    }
}
