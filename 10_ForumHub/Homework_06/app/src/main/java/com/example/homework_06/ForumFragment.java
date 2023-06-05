package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: ForumFragment.java
Full Name of Student 1: Krithika Kasaragod
*/

import static com.example.homework_06.MainActivity.creatorName;
import static com.example.homework_06.MainActivity.myPreference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.homework_06.databinding.CustomCommentsListBinding;
import com.example.homework_06.databinding.FragmentForumBinding;
import com.example.homework_06.databinding.FragmentForumsBinding;
import com.example.homework_06.databinding.RecyclerviewForumslistBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ForumFragment extends Fragment {

    private static final String ARG_FORUM = "FORUM";
    private Forum mForum;
    FragmentForumBinding forumBinding;
    IService mListener;
    SharedPreferences sharedpreferences;
    String forumDocID;
    ArrayList<Comments> commentsList = new ArrayList<>();
    FirebaseFirestore db;
    LinearLayoutManager layoutManager;
    private FirebaseAuth mAuth;
    CommentsAdapter adapter;
    String commentsDocID, deleteDocID;
    static int commentCount = -1;

    public ForumFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Forum) getArguments().getSerializable(ARG_FORUM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        forumBinding = FragmentForumBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_forum));
        return forumBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG", "onViewCreated: delete this doc" + deleteDocID);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        forumBinding.recyclerComments.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        forumBinding.recyclerComments.setLayoutManager(layoutManager);
        forumBinding.recyclerComments.getRecycledViewPool().setMaxRecycledViews(0, 0);

        getListComments();

        adapter = new CommentsAdapter(commentsList, mListener);
        forumBinding.recyclerComments.setAdapter(adapter);

        if (mForum != null) {
            forumBinding.tvForumTitle.setText(mForum.getForumTitle());
            forumBinding.tvCreaterName.setText(mForum.getForumCreator());
            forumBinding.tvForumDescription.setText(mForum.getForumDescription());
            forumDocID = mForum.getDocumentId();
        }

        forumBinding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = forumBinding.etComment.getText().toString();
                if (post.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_alert))
                            .setMessage(getString(R.string.label_please_write_before_post))
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                } else {
                    String creator = "";
                    String UID = "";

                    sharedpreferences = getActivity().getSharedPreferences(myPreference,
                            Context.MODE_PRIVATE);
                    if (sharedpreferences.contains(MainActivity.UID)) {
                        creator = sharedpreferences.getString(creatorName, null);
                        UID = sharedpreferences.getString(MainActivity.UID, null);
                    }

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
                    String createdDateTime = dateFormatter.format(Calendar.getInstance().getTime());


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference newDoc = db.collection("Forum").document(mForum.DocId)
                            .collection("Comments").document();
                    commentsDocID = newDoc.getId();


                    newDoc.set(new Comments(post, createdDateTime, commentsDocID, UID, creator, mForum.DocId))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    forumBinding.etComment.setText("");
                                    getListComments();
                                }
                            });


                }
            }
        });
    }


    private void getListComments() {

        db.collection("Forum").document(mForum.DocId).collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentsList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            Log.d("TAG", "onEvent: doc ID " + document.getId());

                            String doc = document.getString("forumID");
                            Log.d("TAG", "onEvent: forumID from Comments collection " + doc);
                            Log.d("TAG", "onEvent: forum id from Item" + mForum.getDocumentId());

                            if (doc.equals(mForum.getDocumentId())) {
                                Comments comments = document.toObject(Comments.class);
                                commentsList.add(comments);
                            } else {
                                //nothing to do
                            }

                        }
                        if (commentsList != null) {
                            forumBinding.tvComments.setText(commentsList.size() + " Comments");
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

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
        ArrayList<Comments> commentsList;
        IService mListener;

        public CommentsAdapter(ArrayList<Comments> commentsList, IService mListener) {
            this.commentsList = commentsList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CustomCommentsListBinding binding = CustomCommentsListBinding.inflate(getLayoutInflater(), parent, false);
            CommentsViewHolder commentsViewHolder = new CommentsViewHolder(binding);
            return commentsViewHolder;
        }

        @Override
        public int getItemCount() {
            return commentsList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull CommentsViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Comments item = commentsList.get(position);
            holder.setUpData(item);
        }


        public class CommentsViewHolder extends RecyclerView.ViewHolder {

            CustomCommentsListBinding mBinding;
            Comments mItem;

            public CommentsViewHolder(CustomCommentsListBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(Comments item) {
                mItem = item;
                mBinding.tvCommenter.setText(mItem.getCreatorName());
                mBinding.tvCommentDetails.setText(mItem.getCommentDetails());
                mBinding.tvCommentDateTime.setText(mItem.getDateTime());
                if (mAuth.getUid().equals(mItem.getUID())) {
                    mBinding.ivTrash.setImageResource(R.drawable.rubbish_bin);
                } else {
                    mBinding.ivTrash.setVisibility(View.GONE);
                }

                mBinding.ivTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteData(mItem.getCommentDocId());
                    }
                });

            }
        }
    }

    private void deleteData(String commentDocId) {
        db.collection("Forum").document(mForum.DocId).collection("Comments")
                .document(commentDocId)
                .delete()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

}