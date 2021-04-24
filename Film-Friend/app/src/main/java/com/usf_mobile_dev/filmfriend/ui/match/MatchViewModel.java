package com.usf_mobile_dev.filmfriend.ui.match;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.usf_mobile_dev.filmfriend.data_sources.data_classes.Movie;
import com.usf_mobile_dev.filmfriend.data_sources.repository.MovieRepository;
import com.usf_mobile_dev.filmfriend.R;
import com.usf_mobile_dev.filmfriend.data_sources.data_classes.MatchPreferences;
import com.usf_mobile_dev.filmfriend.data_sources.tmdb_api.DiscoverResponse;
import com.usf_mobile_dev.filmfriend.data_sources.tmdb_api.GenreResponse;
import com.usf_mobile_dev.filmfriend.data_sources.tmdb_api.LanguageResponse;
import com.usf_mobile_dev.filmfriend.data_sources.tmdb_api.WatchProviderResponse;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoActivity;
import com.usf_mobile_dev.filmfriend.ui.movieInfo.MovieInfoViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<GenreResponse.Genre>> genres;
    private HashMap<String, Integer> genres_to_api_id;
    private MutableLiveData<List<LanguageResponse>> languages;
    private HashMap<String, String> languages_to_iso_id;
    private MutableLiveData<String> selectedLanguage;
    private MutableLiveData<List<String>> languages_list;
    private MutableLiveData<List<WatchProviderResponse.WPData>> watchProviders;
    private HashMap<String, Integer> watch_providers_to_api_id;
    private MutableLiveData<String> selectedWatchProvider;
    private MutableLiveData<List<String>> watchProviders_list;
    private MovieRepository movieRepository;
    private MatchPreferences MP;
    private String api_key;

    /*private String[] languages_array = new String[]{
            "Abkhazian", "Afar", "Afrikaans", "Akan", "Albanian", "Amharic", "Arabic", "Aragonese", "Armenian", "Assamese", "Avaric", "Avestan", "Aymara", "Azerbaijani", "Bambara",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
    };*/

    public MatchViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("Filters:");
        movieRepository = new MovieRepository(application);
        MP = new MatchPreferences();
        api_key = application.getString(R.string.tmdb_api_key);
        genres = new MutableLiveData<List<GenreResponse.Genre>>();
        genres.setValue(new ArrayList<GenreResponse.Genre>());
        genres_to_api_id = new HashMap<String, Integer>();
        this.refreshGenres(application);
        languages = new MutableLiveData<>();
        languages.setValue(new ArrayList<>());
        languages_list = new MutableLiveData<>();
        languages_list.setValue(new ArrayList<>());
        languages_to_iso_id = new HashMap<>();
        selectedLanguage = new MutableLiveData<>();
        selectedLanguage.postValue("- Any -");
        this.refreshLanguages(application);

        watchProviders = new MutableLiveData<>();
        watchProviders.setValue(new ArrayList<>());
        watchProviders_list = new MutableLiveData<>();
        watchProviders_list.setValue(new ArrayList<>());
        watch_providers_to_api_id = new HashMap<>();
        selectedWatchProvider = new MutableLiveData<>();
        selectedWatchProvider.postValue("- Any -");
        this.refreshWatchProviders(application);
    }


    /*
    // https://api.themoviedb.org/3/genre/movie/list?api_key=25ace50f784640868b88295ea133e67e&language=en-US
    final private HashMap<String, Integer> genres_to_api_id = new HashMap<String, Integer>()
    {{
        put("Action", 28);
        put("Adventure", 12);
        put("Animation", 16);
        put("Comedy", 35);
        put("Crime", 80);
        put("Documentary", 99);
        put("Drama", 18);
        put("Family", 10751);
        put("Fantasy", 14);
        put("History", 36);
        put("Horror", 27);
        put("Music", 10402);
        put("Mystery", 9648);
        put("Romance", 10749);
        put("Sci-Fi", 878);
        put("TV Movie", 10770);
        put("Thriller", 53);
        put("War", 10752);
        put("Western", 37);
    }};
     */

    public void setIncludedGenreVal(String name, Boolean new_val)
    {
        Integer id = genres_to_api_id.get(name);
        MP.setIncludedGenre(id, new_val);
    }

    public void setExcludedGenreVal(String name, Boolean new_val)
    {
        Integer id = genres_to_api_id.get(name);
        MP.setExcludedGenre(id, new_val);
    }

    final public int getGenreID(String name) {

        return genres_to_api_id.get(name);
    }

    public void setRating (double new_rating, boolean is_min)
    {
        if (is_min)
            MP.setRating_min(new_rating);
        else
            MP.setRating_max(new_rating);
    }

    public void setReleaseYear(int year, boolean is_start)
    {
        if (is_start)
            MP.setRelease_year_start(year);
        else
            MP.setRelease_year_end(year);
    }

    public void setRuntime(int mins, boolean is_min)
    {
        if (is_min)
            MP.setRuntime_min(mins);
        else
            MP.setRuntime_max(mins);
    }

    public void setVoteCount(int count, boolean is_min)
    {
        if (is_min)
            MP.setVote_count_min(count);
        else
            MP.setVote_count_max(count);
    }

    public void getTMDBMovie(Context context) {
        movieRepository.getTMDBMovie(
                new Callback<DiscoverResponse>() {
                    @Override
                    public void onResponse(
                            Call<DiscoverResponse> call,
                            Response<DiscoverResponse> response
                    ) {
                        Random randGen = new Random();

                        DiscoverResponse results = response.body();
                        int numMovies = results.movieData.size();
                        // Launches the activity if there are any movies returned
                        if(numMovies > 0) {
                            int movieChoiceIndex = randGen.nextInt(numMovies);
                            DiscoverResponse.MovieData resultMovie =
                                    results.movieData.get(movieChoiceIndex);

                            Movie movie = new Movie();
                            movie.setTitle(resultMovie.title);
                            movie.setOverview(resultMovie.overview);
                            movie.setReleaseYear(Integer.valueOf(
                                                            resultMovie
                                                            .releaseDate
                                                            .split("-")[0]));
                            movie.setRating(resultMovie.voteAverage);
                            movie.setVoteCount(resultMovie.voteCount);
                            movie.setTmdbMovieId(resultMovie.id);
                            movie.setPosterPath(resultMovie.posterPath);
                            movie.setBackdropPath(resultMovie.backdropPath);

                            Intent movieActivityIntent = new Intent(
                                    context,
                                    MovieInfoActivity.class
                            );
                            movieActivityIntent.putExtra(
                                    MovieInfoViewModel.INTENT_EXTRAS_MOVIE_DATA,
                                    movie);
                            movieActivityIntent.putExtra(
                                    MovieInfoViewModel.INTENT_EXTRAS_ACTIVITY_MODE,
                                    MovieInfoViewModel.ACTIVITY_MODE_MATCH
                            );
                            movieActivityIntent.putExtra(
                                    MovieInfoViewModel.INTENT_EXTRAS_MOVIE_PREFERENCES,
                                    getMP()
                            );
                            context.startActivity(movieActivityIntent);
                        }
                        // Pops a toast if there aren't any movies returned
                        else {
                            Toast.makeText(context, "No Matching Movies!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DiscoverResponse> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(context, "SEARCH FAILED", Toast.LENGTH_LONG).show();
                    }
                },
                context.getMainExecutor(),
                MP,
                this.api_key);
    }

    private void refreshGenres(Context context) {
        movieRepository.getTMDBGenres(
                new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call,
                                           Response<GenreResponse> response) {
                        GenreResponse results = response.body();
                        if (results != null) {

                            // Creates a new genres to id HashMap
                            genres_to_api_id.clear();
                            for(GenreResponse.Genre  genre : results.genres)
                                genres_to_api_id.put(genre.name, genre.id);

                            // Resets the genres in the current Match Preference
                            //MP.clearGenres();
                            MP.setGenres(results.genres);
                            genres.setValue(results.genres);
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreResponse> call,
                                          Throwable t) {
                        call.cancel();
                        Toast.makeText(context,
                                "COULDN'T ACCESS GENRES",
                                Toast.LENGTH_LONG).show();
                    }
                },
                context.getMainExecutor(),
                this.api_key
        );
    }

    private void refreshLanguages(Context context) {
        movieRepository.getTMDBLanguages(
                new Callback<List<LanguageResponse>>() {
                    @Override
                    public void onResponse(
                            Call<List<LanguageResponse>> call,
                            Response<List<LanguageResponse>> response
                    ) {
                        List<LanguageResponse> results = response.body();
                        if(results != null) {
                            // Resets the genres in the current Match Preference
                            results.sort((o1, o2) -> o1.english_name.compareTo(o2.english_name));
                            List<String> temp = new ArrayList<>();

                            // Adds an option for any language
                            temp.add("- Any -");
                            languages_to_iso_id.clear();
                            languages_to_iso_id.put(
                                    "- Any -",
                                    "ANY_LANGUAGE"
                            );

                            // Creates a new languages list
                            for(LanguageResponse  language : results) {
                                temp.add(language.english_name);
                                languages_to_iso_id.put(
                                        language.english_name,
                                        language.iso_code);
                            }
                            languages.postValue(results);
                            languages_list.postValue(temp);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<LanguageResponse>> call,
                            Throwable t
                    ) {
                        call.cancel();
                        Toast.makeText(context,
                                "COULDN'T ACCESS LANGUAGES",
                                Toast.LENGTH_LONG).show();
                    }
                },
                context.getMainExecutor(),
                this.api_key
        );
    }

    private void refreshWatchProviders(Context context) {
        movieRepository.getTMDBWatchProviders(
                new Callback<WatchProviderResponse>() {
                    @Override
                    public void onResponse(
                            Call<WatchProviderResponse> call,
                            Response<WatchProviderResponse> response
                    ) {
                        List<WatchProviderResponse.WPData> results = response.body().wpData;
                        if(results != null) {
                            // Resets the watch providers in the current Match Preference
                            results.sort((o1, o2) -> o1.providerName.compareTo(o2.providerName));

                            List<WatchProviderResponse.WPData> filteredResults = new ArrayList<>();
                            List<String> temp = new ArrayList<>();

                            // Adds an option for any watch provider
                            temp.add("- Any -");
                            watch_providers_to_api_id.clear();
                            watch_providers_to_api_id.put(
                                    "- Any -",
                                    -1
                            );

                            // Creates a new watch providers list
                            for(WatchProviderResponse.WPData  watchProvider : results) {
                                if(!temp.contains(watchProvider.providerName)
                                    && watchProvider.providerId != 119) {
                                    filteredResults.add(watchProvider);
                                    temp.add(watchProvider.providerName);
                                    watch_providers_to_api_id.put(
                                            watchProvider.providerName,
                                            watchProvider.providerId);
                                }
                            }
                            watchProviders.postValue(filteredResults);
                            watchProviders_list.postValue(temp);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<WatchProviderResponse> call,
                            Throwable t
                    ) {
                        Log.d("WP_CALLBACK_FAILURE",
                                call.request().toString());
                        Log.d("WP_CALLBACK_FAILURE",
                                t.getMessage());
                        call.cancel();
                        Toast.makeText(context,
                                "COULDN'T ACCESS WATCH PROVIDERS",
                                Toast.LENGTH_LONG).show();
                    }
                },
                context.getMainExecutor(),
                this.api_key
        );
    }

    public MutableLiveData<List<GenreResponse.Genre>> getGenres() {
        return genres;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MatchPreferences getMP() {
        return this.MP;
    }

    public void setMP(MatchPreferences mp) {
        this.MP = mp;
    }

    public MutableLiveData<List<LanguageResponse>> getLanguages() {
        return languages;
    }

    public MutableLiveData<String> getSelectedLanguage() {
        return selectedLanguage;
    }

    public String getLanguageFromID(String id) {
        for (String o : languages_to_iso_id.keySet()) {
            if (languages_to_iso_id.get(o).equals(id)) {
                return o;
            }
        }
        return null;
    }

    public MutableLiveData<List<String>> getLanguages_list() {
        return languages_list;
    }

    public void setSelectedLanguage(String language) {
        selectedLanguage.postValue(language);
        Log.d("LANGUAGE", "in setSelectedLanguage: " + language);
        Log.d("LANGUAGE", "in setSelectedLanguage: " + languages_to_iso_id.get(language));
        this.MP.setSelected_language_code(languages_to_iso_id.get(language));
        this.MP.setSelected_language_name(language);
    }

    public MutableLiveData<List<String>> getWatchProviders_list() {
        return watchProviders_list;
    }

    public void setSelectedWatchProvider(String watchProvider) {
        selectedWatchProvider.postValue(watchProvider);
        Log.d("WATCH_PROVIDER", "in setSelectedWatchProvider: " + watchProvider);
        Log.d("WATCH_PROVIDER", "in setSelectedWatchProvider: " + watch_providers_to_api_id.get(watchProvider));
        this.MP.setSelected_watch_provider_code(watch_providers_to_api_id.get(watchProvider));
        this.MP.setSelected_watch_provider_name(watchProvider);
    }
}