package br.com.jeanjose.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.jeanjose.cm.excecao.ExplosaoException;

public class Tabuleiro {

	private int qtdLinhas;
	private int qtdColunas;
	private int qtdMinas;
	
	private final List<Campo> campos = new ArrayList<>();
	
	//Chamada de construtor, junto com as chamadas de metodo para gerar o campo, associnar os vizinhos e gerar as bombas;
	public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;
		this.qtdMinas = qtdMinas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}

	//Metodo para abrir campo através dos indices
	public void abrir(int qtdLinha, int qtdColuna) {
		try {
			campos.parallelStream()
				.filter(c -> c.getLinha() == qtdLinha && c.getColuna() == qtdColuna)
				//findFirst = pegue o primeiro elemento
				.findFirst()
				//Está presente? Se tiver vai chamar o c.abrir;
				.ifPresent(c -> c.abrir());;
		} catch(ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}
	}
	
	//Metodo para fazer marcacao do campo
	public void alterarMarcacao(int qtdLinha, int qtdColuna) {
		campos.parallelStream()
			.filter(c -> c.getLinha() == qtdLinha && c.getColuna() == qtdColuna)
			//findFirst = pegue o primeiro elemento;
			.findFirst()
			//Está presente? Se tiver chame o metodo marcar
			.ifPresent(c -> c.alternarMarcacao());;
	}
	
	//lógica para gerar o campo, sem usar matriz
	private void gerarCampos() {	
		for (int linha=0; linha<qtdLinhas; linha++) {
			for(int coluna=0; coluna<qtdColunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}
		}
	}
	
	//Vai verificar todos os requisitos de quem pode ser ou não vizinho
	private void associarVizinhos() {	
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}
	
	//logica para sortear as minas do tabuleiro
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas<qtdMinas);
	}
	
	//Se todos os campos der Match, você alcançou o objetivo do jogo e venceu
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	@Override
	public String toString() {
	StringBuilder sb = new StringBuilder();
		//Numeracao de coluna
			sb.append("    ");
		for(int c = 0; c < qtdColunas; c++) {
			sb.append(" ");
			sb.append(c);
			sb.append(" ");
		}
		
		//Criando linhas para separar a numeracao
			sb.append("\n");
			sb.append("   ");
		for (int c = 0; c< qtdColunas; c++) {
			sb.append("---");		
		}
			sb.append("\n");
			

		//imprimindo o valor das linhas e colunas
		int i = 0;
		for(int l = 0; l < qtdLinhas; l++) {
			//numeracao da linha
			sb.append(l + " | ");
			for (int c=0; c < qtdColunas; c++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");
				i++;
			}
			
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
}
