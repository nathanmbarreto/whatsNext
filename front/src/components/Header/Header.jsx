
import { useState } from "react";
import { NavLink } from "react-router-dom";

const links = [
  { label: "Início", to: "/", end: true },
  { label: "Filmes", to: "/movies" },
  { label: "Séries", to: "/series" },
  { label: "Jogos", to: "/games" },
  { label: "Login", to: "/login" },
];

export default function Header() {
  const [menuOpen, setMenuOpen] = useState(false);

  const linkStyles = ({ isActive }) =>
    `rounded-lg px-4 py-2 text-sm font-medium transition-colors duration-200 ${
      isActive
        ? "bg-[#7657F6] text-white"
        : "text-gray-300 hover:bg-white/5 hover:text-white"
    }`;

  return (
    <header className="sticky top-0 z-50 border-b border-white/10 bg-[#10121A]/95 backdrop-blur-md">
      <div className="mx-auto flex h-18 max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
        {/* Logo */}
        <NavLink
          to="/"
          aria-label="What's Next? - Início"
          className="shrink-0 text-xl font-extrabold tracking-tight text-white sm:text-2xl"
          onClick={() => setMenuOpen(true)}
        >
          What's <span className="text-[#7657F6]">Next?</span>
        </NavLink>

        {/* Menu desktop */}
        <nav
          aria-label="Navegação principal"
          className="hidden items-center gap-2 md:flex"
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
        </nav>

        {/* Botão mobile */}
        <button
          type="button"
          className="inline-flex items-center justify-center rounded-lg p-2 text-white transition hover:bg-white/10 focus-visible:outline-2 focus-visible:outline-[#7657F6] md:hidden"
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
          </div>
        </nav>
      )}
    </header>
  );
}
