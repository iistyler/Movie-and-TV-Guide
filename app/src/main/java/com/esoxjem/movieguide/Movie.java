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
    /* Might want later down the road?
    private String imdbId;
    private double imdb;
    private int mc; */

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
        /* Might want later down the road?
        imdbId = in.readString();
        imdb = in.readDouble();
        mc = in.readInt();*/
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

    // Movie = 1, TV Show = 0
    public void setTvMovie(int val) { this.tvMovie = val; }
    public int getTvMovie() { return this.tvMovie; }
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

    /* Might want later down the road?
    public String getImdbId() { return imdbId; }
    public void setImdbId(String theId) { imdbId = theId; }

    public double getImdb() { return imdb; }
    public void setImdb(double theScore) { imdb = theScore; }

    public int getMc()
    {
        return mc;
    }
    public void setMc(int theScore) { mc = theScore; }*/

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
        /* Might want later down the road?
        parcel.writeString(imdbId);
        parcel.writeDouble(imdb);
        parcel.writeInt(mc);*/
    }
}