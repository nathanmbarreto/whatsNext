import { useState } from "react";
import { Link } from "react-router-dom";

export default function Login() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [erro, setErro] = useState('')
    
  return (
    <main className="flex min-h-[calc(100vh-80px)] items-center justify-center bg-[#10121A] px-4 py-12">
      <form
        className="w-full max-w-md space-y-6 rounded-2xl border border-white/10 bg-[#20232E] p-8 shadow-2xl sm:p-10"
        
      >
        <div className="space-y-2 text-center">
          <h1 className="text-3xl font-bold tracking-tight text-white">
            Bem-vindo de volta
          </h1>

          <p className="text-sm text-gray-400">
            Entre na sua conta para continuar descobrindo o que vem por aí.
          </p>
        </div>

        <div className="space-y-2">
          <label
            htmlFor="email"
            className="block text-sm font-medium text-gray-200"
          >
            E-mail
          </label>

          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="seuemail@exemplo.com"
            autoComplete="email"
            required
            className="w-full rounded-xl border border-white/10 bg-[#10121A] px-4 py-3 text-white outline-none transition placeholder:text-gray-500 focus:border-[#7657F6] focus:ring-2 focus:ring-[#7657F6]/30"
          />
        </div>

        <div className="space-y-2">
          <label
            htmlFor="senha"
            className="block text-sm font-medium text-gray-200"
          >
            Senha
          </label>

          <input
            id="senha"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Digite sua senha"
            autoComplete="current-password"
            required
            className="w-full rounded-xl border border-white/10 bg-[#10121A] px-4 py-3 text-white outline-none transition placeholder:text-gray-500 focus:border-[#7657F6] focus:ring-2 focus:ring-[#7657F6]/30"
          />
        </div>

        {erro && (
          <p
            role="alert"
            className="rounded-lg border border-red-500/20 bg-red-500/10 px-4 py-3 text-sm text-red-400"
          >
            {erro}
          </p>
        )}

        <button
          type="submit"
          className="w-full rounded-xl bg-[#7657F6] px-4 py-3 font-semibold text-white transition hover:bg-[#6545E8] focus:outline-none focus:ring-2 focus:ring-[#7657F6] focus:ring-offset-2 focus:ring-offset-[#20232E] active:scale-[0.98]"
        >
          Entrar
        </button>

        <p className="text-center text-sm text-gray-400">
          Ainda não tem uma conta?{" "}
          <Link
            to="/cadastro"
            className="font-semibold text-[#A997FF] transition hover:text-white"
          >
            Cadastre-se
          </Link>
        </p>
      </form>
    </main>
  );
}