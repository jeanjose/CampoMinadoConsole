package br.com.jeanjose.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.jeanjose.cm.excecao.ExplosaoException;

public class Campo {
	
	private final int linha;
	private final int coluna;
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;

	private List<Campo> vizinhos = new ArrayList<>();
	
	Campo(int linha, int coluna){
		this.linha = linha;
		this.coluna = coluna;
	}

	//Metodo para abrir os vizinhos juntos caso seja seguro
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		//Dist�ncia
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaColuna + deltaLinha;
		
		if(deltaGeral == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}
	}
	
	//metodo para colocar a marca��o
	void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
		}
	}
	
	//Metodo para abrir as "casas", caso tenha bomba irar explodir, caso o vizinho ao lado seja seguro vai abrir junto
	boolean abrir() {
	
		if (!aberto && !marcado) {
			aberto = true;
			
			if(minado) {
				throw new ExplosaoException();
			}
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
		
			return true;
		} else {
			return false;
		}
	}
	
	//Metodo para verificar se a vizinha�a tem bomba ou n�o para abrir os campos onde n�o tiver mina
	boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	//Metodo para minar?
	void minar() {
		minado = true;
	}
	
	//Campo est� minado?
	public boolean isMinado() {
		return minado;
	}

	//Campo est� marcado?
	public boolean isMarcado() {
		return marcado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
	}
	
	//Campo est� aberto?
	public boolean isAberto() {
		return aberto;
	}
	
	//Campo est� fechado?
	public boolean isFechado() {
		return !isAberto();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protedigo = minado && marcado;
		return desvendado || protedigo;
	}
	
	long minasNaVizinhanca() {
		//metodo para retornar a quantidade de minas na vizinhan�a
		return vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
	}
	
	public String toString() {
		if(marcado) {
			return "x";
		} else if (aberto && minado) {
			return "*";
		} else if (aberto && minasNaVizinhanca() > 0) {
			return Long.toString(minasNaVizinhanca());
		} else if(aberto) {
			return " ";
		} else {
			return "?";
		}
	}

}
