package com.attorney.service;

import com.attorney.model.Advogado;
import com.attorney.model.Avaliacao;
import com.attorney.model.Cliente;
import com.attorney.repository.AdvogadoRepository;
import com.attorney.repository.AvaliacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários da funcionalidade "Avaliação de Advogados"
 * (AdvogadoService.registrarAvaliacao e AdvogadoService.listarAvaliacoes).
 *
 * Os repositórios são substituídos por mocks (Mockito), isolando a regra de
 * negócio do banco de dados.
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@DisplayName("AdvogadoService - Avaliação de Advogados")
class AdvogadoServiceAvaliacaoTest {

    private static final Long ADVOGADO_ID = 1L;
    private static final Long ADVOGADO_INEXISTENTE_ID = 99L;
    private static final String COMENTARIO_VALIDO = "Atendimento claro e objetivo.";

    @Mock
    private AdvogadoRepository advogadoRepository;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private AdvogadoService advogadoService;

    private Advogado advogado;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        advogado = new Advogado("Dra. Camila Alencar", "SP412390", "advogado@attorney.com",
                "123456", "São Paulo/SP", "Direito do Trabalho");
        advogado.setId(ADVOGADO_ID);

        cliente = new Cliente("Carlos Eduardo Silva", "12345678900", "cliente@attorney.com",
                "123456", "São Paulo/SP");
        cliente.setId(10L);

        // lenient: nem todo cenário chega a consultar o advogado ou salvar a avaliação
        lenient().when(advogadoRepository.findById(ADVOGADO_ID)).thenReturn(Optional.of(advogado));
        lenient().when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    @DisplayName("CT01 - Registra avaliação válida (nota 8 e comentário)")
    void ct01_registraAvaliacaoValida() {
        when(avaliacaoRepository.calcularMediaNotaAdvogado(ADVOGADO_ID)).thenReturn(8.0);
        when(avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(ADVOGADO_ID))
                .thenReturn(List.of(new Avaliacao()));

        Avaliacao salva = advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 8, COMENTARIO_VALIDO);

        assertThat(salva.getNota()).isEqualTo(8);
        assertThat(salva.getComentario()).isEqualTo(COMENTARIO_VALIDO);
        assertThat(salva.getAdvogado()).isSameAs(advogado);
        assertThat(salva.getCliente()).isSameAs(cliente);
        assertThat(salva.getDataAvaliacao()).isNotNull();
        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("CT02 - Aceita a nota mínima permitida (0)")
    void ct02_aceitaNotaMinima() {
        Avaliacao salva = advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 0, COMENTARIO_VALIDO);

        assertThat(salva.getNota()).isZero();
        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("CT03 - Aceita a nota máxima permitida (10)")
    void ct03_aceitaNotaMaxima() {
        Avaliacao salva = advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 10, COMENTARIO_VALIDO);

        assertThat(salva.getNota()).isEqualTo(10);
        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("CT04 - Rejeita nota abaixo do mínimo (-1)")
    void ct04_rejeitaNotaAbaixoDoMinimo() {
        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, -1, COMENTARIO_VALIDO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A nota deve estar entre 0 e 10.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT05 - Rejeita nota acima do máximo (11)")
    void ct05_rejeitaNotaAcimaDoMaximo() {
        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 11, COMENTARIO_VALIDO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A nota deve estar entre 0 e 10.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT06 - Rejeita avaliação sem nota (campo obrigatório)")
    void ct06_rejeitaNotaNula() {
        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, null, COMENTARIO_VALIDO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A nota é obrigatória.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT07 - Rejeita comentário em branco (campo obrigatório)")
    void ct07_rejeitaComentarioEmBranco() {
        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 7, "   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O comentário é obrigatório.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT08 - Aceita comentário com o tamanho máximo (2000 caracteres)")
    void ct08_aceitaComentarioNoLimite() {
        String comentario2000 = "a".repeat(2000);

        Avaliacao salva = advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 9, comentario2000);

        assertThat(salva.getComentario()).hasSize(2000);
        verify(avaliacaoRepository).save(any(Avaliacao.class));
    }

    @Test
    @DisplayName("CT09 - Rejeita comentário acima do tamanho máximo (2001 caracteres)")
    void ct09_rejeitaComentarioAcimaDoLimite() {
        String comentario2001 = "a".repeat(2001);

        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 9, comentario2001))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O comentário deve ter no máximo 2000 caracteres.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT10 - Rejeita avaliação de advogado inexistente")
    void ct10_rejeitaAdvogadoInexistente() {
        when(advogadoRepository.findById(ADVOGADO_INEXISTENTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_INEXISTENTE_ID, cliente, 8, COMENTARIO_VALIDO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Advogado não encontrado.");

        verify(avaliacaoRepository, never()).save(any());
        verify(advogadoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT11 - Rejeita avaliação sem cliente vinculado")
    void ct11_rejeitaClienteNulo() {
        assertThatThrownBy(() -> advogadoService.registrarAvaliacao(ADVOGADO_ID, null, 8, COMENTARIO_VALIDO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O cliente é obrigatório para registrar uma avaliação.");

        verify(avaliacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("CT12 - Recalcula a nota média (1 casa decimal) e o total de avaliações")
    void ct12_recalculaMediaETotal() {
        // notas 10, 9 e 7 -> média 8,666... -> arredondada para 8,7
        when(avaliacaoRepository.calcularMediaNotaAdvogado(ADVOGADO_ID)).thenReturn(26.0 / 3.0);
        when(avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(ADVOGADO_ID))
                .thenReturn(List.of(new Avaliacao(), new Avaliacao(), new Avaliacao()));

        advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 7, COMENTARIO_VALIDO);

        ArgumentCaptor<Advogado> captor = ArgumentCaptor.forClass(Advogado.class);
        verify(advogadoRepository).save(captor.capture());
        assertThat(captor.getValue().getNotaMedia()).isEqualTo(8.7);
        assertThat(captor.getValue().getTotalAvaliacoes()).isEqualTo(3);
    }

    @Test
    @DisplayName("CT13 - Define média 0,0 quando o banco não retorna média (valor nulo)")
    void ct13_mediaNulaViraZero() {
        when(avaliacaoRepository.calcularMediaNotaAdvogado(ADVOGADO_ID)).thenReturn(null);
        when(avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(ADVOGADO_ID)).thenReturn(List.of());

        advogadoService.registrarAvaliacao(ADVOGADO_ID, cliente, 5, COMENTARIO_VALIDO);

        assertThat(advogado.getNotaMedia()).isEqualTo(0.0);
        assertThat(advogado.getTotalAvaliacoes()).isZero();
    }

    @Test
    @DisplayName("CT14 - Lista as avaliações do advogado, da mais recente para a mais antiga")
    void ct14_listaAvaliacoesDoAdvogado() {
        Avaliacao recente = new Avaliacao(advogado, cliente, 10, "Mais recente");
        Avaliacao antiga = new Avaliacao(advogado, cliente, 6, "Mais antiga");
        when(avaliacaoRepository.findByAdvogadoIdOrderByDataAvaliacaoDesc(ADVOGADO_ID))
                .thenReturn(List.of(recente, antiga));

        List<Avaliacao> resultado = advogadoService.listarAvaliacoes(ADVOGADO_ID);

        assertThat(resultado).containsExactly(recente, antiga);
        verify(avaliacaoRepository).findByAdvogadoIdOrderByDataAvaliacaoDesc(ADVOGADO_ID);
    }
}
