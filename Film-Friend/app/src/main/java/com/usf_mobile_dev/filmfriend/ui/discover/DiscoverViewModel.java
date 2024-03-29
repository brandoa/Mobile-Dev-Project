package com.usf_mobile_dev.filmfriend.ui.discover;

import android.app.Application;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MovieListing;
import com.usf_mobile_dev.filmfriend.data_sources.repository.MovieRepository;

import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieListing>> mAllMovies;

    public DiscoverViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is the Discover fragment");
        movieRepository = new MovieRepository(application);
        mAllMovies = new MutableLiveData<>();

    }

    public void getAllMoviesNearby(double radius, FragmentActivity discoverActivity) {
        movieRepository.getAllMoviesNearby(radius, discoverActivity);
    }

    public MutableLiveData<List<MovieListing>> getDiscoverMovieList() {
        return mAllMovies;
    }

    public LiveData<String> getText() {
        return mText;
    }
}