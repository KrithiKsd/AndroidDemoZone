package com.example.inclass_07;
/*
a. Assignment #. InClass07
b. File Name : MainActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements IContactList {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new ContactListFragment(), getString(R.string.tag_contact_list))
                .commit();

    }

    @Override
    public void goToContactDetails(Contacts contact) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ContactDetailsFragment.newInstance(contact), getString(R.string.tag_contact_detail_fragment))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void goToAddContact() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddNewContactFragment(), getString(R.string.tag_add_new_contact))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToContactList() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToContactListFromAdd() {
        ContactListFragment fragment = (ContactListFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_contact_list));
        fragment.getContactRequest();
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToContactListFromUpdate(Contacts contact) {
        ContactDetailsFragment fragment = (ContactDetailsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_contact_detail_fragment));
        fragment.mContact = contact;
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToUpdateContact(Contacts contact) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, UpdateContactFragment.newInstance(contact), getString(R.string.tag_update_contact_fragment))
                .addToBackStack(null)
                .commit();
    }
}