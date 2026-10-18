package com.attorney.service;

import com.attorney.model.*;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final GlossarioService glossarioService;
    private final AdvogadoRepository advogadoRepository;
    private final ClienteRepository clienteRepository;
    private final AdvogadoService advogadoService;
    private final MensagemContatoService mensagemContatoService;
    private final RpsService rpsService;

    public DataInitializer(GlossarioService glossarioService,
                           AdvogadoRepository advogadoRepository,
                           ClienteRepository clienteRepository,
                           AdvogadoService advogadoService,
                           MensagemContatoService mensagemContatoService,
                           RpsService rpsService) {
        this.glossarioService = glossarioService;
        this.advogadoRepository = advogadoRepository;
        this.clienteRepository = clienteRepository;
        this.advogadoService = advogadoService;
        this.mensagemContatoService = mensagemContatoService;
        this.rpsService = rpsService;
    }

    @Override
    public void run(String... args) {
        log.info("Inicializando carga de dados do Attorney App...");

        // 1. Carrega glossário jurídico
        glossarioService.carregarGlossarioInicialSeNecessario();

        // 2. Cria dados de teste se ainda não existirem
        if (clienteRepository.count() == 0 && advogadoRepository.count() == 0) {
            log.info("Criando usuários de demonstração (Clientes e Advogados)...");

            // Cliente Demo
            Cliente cliente1 = new Cliente(
                    "Carlos Eduardo Silva",
                    "123.456.789-00",
                    "cliente@attorney.com",
                    "123456",
                    "Av. Paulista, 1000 - São Paulo/SP"
            );
            cliente1.setFotoPerfilUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80");
            clienteRepository.save(cliente1);

            Cliente cliente2 = new Cliente(
                    "Mariana Souza Ramos",
                    "987.654.321-99",
                    "mariana@email.com",
                    "123456",
                    "Rua das Flores, 250 - Rio de Janeiro/RJ"
            );
            cliente2.setFotoPerfilUrl("https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80");
            clienteRepository.save(cliente2);

            // Advogados Demo
            Advogado adv1 = new Advogado(
                    "Dra. Camila Alencar",
                    "OAB/SP 412.390",
                    "advogado@attorney.com",
                    "123456",
                    "Alameda Santos, 800 - Cerqueira César, São Paulo/SP",
                    "Direito do Trabalho"
            );
            adv1.setBiografia("Especialista em Direito e Processo do Trabalho pela USP, com mais de 12 anos de atuação na defesa de direitos trabalhistas de empregados e consultoria preventiva corporativa.");
            adv1.setTelefone("(11) 98765-4321");
            adv1.setFotoPerfilUrl("https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=300&q=80");
            advogadoRepository.save(adv1);

            Advogado adv2 = new Advogado(
                    "Dr. Roberto Guimarães",
                    "OAB/RJ 198.420",
                    "roberto@attorney.com",
                    "123456",
                    "Av. Rio Branco, 156 - Centro, Rio de Janeiro/RJ",
                    "Direito Civil"
            );
            adv2.setBiografia("Atuação em contratos, responsabilidade civil, direitos possessórios e indenizações. Mestre em Direito Privado.");
            adv2.setTelefone("(21) 99123-4567");
            adv2.setFotoPerfilUrl("https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=300&q=80");
            advogadoRepository.save(adv2);

            Advogado adv3 = new Advogado(
                    "Dra. Beatriz Fontana",
                    "OAB/MG 305.112",
                    "beatriz@attorney.com",
                    "123456",
                    "Rua da Bahia, 1148 - Lourdes, Belo Horizonte/MG",
                    "Direito de Família"
            );
            adv3.setBiografia("Especializada em Direito de Família e Sucessões: divórcios humanizados, guarda compartilhada, pensão alimentícia e inventários extrajudiciais.");
            adv3.setTelefone("(31) 98833-2211");
            adv3.setFotoPerfilUrl("https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=300&q=80");
            advogadoRepository.save(adv3);

            Advogado adv4 = new Advogado(
                    "Dr. Marcelo Siqueira",
                    "OAB/SP 289.771",
                    "marcelo@attorney.com",
                    "123456",
                    "Rua Funchal, 418 - Vila Olímpia, São Paulo/SP",
                    "Direito do Consumidor"
            );
            adv4.setBiografia("Defesa rigorosa do consumidor contra práticas abusivas de companhias aéreas, bancos, planos de saúde e concessionárias.");
            adv4.setTelefone("(11) 97112-9988");
            adv4.setFotoPerfilUrl("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=300&q=80");
            advogadoRepository.save(adv4);

            Advogado adv5 = new Advogado(
                    "Dr. Fernando Nogueira",
                    "OAB/DF 154.982",
                    "fernando@attorney.com",
                    "123456",
                    "Setor Comercial Sul, Quadra 4 - Asa Sul, Brasília/DF",
                    "Direito Penal"
            );
            adv5.setBiografia("Criminalista com foco em Tribunal do Júri, crimes econômicos e acompanhamento em delegacias e audiências de custódia.");
            adv5.setTelefone("(61) 99877-1122");
            adv5.setFotoPerfilUrl("https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80");
            advogadoRepository.save(adv5);

            // Avaliações de exemplo
            advogadoService.registrarAvaliacao(adv1.getId(), cliente1, 10, "Excelente atendimento! A Dra. Camila foi muito clara em relação aos meus direitos e resolveu o processo rapidamente.");
            advogadoService.registrarAvaliacao(adv1.getId(), cliente2, 9, "Profissional extremamente competente e atenciosa. Recomendo muito!");

            advogadoService.registrarAvaliacao(adv2.getId(), cliente1, 10, "Grande conhecimento jurídico e presteza nos esclarecimentos.");
            advogadoService.registrarAvaliacao(adv3.getId(), cliente2, 10, "Conduziu meu divórcio com empatia e agilidade sem igual.");

            // Contato e Conversa Demo
            mensagemContatoService.enviarMensagem(cliente1.getId(), adv1.getId(), "Dúvida sobre verbas rescisórias", "Olá Dra. Camila, gostaria de tirar uma dúvida sobre cálculo de horas extras não pagas no meu desligamento.", TipoUsuario.CLIENTE);
            mensagemContatoService.enviarMensagem(cliente1.getId(), adv1.getId(), "Re: Dúvida sobre verbas rescisórias", "Olá Carlos, com certeza! Podemos agendar uma conversa ou você pode me enviar o extrato do seu FGTS e rescisão para análise preliminar.", TipoUsuario.ADVOGADO);

            // Relação de Prestação de Serviços (RPS) Demo
            rpsService.criarRps(cliente1.getId(), adv1.getId(), "Ação reclamatória trabalhista com pedido de horas extras e equiparação salarial.");

            log.info("Dados de demonstração criados com sucesso.");
        }
    }
}
