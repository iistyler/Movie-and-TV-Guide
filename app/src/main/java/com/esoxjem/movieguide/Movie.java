package com.esoxjem.movieguide;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable
{
    private String id;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private String backdropPath;
    private String title;
    private double voteAverage;
    private int tvMovie;

    public Movie()
    {

    }

    protected Movie(Parcel in)
    {
        id = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        tvMovie = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>()
    {
        @Override
        public Movie createFromParcel(Parcel in)
        {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size)
        {
            return new Movie[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setTvMovie(int val) { this.tvMovie = val; }

    public Boolean isMovie() { if(this.tvMovie == 0) { return false; } return true; }

    public String getOverview()
    {
        return overview;
    }

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath;
    }

    public String getBackdropPath()
    {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath)
    {
        this.backdropPath = backdropPath;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public double getVoteAverage()
    {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(id);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(tvMovie);
    }
}
