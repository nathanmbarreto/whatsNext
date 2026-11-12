const BACKEND_URL =
  import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

export async function sair() {
  const csrfResponse = await fetch(`${BACKEND_URL}/api/csrf`, {
    credentials: "include",
  });

  if (!csrfResponse.ok) {
    throw new Error("Não foi possível obter o token de segurança.");
  }

  const csrf = await csrfResponse.json();

  const response = await fetch(`${BACKEND_URL}/logout`, {
    method: "POST",
    credentials: "include",
    headers: {
      [csrf.headerName]: csrf.token,
    },
  });

  if (!response.ok) {
    throw new Error("Não foi possível encerrar a sessão.");
  }
}

export async function buscarUsuarioAtual() {
  try {
    const response = await fetch(`${BACKEND_URL}/api/users/me`, {
      credentials: "include",
    });

    const contentType = response.headers.get("content-type") || "";

    if (!response.ok || !contentType.includes("application/json")) {
      return null;
    }

    return await response.json();
  } catch {
    return null;
  }
}