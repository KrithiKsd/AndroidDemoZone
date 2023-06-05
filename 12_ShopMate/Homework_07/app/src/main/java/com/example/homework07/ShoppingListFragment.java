package com.example.homework07;

/*
Assignment #: Homework07
File Name: ShoppingListFragment.java
Full Name of Student2: Krithika Kasaragod
 */


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework07.databinding.FragmentShoppinglistBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;


public class ShoppingListFragment extends Fragment {


    FragmentShoppinglistBinding binding;
    IService mListener;
    FirebaseFirestore db;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ShoppingList> shoppingListArray = new ArrayList<>();

    ShoppingListRecyclerViewAdapter listAdapter;
    String currentUserId;
    private FirebaseAuth mAuth;


    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShoppinglistBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_shopping_list_fragment));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getShoppingListData();

        binding.recyclerViewShoppingList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewShoppingList.setLayoutManager(linearLayoutManager);
        listAdapter = new ShoppingListRecyclerViewAdapter(shoppingListArray);
        binding.recyclerViewShoppingList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        binding.recyclerViewShoppingList.setAdapter(listAdapter);

        binding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();
                mListener.gotoLoginFragment();
            }
        });

        binding.buttonNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoNewListFragment();
            }
        });

    }

    public void getShoppingListData() {


        db.collection("ShoppingList")//.whereEqualTo("uid",mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        shoppingListArray.clear();
                        for (QueryDocumentSnapshot document : value) {
                            ShoppingList shoppingList = document.toObject(ShoppingList.class);
                            Log.d("TAG", "onEvent: getList1" + shoppingList);
                            //displays shopping list where either if the user has created any list or he has been invited in any list
                            if (shoppingList.UID.equals(mAuth.getCurrentUser().getUid())
                                    || shoppingList.getInvitedUsers().contains(mAuth.getCurrentUser().getUid())) {

                                shoppingListArray.add(shoppingList);
                                shoppingListArray.sort(new Comparator<ShoppingList>() {
                                    @Override
                                    public int compare(ShoppingList t1, ShoppingList t2) {
                                        if (!shoppingListArray.isEmpty()) {
                                            return (t1.dateTime.compareTo(t2.dateTime));
                                        }
                                        return 0;
                                    }
                                });
                            }
                        }

                        listAdapter.notifyDataSetChanged();
                    }
                });

    }

    private void deleteShoppingList(String document_id) {
        db.collection("ShoppingList").document(document_id).collection("ItemList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {
                            for (QueryDocumentSnapshot doc : value) {
                                db.collection("ShoppingList").document(document_id).collection("ItemList").document(doc.getId())
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG", "onSuccess: delete subcollection" + doc.getId());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure: delete subcollection");
                                    }
                                });
                            }
                        }
                    }
                });

        db.collection("ShoppingList").document(document_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getShoppingListData();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getContext(), "The selected document does not exist", Toast.LENGTH_SHORT).show();
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

    public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ShoppingListViewHolder> {

        ArrayList<ShoppingList> arrayList;

        public ShoppingListRecyclerViewAdapter(ArrayList<ShoppingList> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ShoppingListRecyclerViewAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_shoppinglist, parent, false);
            ShoppingListViewHolder viewHolder = new ShoppingListViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ShoppingListRecyclerViewAdapter.ShoppingListViewHolder holder, int position) {
            ShoppingList shoppingList = this.arrayList.get(position);
            holder.tvSLName.setText(this.arrayList.get(position).name);
            holder.tvOwner.setText(this.arrayList.get(position).owner);
            holder.tvDateTime.setText(this.arrayList.get(position).dateTime);

            currentUserId = mAuth.getCurrentUser().getUid();
            Log.d("TAG", "UID onBindViewHolder:" + currentUserId);
            String user_id = this.arrayList.get(position).UID;
            String document_id = this.arrayList.get(position).DID;
            if (user_id.equals(currentUserId)) {
                holder.imageViewTrash.setImageResource(R.drawable.ic_trash);
            } else {
                holder.imageViewTrash.setVisibility(View.INVISIBLE);
            }

            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteShoppingList(document_id);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToItemList(document_id);
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
            TextView tvSLName, tvOwner, tvDateTime;
            ImageView imageViewTrash;

            public ShoppingListViewHolder(@NonNull View itemView) {
                super(itemView);
                tvSLName = itemView.findViewById(R.id.textViewSLName);
                tvOwner = itemView.findViewById(R.id.textViewOwnerName);
                tvDateTime = itemView.findViewById(R.id.textViewSLDateTime);
                imageViewTrash = itemView.findViewById(R.id.imageViewTrash);
            }
        }
    }


}