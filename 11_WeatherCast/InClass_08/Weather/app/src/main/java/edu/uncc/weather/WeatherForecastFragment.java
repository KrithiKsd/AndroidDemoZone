package edu.uncc.weather;
/*
a. Assignment #. InClass08
b. File Name : WeatherForecastFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.weather.databinding.FragmentWeatherForecastBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class WeatherForecastFragment extends Fragment {


    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    FragmentWeatherForecastBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<DataService.ForeCast> listForecast = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ForecastRecyclerViewAdapter adapter;

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    public static WeatherForecastFragment newInstance(DataService.City city) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherForecastBinding.inflate(inflater,
                container,
                false);
        getActivity().setTitle(getString(R.string.title_weather_forecast));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvCityName.setText(mCity.getCity() + getString(R.string.label_comma) + mCity.getCountry());
        binding.recyclerForecast.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerForecast.setLayoutManager(linearLayoutManager);
        binding.progressBar.setVisibility(View.VISIBLE);

        getWeatherForecast(mCity);
    }

    private void getWeatherForecast(DataService.City mCity) {
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast?").newBuilder()
                .addQueryParameter("lat", mCity.getLat())
                .addQueryParameter("lon", mCity.getLon())
                .addQueryParameter("appid", DataService.ARG_API_KEY)
                .addQueryParameter("units", DataService.ARG_UNITS)
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
                if(response.isSuccessful()){
                    try {
                        ResponseBody responseBody = response.body();
                        String responseValue = responseBody.string();
                        Log.d("TAG", "onResponse: Successful" + responseValue);
                        JSONObject jsonObject = new JSONObject(responseValue);
                        JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArrayList.length(); i++) {
                            JSONObject json = jsonArrayList.getJSONObject(i);

                            DataService.ForeCast foreCast = new DataService.ForeCast();

                            JSONObject jsonMain = json.getJSONObject("main");
                            foreCast.setTemperature(jsonMain.getString("temp"));
                            foreCast.setMinTemp(jsonMain.getString("temp_min"));
                            foreCast.setMaxTemp(jsonMain.getString("temp_max"));
                            foreCast.setHumidity(jsonMain.getString("humidity"));

                            JSONArray jsonWeatherArray = json.getJSONArray("weather");
                            for (int j = 0; j < jsonWeatherArray.length(); j++) {
                                JSONObject jsonWeather = jsonWeatherArray.getJSONObject(j);
                                foreCast.setDescription(jsonWeather.getString("description"));
                                foreCast.setIconID(jsonWeather.getString("icon"));
                            }

                            foreCast.setDateValue(json.getString("dt_txt"));
                            listForecast.add(foreCast);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.progressBar.setVisibility(View.GONE);
                                    adapter = new ForecastRecyclerViewAdapter(listForecast);
                                    binding.recyclerForecast.setAdapter(adapter);
                                }
                            });
                        }
                        Log.d("TAG", "onResponse: forecast list" + listForecast);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    Log.d("TAG", "onResponse: failure");

                }
            }
        });
    }

    public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

        ArrayList<DataService.ForeCast> forecastList;

        public ForecastRecyclerViewAdapter(ArrayList<DataService.ForeCast> contactList) {
            super();
            this.forecastList = contactList;
        }

        @NonNull
        @Override
        public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_forecast_list, parent, false);
            ForecastViewHolder sortViewHolder = new ForecastViewHolder(view);
            return sortViewHolder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

            holder.textViewDate.setText(this.forecastList.get(position).getDateValue());
            String url = "https://openweathermap.org/img/wn/" + forecastList.get(position).getIconID() + "@2x.png";
            Picasso.get()
                    .load(url)
                    .into(holder.iconWeather);
            holder.textViewTemp.setText(this.forecastList.get(position).getTemperature() + getString(R.string.label_fahrenheit));
            holder.textViewTempMax.setText(getString(R.string.label_max) + " " + this.forecastList.get(position).getMaxTemp() + getString(R.string.label_fahrenheit));
            holder.textViewTempMin.setText(getString(R.string.label_min) + " " + this.forecastList.get(position).getMinTemp() + getString(R.string.label_fahrenheit));
            holder.textViewHumidity.setText(getString(R.string.label_humidity) + " " + this.forecastList.get(position).getHumidity() + getString(R.string.label_percentage));
            holder.textViewDescription.setText(this.forecastList.get(position).getDescription());
            holder.setForecast(this.forecastList.get(position));

        }

        @Override
        public int getItemCount() {
            return this.forecastList.size();
        }

        public class ForecastViewHolder extends RecyclerView.ViewHolder {
            ImageView iconWeather;
            TextView textViewDate, textViewTemp, textViewTempMax, textViewTempMin, textViewHumidity, textViewDescription;
            DataService.ForeCast mForecast;

            public ForecastViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewDate = itemView.findViewById(R.id.tvDateValue);
                iconWeather = itemView.findViewById(R.id.ivWeather);
                textViewTemp = itemView.findViewById(R.id.tvTemperature);
                textViewTempMin = itemView.findViewById(R.id.tvMinTemperature);
                textViewTempMax = itemView.findViewById(R.id.tvMaxTemperature);
                textViewHumidity = itemView.findViewById(R.id.tvHumidity);
                textViewDescription = itemView.findViewById(R.id.tvDescription);

            }

            public void setForecast(DataService.ForeCast contact) {
                mForecast = contact;
            }

        }

    }
}