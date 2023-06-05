package com.example.homework07;

/*
Assignment #: Homework07
File Name: ItemListFragment.java
Full Name of Student2: Krithika Kasaragod
*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework07.databinding.FragmentItemListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ItemListFragment extends Fragment {


    private static final String ARG_DOC_ID = "ARG_DOC_ID";

    FragmentItemListBinding listBinding;
    IService mListener;
    String missingField = "";
    String itemDocID, deleteDocID;
    ArrayList<Item> itemList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ItemListAdapter listAdapter;
    boolean checked = false;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    private String slDocID;
    Double total=0.0;

    public static ItemListFragment newInstance(String slDocID) {
        ItemListFragment fragment = new ItemListFragment();
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
        listBinding = FragmentItemListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_item_list));
        return listBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        listBinding.recyclerViewItem.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        listBinding.recyclerViewItem.setLayoutManager(linearLayoutManager);
        listBinding.recyclerViewItem.getRecycledViewPool().setMaxRecycledViews(0, 0);


        if (slDocID != null) {

            getListItems();

            listAdapter = new ItemListAdapter(itemList);
            listBinding.recyclerViewItem.setAdapter(listAdapter);


            listBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String itemName = listBinding.editTextItemName.getText().toString();
                    String itemPrice = listBinding.editTextPrice.getText().toString();

                    boolean success = validation(itemName, itemPrice);
                    if (success) {
                        setItemList(itemName, itemPrice);
                    } else {
                        displayAlert(missingField);
                    }
                }
            });

            listBinding.buttonListInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.goToUserListFragment(slDocID);
                }
            });

            listBinding.buttonItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.gotoPreviousFragment();
                }
            });
        }
    }

    private void setItemList(String itemName, String itemPrice) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference newDoc = db.collection("ShoppingList").document(slDocID)
                .collection("ItemList").document();
        itemDocID = newDoc.getId();
        newDoc.set(new Item(mAuth.getCurrentUser().getUid(), itemDocID, mAuth.getCurrentUser().getDisplayName(), itemName, itemPrice, checked))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        listBinding.editTextItemName.setText("");
                        listBinding.editTextPrice.setText("");
                        getListItems();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void getListItems() {

        db.collection("ShoppingList").document(slDocID).collection("ItemList")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        itemList.clear();
                        for (QueryDocumentSnapshot document : value) {

                            Item item = document.toObject(Item.class);
                            itemList.add(item);

                        }

                        if (itemList != null) {

                            total = 0.0;
                            for (Item item: itemList) {

                                total = total+ (Double.parseDouble(item.getItemPrice()));
                                Log.d("TAG", "onEvent: total"+total);

                            }
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listBinding.textViewTotal.setText(getString(R.string.label_total) + " " + total);
                                    }
                                });
                            }

                        }
                        listAdapter.notifyDataSetChanged();

                    }
                });


    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_alert))
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

    private boolean validation(String itemName, String itemPrice) {
        if (itemName.isEmpty() && itemPrice.isEmpty()) {
            missingField = getString(R.string.label_please_enter_item_name_price);
            return false;
        } else if (itemName.isEmpty()) {
            missingField = getString(R.string.label_please_enter_item_name);
            return false;
        } else if (itemPrice.isEmpty()) {
            missingField = getString(R.string.label_please_enter_item_price);
            return false;
        }
        return true;
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

    private void updateItemList(String slDocID, String docId, boolean checked) {

        DocumentReference newDoc = db.collection("ShoppingList").document(slDocID)
                .collection("ItemList").document(docId);
        newDoc.update("checked", checked)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG", "onSuccess: ");

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: Inside update " + e.getMessage());
            }
        });
    }

    private void deleteData(String documentId) {
        db.collection("ShoppingList").document(slDocID).collection("ItemList")
                .document(documentId)
                .delete()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {

        ArrayList<Item> arrayList;

        public ItemListAdapter(ArrayList<Item> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_itemlist, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Item itemList = this.arrayList.get(position);
            holder.tvItemName.setText(this.arrayList.get(position).itemName);
            holder.tvPrice.setText(this.arrayList.get(position).itemPrice);
            holder.tvCreated.setText(getString(R.string.label_added_by) + this.arrayList.get(position).userName);
            holder.imageViewTrash.setImageResource(R.drawable.ic_trash);

            holder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteData(itemList.getDocumentId());
                }
            });


            if (arrayList.get(position).isChecked()) {
                holder.ivChecked.setImageResource(R.drawable.dark_green);
                holder.tvAcquired.setText(getString(R.string.label_is_got_acquired));
            } else {
                holder.ivChecked.setImageResource(R.drawable.circle_gray);
                holder.tvAcquired.setText(getString(R.string.label_still_not_acquired));
            }


            holder.ivChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checked = !checked;

                    /*if (checked == false) {
                        checked = true;
                    }
                    else {
                        checked = false;
                    }*/

                    updateItemList(slDocID, arrayList.get(position).getDocumentId(), checked);
                    getListItems();
                }
            });


        }

        @Override
        public int getItemCount() {
            return this.arrayList.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView tvItemName, tvPrice, tvCreated, tvAcquired;
            ImageView imageViewTrash, ivChecked;


            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                tvItemName = itemView.findViewById(R.id.textViewItemName);
                tvPrice = itemView.findViewById(R.id.textViewItemQty);
                tvCreated = itemView.findViewById(R.id.tvCreated);
                imageViewTrash = itemView.findViewById(R.id.imageViewItemTrash);
                ivChecked = itemView.findViewById(R.id.ivChecked);
                tvAcquired = itemView.findViewById(R.id.tvAcquired);
            }
        }
    }
}