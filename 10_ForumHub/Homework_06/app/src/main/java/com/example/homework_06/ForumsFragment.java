package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: ForumsFragment.java
Full Name of Student 1: Krithika Kasaragod
*/

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework_06.databinding.FragmentForumsBinding;
import com.example.homework_06.databinding.RecyclerviewForumslistBinding;
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


public class ForumsFragment extends Fragment {

    FragmentForumsBinding forumsBinding;
    IService mListener;
    LinearLayoutManager layoutManager;
    ForumsAdapter adapter;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<Forum> forumsList = new ArrayList<>();
    Forum forums = new Forum();


    public ForumsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        forumsBinding = FragmentForumsBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_forums));
        return forumsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        forumsBinding.recyclerViewForumList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        forumsBinding.recyclerViewForumList.setLayoutManager(layoutManager);
        forumsBinding.recyclerViewForumList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        getForumListData();

        adapter = new ForumsAdapter(forumsList, mListener);
        forumsBinding.recyclerViewForumList.setAdapter(adapter);

        forumsBinding.buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                FirebaseAuth.getInstance().signOut();

                mListener.gotoLoginFromForumsFragment();
            }
        });

        forumsBinding.buttonNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoNewForumFragment();
            }
        });

    }

    public void getForumListData() {

        db.collection("Forum")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forumsList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            forums = document.toObject(Forum.class);
                            forumsList.add(forums);
                        }
                        adapter.notifyDataSetChanged();
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


    private void deleteData(String documentName) {

        db.collection("Forum").document(documentName).collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            Log.d("TAG", "onEvent: " + value.size());

                            for (QueryDocumentSnapshot document : value) {
                                Log.d("TAG", "onEvent: commentID " + document.getId());
                                deleteDoc(document.getId(), documentName);
                            }
                        } else {

                        }
                    }
                });


        db.collection("Forum")
                .document(documentName)
                .delete()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    private void deleteDoc(String id, String name) {
        db.collection("Forum").document(name).collection("Comments")
                .document(id)
                .delete()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumsViewHolder> {
        ArrayList<Forum> postList;
        IService mListener;

        public ForumsAdapter(ArrayList<Forum> sortNameList, IService mListener) {
            this.postList = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerviewForumslistBinding binding = RecyclerviewForumslistBinding.inflate(getLayoutInflater(), parent, false);
            ForumsViewHolder postViewHolder = new ForumsViewHolder(binding);
            return postViewHolder;
        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Forum item = postList.get(position);
            holder.setUpData(item);
        }


        public class ForumsViewHolder extends RecyclerView.ViewHolder {

            RecyclerviewForumslistBinding mBinding;
            Forum mItem;

            public ForumsViewHolder(RecyclerviewForumslistBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(Forum item) {
                mItem = item;
                mBinding.textViewForumName.setText(mItem.getForumTitle());
                mBinding.textViewName.setText(mItem.getForumCreator());
                mBinding.textViewDetail.setText(mItem.getForumDescription());
                mBinding.textViewDateTime.setText(mItem.getForumDateTime());

                ArrayList<String> listUsers = new ArrayList<>();

                if (mItem.getListUID() != null) {
                    listUsers = mItem.getListUID();
                    mBinding.tvLikes.setText(mItem.getListUID().size() + getString(R.string.label_likes));

                    if (listUsers.contains(mAuth.getCurrentUser().getUid())) {
                        mBinding.imageViewLike.setImageResource(R.drawable.like_favorite);
                    } else if (!listUsers.contains(mAuth.getCurrentUser().getUid())) {
                        mBinding.imageViewLike.setImageResource(R.drawable.like_not_favorite);
                    }
                } else {
                    mBinding.tvLikes.setText(getString(R.string.label_zero_like));
                }

                if (mAuth.getUid().equals(mItem.getUID())) {
                    mBinding.imageViewTrash.setImageResource(R.drawable.rubbish_bin);
                }

                mBinding.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteData(mItem.getDocumentId());
                    }
                });

                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG", "onClick: " + mItem);
                        mListener.gotoForumFragment(mItem);
                    }
                });

                ArrayList<String> finalListUsers = listUsers;
                mBinding.imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateForums(mItem.getDocumentId(), finalListUsers);
                        getForumListData();
                    }

                });
            }
        }
    }

    private void updateForums(String documentId, ArrayList<String> listUsers) {
        if (listUsers.contains(mAuth.getCurrentUser().getUid())) {
            listUsers.remove(mAuth.getCurrentUser().getUid());
        } else {
            listUsers.add(mAuth.getCurrentUser().getUid());
        }
        DocumentReference reference = db.collection("Forum").document(documentId);
        reference.update("listUID", listUsers)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}