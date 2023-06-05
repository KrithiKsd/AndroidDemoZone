package com.example.inclass_07;
/*
a. Assignment #. InClass07
b. File Name : ContactListFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inclass_07.databinding.FragmentContactListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ContactListFragment extends Fragment {


    FragmentContactListBinding fragmentContactListBinding;
    LinearLayoutManager linearLayoutManager;
    ContactRecyclerViewAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Contacts> listContacts;
    IContactList iListener;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentContactListBinding = FragmentContactListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_contact_list));
        return fragmentContactListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getContactRequest();
        fragmentContactListBinding.recycler.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());

        fragmentContactListBinding.btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToAddContact();
            }
        });

    }

    public void getContactRequest() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contacts/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    listContacts= new ArrayList<>();
                    try {
                        ResponseBody responseBody = response.body();
                        String responseValue= responseBody.string();
                        JSONObject jsonObject= new JSONObject(responseValue);
                        JSONArray jsonArray = jsonObject.getJSONArray("contacts");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject json= jsonArray.getJSONObject(i);

                            Contacts contact= new Contacts();
                            contact.setId(json.getString("Cid"));
                            contact.setName(json.getString("Name"));
                            contact.setEmail(json.getString("Email"));
                            contact.setPhone(json.getString("Phone"));
                            contact.setType(json.getString("PhoneType"));
                            listContacts.add(contact);

                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ContactRecyclerViewAdapter(listContacts, iListener);
                                adapter.notifyDataSetChanged();
                                fragmentContactListBinding.recycler.setLayoutManager(linearLayoutManager);
                                fragmentContactListBinding.recycler.setAdapter(adapter);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void deleteContact(String id){
        FormBody formBody= new FormBody.Builder()
                .add("id",id).build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/json/delete")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    String responseValue= responseBody.string();
                    Log.d("TAG", "onResponse: Success:delete"+responseValue);
                } else{
                    Log.d("TAG", "onResponse: Failed:delete"+response.body());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getContactRequest();
                        Toast.makeText(getActivity(),getString(R.string.label_contact_deleted),Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }

    public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder>{

        ArrayList<Contacts> contactList;
        IContactList mListener;

        public ContactRecyclerViewAdapter(ArrayList<Contacts> contactList, IContactList mListener) {
            super();
            this.contactList = contactList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_list_recycler_view_layout,parent,false);
            ContactViewHolder sortViewHolder = new ContactViewHolder(view, mListener);
            return sortViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

            holder.textViewSortTitle.setText(this.contactList.get(position).name);
            holder.buttonDelete.setBackgroundResource(R.drawable.ic_trash);
            holder.textViewEmail.setText(this.contactList.get(position).email);
            holder.textViewPhone.setText(this.contactList.get(position).phone);
            holder.textViewType.setText(this.contactList.get(position).type);
            holder.setContact(this.contactList.get(position));

        }

        @Override
        public int getItemCount() {
            return this.contactList.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageButton buttonDelete;
            TextView textViewSortTitle, textViewEmail, textViewPhone, textViewType;
            Contacts mContact;

            IContactList mListener;

            public ContactViewHolder(@NonNull View itemView, IContactList mlistener) {
                super(itemView);

                this.mListener = mlistener;
                textViewSortTitle = itemView.findViewById(R.id.textViewSortTitle);
                buttonDelete= itemView.findViewById(R.id.buttonDelete);
                textViewEmail = itemView.findViewById(R.id.textViewEmail);
                textViewPhone = itemView.findViewById(R.id.textViewPhone);
                textViewType = itemView.findViewById(R.id.textViewtype);
                itemView.setOnClickListener(this);

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteContact(mContact.getId());
                    }
                });
            }

            public void setContact(Contacts contact){
                mContact = contact;
            }

            @Override
            public void onClick(View view) {
                mListener.goToContactDetails(mContact);
            }
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IContactList) {
                iListener = (IContactList) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

}