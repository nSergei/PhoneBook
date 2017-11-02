//package PhoneBook;
//
//import PhoneBook.Entities.Contact;
//import PhoneBook.Entities.User;
//
//import java.util.List;
//
//public class Application {
//
//    public static void main(String[] args) {
////        User user = new User("Fresh user", "fresh password", "fresh user name", 1);
////        System.out.printf("User id is %d%n", user.getId());
////        if(!DbHelper.getInstance()
////                .addUser(user)){
////            System.out.println("couldnt add user - login already exists");
////        }
////        for(User u: DbHelper.getInstance().getAllUsers()){
////            System.out.printf("" +
////                    "Login: %1$s%n" +
////                    "Username: %2$s%n" +
////                    "Password: %3$s%n%n" +
////                    "", u.getLogin(), u.getName(), u.getPassword());
////        }
////        DbHelper.getInstance().deleteUser("Fresh user");
////        User user = DbHelper.getInstance().getUser("Fresh user");
////        user.removeAllContacts();
////        user.addContact(new Contact("Vasya", "111111111111"));
////        user.addContact(new Contact("Vasya1", "2222222222"));
////        user.addContact(new Contact("Vasya3", "3333333333"));
////        DbHelper.getInstance().updateUser(user);
////        Contact c = user.getContacts().get(0);
////        c.setPhoneNumber("saaga12451251g");
////        DbHelper.getInstance().updateContact(c);
////        List<Contact> contacts = DbHelper.getInstance().getAllContacts();
////        for(Contact c: DbHelper.getInstance().getAllContacts()){
////            System.out.printf("" +
////                    "OwnerLogin: %1$s%n" +
////                    "Username: %2$s%n" +
////                    "Phone number: %3$s%n%n" +
////                    "", c.getOwner().getLogin(), c.getName(), c.getPhoneNumber());
////        }
////        System.out.println("Adding user");
////        User user = new User("Fresh user", "fresh password", "fresh user name", 1);
////        System.out.printf("User id is %d%n", user.getId());
////        if(!DbHelper.getInstance()
////                .addUser(user)){
////            System.out.println("couldnt add user - login already exists");
////        }
////        System.out.printf("User id is %d", user.getId());
////        for(User user: DbHelper.getInstance().getAllUsers()){
////            System.out.printf("" +
////                    "Login: %1$s%n" +
////                    "Username: %2$s%n" +
////                    "Password: %3$s%n%n" +
////                    "", user.getLogin(), user.getName(), user.getPassword());
////        }
////        int affectedUsers = DbHelper.getInstance().deleteUser("Fresh user");
////        System.out.printf("Deleted %d users%n", affectedUsers);
////        for(User user: DbHelper.getInstance().getAllUsers()){
////            System.out.printf("" +
////                    "Login: %1$s%n" +
////                    "Username: %2$s%n" +
////                    "Password: %3$s%n%n" +
////                    "", user.getLogin(), user.getName(), user.getPassword());
////        }
//
////        DbHelper.getInstance().closeSessionFactory();
//    }
//}