// const BASE_URL = "https://api.rawg.io/api";
// const API_KEY = import.meta.env.VITE_RAWG_API_KEY;

// async function rawgFetch(endpoint, params = {}) {
//   if (!API_KEY) {
//     throw new Error("A chave VITE_RAWG_API_KEY não foi configurada.");
//   }

//   const searchParams = new URLSearchParams({
//     key: API_KEY,
//     ...params,
//   });

//   const response = await fetch(
//     `${BASE_URL}${endpoint}?${searchParams.toString()}`
//   );

//   if (!response.ok) {
//     throw new Error(`Erro na RAWG: ${response.status}`);
//   }

//   return response.json();
// }

// // Mantém compatibilidade com outros componentes que usam getGames.
// export default async function getGames(page = 1, pageSize = 10) {
//   const data = await rawgFetch("/games", {
//     ordering: "-added",
//     page: String(page),
//     page_size: String(pageSize),
//     exclude_additions: "true",
//   });

//   return data;
// }

// // Busca lançamentos recentes ou futuros.
// export async function getImportantGames({
//   period = "upcoming",
//   page = 1,
//   pageSize = 10,
// } = {}) {
//   const today = new Date();

//   const formatDate = (date) => {
//     const year = date.getFullYear();
//     const month = String(date.getMonth() + 1).padStart(2, "0");
//     const day = String(date.getDate()).padStart(2, "0");

//     return `${year}-${month}-${day}`;
//   };

//   const start = new Date(today);
//   const end = new Date(today);

//   if (period === "upcoming") {
//     end.setMonth(end.getMonth() + 6);
//   } else {
//     start.setMonth(start.getMonth() - 3);
//   }

//   return rawgFetch("/games", {
//     dates: `${formatDate(start)},${formatDate(end)}`,
//     ordering: "-added",
//     page: String(page),
//     page_size: String(pageSize),
//     exclude_additions: "true",
//   });
// }