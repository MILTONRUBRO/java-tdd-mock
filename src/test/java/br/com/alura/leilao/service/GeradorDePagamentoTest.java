package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;

class GeradorDePagamentoTest {
	
	@Mock
	private PagamentoDao pagamentoDao;
	
	private GeradorDePagamento geradorDePagamento;
	
	@Captor
	private ArgumentCaptor<Pagamento> captor;
	
	@Mock
	private Clock clock;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDao, clock);
	}
	

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance lanceVencedor = leilao.getLanceVencedor();
		
		LocalDate data = LocalDate.of(2021, 1, 23);
		Instant instant = data.atStartOfDay(ZoneId.systemDefault()).toInstant();
		Mockito.when(clock.instant()).thenReturn(instant);
		Mockito.when(clock.getZone()).thenReturn(ZoneId.systemDefault());

		geradorDePagamento.gerarPagamento(lanceVencedor);
		Mockito.verify(pagamentoDao).salvar(captor.capture());
	
		Pagamento pagamento = captor.getValue();
		
		Assert.assertEquals(lanceVencedor.getValor(), pagamento.getValor());
		Assert.assertFalse(pagamento.getPago());
		Assert.assertEquals(lanceVencedor.getUsuario(), pagamento.getUsuario());
		Assert.assertEquals(leilao, pagamento.getLeilao());
	}
	
	private Leilao leilao(){
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Ragnar"));
		Lance primeiro = new Lance(new Usuario("Lagertha"), new BigDecimal("600"));
		
		leilao.propoe(primeiro);
		leilao.setLanceVencedor(primeiro);
		
		return leilao;
	}

}
