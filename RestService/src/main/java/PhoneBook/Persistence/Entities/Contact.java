package PhoneBook.Persistence.Entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "Contact")
public class Contact {
    private int id;

    private User owner;

    private String name;
    private String phoneNumber;

    private static String formatPhoneNumber(String phoneNumber){
        return phoneNumber.replaceAll("[\\D]", "");
    }

    public Contact(){

    }
    public Contact(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }
        Contact contact = (Contact) o;
        return this.name.equals(contact.name) &&
                this.phoneNumber.equals(contact.phoneNumber) &&
                this.getOwner().getId() == contact.getOwner().getId();
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + id;
        result = 31 * result + name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        return result;
    }
}
