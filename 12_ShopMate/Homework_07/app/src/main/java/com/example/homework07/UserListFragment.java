package com.example.homework07;
/*
Assignment #: Homework07
File Name: UserListFragment.java
Full Name of Student2: Krithika Kasaragod
*/


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.homework07.databinding.FragmentUserlistBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class UserListFragment extends Fragment {

    private static final String ARG_DOC_ID = "ARG_DOC_ID";
    FragmentUserlistBinding userBinding;
    ArrayList<ListUser> userList = new ArrayList<>();
    ArrayList<String> friendList = new ArrayList<String>();
    ArrayList<ListUser> friendUser = new ArrayList<ListUser>();
    ArrayList<String> invitedUsers = new ArrayList<>();
    HashSet<String> friendset = new HashSet<>();
    ArrayAdapter<ListUser> listAdapter;
    ArrayAdapter<ListUser> friendListAdapter;
    IService mListener;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private String slDocID;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(String slDocID) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOC_ID, slDocID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            slDocID = getArguments().getString(ARG_DOC_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userBinding = FragmentUserlistBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_user_list));
        return userBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getShoppingListData();


        userBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPreviousFragment();
            }
        });


    }

    public void getShoppingListData() {

        db.collection("ShoppingList").document(slDocID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                invitedUsers.clear();
                invitedUsers = (ArrayList<String>) documentSnapshot.get("invitedUsers");
                Log.d("TAG", "onSuccess: invitedUsers" + invitedUsers);
                getUserList();
                getFriendsList();
            }
        });
    }

    public void getUserList() {
        db.collection("ListUser")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        userList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            ListUser listUser = document.toObject(ListUser.class);
                            userList.add(listUser);

                            userList.sort(new Comparator<ListUser>() {
                                @Override
                                public int compare(ListUser user1, ListUser user2) {
                                    if (!userList.isEmpty()) {
                                        return (user1.name.compareTo(user2.name));
                                    }
                                    return 0;
                                }
                            });
                            Log.d("TAG", "onEvent: getList" + userList);

                        }

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (userList != null) {
                                        Log.d("TAG", "run: " + userList);
                                        listAdapter = new UserListAdapter(getContext(), R.layout.listview_userlist, userList);
                                        userBinding.listUser.setAdapter(listAdapter);
                                    }

                                }
                            });
                        }

                    }
                });
    }

    public void getFriendsList() {
        db.collection("ShoppingList").whereEqualTo("uid", mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (QueryDocumentSnapshot doc : value) {
                            ShoppingList shoppingList = doc.toObject(ShoppingList.class);
                            Log.d("TAG", "onEvent: friendslist" + shoppingList);
                            friendList.addAll(shoppingList.getInvitedUsers());

                        }
                        Log.d("TAG", "onEvent: friendslist" + friendList);
                        friendset.addAll(friendList);
                        friendUser.clear();
                        for (String item : friendset) {
                            ListUser user = new ListUser();
                            user.setUserId(item);
                            friendUser.add(user);
                        }
                        getFriendNames(friendUser);

                        Log.d("TAG", "run: " + friendUser);

                    }
                });
    }

    private void getFriendNames(ArrayList<ListUser> friendUser) {

        ArrayList<ListUser> users = new ArrayList<>();
        db.collection("ListUser").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot document : value) {
                    ListUser user = document.toObject(ListUser.class);
                    users.add(user);
                }
                for (int i = 0; i < users.size(); i++) {
                    for (int j = 0; j < friendUser.size(); j++)
                        if (users.get(i).UserId.equals(friendUser.get(j).UserId)) {
                            friendUser.get(j).setName(users.get(i).name);
                        }
                }
                Log.d("TAG", "onEvent: friendswithNames" + friendUser);

                if (getContext() != null) {
                    friendListAdapter = new FriendListAdapter(getContext(), R.layout.listview_friendlist, friendUser);
                    userBinding.listFriends.setAdapter(friendListAdapter);
                }


            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IService) {
                mListener = (IService) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {
        }
    }

    private void updateWithInvitedUser(String slDocID, ArrayList<String> invitedUsers, String inUserId) {
        ArrayList<String> newInvited = invitedUsers;
        if (newInvited != null) {
            if (newInvited.contains(inUserId)) {
                newInvited.remove(inUserId);
            } else if (!newInvited.contains(inUserId)) {
                newInvited.add(inUserId);
            }
        } else if (newInvited == null) {
            newInvited.add(inUserId);
        }

        DocumentReference docRef = db.collection("ShoppingList").document(slDocID);
        docRef.update("invitedUsers", newInvited)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: invitedUser updated");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onSuccess: invitedUser updated NOT");
            }
        });
        listAdapter.notifyDataSetChanged();

    }

    public class UserListAdapter extends ArrayAdapter<ListUser> {

        public UserListAdapter(@NonNull Context context, int resource, @NonNull List<ListUser> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.listview_userlist, parent, false);
                AdapterViewHolder viewHolder = new AdapterViewHolder();
                viewHolder.tvUserName = convertView.findViewById(R.id.textViewUserName);
                viewHolder.imageViewInvite = convertView.findViewById(R.id.imageViewInvite);
                convertView.setTag(viewHolder);

            }

            ListUser user = getItem(position);
            AdapterViewHolder viewHolder = (AdapterViewHolder) convertView.getTag();

            if (invitedUsers != null) {
                if (invitedUsers.contains(user.UserId)) {
                    viewHolder.imageViewInvite.setImageResource(R.drawable.dark_green);
                } else if (!invitedUsers.contains(user.UserId)) {
                    viewHolder.imageViewInvite.setImageResource(R.drawable.circle_gray);
                }
            }

            viewHolder.tvUserName.setText(user.name);
            viewHolder.imageViewInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick: invite" + user.UserId + "***" + invitedUsers + "*****" + slDocID);
                    updateWithInvitedUser(slDocID, invitedUsers, user.UserId);
                }
            });

            return convertView;
        }

        public class AdapterViewHolder {

            TextView tvUserName;
            ImageView imageViewInvite;

        }
    }

    //friendlist adapter
    public class FriendListAdapter extends ArrayAdapter<ListUser> {
        public FriendListAdapter(@NonNull Context context, int resource, @NonNull List<ListUser> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.listview_friendlist, parent, false);
                AdapterViewHolder viewHolder = new AdapterViewHolder();
                viewHolder.tvUserName = convertView.findViewById(R.id.textViewFriendName);
                viewHolder.imageViewInvite = convertView.findViewById(R.id.imageViewFriendInvite);
                convertView.setTag(viewHolder);

            }

            ListUser user = getItem(position);
            AdapterViewHolder viewHolder = (AdapterViewHolder) convertView.getTag();

            if (invitedUsers != null) {
                if (invitedUsers.contains(user.UserId)) {
                    viewHolder.imageViewInvite.setImageResource(R.drawable.dark_green);
                } else if (!invitedUsers.contains(user.UserId)) {
                    viewHolder.imageViewInvite.setImageResource(R.drawable.circle_gray);
                }
            }

            viewHolder.tvUserName.setText(user.name);
            viewHolder.imageViewInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "onClick: invite" + user.UserId + "***" + invitedUsers + "*****" + slDocID);
                    updateWithInvitedUser(slDocID, invitedUsers, user.UserId);
                }
            });

            return convertView;
        }

        public class AdapterViewHolder {

            TextView tvUserName;
            ImageView imageViewInvite;

        }
    }


}