package br.com.alura.leilao.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;

class FinalizarLeilaoServiceTest {
	
	private FinalizarLeilaoService service;
	
	@Mock
	private LeilaoDao leilaoDao;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
		this.service = new FinalizarLeilaoService(leilaoDao);
	}
	
	@Test
	public void finalizarUmLeilao() {
		//LeilaoDao dao = Mockito.mock(LeilaoDao.class);
		//service = new FinalizarLeilaoService(leilaoDao);
		List<Leilao> leiloes = leiloes();
		service.finalizarLeiloesExpirados();
		
	}
	
	private List<Leilao> leiloes(){
		List<Leilao> leiloes = new ArrayList<Leilao>();
		
		Leilao leilao = new Leilao("Celular", new BigDecimal("500"), new Usuario("Ragnar"));
		
		Lance primeiro = new Lance(new Usuario("Lagertha"), new BigDecimal("600"));
		Lance segundo = new Lance(new Usuario("Rollo"), new BigDecimal("900"));
		
		leilao.propoe(primeiro);
		leilao.propoe(primeiro);
		
		leiloes.add(leilao);
		return leiloes;
	}


}
