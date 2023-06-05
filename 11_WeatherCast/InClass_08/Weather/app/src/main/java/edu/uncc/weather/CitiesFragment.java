package edu.uncc.weather;
/*
a. Assignment #. InClass08
b. File Name : CitiesFragment.java
c. Full name of the student 1: Krithika Kasaragod
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.uncc.weather.databinding.FragmentCitiesBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CitiesFragment extends Fragment {
    FragmentCitiesBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    DataService.City city = new DataService.City();


    public CitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ArrayAdapter<DataService.City> adapter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_cities));


        adapter = new ArrayAdapter<DataService.City>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DataService.cities);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //method to get lat & lon
                getLat_Lon(adapter.getItem(position));
            }
        });
    }

    private void getLat_Lon(DataService.City cityName) {
        city.setCity(cityName.getCity());
        city.setCountry(cityName.getCountry());
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/geo/1.0/direct?").newBuilder()
                .addQueryParameter("q", city.getCity())
                .addQueryParameter("limit", "1")
                .addQueryParameter("appid", DataService.ARG_API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("DEMO", "onFailure: ****");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String responseValue = responseBody.string();
                   // Log.d("TAG", "onResponse: Successful" + responseValue);
                    try {
                        JSONArray jsonArray = new JSONArray(responseValue);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            city.setLat(jsonObject.getString("lat"));
                            city.setLon(jsonObject.getString("lon"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.gotoCurrentWeather(city);
                        }
                    });
                } else {
                    Log.d("DEMO", "onResponse Failure: ****");
                }
            }
        });
    }

    CitiesFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitiesFragmentListener) context;
    }

    interface CitiesFragmentListener {
        void gotoCurrentWeather(DataService.City city);
    }
}