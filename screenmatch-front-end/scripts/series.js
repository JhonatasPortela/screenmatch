import getDados from "./getDados.js";

const params = new URLSearchParams(window.location.search);
const serieId = params.get('id');
const listaTemporadas = document.getElementById('temporadas-select');
const fichaSerie = document.getElementById('temporadas-episodios');
const fichaDescricao = document.getElementById('ficha-descricao');

// Função para carregar temporadas
function carregarTemporadas() {
    getDados(`/series/${serieId}/temporadas/todas`)
        .then(data => {
            const temporadasUnicas = [...new Set(data.map(temporada => temporada.temporada))];
            listaTemporadas.innerHTML = ''; // Limpa as opções existentes

            const optionDefault = document.createElement('option');
            optionDefault.value = '';
            optionDefault.textContent = 'Selecione a temporada'
            listaTemporadas.appendChild(optionDefault); 
           
            temporadasUnicas.forEach(temporada => {
                const option = document.createElement('option');
                option.value = temporada;
                option.textContent = temporada;
                listaTemporadas.appendChild(option);
            });

            const optionTodos = document.createElement('option');
            optionTodos.value = 'todas';
            optionTodos.textContent = 'Todas as temporadas';
            listaTemporadas.appendChild(optionTodos);

            const optionTop5 = document.createElement('option');
            optionTop5.value = 'top';
            optionTop5.textContent = 'Top 5 episódios';
            listaTemporadas.appendChild(optionTop5);
        })
        .catch(error => {
            console.error('Erro ao obter temporadas:', error);
        });
}

function renderizarEpisodios(data, tituloSecao) {
    fichaSerie.innerHTML = '';

    const paragrafo = document.createElement('p');
    paragrafo.textContent = tituloSecao;
    fichaSerie.appendChild(paragrafo);
    fichaSerie.appendChild(document.createElement('br'));

    const ul = document.createElement('ul');
    ul.className = 'episodios-lista';
    ul.innerHTML = data.map(episodio => `
        <li>
            T${episodio.temporada} E${episodio.numeroEpisodio} - ${episodio.titulo}
        </li>
    `).join('');
    fichaSerie.appendChild(ul);
}

function renderizarEpisodiosPorTemporada(data) {
    const temporadasUnicas = [...new Set(data.map(episodio => episodio.temporada))];
    fichaSerie.innerHTML = '';

    temporadasUnicas.forEach(temporada => {
        const ul = document.createElement('ul');
        ul.className = 'episodios-lista';

        const episodiosTemporadaAtual = data.filter(episodio => episodio.temporada === temporada);

        ul.innerHTML = episodiosTemporadaAtual.map(episodio => `
            <li>
                ${episodio.numeroEpisodio} - ${episodio.titulo}
            </li>
        `).join('');

        const paragrafo = document.createElement('p');
        paragrafo.textContent = `Temporada ${temporada}`;
        fichaSerie.appendChild(paragrafo);
        fichaSerie.appendChild(document.createElement('br'));
        fichaSerie.appendChild(ul);
    });
}

// Função para carregar episódios de uma temporada (ou top 5)
function carregarEpisodios() {
    const valorSelecionado = listaTemporadas.value;

    if (!valorSelecionado) {
        fichaSerie.innerHTML = '';
        return;
    }

    const endpoint = valorSelecionado === 'top'
        ? `/series/${serieId}/episodios/top`
        : `/series/${serieId}/temporadas/${valorSelecionado}`;

    getDados(endpoint)
        .then(data => {
            if (!Array.isArray(data)) {
                console.error('Resposta inesperada ao obter episódios:', data);
                return;
            }

            if (valorSelecionado === 'top') {
                renderizarEpisodios(data, 'Top 5 episódios');
                return;
            }

            renderizarEpisodiosPorTemporada(data);
        })
        .catch(error => {
            console.error('Erro ao obter episódios:', error);
        });
}

// Função para carregar informações da série
function carregarInfoSerie() {
    getDados(`/series/${serieId}`)
        .then(data => {
            fichaDescricao.innerHTML = `
                <img src="${data.poster}" alt="${data.titulo}" />
                <div>
                    <h2>${data.titulo}</h2>
                    <div class="descricao-texto">
                        <p><b>Média de avaliações:</b> ${data.avaliacao}</p>
                        <p>${data.sinopse}</p>
                        <p><b>Estrelando:</b> ${data.atores}</p>
                    </div>
                </div>
            `;
        })
        .catch(error => {
            console.error('Erro ao obter informações da série:', error);
        });
}

// Adiciona ouvinte de evento para o elemento select
listaTemporadas.addEventListener('change', carregarEpisodios);

// Carrega as informações da série e as temporadas quando a página carrega
carregarInfoSerie();
carregarTemporadas();
