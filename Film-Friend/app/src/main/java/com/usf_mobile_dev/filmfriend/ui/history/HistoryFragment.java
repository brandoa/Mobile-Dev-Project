package com.usf_mobile_dev.filmfriend.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.usf_mobile_dev.filmfriend.HistoryRecyclerViewAdapter;
import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoActivity;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoViewModel;

import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView historyRecyclerView;
    String[] filters;
    Spinner spinnerFilters;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        //final TextView textView = root.findViewById(R.id.text_history);

        /*historyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);

        historyRecyclerView = view.findViewById(R.id.history_recyclerview);
        HistoryRecyclerViewAdapter adapter  = new HistoryRecyclerViewAdapter(getContext());
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyRecyclerView.setAdapter(adapter);

        historyViewModel.getMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieListing>>() {
            @Override
            public void onChanged(@Nullable final List<MovieListing> movies) {
                Log.d("MOVIELIST", "Movie list changed");
                // Update the cached copy of the words in the adapter.
                adapter.setMovies(movies);
            }
        });

        adapter.setOnItemClickListener(new HistoryRecyclerViewAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                MovieListing movieListing = adapter.getMovieAtPosition(position);
                launchMovieInfoActivity(movieListing.getMovie());
            }
        });

        spinnerFilters = view.findViewById(R.id.spinner_filters);
        populateSpinnerFilters();

        spinnerFilters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);

                if(selected.equals("All Movies")){
                    historyViewModel.getAllMovies();
                }
                if(selected.equals("Watch List")){
                    historyViewModel.getAllWatchList();
                }
                //Log.d("hasObserver", String.valueOf(discoverViewModel.getDiscoverMovieList().hasObservers()));
                //Log.d("hasObserver", String.valueOf(discoverViewModel.getDiscoverMovieList().hasObservers()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.analytics_menu) {
            Intent intent_pref = new Intent(getActivity(),
                    AnalyticsActivity.class);
            startActivity(intent_pref);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchMovieInfoActivity( Movie movie) {
        Context context = getActivity();
        Intent movieActivityIntent = new Intent(
                context,
                MovieInfoActivity.class
        );
        movieActivityIntent.putExtra(
                MovieInfoActivity.INTENT_EXTRAS_MOVIE_DATA,
                movie);
        movieActivityIntent.putExtra(
                MovieInfoViewModel.INTENT_EXTRAS_ACTIVITY_MODE,
                MovieInfoViewModel.ACTIVITY_MODE_HISTORY);
        if (context != null) {
            context.startActivity(movieActivityIntent);
        }
    }

    private void populateSpinnerFilters() {
        filters = new String[]{"All Movies", "Watch List"};
        ArrayAdapter<String> rangeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, filters);
        rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilters.setAdapter(rangeAdapter);

    }

}