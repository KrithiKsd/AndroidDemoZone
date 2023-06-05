package com.example.homework_04;

/*
a. Assignment Homework04.
b. File Name: FilterByStateFragment.java
c. Full name of the student : Krithika Kasaragod
*/

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.homework_04.databinding.FragmentFilterByStateBinding;
import java.util.ArrayList;
import java.util.List;


public class FilterByStateFragment extends Fragment {

    FragmentFilterByStateBinding fragmentFilterByStateBinding;
    ArrayList<DataServices.User> uniqueList;
    ArrayAdapter<DataServices.User> adapter;
    IUserInterface mListener;

    public FilterByStateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentFilterByStateBinding= FragmentFilterByStateBinding.inflate(inflater,container,false);
        getActivity().setTitle(R.string.label_filter_by_state_fragment_title);
        return fragmentFilterByStateBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uniqueList = DataServices.methodToGetUniqueUserListArray();
        adapter = new FilterByState(getContext(), R.layout.custom_filter_by_state_list, uniqueList);
        DataServices.User state= new DataServices.User(getString(R.string.label_all_states));
        adapter.insert(state,0);
        adapter.notifyDataSetChanged();
        fragmentFilterByStateBinding.listviewFilterByState.setAdapter(adapter);

        fragmentFilterByStateBinding.listviewFilterByState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //passing selected state as criteria to UsersFragment
                DataServices.User user= new DataServices.User();
                if(position==0){
                    user.state = getString(R.string.label_all_states);
                }else {
                    user.state = uniqueList.get(position).getState();
                }
                mListener.gotoUsersFragmentWithSelectedCriteria(user);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if(context instanceof IUserInterface){
                mListener = (IUserInterface) context;
            }else{
                throw new RuntimeException();
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    static class FilterByState extends ArrayAdapter<DataServices.User>{

        public FilterByState(@NonNull Context context, int resource, @NonNull List<DataServices.User> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_filter_by_state_list,parent,false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvSateName= convertView.findViewById(R.id.tvStateName);
                convertView.setTag(viewHolder);
            }

            DataServices.User user = getItem(position);
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvSateName.setText(user.state);
            return convertView;
        }

        public static class ViewHolder{
            TextView tvSateName;
        }
    }
}