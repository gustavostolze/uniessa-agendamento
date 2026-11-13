import { useRef, useState } from 'react'
import type { FormEvent } from 'react'
import { criarAgendamento, listarAgendamentos } from './services/AgendamentoService'
import type { Agendamento } from './services/AgendamentoService'
import './App.css'

function App() {
  const [formulario, setFormulario] = useState(false)
  const [lista, setLista] = useState<Agendamento[] | null>(null)
  const [ocupado, setOcupado] = useState(false)
  const lock = useRef(false)
  const [mensagem, setMensagem] = useState('')
  const [erro, setErro] = useState('')
  async function executar(acao: () => Promise<void>) {
    if (lock.current) return
    lock.current = true; setOcupado(true); setErro(''); setMensagem('')
    try { await acao() } catch (e) { setErro(e instanceof Error ? e.message : 'Falha inesperada.') }
    finally { lock.current = false; setOcupado(false) }
  }
  function salvar(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const form = event.currentTarget
    const dados = new FormData(form)
    const data = String(dados.get('data'))
    const hora = String(dados.get('hora'))
    void executar(async () => {
      const criado = await criarAgendamento({ data, horario: `${data}T${hora}:00`,
        clienteId: Number(dados.get('cliente')), profissionalId: Number(dados.get('profissional')) })
      setMensagem(`Agendamento ${criado.id} criado com sucesso.`)
      form.reset()
      if (lista !== null) {
        try { setLista(await listarAgendamentos()) }
        catch { setErro('Agendamento criado. A lista anterior não pôde ser atualizada; recarregue a consulta.') }
      }
    })
  }
  return <main aria-busy={ocupado}>
    <h1>Agendamentos</h1>
    <p>Cadastre um horário ou consulte a agenda do estabelecimento.</p>
    <nav aria-label="Ações de agendamento">
      <button disabled={ocupado} onClick={() => setFormulario(true)}>Criar agendamento</button>
      <button disabled={ocupado} onClick={() => void executar(async () => { setLista(await listarAgendamentos()) })}>Listar/atualizar agendamentos</button>
    </nav>
    {ocupado && <p role="status">Aguarde…</p>}
    {mensagem && <p role="status">{mensagem}</p>}
    {erro && <p role="alert" className="erro">{erro} {lista !== null && 'A lista exibida pode estar desatualizada.'}</p>}
    {formulario && <section aria-labelledby="cadastro"><h2 id="cadastro">Novo agendamento</h2>
      <p>Informe IDs de cliente e profissional já cadastrados. Horário local do estabelecimento (São Paulo).</p>
      <form onSubmit={salvar}><fieldset disabled={ocupado}>
        <label>Data<input name="data" type="date" required /></label>
        <label>Horário<input name="hora" type="time" required /></label>
        <label>ID do cliente<input name="cliente" type="number" min="1" step="1" required /></label>
        <label>ID do profissional<input name="profissional" type="number" min="1" step="1" required /></label>
        <button type="submit">Confirmar criação</button>
      </fieldset></form>
    </section>}
    {lista !== null && <section aria-labelledby="consulta"><h2 id="consulta">Agenda consultada</h2>
      {lista.length === 0 ? <p>Nenhum agendamento encontrado.</p> : <div className="tabela"><table>
        <thead><tr>{['ID', 'Data', 'Horário', 'Status', 'Cliente', 'Profissional'].map(t => <th key={t} scope="col">{t}</th>)}</tr></thead>
        <tbody>{lista.map(a => <tr key={a.id}><td>{a.id}</td><td>{a.data.split('-').reverse().join('/')}</td><td>{a.horario.slice(11, 16)}</td><td>{a.status}</td><td>{a.clienteId}</td><td>{a.profissionalId}</td></tr>)}</tbody>
      </table></div>}
    </section>}
  </main>
}
export default App
