public class ArvoreB {
	private Nodo nod;
	private int ordem;

	public ArvoreB(int order) {
		this.ordem = order;
		this.nod = new Nodo(this.ordem);
	}

	public void insertKey(int key) {
		Chave newKey = new Chave(key);
		newKey = insertKey(newKey, nod);
		if (newKey != null) {
			Nodo newNode = new Nodo(ordem);
			newNode.insereChave(newKey);
			nod = newNode;
		}
	}

	private Chave insertKey(Chave newKey, Nodo nodo) {
		Chave keyReturned = null;

		if (nodo.isLeaf()) {
			/*
			 * Se � uma Folha ele apenas insere nela
			 */
			keyReturned = nodo.insereChave(newKey);
		} else {
			/*
			 * Caso n�o for ele procura a folha pela recursividade.
			 */
			Chave[] keys = nodo.getKeys();
			int countKeys = nodo.getCountKeys();
			for (int i = 0; i < countKeys; i++) {
				if (newKey.getKey() < keys[i].getKey()) {
					keyReturned = insertKey(newKey, keys[i].getAnterior());
					break;
				} else if (i == countKeys - 1) {
					keyReturned = insertKey(newKey, keys[i].getProximo());
					break;
				}
			}

			if (keyReturned != null)
				keyReturned = nodo.insereChave(keyReturned);
		}

		return keyReturned;
	}

	public boolean searchKey(int value) {
		return nod.searchKey(new Chave(value), nod);
	}

	public void removeKey(int key) {
		/*ArvoreB tree = potato.removeKeyEasy(new Chave(key));	
		if (tree != null) {
			this.potato = tree.potato;
		}*/
		nod.removeKeyHard(new Chave(key));
	}

	/*private void removeKey(Chave key, Nodo node) {
		node.removeKeyHard(key);
	}*/

	public String printBTree() {
		String retorno = "";

		String[] nodesWithLevel = nod.printNode(nod, 0).split("\n");
		int maxLevel = 0;
		String[][] levels = new String[10][100];
		int[] countlevels = new int[10];
		for (int i = 0; i < nodesWithLevel.length; i++) {
			String[] division = nodesWithLevel[i].split("-");
			int level = Integer.parseInt("" + division[division.length - 1].charAt(1)) - 1;
			levels[level][countlevels[level]++] = division[0];
			if (level > maxLevel)
				maxLevel = level;
		}

		String tabInicial = "";
		for (int i = 0; i <= maxLevel; i++) {
			tabInicial += "";
			retorno += "\n";
			for (int j = 0; j < countlevels[i]; j++) {
				retorno += tabInicial + levels[i][j] + "\t\t";
			}
		}

		nod.nivel = 1;
		nod.print = "";
		return retorno;
	}

	private String multiNivel(int nivel) {
		String retorno = "";
		for (int i = 0; i <= nivel; i++) {
			retorno += "\t\t";
		}
		return retorno;
	}
}
