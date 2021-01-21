package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {
	
	private FinalizarLeilaoService service;
	
	@Mock
	private EnviadorDeEmails enviadorDeEmails;

	@Mock
	private LeilaoDao leilaoDao;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao, enviadorDeEmails);
	}
	
	@Test
	public void finalizarUmLeilao() {
		//LeilaoDao dao = Mockito.mock(LeilaoDao.class);
		//service = new FinalizarLeilaoService(leilaoDao);
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		
		Assert.assertTrue(leilao.isFechado());
		Assert.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
		Mockito.verify(leilaoDao).salvar(leilao);
	}
	
	private List<Leilao> leiloes(){
		List<Leilao> leiloes = new ArrayList<Leilao>();
		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Ragnar"));
		
		Lance primeiro = new Lance(new Usuario("Lagertha"), new BigDecimal("600"));
		Lance segundo = new Lance(new Usuario("Rollo"), new BigDecimal("900"));
		
		leilao.propoe(primeiro);
		leilao.propoe(segundo);
		
		leiloes.add(leilao);
		return leiloes;
	}
	
	@Test
	public void deveriaEnviarEmailparaVencedorDoLeilao() {
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		service.finalizarLeiloesExpirados();
		
		Leilao leilao = leiloes.get(0);
		Lance lanceVencedor = leilao.getLanceVencedor();
		
		Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);
	
	}
	
	@Test
	public void naoDeveriaEnviarEmailAoVencedorEmCasoDeErro() {
		List<Leilao> leiloes = leiloes();
		
		Mockito.when(leilaoDao.buscarLeiloesExpirados()).thenReturn(leiloes);
		Mockito.when(leilaoDao.salvar(Mockito.any())).thenThrow(RuntimeException.class);
		
		try {
			service.finalizarLeiloesExpirados();
			Mockito.verifyNoInteractions(enviadorDeEmails);

		} catch (Exception e) {}
		
	}


}
