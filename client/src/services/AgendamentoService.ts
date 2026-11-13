export type CriarAgendamento = { data: string; horario: string; clienteId: number; profissionalId: number }
export type Agendamento = CriarAgendamento & { id: number; status: string }

const statuses = ['PENDENTE', 'CONFIRMADO', 'CANCELADO', 'CONCLUIDO']

function registro(value: unknown): value is Agendamento {

  if (!value || typeof value !== 'object') return false
  
  const a = value as Agendamento
  return Number.isInteger(a.id) && a.id > 0 && typeof a.data === 'string' &&
    typeof a.horario === 'string' && statuses.includes(a.status) &&
    Number.isInteger(a.clienteId) && Number.isInteger(a.profissionalId)
}

async function request(method: 'GET' | 'POST', dados?: CriarAgendamento): Promise<unknown> {
  const base = import.meta.env.VITE_API_BASE_URL
  if (!base) throw new Error('URL da API não configurada neste ambiente.')
  const controller = new AbortController()
  const timer = setTimeout(() => controller.abort(), 10000)
  try {
    const response = await fetch(`${base.replace(/\/$/, '')}/agendamento`, {
      method, signal: controller.signal, credentials: 'omit',
      headers: dados ? { 'Content-Type': 'application/json' } : undefined,
      body: dados ? JSON.stringify(dados) : undefined,
    })
    const body: unknown = await response.json().catch(() => null)
    if (!response.ok) {
      const message = body && typeof body === 'object' && 'mensagem' in body ? body.mensagem : null
      throw new Error(typeof message === 'string' ? message : `Falha na API (HTTP ${response.status}).`)
    }
    return body
  } catch (error) {
    if (controller.signal.aborted) throw new Error('Tempo de espera excedido. Confira a lista antes de tentar criar novamente.', { cause: error })
    if (error instanceof TypeError) throw new Error('Não foi possível acessar a API. Verifique a conexão e a configuração de CORS.', { cause: error })
    throw error
  } finally { clearTimeout(timer) }
}
export async function listarAgendamentos(): Promise<Agendamento[]> {
  const body = await request('GET')
  if (!Array.isArray(body) || !body.every(registro)) throw new Error('Resposta inesperada da API.')
  return body
}
export async function criarAgendamento(dados: CriarAgendamento): Promise<Agendamento> {
  const body = await request('POST', dados)
  if (!registro(body)) throw new Error('Resposta inesperada. Confira a lista antes de reenviar.')
  return body
}
