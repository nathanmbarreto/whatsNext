
import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import {
  discoverMovies,
  discoverSeries,
} from "../../services/tmdb";

const IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

export default function FeaturedGrid({ type = "all" }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function loadItems() {
      setLoading(true);
      setError("");

      try {
        let results = [];

        if (type === "movie" || type === "all") {
          const data = await discoverMovies({
            sort_by: "popularity.desc",
            include_adult: "false",
            page: "1",
          });

          results.push(
            ...(data.results || []).map((item) => ({
              ...item,
              mediaType: "movie",
              displayTitle: item.title,
              displayDate: item.release_date,
            }))
          );
        }

        if (type === "series" || type === "all") {
          const data = await discoverSeries({
            sort_by: "popularity.desc",
            page: "1",
          });

          results.push(
            ...(data.results || []).map((item) => ({
              ...item,
              mediaType: "series",
              displayTitle: item.name,
              displayDate: item.first_air_date,
            }))
          );
        }

        results = results
          .filter((item) => item.poster_path)
          .sort(
            (a, b) =>
              (b.popularity || 0) - (a.popularity || 0)
          )
          .slice(0, 18);

        if (!cancelled) {
          setItems(results);

          if (results.length === 0) {
            setError("Nenhum título encontrado.");
          }
        }
      } catch (err) {
        if (!cancelled) {
          setError(
            err.message || "Erro ao carregar o catálogo."
          );
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    loadItems();

    return () => {
      cancelled = true;
    };
  }, [type]);

  const heading =
    type === "movie"
      ? "Filmes em destaque"
      : type === "series"
        ? "Séries em destaque"
        : "Em destaque";

  return (
    <section className="mt-12">
      <div className="mb-6">
        <p className="text-sm font-medium text-[#7657F6]">
          WHAT'S NEXT?
        </p>

        <h2 className="mt-1 text-2xl font-bold text-white sm:text-3xl">
          {heading}
        </h2>
      </div>

      {loading && (
        <p className="text-gray-400">Carregando títulos...</p>
      )}

      {!loading && error && (
        <p className="text-sm text-red-400">{error}</p>
      )}

      {!loading && items.length > 0 && (
        <div className="grid grid-cols-2 gap-5 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6">
          {items.map((item) => (
            <article
              key={`${item.mediaType}-${item.id}`}
              className="min-w-0"
            >
              <NavLink
                to={`/detalhes/${item.mediaType}/${item.id}`}
                className="group block rounded-xl focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#7657F6]"
              >
                <div className="overflow-hidden rounded-xl bg-[#20232E]">
                  <img
                    src={`${IMAGE_BASE_URL}${item.poster_path}`}
                    alt={`Cartaz de ${item.displayTitle}`}
                    loading="lazy"
                    className="aspect-[2/3] w-full object-cover transition duration-300 group-hover:scale-105"
                  />
                </div>

                <h3 className="mt-3 truncate font-semibold text-white transition group-hover:text-[#A996FF]">
                  {item.displayTitle}
                </h3>

                <div className="mt-1 flex items-center gap-2 text-sm">
                  <span className="text-[#FFD166]">★</span>
                  <span className="text-gray-200">
                    {typeof item.vote_average === "number"
                      ? `${item.vote_average.toFixed(1)}/10`
                      : "Sem avaliação"}
                  </span>
                  <span className="text-gray-500">
                    {item.displayDate
                      ? item.displayDate.slice(0, 4)
                      : "—"}
                  </span>
                </div>
              </NavLink>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}
