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
	
	public int getKey() {
		return chave;
	}

	public boolean hasSpeareKey() {
		Chave speareKey = getAnterior().getBiggerKeySpare();
		if (speareKey == null)
			speareKey = getProximo().getSmallerKeySpare();
		else
			return true;
		
		if (speareKey == null)
			return false;
		else
			return true;
	}
}
