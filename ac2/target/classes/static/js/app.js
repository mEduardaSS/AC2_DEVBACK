
const API = "/api/projetos";

let projetos = [];

const tabela = document.getElementById("tabelaProjetos");
const alerta = document.getElementById("alerta");
const form = document.getElementById("formProjeto");
const filtro = document.getElementById("filtro");

// Converte a data de AAAA-MM-DD para DD/MM/AAAA.
function dataBR(valor) {
    if (!valor) return "—";

    const [ano, mes, dia] = valor.split("-");

    return `${dia}/${mes}/${ano}`;
}

// Mostra mensagens de sucesso ou erro.
function mostrarAlerta(mensagem, tipo = "success") {
    alerta.className = `alert alert-${tipo}`;
    alerta.textContent = mensagem;

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });

    setTimeout(() => {
        alerta.classList.add("d-none");
    }, 5000);
}

// Função reutilizável para chamar a API.
async function requisicao(url, opcoes = {}) {
    const resposta = await fetch(url, {
        ...opcoes,
        headers: {
            "Content-Type": "application/json",
            ...(opcoes.headers || {})
        }
    });

    if (!resposta.ok) {
        let mensagem = `Erro HTTP ${resposta.status}`;

        try {
            const corpo = await resposta.json();

            mensagem =
                corpo.erro ||
                corpo.message ||
                mensagem;
        } catch (_) {
            // Mantém a mensagem HTTP padrão.
        }

        throw new Error(mensagem);
    }

    if (resposta.status === 204) {
        return null;
    }

    return resposta.json();
}

// Busca os projetos cadastrados no backend.
async function carregarProjetos() {
    try {
        projetos = await requisicao(API);

        renderizarProjetos();
    } catch (erro) {
        tabela.innerHTML = `
            <tr>
                <td colspan="5"
                    class="text-center text-danger py-4">
                    ${escapar(erro.message)}.
                    Verifique se o Spring Boot está rodando.
                </td>
            </tr>
        `;
    }
}

// Renderiza a tabela de projetos.
function renderizarProjetos() {
    const termo = filtro.value
        .trim()
        .toLocaleLowerCase("pt-BR");

    const lista = projetos.filter(projeto =>
        (projeto.descricao || "")
            .toLocaleLowerCase("pt-BR")
            .includes(termo)
    );

    document.getElementById("totalProjetos").textContent =
        projetos.length;

    document.getElementById("vazio")
        .classList.toggle("d-none", lista.length > 0);

    if (lista.length === 0) {
        tabela.innerHTML = `
            <tr>
                <td colspan="5"
                    class="text-center text-secondary py-4">
                    ${
                        projetos.length
                            ? "Nenhum resultado para esse filtro."
                            : "Nenhum projeto cadastrado ainda."
                    }
                </td>
            </tr>
        `;

        return;
    }

    tabela.innerHTML = lista.map(projeto => `
        <tr>
            <td class="text-secondary">
                #${projeto.id}
            </td>

            <td class="fw-semibold">
                ${escapar(projeto.descricao)}
            </td>

            <td>${dataBR(projeto.dataInicio)}</td>

            <td>${dataBR(projeto.dataFim)}</td>

            <td class="text-end">
                <button
                    class="btn btn-sm btn-outline-secondary"
                    onclick="verDetalhes(${projeto.id})">
                    Detalhes
                </button>
            </td>
        </tr>
    `).join("");
}

// Consulta um projeto específico e exibe seus detalhes.
async function verDetalhes(id) {
    try {
        const projeto = await requisicao(`${API}/${id}`);

        document.getElementById("detalheConteudo").innerHTML = `
            <div class="mb-3">
                <span class="eyebrow">
                    PROJETO #${projeto.id}
                </span>

                <h3 class="h5 fw-bold mt-1">
                    ${escapar(projeto.descricao)}
                </h3>
            </div>

            <div class="row g-3">
                <div class="col-6">
                    <div class="text-secondary small">Início</div>
                    <strong>${dataBR(projeto.dataInicio)}</strong>
                </div>

                <div class="col-6">
                    <div class="text-secondary small">Fim</div>
                    <strong>${dataBR(projeto.dataFim)}</strong>
                </div>
            </div>
        `;

        bootstrap.Modal
            .getOrCreateInstance(
                document.getElementById("detalheModal")
            )
            .show();

    } catch (erro) {
        mostrarAlerta(erro.message, "danger");
    }
}

// Permite que o botão da tabela acesse a função.
window.verDetalhes = verDetalhes;

// Cadastra um projeto ao enviar o formulário.
form.addEventListener("submit", async event => {
    event.preventDefault();

    const descricao = document
        .getElementById("descricao")
        .value.trim();

    const dataInicio =
        document.getElementById("dataInicio").value;

    const dataFim =
        document.getElementById("dataFim").value;

    const erroEl =
        document.getElementById("erroFormulario");

    erroEl.classList.add("d-none");

    if (!descricao) {
        erroEl.textContent =
            "A descrição é obrigatória.";

        erroEl.classList.remove("d-none");

        return;
    }

    if (dataFim < dataInicio) {
        erroEl.textContent =
            "A data de fim não pode ser anterior à data de início.";

        erroEl.classList.remove("d-none");

        return;
    }

    const botao = document.getElementById("salvarBtn");

    botao.disabled = true;
    botao.textContent = "Salvando...";

    try {
        await requisicao(API, {
            method: "POST",
            body: JSON.stringify({
                descricao,
                dataInicio,
                dataFim
            })
        });

        bootstrap.Modal
            .getOrCreateInstance(
                document.getElementById("modalProjeto")
            )
            .hide();

        form.reset();

        await carregarProjetos();

        mostrarAlerta(
            "Projeto cadastrado com sucesso!"
        );

    } catch (erro) {
        erroEl.textContent = erro.message;
        erroEl.classList.remove("d-none");

    } finally {
        botao.disabled = false;
        botao.textContent = "Salvar projeto";
    }
});

// Filtra a tabela enquanto o usuário digita.
filtro.addEventListener("input", renderizarProjetos);

// Evita interpretar texto recebido como HTML.
function escapar(valor) {
    return String(valor ?? "").replace(
        /[&<>"']/g,
        caractere => ({
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#39;"
        })[caractere]
    );
}

// Carrega os dados assim que a página abre.
carregarProjetos();
