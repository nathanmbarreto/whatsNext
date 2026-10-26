import { Link } from "react-router-dom";
import { useState } from "react";

export default function Cadastro() {

    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [erro, setErro] = useState('')

    return (
        <main className="flex min-h-[calc(100vh-80px)] items-center justify-center bg-[#10121A] px-4 py-12">
            <form
                onSubmit={(e) => e.preventDefault()}
                className="w-full max-w-md space-y-5 rounded-2xl border border-white/10 bg-[#20232E] p-8 shadow-2xl sm:p-10"
            >
                <div className="space-y-2 text-center">
                    <h1 className="text-3xl font-bold tracking-tight text-white">
                        Crie sua conta
                    </h1>

                    <p className="text-sm text-gray-400">
                        Entre para descobrir seu próximo filme, série ou jogo favorito.
                    </p>
                </div>

                <div className="space-y-2">
                    <label
                        htmlFor="username"
                        className="block text-sm font-medium text-gray-200"
                    >
                        Nome de usuário
                    </label>

                    <input
                        id="username"
                        name="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Escolha um username"
                        autoComplete="username"
                        minLength={3}
                        maxLength={50}
                        required
                        className="w-full rounded-xl border border-white/10 bg-[#10121A] px-4 py-3 text-white outline-none transition placeholder:text-gray-500 focus:border-[#7657F6] focus:ring-2 focus:ring-[#7657F6]/30"
                    />

                    <p className="text-xs text-gray-500">
                        Esse será o nome exibido para identificar você no site.
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
                        name="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="seuemail@exemplo.com"
                        autoComplete="email"
                        maxLength={254}
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
                        name="senha"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Crie uma senha segura"
                        autoComplete="new-password"
                        minLength={8}
                        required
                        className="w-full rounded-xl border border-white/10 bg-[#10121A] px-4 py-3 text-white outline-none transition placeholder:text-gray-500 focus:border-[#7657F6] focus:ring-2 focus:ring-[#7657F6]/30"
                    />

                    <p className="text-xs text-gray-500">
                        Use pelo menos 8 caracteres.
                    </p>
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
                    Criar conta
                </button>

                <p className="text-center text-sm text-gray-400">
                    Já tem uma conta?{" "}
                    <Link
                        to="/login"
                        className="font-semibold text-[#A997FF] transition hover:text-white"
                    >
                        Entrar
                    </Link>
                </p>
            </form>
        </main>
    );
}