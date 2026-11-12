
const BACKEND_URL =  import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";
const TMDB_URL = `${BACKEND_URL}/api/tmdb`

export async function searchMovies(query, page = 1) {
  const searchParams = new URLSearchParams({
    query,
    page: String(page),
  })
  const response = await fetch(
    `${TMDB_URL}/search?${searchParams.toString()}`
  )

  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`
    )
  }
  return response.json()

}

export async function searchSeries(query, page = 1) {
  const searchParams = new URLSearchParams({
    query,
    page: String(page),
  })
  const response = await fetch(
    `${TMDB_URL}/series/search?${searchParams.toString()}`
  )

  if(!response.ok){
    throw new Error(`Erro no backend: ${response.status}`)
  }

  return response.json()
}

export async function getMovieDetails(id) {
  const response = await fetch(`${TMDB_URL}/${id}`);

  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`);
  }

  const movie = await response.json();

  return {
    ...movie,
    poster_path: movie.posterPath,
    release_date: movie.releaseDate,
    vote_average: movie.notaTmdb,
    original_title: movie.originalTitle,
  };
}

export async function getSeriesDetails(id) {
  const response = await fetch(`${TMDB_URL}/series/${id}`)
  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`)
  }

  const serie = await response.json()

  return serie;

}

export async function getPopularMovies(page = 1) {
  const response = await fetch(
    `${TMDB_URL}/popular?page=${page}`
  )

  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`);

  }
  return response.json();
}


export async function getPopularSeries(page = 1) {
  const response = await fetch(
    `${TMDB_URL}/series/popular?page=${page}`
  )
  if (!response.ok) {
    throw new Error(`Erro no backend: ${response.status}`)
  }
  return response.json()

}