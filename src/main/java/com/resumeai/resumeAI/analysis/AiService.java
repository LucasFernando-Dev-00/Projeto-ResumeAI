package com.resumeai.resumeAI.analysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String analyzeResumeText(String resumeText) {
        String systemPrompt = """
                Você é um recrutador técnico e especialista em RH sênior.
                            Analise o texto do currículo fornecido e retorne um relatório estruturado contendo:
                            1. Pontos Fortes: Competências e experiências de destaque.
                            2. Lacunas/Pontos Fracos: O que falta no currículo para vagas de tecnologia atuais ou o que pode ser melhorado.
                            3. Recomendações: Dicas práticas e acionáveis para melhorar o currículo.
                
                            Seja direto, profissional e construtivo.
                """;

        try {
            return this.chatClient.prompt()
                    .system(systemPrompt)
                    .user("Aqui está o texto do currículo:\n\n" + resumeText)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com a API de Inteligência Artificial: " + e.getMessage());
        }
    }
}
