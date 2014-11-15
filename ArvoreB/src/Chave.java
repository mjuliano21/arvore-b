public class Chave {
	private int chave;
	private Nodo anterior;
	private Nodo proximo;

	public Chave(int chave) {
		this.chave = chave;
	}
	
	public void criarAnterior(Chave novaChave) {
		anterior.insereChave(novaChave);
	}
	
	public void criarProximo(Chave novaChave) {
		proximo.insereChave(novaChave);
	}
	
	public void setAnterior(Nodo anterior) {
		this.anterior = anterior;
	}
	
	public void setProximo(Nodo proximo) {
		this.proximo = proximo;
	}
	
	public Nodo getAnterior() {
		return anterior;
	}
	
	public Nodo getProximo() {
		return proximo;
	}
	
	public int getChave() {
		return chave;
	}

	
}
