import { NavLink } from "react-router-dom";
import { useEffect, useState } from "react";
import { Star, Gamepad2, ArrowUpRight } from "lucide-react";
import { getImportantGames } from "../../services/rawg";

export default function GamesGrid({
    page = 1,
    pageSize = 10,
    period = "upcoming",
}) {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let cancelled = false;

        async function loadItems() {
            try {
                setLoading(true);
                setError("");

                const data = await getImportantGames({
                    period,
                    page,
                    pageSize,
                });

                if (!cancelled) {
                    setItems(data.results ?? []);
                }
            } catch (err) {
                console.error("Erro ao carregar jogos:", err);

                if (!cancelled) {
                    setError("Não foi possível carregar os jogos.");
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
    }, [page, pageSize, period]);

    return (
        <section className="mt-10 sm:mt-14">
            {/* Cabeçalho */}
            <div className="mb-6 flex items-center justify-between gap-4">
                <div>
                    <p className="mb-2 text-xs font-bold uppercase tracking-[0.2em] text-[#A996FF]">
                        Explore o catálogo
                    </p>

                    <h2 className="text-2xl font-extrabold tracking-tight text-white sm:text-3xl">
                        {period === "upcoming"
                            ? "Próximos lançamentos"
                            : "Jogos recentes em destaque"}
                    </h2>

                    <p className="mt-2 text-sm text-gray-400">
                        Descubra sua próxima aventura.
                    </p>
                </div>

                <div className="flex size-12 shrink-0 items-center justify-center rounded-2xl border border-[#7657F6]/30 bg-[#7657F6]/15 text-[#A996FF]">
                    <Gamepad2 size={24} />
                </div>
            </div>

            {/* Carregamento */}
            {loading && (
                <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5">
                    {Array.from({ length: pageSize }).map((_, index) => (
                        <div key={index} className="animate-pulse">
                            <div className="aspect-[4/3] rounded-xl bg-[#20232E]" />
                            <div className="mt-3 h-4 w-3/4 rounded bg-[#20232E]" />
                            <div className="mt-2 h-3 w-1/3 rounded bg-[#20232E]" />
                        </div>
                    ))}
                </div>
            )}

            {/* Erro */}
            {!loading && error && (
                <div className="rounded-2xl border border-red-400/20 bg-[#181B24] p-6">
                    <p className="font-semibold text-white">
                        Ops! Algo deu errado.
                    </p>

                    <p className="mt-2 text-sm text-gray-400">{error}</p>

                    <button
                        type="button"
                        onClick={() => {
                            setError("");
                            setLoading(true);
                            setItems([]);
                            // O efeito será executado novamente quando a página mudar.
                            // Para tentar novamente sem mudar a página, use o botão
                            // de recarregar indicado na versão aprimorada abaixo.
                        }}
                        className="mt-4 rounded-lg bg-[#7657F6] px-4 py-2 text-sm font-semibold text-white hover:bg-[#6545E8]"
                    >
                        Tentar novamente
                    </button>
                </div>
            )}

            {/* Catálogo */}
            {!loading && !error && items.length > 0 && (
                <div className="grid grid-cols-2 gap-x-4 gap-y-7 sm:grid-cols-3 sm:gap-x-5 lg:grid-cols-4 xl:grid-cols-5">
                    {items.map((item) => (
                        <NavLink
                            key={item.id}
                            to={`/detalhes/game/${item.id}`}
                            className="group min-w-0 rounded-xl focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[#7657F6]"
                        >
                            {/* Imagem */}
                            <div className="relative overflow-hidden rounded-xl border border-white/5 bg-[#20232E]">
                                {item.background_image ? (
                                    <img
                                        src={item.background_image.replace(
                                            "/media/",
                                            "/media/crop/600/400/"
                                        )}
                                        alt={`Capa de ${item.name}`}
                                        loading="lazy"
                                        className="aspect-[4/3] w-full object-cover transition duration-500 ease-out group-hover:scale-105"
                                    />
                                ) : (
                                    <div className="flex aspect-[4/3] items-center justify-center text-gray-500">
                                        <Gamepad2 size={36} />
                                    </div>
                                )}

                                <div className="pointer-events-none absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent opacity-70 transition group-hover:opacity-100" />

                                <div className="absolute right-3 top-3 flex size-8 translate-y-1 items-center justify-center rounded-lg bg-[#7657F6] text-white opacity-0 shadow-lg transition duration-200 group-hover:translate-y-0 group-hover:opacity-100">
                                    <ArrowUpRight size={17} />
                                </div>
                            </div>

                            {/* Informações */}
                            <div className="px-0.5 pt-3">
                                <h3 className="truncate font-semibold text-gray-100 transition-colors group-hover:text-[#A996FF]">
                                    {item.name}
                                </h3>

                                <div className="mt-2 flex flex-wrap items-center gap-x-2 gap-y-1 text-sm">
                                    {period !== "upcoming" && (
                                        <span className="inline-flex items-center gap-1.5 font-medium text-gray-200">
                                            <Star
                                                size={15}
                                                className="fill-[#FFD166] text-[#FFD166]"
                                            />
                                            {item.rating != null
                                                ? `${item.rating}/5`
                                                : "Sem nota"}
                                        </span>
                                    )}
                                </div>
                            </div>
                        </NavLink>
                    ))}
                </div>
            )}

            {/* Lista vazia */}
            {!loading && !error && items.length === 0 && (
                <p className="rounded-xl bg-[#181B24] p-6 text-gray-400">
                    Nenhum jogo encontrado neste período.
                </p>
            )}
        </section>
    );
}