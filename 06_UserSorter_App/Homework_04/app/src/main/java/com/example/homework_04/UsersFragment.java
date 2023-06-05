package com.example.homework_04;
/*
a. Assignment Homework04.
b. File Name: UsersFragment.java
c. Full name of the student : Krithika Kasaragod
*/

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homework_04.Sort.StateAscendingSort;
import com.example.homework_04.databinding.FragmentUsersBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UsersFragment extends Fragment {


    FragmentUsersBinding fragmentUsersBinding;
    static ArrayList<DataServices.User> userArrayList;
    ArrayAdapter<DataServices.User> adapter;
    IUserInterface mListener;
    private static final String ARG_CRITERIA = "ARG_CRITERIA";
    private DataServices.User mUser;
    static String filter_state;
    static String sort_criteria;
    String filter_State_data, sort_Criteria_Data;


    public UsersFragment() {
    }

    public static UsersFragment newInstance(DataServices.User state) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRITERIA, state);
        fragment.setArguments(args);
        return fragment;
    }
    public void setFilterData(String filterData){
        filter_State_data= filterData;
    }
    public void setSortData(String sortData){
        sort_Criteria_Data= sortData;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (DataServices.User) getArguments().getSerializable(ARG_CRITERIA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentUsersBinding = FragmentUsersBinding.inflate(inflater, container, false);
        getActivity().setTitle(R.string.label_users_fragment_title);
        return fragmentUsersBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //checks for any filter or criteria

        if(filter_State_data!=null || sort_Criteria_Data!=null){
            Log.d("TAG", "onViewCreated: filterStateData"+filter_State_data);
            if ((filter_State_data != null) && filter_State_data.equals(getString(R.string.label_all_states))) {
                //displays getAll states
                userArrayList = DataServices.getAllUsers();
                //checks if any criteria
                if (sort_Criteria_Data != null) {
                    methodSortWithCriteria();
                }
            }
            //if filter has any particular state
            else {
                if (filter_State_data != null && (!filter_State_data.equals(getString(R.string.label_all_states)))) {
                    //gets particular state
                    userArrayList = methodToGetStateList(filter_State_data);
                } else {
                    userArrayList = DataServices.getAllUsers();
                }
                //checks if any criteria
                if (sort_Criteria_Data != null) {
                    methodSortWithCriteria();
                } else {
                    userArrayList = methodToGetStateList(filter_State_data);
                }
            }
        }else{
            userArrayList = DataServices.getAllUsers();
        }

        adapter = new UserAdapter(getActivity(), R.layout.custom_user_list, userArrayList);
        fragmentUsersBinding.listUser.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        fragmentUsersBinding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoFilterByStateFragment();
            }
        });

        fragmentUsersBinding.btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSortFragment();
            }
        });

    }

    private void methodSortWithCriteria() {
        if (sort_Criteria_Data.equals(getString(R.string.label_age_ascending))) {
            Collections.sort(userArrayList, new Comparator<DataServices.User>() {
                @Override
                public int compare(DataServices.User user1, DataServices.User user2) {
                    return (user1.age - (user2.age));
                }
            });
        } else if (sort_Criteria_Data.equals(getString(R.string.label_age_descending))) {
            //Filters users_list by descending order of age
            Collections.sort(userArrayList, new Comparator<DataServices.User>() {
                @Override
                public int compare(DataServices.User user1, DataServices.User user2) {
                    return -1 * (user1.age - (user2.age));
                }
            });
        } else if (sort_Criteria_Data.equals(getString(R.string.label_name_ascending))) {
            //Filters users_list by ascending order of Name
            Collections.sort(userArrayList, new Comparator<DataServices.User>() {
                @Override
                public int compare(DataServices.User user1, DataServices.User user2) {
                    return (user1.name.compareTo(user2.name));
                }
            });

        } else if (sort_Criteria_Data.equals(getString(R.string.label_name_descending))) {
            //Filters users_list by descending order of Name
            Collections.sort(userArrayList, new Comparator<DataServices.User>() {
                @Override
                public int compare(DataServices.User user1, DataServices.User user2) {
                    return -1 * (user1.name.compareTo(user2.name));
                }
            });

        } else if (sort_Criteria_Data.equals(getString(R.string.label_state_ascending))) {
            //Filters users_list by ascending order of State
            Collections.sort(userArrayList, new StateAscendingSort());

        } else if (sort_Criteria_Data.equals(getString(R.string.label_state_descending))) {
            //Filters users_list by descending order of State
            Collections.sort(userArrayList, new Comparator<DataServices.User>() {
                @Override
                public int compare(DataServices.User user1, DataServices.User user2) {
                    return -1 * (user1.getState().compareTo(user2.getState()));
                }
            });
        }
    }

    // method to filter all users list based on selected state
    private ArrayList<DataServices.User> methodToGetStateList(String state) {
        ArrayList<DataServices.User> userList = new ArrayList<>();
        for (int i = 0; i < DataServices.getAllUsers().size(); i++) {
            if (DataServices.getAllUsers().get(i).getState().equals(state)) {
                userList.add(DataServices.getAllUsers().get(i));
            }
        }
        return userList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IUserInterface) {
                mListener = (IUserInterface) context;
            } else {
                throw new RuntimeException();
            }

        } catch (RuntimeException e) {

        }
    }

    class UserAdapter extends ArrayAdapter<DataServices.User> {

        public UserAdapter(@NonNull Context context, int resource, @NonNull List<DataServices.User> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_user_list, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvName = convertView.findViewById(R.id.tvName);
                viewHolder.tvState = convertView.findViewById(R.id.tvState);
                viewHolder.tvAge = convertView.findViewById(R.id.tvAge);
                viewHolder.tvGroup = convertView.findViewById(R.id.tvGroup);
                viewHolder.ivGender = convertView.findViewById(R.id.imageView);
                convertView.setTag(viewHolder);
            }

            DataServices.User user = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvName.setText(user.name);
            viewHolder.tvState.setText(user.state);
            viewHolder.tvAge.setText(String.format(getString(R.string.label_years_old), user.age));
            viewHolder.tvGroup.setText(user.group);

            if (user.gender.equals("Male")) {
                viewHolder.ivGender.setImageResource(R.drawable.avatar_male);
            } else if (user.gender.equals("Female")) {
                viewHolder.ivGender.setImageResource(R.drawable.avatar_female);
            }
            return convertView;
        }

        public class ViewHolder {
            TextView tvName, tvState, tvAge, tvGroup;
            ImageView ivGender;
        }
    }
}