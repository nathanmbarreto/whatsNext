
import { useEffect, useState } from "react";
import { getPopularMovies } from "../../services/tmdb";
import FeaturedGrid from "../../components/FeaturedGrid/FeaturedGrid";
import { NavLink } from "react-router-dom";

const IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";

export default function Home() {
  const [featured, setFeatured] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadFeatured() {
      try {
        setLoading(true);
        setError("");

        const data = await getPopularMovies(1);

        // Seleciona o primeiro filme que tenha cartaz.
        const movie = data.find((item) => item.posterPath);

        if (!movie) {
          throw new Error("Nenhum filme em destaque foi encontrado.");
        }

        if (!cancelled) {
          setFeatured({
            ...movie,
            poster_path: movie.posterPath,
            release_date: movie.releaseDate,
            vote_average: movie.voteAverage,
          });
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            err.message || "Erro ao carregar o destaque."
          );
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadFeatured();

    return () => {
      cancelled = true;
    };
  }, []);

  if (loading) {
    return (
      <section className="flex min-h-96 items-center justify-center text-gray-300">
        Carregando destaque...
      </section>
    );
  }

  if (error) {
    return (
      <section className="rounded-xl bg-[#181B24] p-6 text-white">
        <h1 className="text-xl font-bold">
          Não foi possível carregar o destaque
        </h1>
        <p className="mt-2 text-gray-400">{error}</p>

        <FeaturedGrid />
        <GamesGrid page={1} pageSize={5} />
      </section>
    );
  }

  const title = featured.title || "Título indisponível";
  const posterAvailable = Boolean(featured.poster_path);
  const posterUrl = posterAvailable
    ? `${IMAGE_BASE_URL}${featured.poster_path}`
    : null;

  const releaseYear = featured.release_date
    ? featured.release_date.slice(0, 4)
    : "Ano não informado";

  return (
    <>
      <section className="relative overflow-hidden rounded-2xl bg-[#181B24]">
        <div className="grid items-center gap-8 p-6 sm:p-10 md:grid-cols-[1fr_240px] lg:grid-cols-[1fr_280px]">
          <div className="order-2 md:order-1">
            <span className="text-sm font-semibold uppercase tracking-[0.2em] text-[#7657F6]">
              Destaque da semana
            </span>

            <h1 className="mt-4 text-3xl font-extrabold text-white sm:text-4xl lg:text-5xl">
              {title}
            </h1>

            <p className="mt-3 text-sm text-gray-400">
              {releaseYear} · Filme
            </p>

            <p className="mt-6 max-w-2xl leading-7 text-gray-300 text-justify">
              {featured.overview ||
                "A sinopse deste título ainda não está disponível."}
            </p>

            <div className="mt-6 flex flex-wrap gap-3">
              <NavLink
                to={`/detalhes/movie/${featured.id}`}
                className="rounded-xl bg-[#7657F6] px-6 py-3 font-semibold text-white transition hover:bg-[#6545E8]"
              >
                Ver detalhes
              </NavLink>


              <span className="flex items-center gap-2 rounded-xl border border-white/10 px-4 py-3 text-white">
                <span className="text-[#FFD166]">★</span>
                {typeof featured.vote_average === "number"
                  ? `${featured.vote_average.toFixed(1)}/10`
                  : "Sem avaliação"}
                <span className="text-sm text-gray-400">
                  TMDB
                </span>
              </span>
            </div>
          </div>

          <div className="order-1 mx-auto w-full max-w-[220px] md:order-2 md:max-w-none">
            {posterAvailable ? (
              <img
                src={posterUrl}
                alt={`Cartaz de ${title}`}
                className="w-full rounded-xl object-cover shadow-2xl shadow-black/40"
              />
            ) : (
              <div className="flex aspect-[2/3] items-center justify-center rounded-xl bg-[#10121A] text-gray-400">
                Cartaz indisponível
              </div>
            )}
          </div>
        </div>
      </section>

      <FeaturedGrid />
      {/* <GamesGrid page={1} pageSize={5} period="recent" /> */}
    </>
  );
}
