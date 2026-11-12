const BASE_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";
const WATCH_LIST_URL = `${BASE_URL}/api/watchlist`

const MEDIA_BASE_URL = `${BASE_URL}/api/medias`;

export async function getWatchlist({ status, page = 0, size = 10 } = {}) {
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });

  if (status) {
    params.set("status", status);
  }

 const response = await fetch(`${WATCH_LIST_URL}?${params.toString()}`, {
  credentials: "include",
});

  const contentType = response.headers.get("content-type") || "";

  if (!response.ok || !contentType.includes("application/json")) {
    throw new Error("Não foi possível carregar sua lista. Confira se está logado.");
  }

  return response.json();
}

async function buscarTokenCsrf() {
  const response = await fetch(`${BASE_URL}/api/csrf`, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Não foi possível obter o token de segurança.");
  }

  return response.json();
}

async function enviarAlteracao(
  caminho,
  method,
  body,
  baseUrl = WATCH_LIST_URL
) {
  const csrf = await buscarTokenCsrf();

  const headers = {
    [csrf.headerName]: csrf.token,
  };

  const options = {
    method,
    credentials: "include",
    headers,
  };

  if (body !== undefined) {
    headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(body);
  }

  const response = await fetch(`${baseUrl}${caminho}`, options);

  if (!response.ok) {
    throw new Error(`Não foi possível concluir a operação (${response.status}).`);
  }

  if (method === "DELETE") {
    return;
  }

  return response.json();
}

export async function adicionarDoTmdb(tmdbId, type) {
  let caminho;

  if (type === "movie") {
    caminho = `/tmdb/${tmdbId}`;
  } else if (type === "series") {
    caminho = `/tmdb/series/${tmdbId}`;
  } else {
    throw new Error("Só é possível adicionar filmes ou séries.");
  }

  const mediaSalva = await enviarAlteracao(
    caminho,
    "POST",
    undefined,
    MEDIA_BASE_URL
  );

  return adicionarItem(mediaSalva.id);
}

export function atualizarStatus(itemId, status) {
  return enviarAlteracao(`/${itemId}/status`, "PATCH", { status });
}

export function removerItem(itemId) {
  return enviarAlteracao(`/${itemId}`, "DELETE");
}

export function adicionarItem(mediaId) {
  return enviarAlteracao(`/${mediaId}`, "POST");
}

export async function verificarNaWatchlist(tmdbId, type) {
  const params = new URLSearchParams({
    tmdbId: String(tmdbId),
    type,
  });

  const response = await fetch(
    `${WATCH_LIST_URL}/contains?${params.toString()}`,
    { credentials: "include" }
  );

  if (!response.ok) {
    throw new Error("Não foi possível verificar sua lista.");
  }

  // Se não houver sessão, o Spring pode redirecionar para uma página HTML de login.
  const contentType = response.headers.get("content-type") || "";
  if (!contentType.includes("application/json")) {
    return false;
  }

  return response.json(); // O backend responde true ou false.
}