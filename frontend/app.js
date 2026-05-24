const API_URL = "http://localhost:8080/api";

// VARIÁVEIS DE ESTADO DO SISTEMA (Guardam os IDs na memória da página)
let currentResumeId = null;
let currentAnalysisId = null;

// Captura dos elementos do HTML
const uploadForm = document.getElementById("uploadForm");
const fileInput = document.getElementById("fileInput");
const btnAnalyze = document.getElementById("btnAnalyze");
const btnDownload = document.getElementById("btnDownload");
const insightOutput = document.getElementById("insightOutput");
const loadingSpinner = document.getElementById("loadingSpinner");
const btnLogout = document.getElementById("btnLogout");

// ==========================================
// 🔐 PASSO A: CAPTURA E GERENCIAMENTO DO TOKEN JWT
// ==========================================

// Quando o Google redireciona de volta para o app, o token costuma vir no parâmetro da URL (Query Parameter)
// Exemplo: http://localhost:5500/dashboard.html?token=eyJhbGci...
const urlParams = new URLSearchParams(window.location.search);
const tokenFromUrl = urlParams.get("token");

if (tokenFromUrl) {
    // Salva o token no navegador para usar nas próximas requisições
    localStorage.setItem("token", tokenFromUrl);
    // Limpa a URL para deixar o endereço bonito sem o token exposto
    window.history.replaceState({}, document.title, window.location.pathname);
}

// Função auxiliar obrigatória para injetar o Token no cabeçalho das requisições do Java
function getAuthHeaders() {
    const token = localStorage.getItem("token");
    if (!token) {
        // Se o usuário tentar acessar sem token, joga ele de volta para a tela de login
        window.location.href = "index.html";
        return {};
    }
    return {
        "Authorization": `Bearer ${token}`
    };
}

// ==========================================
// 🛠️ ENDPOINT 1: UPLOAD E EXTRAÇÃO DO PDF
// ==========================================
uploadForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const file = fileInput.files[0];
    if (!file) return;

    // Como estamos enviando um arquivo físico binário, precisamos usar o objeto FormData
    const formData = new FormData();
    formData.append("file", file);

    try {
        insightOutput.innerText = "Enviando arquivo e extraindo texto com o PDFBox...";

        const response = await fetch(`${API_URL}/resumes/upload`, {
            method: "POST",
            headers: getAuthHeaders(), // Injeta o cabeçalho Authorization: Bearer <TOKEN>
            body: formData // O FormData define o Content-Type como multipart/form-data automaticamente
        });

        if (!response.ok) throw new Error("Falha ao fazer upload do currículo.");

        const data = await response.json();
        currentResumeId = data.id; // Guarda o ID do currículo gerado pelo PostgreSQL

        insightOutput.innerText = `Sucesso! Texto extraído do arquivo: ${data.fileName}. Agora clique em 'Analisar com IA'.`;
        btnAnalyze.disabled = false; // Libera o botão do próximo passo (Análise)

    } catch (error) {
        insightOutput.innerText = `Erro: ${error.message}`;
    }
});

// ==========================================
// 🤖 ENDPOINT 2: DISPARAR ANÁLISE DO GEMINI
// ==========================================
btnAnalyze.addEventListener("click", async () => {
    if (!currentResumeId) return;

    try {
        btnAnalyze.disabled = true;
        loadingSpinner.classList.remove("hidden");
        insightOutput.innerText = "";

        const response = await fetch(`${API_URL}/analyses/resume/${currentResumeId}`, {
            method: "POST",
            headers: getAuthHeaders()
        });

        if (!response.ok) throw new Error("Erro ao processar análise com o Gemini.");

        const data = await response.json();
        currentAnalysisId = data.id; // Guarda o ID da análise criada no banco

        loadingSpinner.classList.add("hidden");
        // Mostra o relatório do Gemini na tela do usuário
        insightOutput.innerText = data.insights;

        btnDownload.disabled = false; // Libera o botão do último passo (Download do iText)

    } catch (error) {
        loadingSpinner.classList.add("hidden");
        insightOutput.innerText = `Erro na análise: ${error.message}`;
        btnAnalyze.disabled = false;
    }
});

// ==========================================
// 💾 ENDPOINT 3: DOWNLOAD DO NOVO PDF (iTEXT)
// ==========================================
btnDownload.addEventListener("click", async () => {
    if (!currentAnalysisId) return;

    try {
        const response = await fetch(`${API_URL}/generation/download/${currentAnalysisId}`, {
            method: "GET",
            headers: getAuthHeaders()
        });

        if (!response.ok) throw new Error("Erro ao baixar o arquivo gerado.");

        // CAPTURA DE ARQUIVO BINÁRIO (Blob)
        // Como a resposta do Java é um arquivo físico (array de bytes) e não um JSON, usamos .blob()
        const blob = await response.blob();

        // Cria uma URL temporária na memória do navegador para simular um link de download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `curriculo_otimizado_${currentAnalysisId}.pdf`;
        document.body.appendChild(a);
        a.click(); // Dispara o download automaticamente no Windows do usuário
        a.remove();

    } catch (error) {
        alert(`Erro ao baixar: ${error.message}`);
    }
});

// LOGOUT
btnLogout.addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "index.html";
});