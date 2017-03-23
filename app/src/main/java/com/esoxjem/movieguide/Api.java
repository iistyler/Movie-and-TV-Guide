package com.esoxjem.movieguide;

/**
 * @author arun
 */
public class Api
{
    public static final String API_KEY = "9284efd94597f7b77991b89487c2e778";

    public static final String GET_POPULAR_MOVIES = "http://api.themoviedb.org/3/discover/movie?language=en&sort_by=popularity.desc&api_key=" + API_KEY;
    public static final String GET_POPULAR_TV = "http://api.themoviedb.org/3/discover/tv?language=en&sort_by=popularity.desc&api_key=" + API_KEY;

    public static final String GET_MOVIE_DETAILS = "http://api.themoviedb.org/3/movie/%s?api_key=" + API_KEY;
    public static final String GET_TV_IMDB = "http://api.themoviedb.org/3/tv/%s/external_ids?api_key=" + API_KEY;
    public static final String GET_TV_DETAILS = "http://api.themoviedb.org/3/tv/%s?api_key=" + API_KEY;
    public static final String GET_OMDB_DETAILS = "https://omdbapi.com/?i=%s";

    public static final String GET_HIGHEST_RATED_MOVIES = "http://api.themoviedb.org/3/discover/movie?vote_count.gte=500&language=en&sort_by=vote_average.desc&api_key=" + API_KEY;
    public static final String GET_HIGHEST_RATED_TV = "http://api.themoviedb.org/3/discover/tv?vote_count.gte=500&language=en&sort_by=vote_average.desc&api_key=" + API_KEY;

    public static final String GET_TRAILERS = "http://api.themoviedb.org/3/movie/%s/videos?api_key=" + API_KEY;
    public static final String GET_REVIEWS = "http://api.themoviedb.org/3/movie/%s/reviews?api_key=" + API_KEY;

    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w342";
    public static final String BACKDROP_PATH = "http://image.tmdb.org/t/p/w780";

    static final String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%1$s";
    static final String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%1$s/0.jpg";

    public static final String GET_SEARCH_MOVIES = "http://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=";
    public static final String GET_SEARCH_TV = "http://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&query=";

    private Api()
    {
        // hide implicit public constructor
    }
}
