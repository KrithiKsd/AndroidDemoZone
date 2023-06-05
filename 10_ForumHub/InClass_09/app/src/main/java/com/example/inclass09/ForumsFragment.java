package com.example.inclass09;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.inclass09.databinding.FragmentForumsBinding;
import com.example.inclass09.databinding.RecyclerviewForumslistBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    SharedPreferences sharedpreferences;
   // String UID="";

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
        getActivity().setTitle("Forum");
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

                        if (error != null) {
                            Log.d("TAG", "Error:" + error.getMessage());
                        } else {
                            forumsList.clear();
                            for (QueryDocumentSnapshot document : value) {
                                Log.d("TAG", "onEvent: doc ID " + document.getId());
                                //users.add(new User(document.getString("name") ,document.getString("cell")));
                           /* forumsList.add(new Forum(document.getString("uid"),
                                    document.getString("forumTitle"),document.getString("forumDescription"),document.getString("forumCreator"),
                                    document.getString("forumDateTime")));*/

                          /*  Forum forums=new Forum(document.getString("uid"),
                                    document.getString("forumTitle"),document.getString("forumDescription"),document.getString("forumCreator"),
                                    document.getString("forumDateTime"));*/

                                Forum forums = document.toObject(Forum.class);
                                forums.setDocumentId(document.getId());

                                forumsList.add(forums);
                            }

                            adapter.notifyDataSetChanged();
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

    private void deleteData(String documentName) {

        db.collection("Forum")
                .document(documentName)
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

                if (mAuth.getUid().equals(mItem.getUID())) {
                    mBinding.imageViewTrash.setImageResource(R.drawable.ic_trash);
                }

                mBinding.imageViewTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  String docName= mItem.getDocumentId();
                     //   Log.d("TAG", "onClick: docId"+docName);
                        deleteData(mItem.getDocumentId());
                    }
                });

            }
        }
    }
}