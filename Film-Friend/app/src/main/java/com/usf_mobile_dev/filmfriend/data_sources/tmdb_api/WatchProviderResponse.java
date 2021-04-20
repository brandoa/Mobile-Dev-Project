package com.usf_mobile_dev.filmfriend.data_sources.tmdb_api;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WatchProviderResponse {

    @SerializedName("results")
    public List<WatchProviderResponse.WPData> wpData = null;

    public class WPData {

        @SerializedName("display_priority")
        public int displayPriority;
        @SerializedName("logo_path")
        public String logoPath;
        @SerializedName("provider_name")
        public String providerName;
        @SerializedName("provider_id")
        public int providerId;
    }
}
