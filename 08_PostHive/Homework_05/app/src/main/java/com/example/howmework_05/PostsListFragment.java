package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: PostsListFragment.java
c. Full name of the student : Krithika Kasaragod
*/
import static com.example.howmework_05.MainActivity.myPreference;
import static com.example.howmework_05.MainActivity.tokenId;

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

import com.example.howmework_05.databinding.CustomPostListItemBinding;
import com.example.howmework_05.databinding.CustomPostPageListBinding;
import com.example.howmework_05.databinding.FragmentPostsListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PostsListFragment extends Fragment {


    FragmentPostsListBinding postBinding;
    IServices mListener;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<DataService.Post> postList = new ArrayList<>();
    ArrayList<String> pageList= new ArrayList<>();
    LinearLayoutManager layoutManager;
    PostListAdapter postListAdapter;
    PostPagerAdapter postPagerAdapter;
    String userId, tokenIdValue;

    public PostsListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postBinding = FragmentPostsListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_post_list_fragment));
        return postBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postBinding.recyclerPostList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        postBinding.recyclerPostList.setLayoutManager(layoutManager);

        postBinding.recyclerPager.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false);
        postBinding.recyclerPager.setLayoutManager(layoutManager);

        //Reading data from shared preferences
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(tokenId)) {
            tokenIdValue = sharedpreferences.getString(MainActivity.tokenId, null);
            String fullName = sharedpreferences.getString(MainActivity.fullName, null);
            userId = sharedpreferences.getString(MainActivity.userId, null);

            postBinding.tvWelcome.setText(getString(R.string.label_welcome) + " " + fullName);

            //method to show data from API
            getPostList(getString(R.string.page_number));

            postListAdapter = new PostListAdapter(postList, mListener);
            postBinding.recyclerPostList.setAdapter(postListAdapter);

            postPagerAdapter = new PostPagerAdapter(pageList, mListener);
            postBinding.recyclerPager.setAdapter(postPagerAdapter);

        }

        postBinding.btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreatePostFragment();
            }
        });

        postBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPreference,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.remove("tokenId");
                editor.remove("fullName");
                editor.commit();

                editor.clear(); // REMOVE ALL DATA!
                editor.commit(); // commit changes

                mListener.logout();
            }
        });


    }

    public void getPostList(String pageNumber) {

        HttpUrl url = HttpUrl.parse("https://www.theappsdr.com/posts").newBuilder()
                .addQueryParameter("page", pageNumber)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "BEARER " + tokenIdValue)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure:******************* While get post list");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: Success" + responseValue);

                    try {
                        ArrayList<DataService.Post> list = new ArrayList<>();
                        JSONObject jsonObject = new JSONObject(responseValue);

                        JSONArray jsonPost = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < jsonPost.length(); i++) {
                            JSONObject json = jsonPost.getJSONObject(i);
                            DataService.Post post = new DataService.Post();
                            post.setCreatedByName(json.getString("created_by_name"));
                            post.setPostId(json.getString("post_id"));
                            post.setCreatedByUId(json.getString("created_by_uid"));
                            post.setPostText(json.getString("post_text"));
                            post.setCreatedAt(json.getString("created_at"));

                            post.setPage(jsonObject.getString("page"));
                            post.setPageSize(jsonObject.getString("pageSize"));
                            post.setTotalCount(jsonObject.getString("totalCount"));

                            list.add(post);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postList.clear();
                                postList.addAll(list);
                                postListAdapter.notifyDataSetChanged();

                                //Pager summary & Pager implementation
                                int total = (int) Math.ceil(Double.parseDouble(postList.get(0).getTotalCount()) / Double.parseDouble(postList.get(0).getPageSize()));
                                postBinding.tvShowingPage.setText(String.format(getString(R.string.label_showing_page),Integer.parseInt(pageNumber),total));
                                ArrayList<String> listPage = new ArrayList<>();
                                for (int i = 1; i <= total ; i++) {
                                    listPage.add(String.valueOf(i));
                                }
                                pageList.clear();
                                pageList.addAll(listPage);
                                postPagerAdapter.notifyDataSetChanged();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d("TAG", "onResponse: Failure" + responseValue);
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IServices) {
                mListener = (IServices) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {

        }
    }

    class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostListViewHolder> {
        ArrayList<DataService.Post> postList;
        IServices mListener;

        public PostListAdapter(ArrayList<DataService.Post> sortNameList, IServices mListener) {
            this.postList = sortNameList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CustomPostListItemBinding binding = CustomPostListItemBinding.inflate(getLayoutInflater(), parent, false);
            PostListViewHolder postViewHolder = new PostListViewHolder(binding);
            return postViewHolder;

        }

        @Override
        public int getItemCount() {
            return postList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull PostListViewHolder holder, @SuppressLint("RecyclerView") int position) {

            DataService.Post item = postList.get(position);
            holder.setUpData(item);
        }

        public class PostListViewHolder extends RecyclerView.ViewHolder {

            CustomPostListItemBinding mBinding;
            DataService.Post mItem;

            public PostListViewHolder(CustomPostListItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(DataService.Post item) {
                mItem = item;
                mBinding.tvCreatedBy.setText(mItem.getCreatedByName());
                mBinding.tvPostName.setText(mItem.getPostText());


                String dateTime=(mItem.getCreatedAt());
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = dateFormatter.parse(dateTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String formattedDate = dateFormat.format(date);
                mBinding.tvCreatedDate.setText(formattedDate);

                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                String formattedTime = timeFormat.format(date);
                mBinding.tvCreatedTime.setText(formattedTime);


                //Trash can visible only to owner
                if (userId.equals(mItem.getCreatedByUId())) {
                    mBinding.ivTrash.setVisibility(View.VISIBLE);
                    mBinding.ivTrash.setImageResource(R.drawable.ic_trash);
                } else {
                    mBinding.ivTrash.setVisibility(View.GONE);

                }

                mBinding.ivTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.label_post_will_be_deleted))
                                .setMessage(getString(R.string.label_post_delete))
                                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Delete function
                                        deletePost(mItem.getPostId());
                                    }
                                }).setNegativeButton(getString(R.string.label_button_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
    }

    public void deletePost(String postID) {
        FormBody formBody = new FormBody.Builder()
                .add("post_id", postID).build();
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/delete")
                .addHeader("Authorization", "BEARER " + tokenIdValue)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure:******************* While login");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: Success" + responseValue);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getPostList(getString(R.string.page_number));
                        }
                    });

                } else {
                    Log.d("TAG", "onResponse: Failure" + responseValue);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(responseValue);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(status)
                                        .setMessage(message)
                                        .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    class PostPagerAdapter extends RecyclerView.Adapter<PostPagerAdapter.PostPagerViewHolder> {
        ArrayList<String> postPageList;
        IServices mListener;

        public PostPagerAdapter(ArrayList<String> postPageList, IServices mListener) {
            this.postPageList = postPageList;
            this.mListener = mListener;
        }

        @NonNull
        @Override
        public PostPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CustomPostPageListBinding binding = CustomPostPageListBinding.inflate(getLayoutInflater(), parent, false);
            PostPagerViewHolder postViewHolder = new PostPagerViewHolder(binding);
            return postViewHolder;

        }

        @Override
        public int getItemCount() {
            return postPageList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull PostPagerViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String item = postPageList.get(position);
            holder.setUpData(item);
        }

        public class PostPagerViewHolder extends RecyclerView.ViewHolder {

            CustomPostPageListBinding mBinding;
            String mItem;

            public PostPagerViewHolder(CustomPostPageListBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setUpData(String item) {
                mItem = item;
                mBinding.tvPageNumber.setText(item);


                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       getPostList(mItem);
                    }
                });
            }

        }
    }

}