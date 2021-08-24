package br.com.jeanjose.cm;

import br.com.jeanjose.cm.modelo.Tabuleiro;
import br.com.jeanjose.cm.visao.TabuleiroConsole;

public class Menu {

	public static void main(String[] args) {
		
		//(linha, coluna, bombas)
		Tabuleiro tabuleiro = new Tabuleiro(8, 8, 6);
		new TabuleiroConsole(tabuleiro);
	
		
	}
}
