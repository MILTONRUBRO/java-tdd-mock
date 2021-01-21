package br.com.alura.leilao.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
	}
	

	@Test
	void deveriaCriarPagamentoParaVencedorDoLeilao() {
		Leilao leilao = leilao();
		Lance lanceVencedor = leilao.getLanceVencedor();
		geradorDePagamento.gerarPagamento(lanceVencedor);
		
		Mockito.verify(pagamentoDao).salvar(captor.capture());
	
		Pagamento pagamento = captor.getValue();
		
		Assert.assertEquals(LocalDate.now().plusDays(1), pagamento.getVencimento());
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
