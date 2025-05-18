package com.example.rma_2_eldar_salihovic;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {
    private int id;
    private String title;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    private String overview;

    public Movie() {
    }

    public Movie(int id, String title, String releaseDate, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(overview);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}