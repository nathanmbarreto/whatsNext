import { Gamepad2, Clapperboard, ExternalLink } from "lucide-react";

export default function Footer() {
  return (
    <footer className="mt-16 border-t border-white/10 bg-[#10121A]">
      <div className="mx-auto max-w-7xl px-4 py-10 sm:px-6 lg:px-8">
        {/* Identidade */}
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <a href="/" className="flex w-fit items-center gap-3">
            <div className="flex size-10 items-center justify-center rounded-xl bg-[#7657F6] text-white">
              <Clapperboard size={22} />
            </div>

            <div>
              <h2 className="text-lg font-extrabold text-white">
                What’s Next?
              </h2>
              <p className="text-xs text-[#A996FF]">
                Sua próxima aventura começa aqui.
              </p>
            </div>
          </a>

          <p className="max-w-md text-sm leading-6 text-gray-400">
            Um projeto independente para explorar filmes, séries e jogos.
          </p>
        </div>

        {/* Créditos */}
        <div className="mt-8 grid grid-cols-1 gap-8 sm:grid-cols-2">
          <div>
            <h3 className="mb-2 text-sm font-semibold text-white">
              Filmes e séries
            </h3>

            <p className="text-sm leading-6 text-gray-400">
              Dados e imagens de filmes e séries fornecidos por{" "}
              <a
                href="https://www.themoviedb.org/"
                target="_blank"
                rel="noreferrer"
                className="font-medium text-[#A996FF] hover:underline"
              >
                TMDB
                <ExternalLink className="ml-1 inline" size={12} />
              </a>
              .
            </p>

            <p className="mt-2 text-xs leading-5 text-gray-500">
              This product uses the TMDB API but is not endorsed or
              certified by TMDB.
            </p>

            <a
              href="https://www.themoviedb.org/about/logos-attribution"
              target="_blank"
              rel="noreferrer"
              className="mt-2 inline-block text-xs text-[#A996FF] hover:underline"
            >
              Logos e diretrizes oficiais da TMDB
            </a>
          </div>

          <div>
            <h3 className="mb-2 flex items-center gap-2 text-sm font-semibold text-white">
              <Gamepad2 size={16} className="text-[#A996FF]" />
              Jogos
            </h3>

            <p className="text-sm leading-6 text-gray-400">
              Dados de jogos fornecidos por{" "}
              <a
                href="https://rawg.io/"
                target="_blank"
                rel="noreferrer"
                className="font-medium text-[#A996FF] hover:underline"
              >
                RAWG
                <ExternalLink className="ml-1 inline" size={12} />
              </a>
              .
            </p>

            <p className="mt-2 text-xs leading-5 text-gray-500">
              Consulte a{" "}
              <a
                href="https://rawg.io/apidocs"
                target="_blank"
                rel="noreferrer"
                className="text-[#A996FF] hover:underline"
              >
                documentação da API RAWG
              </a>
              .
            </p>
          </div>
        </div>

        {/* Rodapé inferior */}
        <div className="mt-8 flex flex-col gap-2 border-t border-white/10 pt-5 text-xs text-gray-500 sm:flex-row sm:items-center sm:justify-between">
          <p>
            © {new Date().getFullYear()} What’s Next? Todos os direitos
            reservados ao projeto.
          </p>

          <p>Projeto independente, sem afiliação com TMDB ou RAWG.</p>
        </div>
      </div>
    </footer>
  );
}