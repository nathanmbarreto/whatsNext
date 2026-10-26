
const BASE_URL = "https://api.themoviedb.org/3";

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

export function searchMovies(query) {
  return tmdbFetch("/search/movie", {
    query,
    include_adult: "false",
  });
}

export function searchSeries(query) {
  return tmdbFetch("/search/tv", {
    query,
    include_adult: "false",
  });
}

export function getMovieDetails(id) {
  return tmdbFetch(`/movie/${id}`);
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
