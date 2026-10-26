
import { useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import {
  Star,
  CalendarDays,
  Clapperboard,
  Gamepad2,
  ArrowLeft,
} from "lucide-react";
import {
  getMovieDetails,
  getSeriesDetails,
} from "../../services/tmdb";
import Comments from "../../components/Comments/Comments";

const TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500";

export default function Details() {
  const { type, id } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function fetchData() {
      setLoading(true);
      setError("");
      setData(null);

      try {
        let result;

        if (type === "movie") {
          result = await getMovieDetails(id);
        } else if (type === "series") {
          result = await getSeriesDetails(id);
        } else if (type === "game") {
          const key = import.meta.env.VITE_RAWG_API_KEY;

          if (!key) {
            throw new Error("Configure VITE_RAWG_API_KEY no arquivo .env.");
          }

          const params = new URLSearchParams({ key });
          const response = await fetch(
            `https://api.rawg.io/api/games/${id}?${params}`
          );

          if (!response.ok) {
            throw new Error("Não foi possível carregar os detalhes do jogo.");
          }

          result = await response.json();
        } else {
          throw new Error("Tipo de conteúdo inválido.");
        }

        if (!cancelled) {
          setData(result);
        }
      } catch (err) {
        console.error("Erro ao buscar detalhes:", err);

        if (!cancelled) {
          setError(
            err.message || "Não foi possível carregar os detalhes."
          );
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    fetchData();

    return () => {
      cancelled = true;
    };
  }, [type, id]);

  if (loading) {
    return (
      <main className="mx-auto max-w-6xl animate-pulse px-4 py-10">
        <div className="h-5 w-32 rounded bg-[#20232E]" />
        <div className="mt-8 grid gap-8 md:grid-cols-[280px_1fr]">
          <div className="aspect-[2/3] rounded-2xl bg-[#20232E]" />
          <div className="space-y-4">
            <div className="h-10 w-3/4 rounded bg-[#20232E]" />
            <div className="h-5 w-1/2 rounded bg-[#20232E]" />
            <div className="h-28 rounded bg-[#20232E]" />
          </div>
        </div>
      </main>
    );
  }

  if (error || !data) {
    return (
      <main className="mx-auto max-w-4xl px-4 py-20 text-center text-white">
        <h1 className="text-2xl font-bold">
          Não foi possível carregar este conteúdo
        </h1>

        <p className="mt-3 text-gray-400">
          {error || "Tente novamente mais tarde."}
        </p>

        <Link
          to="/"
          className="mt-6 inline-flex items-center gap-2 rounded-xl bg-[#7657F6] px-5 py-3 font-semibold text-white transition hover:bg-[#6545E8]"
        >
          <ArrowLeft size={18} />
          Voltar ao início
        </Link>
      </main>
    );
  }

  const isGame = type === "game";

  const title = isGame
    ? data.name
    : type === "series"
      ? data.name
      : data.title;

  const image = isGame
    ? data.background_image
    : data.poster_path
      ? `${TMDB_IMAGE_URL}${data.poster_path}`
      : null;

  const description = isGame
    ? data.description_raw
    : data.overview;

  const rating = isGame
    ? data.rating
    : data.vote_average;

  const year = isGame
    ? data.released?.slice(0, 4)
    : (type === "series"
        ? data.first_air_date
        : data.release_date
      )?.slice(0, 4);

  const genres = data.genres
    ?.map((genre) => genre.name)
    .join(", ");

  const platforms = data.platforms
    ?.map((item) => item.platform.name)
    .join(", ");

  const details = isGame
    ? [
        { label: "Lançamento", value: data.released },
        {
          label: "Desenvolvedora",
          value: data.developers?.map((d) => d.name).join(", "),
        },
        { label: "Plataformas", value: platforms },
        { label: "Metacritic", value: data.metacritic },
      ]
    : type === "series"
      ? [
          {
            label: "Primeira exibição",
            value: data.first_air_date,
          },
          {
            label: "Última exibição",
            value: data.last_air_date,
          },
          {
            label: "Número de temporadas",
            value: data.number_of_seasons,
          },
          {
            label: "Número de episódios",
            value: data.number_of_episodes,
          },
          {
            label: "Status",
            value: data.status,
          },
          {
            label: "Criadores",
            value: data.created_by?.map((person) => person.name).join(", "),
          },
        ]
      : [
          {
            label: "Lançamento",
            value: data.release_date,
          },
          {
            label: "Duração",
            value: data.runtime ? `${data.runtime} minutos` : null,
          },
          {
            label: "Título original",
            value: data.original_title,
          },
          {
            label: "Status",
            value: data.status,
          },
          {
            label: "Orçamento",
            value: data.budget
              ? new Intl.NumberFormat("pt-BR", {
                  style: "currency",
                  currency: "USD",
                  maximumFractionDigits: 0,
                }).format(data.budget)
              : null,
          },
          {
            label: "Receita",
            value: data.revenue
              ? new Intl.NumberFormat("pt-BR", {
                  style: "currency",
                  currency: "USD",
                  maximumFractionDigits: 0,
                }).format(data.revenue)
              : null,
          },
        ];

  const ratingLabel = isGame ? "RAWG" : "TMDB";
  const backPath = isGame
    ? "/games"
    : type === "series"
      ? "/series"
      : "/movies";

  return (
    <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6 sm:py-12">
      <Link
        to={backPath}
        className="mb-8 inline-flex items-center gap-2 text-sm text-gray-400 transition hover:text-[#A996FF]"
      >
        <ArrowLeft size={17} />
        Voltar ao catálogo
      </Link>

      <section className="relative overflow-hidden rounded-3xl border border-white/10 bg-[#181B24]">
        {isGame && data.background_image && (
          <div
            className="absolute inset-0 bg-cover bg-center opacity-15 blur-sm"
            style={{
              backgroundImage: `url("${data.background_image}")`,
            }}
          />
        )}

        <div className="relative grid gap-8 p-5 sm:p-8 md:grid-cols-[260px_1fr] lg:gap-12 lg:p-10">
          <div className="mx-auto w-full max-w-[260px]">
            {image ? (
              <img
                src={image}
                alt={`Imagem de ${title}`}
                className={`w-full rounded-2xl object-cover shadow-2xl shadow-black/40 ${
                  isGame ? "aspect-[4/3]" : "aspect-[2/3]"
                }`}
              />
            ) : (
              <div className="flex aspect-[2/3] items-center justify-center rounded-2xl bg-[#10121A] p-4 text-center text-gray-500">
                Imagem indisponível
              </div>
            )}
          </div>

          <div className="flex flex-col justify-center">
            <div className="mb-4 flex flex-wrap items-center gap-2">
              <span className="inline-flex items-center gap-1.5 rounded-full border border-[#7657F6]/30 bg-[#7657F6]/15 px-3 py-1.5 text-xs font-semibold uppercase tracking-wider text-[#B5A5FF]">
                {isGame ? (
                  <Gamepad2 size={14} />
                ) : (
                  <Clapperboard size={14} />
                )}
                {isGame
                  ? "Jogo"
                  : type === "series"
                    ? "Série"
                    : "Filme"}
              </span>

              {year && (
                <span className="inline-flex items-center gap-1.5 rounded-full bg-white/5 px-3 py-1.5 text-xs text-gray-300">
                  <CalendarDays size={14} />
                  {year}
                </span>
              )}
            </div>

            <h1 className="text-3xl font-extrabold leading-tight tracking-tight text-white sm:text-4xl lg:text-5xl">
              {title || "Título indisponível"}
            </h1>

            {genres && (
              <p className="mt-4 text-sm text-gray-400">
                {genres}
              </p>
            )}

            <div className="mt-6 flex flex-wrap gap-3">
              {rating != null && (
                <div className="flex items-center gap-3 rounded-xl border border-white/10 bg-black/20 px-4 py-3">
                  <Star
                    size={24}
                    className="fill-[#FFD166] text-[#FFD166]"
                  />
                  <div>
                    <p className="text-xl font-bold text-white">
                      {Number(rating).toFixed(1)}
                      <span className="text-sm font-normal text-gray-400">
                        /10
                      </span>
                    </p>
                    <p className="text-xs text-gray-500">
                      Nota {ratingLabel}
                    </p>
                  </div>
                </div>
              )}

              {isGame && data.metacritic != null && (
                <div className="flex items-center gap-3 rounded-xl border border-white/10 bg-black/20 px-4 py-3">
                  <div>
                    <p className="text-xl font-bold text-white">
                      {data.metacritic}
                    </p>
                    <p className="text-xs text-gray-500">
                      Metacritic
                    </p>
                  </div>
                </div>
              )}
            </div>

            {description && (
              <div className="mt-8">
                <h2 className="mb-3 text-lg font-bold text-white">
                  Sobre {isGame ? "o jogo" : "a obra"}
                </h2>

                <p className="max-w-3xl text-sm leading-7 text-gray-300 sm:text-base text-justify">
                  {description}
                </p>
              </div>
            )}
          </div>
        </div>
      </section>

      <section className="mt-8 rounded-2xl border border-white/10 bg-[#181B24] p-6 sm:p-8">
        <h2 className="text-xl font-bold text-white">
          Informações
        </h2>

        <div className="mt-5 grid gap-5 sm:grid-cols-2">
          {details
            .filter(
              (item) =>
                item.value != null &&
                item.value !== "" &&
                item.value !== "N/A" &&
                item.value !== 0
            )
            .map((item) => (
              <div key={item.label}>
                <p className="text-xs font-semibold uppercase tracking-wider text-gray-500">
                  {item.label}
                </p>
                <p className="mt-1 text-sm leading-6 text-gray-200">
                  {item.value}
                </p>
              </div>
            ))}
        </div>
      </section>
      <Comments key={`${type}-${id}`} type={type} id={id}/>
    </main>
  );
}
