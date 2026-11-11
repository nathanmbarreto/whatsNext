import { useEffect, useState } from "react";
import { getWatchlist, atualizarStatus, removerItem } from "../../services/watchlist";

export default function Watchlist() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [status, setStatus] = useState("");
    const [actionError, setActionError] = useState("");
    const [pendingItemId, setPendingItemId] = useState(null);

    async function alterarStatus(item) {
        const novoStatus = item.status === "WATCHED" ? "PLANNED" : "WATCHED";

        setActionError("");
        setPendingItemId(item.watchListItemId);

        try {
            const atualizado = await atualizarStatus(
                item.watchListItemId,
                novoStatus
            );

            setItems((atuais) => {
                if (status && atualizado.status !== status) {
                    return atuais.filter(
                        (atual) => atual.watchListItemId !== item.watchListItemId
                    );
                }

                return atuais.map((atual) =>
                    atual.watchListItemId === item.watchListItemId ? atualizado : atual
                );
            });
        } catch (err) {
            setActionError(err.message || "Não foi possível alterar o status.");
        } finally {
            setPendingItemId(null);
        }
    }

    async function removerDaLista(itemId) {
        setActionError("");
        setPendingItemId(itemId);

        try {
            await removerItem(itemId);
            setItems((atuais) =>
                atuais.filter((item) => item.watchListItemId !== itemId)
            );
        } catch (err) {
            setActionError(err.message || "Não foi possível remover o item.");
        } finally {
            setPendingItemId(null);
        }
    }

    useEffect(() => {
        let cancelled = false;

        async function carregarLista() {
            setLoading(true);
            setError("");

            try {
                const data = await getWatchlist({
                    status: status || undefined,
                    page: 0,
                    size: 10,
                });

                if (!cancelled) {
                    setItems(data.content || []);
                }
            } catch (err) {
                if (!cancelled) {
                    setError(err.message || "Erro ao carregar sua lista.");
                }
            } finally {
                if (!cancelled) {
                    setLoading(false);
                }
            }
        }

        carregarLista();

        return () => {
            cancelled = true;
        };
    }, [status]);

    if (loading) {
        return <p className="text-gray-300">Carregando sua lista...</p>;
    }

    if (error) {
        return <p className="text-red-400">{error}</p>;
    }

    return (
        <section>
            <h1 className="mb-6 text-3xl font-bold text-white">Minha Lista</h1>
            <div className="mb-6 flex flex-wrap gap-2">
                {[
                    { label: "Todos", value: "" },
                    { label: "Planejados", value: "PLANNED" },
                    { label: "Assistidos", value: "WATCHED" },
                ].map((filtro) => (
                    <button
                        key={filtro.value || "all"}
                        type="button"
                        onClick={() => setStatus(filtro.value)}
                        className={`rounded-lg px-4 py-2 text-sm font-medium ${status === filtro.value
                            ? "bg-[#7657F6] text-white"
                            : "bg-[#20232E] text-gray-300 hover:text-white"
                            }`}
                    >
                        {filtro.label}
                    </button>
                ))}
            </div>

            {actionError && (
                <p role="alert" className="mb-4 text-sm text-red-400">
                    {actionError}
                </p>
            )}
            {items.length === 0 ? (
                <p className="text-gray-400">Sua lista ainda está vazia.</p>

            ) : (
                <div className="grid grid-cols-2 gap-5 sm:grid-cols-3 lg:grid-cols-4">
                    {items.map((item) => (
                        <article
                            key={item.watchListItemId}
                            className="overflow-hidden rounded-xl bg-[#20232E] text-white"
                        >
                            {item.posterUrl ? (
                                <img
                                    src={item.posterUrl}
                                    alt={`Cartaz de ${item.title}`}
                                    className="aspect-[2/3] w-full object-cover"
                                />
                            ) : (
                                <div className="flex aspect-[2/3] items-center justify-center text-gray-400">
                                    Cartaz indisponível
                                </div>
                            )}

                            <div className="p-4">
                                <h2 className="truncate font-semibold">{item.title}</h2>
                                <p className="mt-1 text-sm text-gray-400">
                                    {item.status === "WATCHED" ? "Assistido" : "Planejado"}
                                </p>
                            </div>
                            <div className="mt-4 flex flex-wrap gap-2">
                                <button
                                    type="button"
                                    disabled={pendingItemId === item.watchListItemId}
                                    onClick={() => alterarStatus(item)}
                                    className="rounded-lg bg-[#7657F6] px-3 py-2 text-sm text-white disabled:opacity-50"
                                >
                                    {item.status === "WATCHED"
                                        ? "Marcar como planejado"
                                        : "Marcar como assistido"}
                                </button>

                                <button
                                    type="button"
                                    disabled={pendingItemId === item.watchListItemId}
                                    onClick={() => removerDaLista(item.watchListItemId)}
                                    className="rounded-lg bg-white/10 px-3 py-2 text-sm text-white hover:bg-white/20 disabled:opacity-50"
                                >
                                    Remover
                                </button>
                            </div>
                        </article>
                    ))}
                </div>
            )}
        </section>
    );
}