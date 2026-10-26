import { useEffect, useState } from "react"
import { FaUser } from 'react-icons/fa';


function Comments({ type, id }) {

    const [textComment, setTextComment] = useState('')
    const [erro, setErro] = useState('')
    const [comments, setComments] = useState(() => {
        const dados = localStorage.getItem(`comments-${type}-${id}`)
        return dados ? JSON.parse(dados) : []
    })

    const handleChange = event => {
        setTextComment(event.target.value)
        if (erro) { setErro('') }
    }

    const handleSubmit = event => {
        event.preventDefault()

        if (textComment.trim().length < 1) {
            setErro('Comentário não pode ser vazio.')
            return
        }

        setComments(comments => {
            return [
                ...comments,
                textComment.trim()
            ]
        })
        setTextComment('')
    }

    const usuario = 'Visitante'


    useEffect(() => {
        localStorage.setItem(`comments-${type}-${id}`, JSON.stringify(comments))
    }, [comments, type, id])

    

    return (
        <section className="mt-8 rounded-2xl border border-white/10 text-white bg-[#181B24] p-6 sm:p-8">
            <h1 className="pb-3 text-xl font-bold">Comentários</h1>
            <form className="flex flex-col"
                onSubmit={handleSubmit}>
                <textarea placeholder="Adicione um comentário" className="px-4 py-3 bg-black/80 w-full min-h-28 border border-white/10 rounded-xl focus:border-[#7657F6] focus:outline-none"

                    value={textComment}
                    onChange={handleChange}
                />
                {erro && <p>{erro}</p>}
                <button type="submit" className="mt-2 self-end bg-[#7657F6] text-white rounded-xl p-2 hover:bg-[#7657F6]/80">Enviar comentário</button>
            </form>

            {comments.map((comentario, indice) => {
                return (
                    <div key={indice} className="bg-black/60 p-3 mt-5 rounded-xl">
                        <div className="flex p-2 gap-2">
                            <FaUser size={24} color="#7657F6" /><p className="text-[#7657F6]">{usuario}</p></div>
                        {comentario}</div>)

            })}
        </section>
    )
}

export default Comments