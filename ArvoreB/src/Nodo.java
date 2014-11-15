public class Nodo {
	private int countKeys;
	private Chave[] keys;

	/**
	 * Cria a �rvore baseado na ordem que � informada. Quantidade de chaves � igual � ordem vezes dois.
	 * 
	 * @param order
	 *            Ordem da �rvore.
	 */
	public Nodo(int order) {
		this.countKeys = 0;
		keys = new Chave[order * 2];
	}

	public Chave[] getKeys() {
		return keys;
	}

	public boolean ehFolha() {
		return keys[0] == null || keys[0].getAnterior() == null;
	}

	/**
	 * Insere a chave no final e ordena as chaves at� achar o lugar correto da chave.
	 * 
	 * @param key
	 *            Chave a ser inserida.
	 */
	public Chave insereChave(Chave key) {
		Chave keyReturn = null;
		if (countKeys < keys.length) {
			keys[countKeys] = key;
			if (ehFolha()) {
				keys = orderKey(keys, countKeys);
			} else {
				Chave[] tempKey = organizeReferencesKeys(keys, countKeys, key);
				keys = tempKey;
			}
			countKeys++;
		} else {
			keyReturn = breakFuckingNode(key);
		}
		return keyReturn;
	}

	public boolean searchKey(Chave key, Nodo node) {
		if (node.getIndexKey(node.keys, key, node.getCountKeys()) != -1)
			return true;

		if (node.ehFolha())
			return false;
		else {
			for (int i = 0; i < node.countKeys; i++) {
				if (searchKey(key, node.keys[i].getAnterior()))
					return true;
				if (searchKey(key, node.keys[i].getProximo()))
					return true;
			}
		}

		return false;
	}

	private int getIndexKey(Chave[] keys, Chave key, int count) {
		for (int i = 0; i < count; i++) {
			if (keys[i].getChave() == key.getChave())
				return i;
		}
		return -1;
	}

	private Chave breakFuckingNode(Chave key) {
		/*
		 * Procura o elemento mediano e o guarda na vari�vel middleKey.
		 */
		int newLength = countKeys + 1;
		Chave[] tempKeys = new Chave[newLength];

		for (int i = 0; i < tempKeys.length - 1; i++) {
			tempKeys[i] = keys[i];
		}

		tempKeys[newLength - 1] = key;
		if (ehFolha()) {
			tempKeys = orderKey(tempKeys, newLength - 1);
		} else {
			tempKeys = organizeReferencesKeys(tempKeys, newLength - 1, key);
		}
		int middlePos = (newLength + 1) / 2;
		Chave middleKey = tempKeys[middlePos - 1];

		/*
		 * Separa as chaves maiores e menores em dois novos nodos.
		 */
		Nodo smallerNode = breakKeys(tempKeys, 0, middlePos - 2);
		Nodo biggerNode = breakKeys(tempKeys, middlePos, newLength - 1);

		middleKey.setProximo(biggerNode);
		middleKey.setAnterior(smallerNode);
		return middleKey;
	}

	private Chave[] organizeReferencesKeys(Chave[] keys, int count, Chave key) {
		Chave[] tempKey = orderKey(keys, count);
		if (count > 0) {
			// achar a posicao para inserir
			int posKey = getIndexKey(tempKey, key, count + 1);

			// matar a referencia velha da folha que subiu e substituir a copia da referencia atual dessa folha para a referencia velha dela mesma
			if (posKey == 0) {
				tempKey[1].setAnterior(key.getProximo());
			} else if (posKey == count) {
				tempKey[count - 1].setProximo(key.getAnterior());
			} else {
				tempKey[posKey - 1].setProximo(key.getAnterior());
				tempKey[posKey + 1].setAnterior(key.getProximo());
			}
		}
		return tempKey;
	}

	private Nodo breakKeys(Chave[] keys, int startPos, int finalPos) {
		int order = countKeys / 2;
		Nodo newNode = new Nodo(order);
		for (int i = startPos; i <= finalPos; i++) {
			newNode.insereChave(keys[i]);
		}
		return newNode;
	}

	private Chave[] orderKey(Chave[] keys, int count) {
		for (int i = count; i > 0; i--) {
			if (keys[i].getChave() < keys[i - 1].getChave()) {
				Chave temp = keys[i - 1];
				keys[i - 1] = keys[i];
				keys[i] = temp;
			} else {
				break;
			}
		}
		return keys;
	}

	/**
	 * Retorna true se a �rvore perdeu um n�vel e ficar� desbalanceada.
	 * 
	 * N�O FUNCIONA AINDA
	 */
	public boolean removeKeyHard(Chave key) {
		/**
		 * EXPLICA��O
		 * 
		 * (Sempre no contexto da chave atual) Se a chave estiver numa folha com mais de uma chave � s� retirar do nodo, por�m de o nodo tiver apenas aquela chave tem juntar a chave irm�o com a pai e
		 * fazer a pai da pai se juntar com o irm�o do pai. Ap�s isso tem que equivaler o level da �rvore juntando o irm�o do "av�"(pai do pai) com o irm�o deles mesmo. Tem que fazer recursivamente.
		 * Pode-se fazer isso fazendo a conta de nivel da arvore se o nivel estiver com -1 � s� puxar o lado direto e se estiver 1 tem que puxar o lado esquerdo.
		 * 
		 * Nivel da �rvore = nivel_filho_esquerdo - nivel_filho_direito
		 */
		boolean found = false;
		int i = 0;
		for (; i < countKeys; i++) {
			if (keys[i].getChave() == key.getChave())
				found = true;
			if (keys[i].getChave() > key.getChave() || found)
				break;
		}

		if (ehFolha()) {
			if (found) {
				if (countKeys > 1) {
					int j = i;
					for (; j < countKeys - 1; j++) {
						keys[j] = keys[j + 1];
					}
					keys[j] = null;
					countKeys--;
				} else {
					keys[0] = null;
					countKeys--;
					return true;
				}
			}
		} else {
			if (found) {
				Nodo bigger = null;
				Nodo smaller = null;
				if (i == countKeys) {
					bigger = keys[i - 1].getProximo();
					smaller = keys[i - 1].getAnterior();
				} else {
					bigger = keys[i].getProximo();
					smaller = keys[i].getAnterior();
				}
				if (bigger.ehFolha()) {
					if (bigger.getCountKeys() > 1) {
						Chave smallerKey = new Chave(bigger.getSmallerKey());
						bigger.removeKeyHard(smallerKey);
						if (i == countKeys) {
							smallerKey.setProximo(keys[i - 1].getProximo());
							smallerKey.setAnterior(keys[i - 1].getAnterior());
							keys[i - 1] = smallerKey;
						} else {
							smallerKey.setProximo(keys[i].getProximo());
							smallerKey.setAnterior(keys[i].getAnterior());
							keys[i] = smallerKey;
						}
					} else if (smaller.getCountKeys() > 1) {
						Chave biggerKey = new Chave(smaller.getBiggerKey());
						smaller.removeKeyHard(biggerKey);
						if (i == countKeys) {
							biggerKey.setProximo(keys[i - 1].getProximo());
							biggerKey.setAnterior(keys[i - 1].getAnterior());
							keys[i - 1] = biggerKey;
						} else {
							biggerKey.setProximo(keys[i].getProximo());
							biggerKey.setAnterior(keys[i].getAnterior());
							keys[i] = biggerKey;
						}
					} else {
						if (countKeys == 1) {
							keys[0] = smaller.getKeys()[0];
							keys[1] = bigger.getKeys()[0];
							countKeys++;
							return true;
						} else {
							if (i == countKeys - 1) {
								Nodo leftOver = keys[i].getProximo();
								keys[i] = null;
								countKeys--;
								for (Chave leftOverKeys : leftOver.getKeys()) {
									this.insereChave(leftOverKeys);
								}
							} else if (i == 0) {
								Nodo leftOver = keys[i].getAnterior();
								keys[0] = null;
								countKeys--;
								for (Chave leftOverKeys : leftOver.getKeys()) {
									this.insereChave(leftOverKeys);
								}
							} else {
								Nodo leftOver = keys[i].getProximo();

								for (int j = i; j < countKeys - 1; j++) {
									keys[j] = keys[j - 1];
								}
								keys[countKeys] = null;
								countKeys--;
								keys[i].setAnterior(keys[i - 1].getProximo());

								for (Chave leftOverKeys : leftOver.getKeys()) {
									this.insereChave(leftOverKeys);
								}
							}
						}
					}
				} else {
					boolean unbalanced = false;

					// Verifica de que lado do nodo que ficaria/fica a chave
					boolean right = false;
					if (countKeys > 1) {
						if (i == countKeys)
							right = true;
					} else {
						right = keys[0].getChave() < key.getChave();
					}

					Chave referenceKey = null; // Apenas para facilitar quando acessar o pai da removida(ou n�o);
					if (right) {
						referenceKey = keys[i - 1];
						unbalanced = referenceKey.getProximo().removeKeyHard(key);
					} else {
						referenceKey = keys[i];
						unbalanced = referenceKey.getAnterior().removeKeyHard(key);
					}

					if (unbalanced) {
						if (right) {
							Nodo brother = referenceKey.getAnterior();
							int countKeysBrother = brother.getCountKeys();
							if (countKeysBrother > 1) {
								// Remove essa chave sobressalente do irm�o.
								int keyNewFather = brother.getKeys()[brother.getCountKeys() - 1].getChave();
								Chave newFather = new Chave(keyNewFather);
								brother.removeKeyHard(newFather);

								// Copia as referencias do pai Antigo.
								Chave oldFather = keys[i];
								newFather.setAnterior(oldFather.getAnterior());
								keys[i] = newFather;

								// Pai antigo vira filho pra equilibrar a �rvore
								Nodo newNode = new Nodo(keys.length / 2);
								newNode.insereChave(oldFather);
								keys[i].setProximo(newNode);
							} else {
								if (countKeys > 1) {
									Chave fatherKey = keys[i - 1];
									keys[i - 1] = null;
									countKeys--;
									insereChave(fatherKey);
								} else {
									Chave keyBrother = brother.keys[0];
									removeKeyHard(keyBrother);
									keys[countKeys] = keyBrother;
									keys[i - 1].setProximo(keyBrother.getAnterior());
								}
							}
						} else {
							Nodo brother = referenceKey.getProximo();
							Chave spareKey = brother.getSmallerKeySpare();
							/*if (brother.ehFolha()) {
								int countKeysBrother = brother.getCountKeys();
								if (countKeysBrother > 1) {
									// Remove essa chave sobressalente do irm�o.
									int keyNewFather = brother.getKeys()[0].getChave();
									Chave newFather = new Chave(keyNewFather);
									brother.removeKeyHard(newFather);

									// Copia as referencias do pai Antigo.
									Chave oldFather = keys[i];
									newFather.setProximo(oldFather.getProximo());
									oldFather.setProximo(null);
									oldFather.setAnterior(null);
									keys[i] = newFather;

									// Pai antigo vira filho pra equilibrar a �rvore.
									Nodo newNode = new Nodo(keys.length / 2);
									newNode.insereChave(oldFather);
									keys[i].setAnterior(newNode);
									if (i > 0)
										keys[i - 1].setProximo(newNode);
								} else {
									// Pai antigo vira filho pra equilibrar a �rvore.
									int father = keys[i].getChave();
									int j = i;
									for (; j < countKeys - 1; j++) {
										keys[j] = keys[j + 1];
									}
									keys[j] = null;
									countKeys--;
									brother.insereChave(new Chave(father));
								}
							} else {

							}*/
						}
					}

				}
			}
		}
		return false;
	}

	public ArvoreB removeKeyEasy(Chave key) {
		/**
		 * EXPLICA��O
		 * 
		 * Se a remo��o for feita do lado direito da �rvore ter� que inserir todos os valores menores que a chave removida em ordem crescente e os maiores em ordem decrescente. Se a remo��o for feita
		 * do lado esquerdo da �rvore tem que inserir todos os valores maiores que a chave removida em ordem decrescente e os menores em ordem crescente. Se a remo��o for feita do meio da �rvore tem
		 * que inserir todos os valores maiores que a chave removida em ordem decrescente e os menores em ordem crescente.
		 */

		ArvoreB newTree = new ArvoreB(keys.length / 2);
		String[] returned = getAllKeys(this).split(" ");
		int[] keysReturned = new int[returned.length];
		int numberKeys = 0;
		for (int i = 0; i < returned.length; i++) {
			if (!returned[i].isEmpty()) {
				keysReturned[numberKeys] = Integer.parseInt(returned[i]);
				numberKeys++;
			}
		}

		boolean achou = false;
		int i = 0;
		for (; i < countKeys; i++) {
			if (keys[i].getChave() == key.getChave())
				achou = true;
			if (keys[i].getChave() > key.getChave())
				break;
		}

		if (achou) {
			for (int j = 0; j < numberKeys; j++) {
				if (keysReturned[j] != key.getChave())
					newTree.insereChave(keysReturned[j]);
			}
		} else {
			if (i == countKeys) {
				achou = keys[i - 1].getProximo().searchKey(key, keys[i - 1].getProximo());
				if (achou) {
					for (int j = 0; j < numberKeys && keysReturned[j] != key.getChave(); j++) {
						newTree.insereChave(keysReturned[j]);
					}
					for (int j = numberKeys - 1; j > 0 && keysReturned[j] != key.getChave(); j--) {
						newTree.insereChave(keysReturned[j]);
					}
				}
			} else {
				achou = keys[i].getAnterior().searchKey(key, keys[i].getAnterior());
				if (achou) {
					if (i == 0) {
						for (int j = 0; j < numberKeys && keysReturned[j] != key.getChave(); j++) {
							newTree.insereChave(keysReturned[j]);
						}
						for (int j = numberKeys - 1; j > 0 && keysReturned[j] != key.getChave(); j--) {
							newTree.insereChave(keysReturned[j]);
						}
					} else {
						Nodo nodeWithKey = getNode(key.getChave());
						int biggerKey = nodeWithKey.getBiggerKey();
						for (int j = numberKeys - 1; j > 0 && keysReturned[j] != biggerKey; j--) {
							newTree.insereChave(keysReturned[j]);
						}
						for (int j = 0; j < numberKeys; j++) {
							if (keysReturned[j] != key.getChave())
								newTree.insereChave(keysReturned[j]);
							if (keysReturned[j] == biggerKey)
								break;
						}
					}
				}
			}
		}

		if (achou) {
			return newTree;
		} else {
			return null;
		}
	}

	private Nodo getNode(int keyValue) {
		for (int i = 0; i < countKeys; i++) {
			if (keys[i].getChave() == keyValue)
				return this;
		}

		Nodo returned = null;
		for (int i = 0; i < countKeys; i++) {
			returned = keys[i].getAnterior().getNode(keyValue);
			if (returned == null)
				return keys[i].getProximo().getNode(keyValue);
		}
		return returned;
	}

	private String getAllKeys(Nodo node) {
		String returnKey = "";
		for (int i = 0; i < node.countKeys; i++) {
			if (node.keys[i].getAnterior() != null) {
				returnKey += node.getAllKeys(node.keys[i].getAnterior()) + " ";
			}

			returnKey += node.keys[i].getChave() + " ";
			if (i == node.countKeys - 1) {
				if (node.keys[i].getProximo() != null) {
					returnKey += node.getAllKeys(node.keys[i].getProximo()) + " ";
				}
			}
		}
		return returnKey;
	}

	public int getCountKeys() {
		return countKeys;
	}

	public int getSmallerKey() {
		if (ehFolha())
			return keys[0].getChave();
		else
			return keys[0].getAnterior().getSmallerKey();
	}

	public int getBiggerKey() {
		if (ehFolha())
			return keys[countKeys - 1].getChave();
		else
			return keys[countKeys - 1].getProximo().getBiggerKey();
	}

	public Chave getSmallerKeySpare() {
		Chave returned = null;
		if (ehFolha()) {
			if (countKeys > 1)
				return keys[0];
			else
				return null;
		} else {
			returned = keys[0].getAnterior().getSmallerKeySpare();
			if (returned == null)
				returned = keys[0].getProximo().getSmallerKeySpare();
		}
		return returned;
	}

	public Chave getBiggerKeySpare() {
		Chave returned = null;
		if (ehFolha()) {
			if (countKeys > 1)
				return keys[countKeys - 1];
			else
				return null;
		} else {
			returned = keys[countKeys - 1].getProximo().getBiggerKeySpare();
			if (returned == null)
				returned = keys[countKeys - 1].getAnterior().getBiggerKeySpare();
		}
		return returned;
	}

	/* GAMBIARRA - ARRUMAR URGENTE  -- guarda a info se é pai ou não*/
	public static int nivel = 1;
	public static String print = "";

	public String printNode(Nodo node, int nivelAnterior) {
            /*String print = "";
            int nivel = 1;*/
            int count = node.getCountKeys();
		Chave[] keys = node.getKeys();
		for (int i = 0; i < count; i++) {
			print += keys[i].getChave() + " ";
		}
		print += "-[" + (nivel + nivelAnterior) + "]\n";

		if (!node.ehFolha()) {
			for (int i = 0; i < node.countKeys; i++) {
				printNode(node.keys[i].getAnterior(), nivel + nivelAnterior);
				if (i == node.countKeys - 1)
					printNode(node.keys[i].getProximo(), nivel + nivelAnterior);
			}
		}
		return print;
	}
}
