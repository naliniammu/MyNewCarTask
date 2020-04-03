package com.example.mynewcartask;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.mynewcartask.AppConstants.PICK_CONTACT;
import static com.example.mynewcartask.AppConstants.REQUEST_MULTIPLE_PERMISSIONS;

public class ContactDetailsActivity extends AppCompatActivity {
    private Button addContactButton;
    private static RecyclerView contact_listview;
    private ArrayList<Contact_Model> selectUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AccessContact();
        }

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

    }

    public void initviews() {
        addContactButton = findViewById(R.id.btnload);
        contact_listview = findViewById(R.id.contact_listview);
        LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contact_listview.setLayoutManager(layoutManager);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessContact() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (addPermissionFetchingContacts(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add(getString(R.string.read_contacts));
        if (addPermissionFetchingContacts(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add(getString(R.string.write_contacts));
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                StringBuilder message = new StringBuilder(getString(R.string.grant_access) + permissionsNeeded.get(0));
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message.append(", ").append(permissionsNeeded.get(i));
                showMessageOKCancel(message.toString(),
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[0]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[0]),
                    REQUEST_MULTIPLE_PERMISSIONS);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean addPermissionFetchingContacts(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            return !shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ContactDetailsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode != PICK_CONTACT) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String mContactId;
                mContactId = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                try {
                    if (hasPhone.equalsIgnoreCase("1")) {
                        @SuppressLint("Recycle") Cursor phones = getContentResolver().query(
                                Phone.CONTENT_URI, null,
                                Phone.CONTACT_ID + " = " + mContactId,
                                null, null);
                        assert phones != null;
                        phones.moveToFirst();
                        String mConbtactNumber = phones.getString(phones.getColumnIndex("data1"));
                        String mContactName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Contact_Model contact = new Contact_Model();
                        contact.setContactName(mContactName);
                        contact.setContactNumber(mConbtactNumber);
                        selectUsers.add(contact);
                        Contact_Adapter contact_adapter = new Contact_Adapter(getApplicationContext(), selectUsers);
                        contact_listview.setAdapter(contact_adapter);// set adapter
                        contact_adapter.notifyDataSetChanged();
                    } else {
                        // If adapter is null then show toast
                        Toast.makeText(ContactDetailsActivity.this, getString(R.string.no_contacts),
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ignored) {

                }
            }
        }
    }

}
