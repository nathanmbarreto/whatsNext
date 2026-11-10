
const BASE_URL = "https://api.themoviedb.org/3";

const BACKEND_URL = "http://localhost:8080/api/tmdb";

const API_KEY = import.meta.env.VITE_TMDB_API_KEY

async function tmdbFetch(endpoint, params = {}) {
  if (!API_KEY) {
    throw new Error("Configure VITE_TMDB_API_KEY no arquivo .env.");
  }

  const searchParams = new URLSearchParams({
    api_key: API_KEY,
    language: "pt-BR",
    ...params,
  });

  const response = await fetch(
    `${BASE_URL}${endpoint}?${searchParams}`
  );

  if (!response.ok) {
    throw new Error(`Erro na TMDB: ${response.status}`);
  }

  return response.json();
}

export function discoverMovies(params = {}) {
  return tmdbFetch("/discover/movie", params);
}

export function discoverSeries(params = {}) {
  return tmdbFetch("/discover/tv", params);
}

export async function searchMovies(query, page = 1) {
  const searchParams = new URLSearchParams({
    query,
    page: String(page),
  })
  const response = await fetch(
    `${BACKEND_URL}/search?${searchParams.toString()}`
  )

  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`
    )
  }
  return response.json()

}

export function searchSeries(query) {
  return tmdbFetch("/search/tv", {
    query,
    include_adult: "false",
  });
}

export async function getMovieDetails(id) {
  const response = await fetch(`${BACKEND_URL}/${id}`);

  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`);
  }

  const movie = await response.json();

  return {
    ...movie,
    poster_path: movie.posterPath,
    release_date: movie.releaseDate,
    vote_average: movie.notaTmdb,
  };
}

export function getSeriesDetails(id) {
  return tmdbFetch(`/tv/${id}`);
}

export function getMovieGenres() {
  return tmdbFetch("/genre/movie/list");
}

export function getSeriesGenres() {
  return tmdbFetch("/genre/tv/list");
}

export async function getPopularMovies(page = 1) {
  const response = await fetch(
    `${BACKEND_URL}/popular?page=${page}`
  )

  if (!response.ok) {
    throw new Error(`Errp mp bacemd: ${response.status}`);

  }
  return response.json();
}
