import { useEffect, useState } from "react";
import { searchMovies, searchSeries } from "../../services/tmdb";
import { NavLink, useSearchParams } from "react-router-dom";



export default function Search() {
    const TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    const [searchParams, setSearchParams] = useSearchParams();

    const query = (searchParams.get("query") || "").trim();
    const requestedType = searchParams.get("type");
    const type = requestedType === "series" ? "series" : "movie";
    const page = Number(searchParams.get("page") || 1);
    const [totalPages, setTotalPages] = useState(1);

    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        let cancelled = false;

        async function loadResults() {
            if (!query) {
                setResults([]);
                setError("");
                setLoading(false);
                return;
            }

            setLoading(true);
            setError("");

            try {
                let data;

                if (type === "movie") {
                    data = await searchMovies(query, page);
                } else {
                    data = await searchSeries(query, page);
                }

                const pages = data.totalPages ?? data.total_pages ?? 1;

                const items = data.results;

                if (!cancelled) {
                    setResults(items || []);
                    setTotalPages(pages)
                }
            } catch (err) {
                if (!cancelled) {
                    setError(err.message || "Erro ao buscar títulos.");
                    setResults([]);
                }
            } finally {
                if (!cancelled) {
                    setLoading(false);
                }
            }
        }

        loadResults();

        return () => {
            cancelled = true;
        };
    }, [query, type, page]);

    const cards = results.map(item => {
        const isMovie = type === "movie"
        const date = isMovie ? item.releaseDate : item.first_air_date

        return {
            id: item.id,
            title: isMovie ? item.title : item.name,
            posterPath: isMovie ? item.posterPath : item.poster_path,
            year: date ? date.slice(0, 4) : null,
            rating: isMovie ? item.voteAverage : item.vote_average,
            mediaType: type,

        }
    })

    function irParaPagina(novaPagina) {
        const params = new URLSearchParams(searchParams);
        params.set("page", String(novaPagina));
        setSearchParams(params);
    }

    return (
        <section className="text-white">
            <h1 className="text-2xl font-bold">
                Resultados para “{query}”
            </h1>

            {loading && <p className="mt-4 text-gray-400">Buscando...</p>}
            {error && <p className="mt-4 text-red-400">{error}</p>}

            {!loading && !error && query && results.length === 0 && (
                <p className="mt-4 text-gray-400">Nenhum título encontrado.</p>
            )}

            <div className="mt-6 grid grid-cols-2 gap-5 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6">
                {cards.map((card) => (
                    <article key={`${card.mediaType}-${card.id}`} className="min-w-0">
                        <NavLink
                            to={`/detalhes/${card.mediaType}/${card.id}`}
                            className="group block rounded-xl"
                        >
                            <div className="overflow-hidden rounded-xl bg-[#20232E]">
                                {card.posterPath ? (
                                    <img
                                        src={`${TMDB_IMAGE_URL}${card.posterPath}`}
                                        alt={`Cartaz de ${card.title}`}
                                        className="aspect-[2/3] w-full object-cover transition duration-300 group-hover:scale-105"
                                    />
                                ) : (
                                    <div className="flex aspect-[2/3] items-center justify-center text-gray-400">
                                        Cartaz indisponível
                                    </div>
                                )}
                            </div>

                            <h2 className="mt-3 truncate font-semibold text-white group-hover:text-[#A996FF]">
                                {card.title}
                            </h2>

                            <p className="mt-1 text-sm text-gray-400">
                                {typeof card.rating === "number"
                                    ? `★ ${card.rating.toFixed(1)}/10`
                                    : "Sem avaliação"}
                                {" · "}
                                {card.year || "Ano não informado"}
                            </p>
                        </NavLink>

                    </article>
                ))}


            </div>
            {results.length > 0 && (
                <div className="mt-8 flex items-center justify-center gap-4">
                    <button
                        type="button"
                        onClick={() => irParaPagina(page - 1)}
                        disabled={page <= 1}
                        className="rounded-lg bg-[#7657F6] px-4 py-2 text-white transition hover:bg-[#6545E8] disabled:cursor-not-allowed disabled:opacity-40"
                    >
                        Anterior
                    </button>

                    <span className="text-gray-300">
                        Página {page} de {totalPages}
                    </span>

                    <button
                        type="button"
                        onClick={() => irParaPagina(page + 1)}
                        disabled={page >= totalPages}
                        className="rounded-lg bg-[#7657F6] px-4 py-2 text-white transition hover:bg-[#6545E8] disabled:cursor-not-allowed disabled:opacity-40"
                    >
                        Próxima
                    </button>
                </div>
            )}
        </section>
    );
}