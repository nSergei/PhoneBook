# PhoneBook
Desktop application using H2 database for persistence, Hibernate as ORM tool, JavaFX as GUI platform.
Data is stored in 2 entities - User and Contact, there is established a one-to-many relationship between these entities, 
all the changes are being cascaded.
Application is built according to MVP-pattern, with separated persistence layer, UI layer and a Presenter in between. UI-layer exposes rx.Observable, Presenter subscribes persistence layer and itself in order to listen to the data changes, coming from UI-layer. Certain events are propagating back to UI-layer using bus.

Working with the app:
1. Login screen - everyone can sign up, in order to get admin user with admin set of privilegies sign up must be done with login "admin".
2. Contacts list screen - contacts are always displayed, adding a new contact is possible by entering information about new contact at the bottom of the screen. Name must be not empty, phone can contain anything but only numbers will be retained.
Editing contact is available at double click, saving changes by pressing enter.
In order to delete contact name must be erased.
3. Users list(admin only) - editing and deleting is performed the same way as contacts

Switching between Contacts list and Users list is possible via menu bar(admin only)
Simple users have a menu bar with "logout" and "delete account" options.
