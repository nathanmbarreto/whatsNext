
import { useState, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { sair } from "../../services/auth";

const BACKEND_URL = import.meta.env.VITE_BACKEND_URL || "http://localhost:8080";

const links = [
  { label: "Início", to: "/", end: true },
  { label: "Filmes", to: "/movies" },
  { label: "Séries", to: "/series" },
];


function UserDropdown({ user, onLogout, onNavigate }) {
  const [open, setOpen] = useState(false);

  function fecharMenu() {
    setOpen(false);
    onNavigate?.();
  }

  return (
    <div className="relative">
      <button
        type="button"
        aria-haspopup="menu"
        aria-expanded={open}
        onClick={() => setOpen((atual) => !atual)}
        className="rounded-lg px-4 py-2 text-sm font-medium text-white hover:bg-white/5"
      >
        Olá, {user.name} <span aria-hidden="true">⌄</span>
      </button>

      {open && (
        <div
          role="menu"
          className="absolute right-0 top-full z-50 mt-2 w-48 rounded-xl border border-white/10 bg-[#20232E] p-2 shadow-xl"
        >
          <NavLink
            to="/watchlist"
            role="menuitem"
            onClick={fecharMenu}
            className="block rounded-lg px-3 py-2 text-sm text-gray-200 hover:bg-white/10"
          >
            Minha Lista
          </NavLink>

          <button
            type="button"
            role="menuitem"
            onClick={() => {
              fecharMenu();
              onLogout();
            }}
            className="w-full rounded-lg px-3 py-2 text-left text-sm text-gray-200 hover:bg-white/10"
          >
            Sair
          </button>
        </div>
      )}
    </div>
  );
}

export default function Header() {
  const [menuOpen, setMenuOpen] = useState(false);
  const [user, setUser] = useState(null);
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [searchType, setSearchType] = useState("movie");



  function handleSearch(event) {
    event.preventDefault();

    const query = searchQuery.trim();
    if (!query) return;

    const params = new URLSearchParams({
      type: searchType,
      query,
    });

    navigate(`/search?${params.toString()}`);
    setMenuOpen(false);
  }

  async function handleLogout() {
    try {
      await sair();
      setUser(null);
      setMenuOpen(false);
      navigate("/login", { replace: true });
    } catch (err) {
      console.error("Erro ao sair:", err);
    }
  }

  useEffect(() => {
    async function carregarUsuario() {
      try {
        const response = await fetch(
          `${BACKEND_URL}/api/users/me`,
          { credentials: "include" }
        );

        const contentType = response.headers.get("content-type") || "";

        if (!response.ok || !contentType.includes("application/json")) {
          setUser(null);
          return;
        }

        setUser(await response.json());
      } catch {
        setUser(null);
      }
    }

    carregarUsuario();
  }, []);


  const linkStyles = ({ isActive }) =>
    `rounded-lg px-4 py-2 text-sm font-medium transition-colors duration-200 ${isActive
      ? "bg-[#7657F6] text-white"
      : "text-gray-300 hover:bg-white/5 hover:text-white"
    }`;

  return (
    <header className="sticky top-0 z-50 border-b border-white/10 bg-[#10121A]/95 backdrop-blur-md">
      <div className="mx-auto grid grid-cols-[minmax(0,1fr)_auto] items-center gap-x-3 px-4 py-3 sm:px-6 lg:min-h-[72px] lg:max-w-7xl lg:grid-cols-[auto_minmax(0,1fr)_auto] lg:gap-x-6 lg:px-8 lg:py-0">
        <NavLink
          to="/"
          aria-label="What's Next? - Início"
          className="col-start-1 row-start-1 shrink-0 text-xl font-extrabold tracking-tight text-white sm:text-2xl"
          onClick={() => setMenuOpen(false)}
        >
          What's <span className="text-[#7657F6]">Next?</span>
        </NavLink>

        <form
          onSubmit={handleSearch}
          className="col-span-2 row-start-2 flex min-w-0 w-full gap-2 border-t border-white/10 pt-3 lg:col-span-1 lg:col-start-2 lg:row-start-1 lg:mx-auto lg:max-w-2xl lg:border-t-0 lg:pt-0"
        >
          <select
            value={searchType}
            onChange={(event) => setSearchType(event.target.value)}
            aria-label="Tipo de conteúdo"
            className="w-24 shrink-0 rounded-lg bg-[#20232E] px-2 py-2 text-white sm:w-auto sm:px-3"
          >
            <option value="movie">Filmes</option>
            <option value="series">Séries</option>
          </select>

          <input
            type="search"
            value={searchQuery}
            onChange={(event) => setSearchQuery(event.target.value)}
            placeholder="Buscar título..."
            aria-label="Buscar título"
            className="min-w-0 flex-1 rounded-lg bg-[#20232E] px-3 py-2 text-white"
          />

          <button
            type="submit"
            className="shrink-0 rounded-lg bg-[#7657F6] px-3 py-2 font-medium text-white sm:px-4"
          >
            Buscar
          </button>
        </form>

        <nav
          aria-label="Navegação principal"
          className="hidden items-center gap-2 md:col-start-2 md:row-start-1 md:flex lg:col-start-3"
        >
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.end}
              className={linkStyles}
            >
              {link.label}
            </NavLink>
          ))}
          {user ? (
            <UserDropdown
              user={user}
              onLogout={handleLogout}
              onNavigate={() => setMenuOpen(false)}
            />
          ) : (
            <NavLink
              to="/login"
              className={linkStyles}
              onClick={() => setMenuOpen(false)}
            >
              Login
            </NavLink>
          )}
        </nav>

        <button
          type="button"
          className="col-start-2 row-start-1 inline-flex items-center justify-center rounded-lg p-2 text-white transition hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-[#7657F6] md:hidden"
          aria-label={menuOpen ? "Fechar menu" : "Abrir menu"}
          aria-expanded={menuOpen}
          aria-controls="mobile-menu"
          onClick={() => setMenuOpen(!menuOpen)}
        >
          {menuOpen ? (
            <span className="text-2xl leading-none">×</span>
          ) : (
            <span className="text-2xl leading-none">☰</span>
          )}
        </button>
      </div>

      {/* Menu mobile */}
      {menuOpen && (
        <nav
          id="mobile-menu"
          aria-label="Navegação mobile"
          className="border-t border-white/10 px-4 py-3 md:hidden"
        >
          <div className="mx-auto flex max-w-7xl flex-col gap-2">
            {links.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                end={link.end}
                className={linkStyles}
                onClick={() => setMenuOpen(false)}
              >
                {link.label}
              </NavLink>
            ))}
            {user ? (
              <UserDropdown
                user={user}
                onLogout={handleLogout}
                onNavigate={() => setMenuOpen(false)}
              />
            ) : (
              <NavLink
                to="/login"
                className={linkStyles}
                onClick={() => setMenuOpen(false)}
              >
                Login
              </NavLink>
            )}
          </div>
        </nav>
      )}
    </header>
  );
}
