package com.usf_mobile_dev.filmfriend.ui.history;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.Movie;
import com.usf_mobile_dev.filmfriend.MovieListing;
import com.usf_mobile_dev.filmfriend.MovieRepository;
import com.usf_mobile_dev.filmfriend.RoomCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnalyticsViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieListing>> mAllMovies;
    private int totalMovies = 0;
    private double averageRating = 0;
    private List<Integer> decadeList = new ArrayList<>();
    private Map<Integer, Integer> decadeCounts = new HashMap<Integer, Integer>();

    public AnalyticsViewModel(@NonNull Application application) {
        super(application);

        movieRepository = new MovieRepository(application);
        mAllMovies = new MutableLiveData<>();
        movieRepository.getAllMovies(new RoomCallback() {
            @Override
            public void onComplete(List<MovieListing> result) {
                if(result != null){
                    //Log.d("WATCHLIST", "Result not null");
                    //Log.d("WATCHLIST", String.valueOf(result.getValue().get(0).getWillWatch()));
                    mAllMovies.postValue(result);
                }
            }

            @Override
            public void onComplete(MovieListing result) {

            }
        });
    }

    LiveData<List<MovieListing>> getMovieList() {return mAllMovies;}

    public void setStatistics(List<MovieListing> movies){
        int count = 0;
        int ratingTotal = 0;
        int decade;
        for(MovieListing m : movies)
        {
            count++;
            ratingTotal += m.getMovie().getRating();
            decade = ((m.getMovie().getReleaseYear()/10)*10);
            if(!decadeList.contains(decade))
                decadeList.add(decade);

            if(decadeCounts.get(decade) == null) {
                //Log.d("DECADE_MAP", "decade is null");
                decadeCounts.put(decade, 1);
            }
            else
            {
                //Log.d("DECADE_MAP", "decade exists");
                int decadeCount = decadeCounts.get(decade).intValue();
                decadeCount++;

                decadeCounts.put(decade, decadeCount);
            }
        }
        totalMovies = count;
        averageRating = ratingTotal/(double)count;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getTotalMovies() {
        return totalMovies;
    }

    public List<Integer> getDecadeList() {
        Collections.sort(decadeList);
        return decadeList;
    }

    public int getDecadeCount(int decade) {

        if(decadeCounts.get(decade) == null)
            return 0;

        return decadeCounts.get(decade);
    }
}
