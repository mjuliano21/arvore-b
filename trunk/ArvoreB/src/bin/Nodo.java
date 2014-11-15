package bin;

public class Nodo {

    private int somaChaves;
    private Chave[] chaves;

    /**
     * Cria a árvore com base na ordem repassada como parâmetro
     */
    public Nodo(int ordem) {
        somaChaves = 0;
        chaves = new Chave[ordem * 2];
    }

    public Chave[] getChaves() {
        return chaves;
    }

    public boolean ehFolha() {
        return chaves[0] == null || chaves[0].getAnterior() == null;
    }

    /**
     * Insere a chave ao final e depois ordena até achar a posição correta.
     */
    public Chave insereChave(Chave chave) {
        Chave chaveRetorno = null;
        if (somaChaves < chaves.length) {
            chaves[somaChaves] = chave;
            if (ehFolha()) {
                chaves = ordemChave(chaves, somaChaves);
            } else {
                Chave[] tempChave = organizaChavesReferencia(chaves, somaChaves, chave);
                chaves = tempChave;
            }
            somaChaves++;
        } else {
            chaveRetorno = quebraNodo(chave);
        }

        return chaveRetorno;
    }

    public boolean buscaChave(Chave chave, Nodo node) {
        if (node.buscaIndiceChave(node.chaves, chave, node.getSomaChaves()) != -1) {
            return true;
        }

        if (node.ehFolha()) {
            return false;
        } else {
            for (int i = 0; i < node.somaChaves; i++) {
                if (buscaChave(chave, node.chaves[i].getAnterior())) {
                    return true;
                }
                if (buscaChave(chave, node.chaves[i].getProximo())) {
                    return true;
                }
            }
        }

        return false;
    }

    private int buscaIndiceChave(Chave[] chaves, Chave chave, int conta) {
        for (int i = 0; i < conta; i++) {
            if (chaves[i].getChave() == chave.getChave()) {
                return i;
            }
        }
        return -1;
    }

    private Chave[] organizaChavesReferencia(Chave[] chaves, int conta, Chave chave) {
        Chave[] tempChaves = ordemChave(chaves, conta);
        if (conta > 0) {
            // achar a posicao para inserir
            int posKey = buscaIndiceChave(tempChaves, chave, conta + 1);

            // matar a referencia velha da folha que subiu e substituir a copia da referencia atual dessa folha para a referencia velha dela mesma
            if (posKey == 0) {
                tempChaves[1].setAnterior(chave.getProximo());
            } else if (posKey == conta) {
                tempChaves[conta - 1].setProximo(chave.getAnterior());
            } else {
                tempChaves[posKey - 1].setProximo(chave.getAnterior());
                tempChaves[posKey + 1].setAnterior(chave.getProximo());
            }
        }
        return tempChaves;
    }

    private Nodo quebraChaves(Chave[] chaves, int posInicial, int posFinal) {
        int ordem = somaChaves / 2;
        Nodo novoNodo = new Nodo(ordem);
        for (int i = posInicial; i <= posFinal; i++) {
            novoNodo.insereChave(chaves[i]);
        }
        return novoNodo;
    }

    private Chave quebraNodo(Chave chave) {
        /*
         * Procura o elemento mediano e o guarda na variável chaveMedia.
         */
        int novoComprimento = somaChaves + 1;
        Chave[] tempChaves = new Chave[novoComprimento];

        for (int i = 0; i < tempChaves.length - 1; i++) {
            tempChaves[i] = chaves[i];
        }

        tempChaves[novoComprimento - 1] = chave;
        if (ehFolha()) {
            tempChaves = ordemChave(tempChaves, novoComprimento - 1);
        } else {
            tempChaves = organizaChavesReferencia(tempChaves, novoComprimento - 1, chave);
        }
        int posicaoMedia = (novoComprimento + 1) / 2;
        Chave chaveMedia = tempChaves[posicaoMedia - 1];

        /*
         * Separa as chaves maiores e menores em dois novos nodos.
         */
        Nodo menorNodo = quebraChaves(tempChaves, 0, posicaoMedia - 2);
        Nodo maiorNodo = quebraChaves(tempChaves, posicaoMedia, novoComprimento - 1);

        chaveMedia.setProximo(maiorNodo);
        chaveMedia.setAnterior(menorNodo);
        return chaveMedia;
    }

    private Chave[] ordemChave(Chave[] chaves, int conta) {
        for (int i = conta; i > 0; i--) {
            if (chaves[i].getChave() < chaves[i - 1].getChave()) {
                Chave temp = chaves[i - 1];
                chaves[i - 1] = chaves[i];
                chaves[i] = temp;
            } else {
                break;
            }
        }
        return chaves;
    }

    /**
     * Retorna true se a �rvore perdeu um n�vel e ficar� desbalanceada.
     *
     * N�O FUNCIONA AINDA
     */
    /* FODEU entender esses dois, ainda nem funcionam mesmo, vamos ter que implementar*/
    public boolean removeKeyHard(Chave key) {
        /**
         * EXPLICA��O
         *
         * (Sempre no contexto da chave atual) Se a chave estiver numa folha com
         * mais de uma chave � s� retirar do nodo, por�m de o nodo tiver apenas
         * aquela chave tem juntar a chave irm�o com a pai e fazer a pai da pai
         * se juntar com o irm�o do pai. Ap�s isso tem que equivaler o level da
         * �rvore juntando o irm�o do "av�"(pai do pai) com o irm�o deles mesmo.
         * Tem que fazer recursivamente. Pode-se fazer isso fazendo a conta de
         * nivel da arvore se o nivel estiver com -1 � s� puxar o lado direto e
         * se estiver 1 tem que puxar o lado esquerdo.
         *
         * Nivel da �rvore = nivel_filho_esquerdo - nivel_filho_direito
         */
        boolean found = false;
        int i = 0;
        for (; i < somaChaves; i++) {
            if (chaves[i].getChave() == key.getChave()) {
                found = true;
            }
            if (chaves[i].getChave() > key.getChave() || found) {
                break;
            }
        }

        if (ehFolha()) {
            if (found) {
                if (somaChaves > 1) {
                    int j = i;
                    for (; j < somaChaves - 1; j++) {
                        chaves[j] = chaves[j + 1];
                    }
                    chaves[j] = null;
                    somaChaves--;
                } else {
                    chaves[0] = null;
                    somaChaves--;
                    return true;
                }
            }
        } else {
            if (found) {
                Nodo bigger = null;
                Nodo smaller = null;
                if (i == somaChaves) {
                    bigger = chaves[i - 1].getProximo();
                    smaller = chaves[i - 1].getAnterior();
                } else {
                    bigger = chaves[i].getProximo();
                    smaller = chaves[i].getAnterior();
                }
                if (bigger.ehFolha()) {
                    if (bigger.getSomaChaves() > 1) {
                        Chave smallerKey = new Chave(bigger.getMenorChave());
                        bigger.removeKeyHard(smallerKey);
                        if (i == somaChaves) {
                            smallerKey.setProximo(chaves[i - 1].getProximo());
                            smallerKey.setAnterior(chaves[i - 1].getAnterior());
                            chaves[i - 1] = smallerKey;
                        } else {
                            smallerKey.setProximo(chaves[i].getProximo());
                            smallerKey.setAnterior(chaves[i].getAnterior());
                            chaves[i] = smallerKey;
                        }
                    } else if (smaller.getSomaChaves() > 1) {
                        Chave biggerKey = new Chave(smaller.getMaiorChave());
                        smaller.removeKeyHard(biggerKey);
                        if (i == somaChaves) {
                            biggerKey.setProximo(chaves[i - 1].getProximo());
                            biggerKey.setAnterior(chaves[i - 1].getAnterior());
                            chaves[i - 1] = biggerKey;
                        } else {
                            biggerKey.setProximo(chaves[i].getProximo());
                            biggerKey.setAnterior(chaves[i].getAnterior());
                            chaves[i] = biggerKey;
                        }
                    } else {
                        if (somaChaves == 1) {
                            chaves[0] = smaller.getChaves()[0];
                            chaves[1] = bigger.getChaves()[0];
                            somaChaves++;
                            return true;
                        } else {
                            if (i == somaChaves - 1) {
                                Nodo leftOver = chaves[i].getProximo();
                                chaves[i] = null;
                                somaChaves--;
                                for (Chave leftOverKeys : leftOver.getChaves()) {
                                    this.insereChave(leftOverKeys);
                                }
                            } else if (i == 0) {
                                Nodo leftOver = chaves[i].getAnterior();
                                chaves[0] = null;
                                somaChaves--;
                                for (Chave leftOverKeys : leftOver.getChaves()) {
                                    this.insereChave(leftOverKeys);
                                }
                            } else {
                                Nodo leftOver = chaves[i].getProximo();

                                for (int j = i; j < somaChaves - 1; j++) {
                                    chaves[j] = chaves[j - 1];
                                }
                                chaves[somaChaves] = null;
                                somaChaves--;
                                chaves[i].setAnterior(chaves[i - 1].getProximo());

                                for (Chave leftOverKeys : leftOver.getChaves()) {
                                    this.insereChave(leftOverKeys);
                                }
                            }
                        }
                    }
                } else {
                    boolean unbalanced = false;

                    // Verifica de que lado do nodo que ficaria/fica a chave
                    boolean right = false;
                    if (somaChaves > 1) {
                        if (i == somaChaves) {
                            right = true;
                        }
                    } else {
                        right = chaves[0].getChave() < key.getChave();
                    }

                    Chave referenceKey = null; // Apenas para facilitar quando acessar o pai da removida(ou n�o);
                    if (right) {
                        referenceKey = chaves[i - 1];
                        unbalanced = referenceKey.getProximo().removeKeyHard(key);
                    } else {
                        referenceKey = chaves[i];
                        unbalanced = referenceKey.getAnterior().removeKeyHard(key);
                    }

                    if (unbalanced) {
                        if (right) {
                            Nodo brother = referenceKey.getAnterior();
                            int countKeysBrother = brother.getSomaChaves();
                            if (countKeysBrother > 1) {
                                // Remove essa chave sobressalente do irm�o.
                                int keyNewFather = brother.getChaves()[brother.getSomaChaves() - 1].getChave();
                                Chave newFather = new Chave(keyNewFather);
                                brother.removeKeyHard(newFather);

                                // Copia as referencias do pai Antigo.
                                Chave oldFather = chaves[i];
                                newFather.setAnterior(oldFather.getAnterior());
                                chaves[i] = newFather;

                                // Pai antigo vira filho pra equilibrar a �rvore
                                Nodo newNode = new Nodo(chaves.length / 2);
                                newNode.insereChave(oldFather);
                                chaves[i].setProximo(newNode);
                            } else {
                                if (somaChaves > 1) {
                                    Chave fatherKey = chaves[i - 1];
                                    chaves[i - 1] = null;
                                    somaChaves--;
                                    insereChave(fatherKey);
                                } else {
                                    Chave keyBrother = brother.chaves[0];
                                    removeKeyHard(keyBrother);
                                    chaves[somaChaves] = keyBrother;
                                    chaves[i - 1].setProximo(keyBrother.getAnterior());
                                }
                            }
                        } else {
                            Nodo brother = referenceKey.getProximo();
                            Chave spareKey = brother.getSmallerKeySpare();
                            /*if (brother.ehFolha()) {
                             int countKeysBrother = brother.getSomaChaves();
                             if (countKeysBrother > 1) {
                             // Remove essa chave sobressalente do irm�o.
                             int keyNewFather = brother.getChaves()[0].getChave();
                             Chave newFather = new Chave(keyNewFather);
                             brother.removeKeyHard(newFather);

                             // Copia as referencias do pai Antigo.
                             Chave oldFather = chaves[i];
                             newFather.setProximo(oldFather.getProximo());
                             oldFather.setProximo(null);
                             oldFather.setAnterior(null);
                             chaves[i] = newFather;

                             // Pai antigo vira filho pra equilibrar a �rvore.
                             Nodo newNode = new Nodo(chaves.length / 2);
                             newNode.insereChave(oldFather);
                             chaves[i].setAnterior(newNode);
                             if (i > 0)
                             chaves[i - 1].setProximo(newNode);
                             } else {
                             // Pai antigo vira filho pra equilibrar a �rvore.
                             int father = chaves[i].getChave();
                             int j = i;
                             for (; j < somaChaves - 1; j++) {
                             chaves[j] = chaves[j + 1];
                             }
                             chaves[j] = null;
                             somaChaves--;
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
         * Se a remo��o for feita do lado direito da �rvore ter� que inserir
         * todos os valores menores que a chave removida em ordem crescente e os
         * maiores em ordem decrescente. Se a remo��o for feita do lado esquerdo
         * da �rvore tem que inserir todos os valores maiores que a chave
         * removida em ordem decrescente e os menores em ordem crescente. Se a
         * remo��o for feita do meio da �rvore tem que inserir todos os valores
         * maiores que a chave removida em ordem decrescente e os menores em
         * ordem crescente.
         */

        ArvoreB newTree = new ArvoreB(chaves.length / 2);
        String[] returned = getTodasChave(this).split(" ");
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
        for (; i < somaChaves; i++) {
            if (chaves[i].getChave() == key.getChave()) {
                achou = true;
            }
            if (chaves[i].getChave() > key.getChave()) {
                break;
            }
        }

        if (achou) {
            for (int j = 0; j < numberKeys; j++) {
                if (keysReturned[j] != key.getChave()) {
                    newTree.insereChave(keysReturned[j]);
                }
            }
        } else {
            if (i == somaChaves) {
                achou = chaves[i - 1].getProximo().buscaChave(key, chaves[i - 1].getProximo());
                if (achou) {
                    for (int j = 0; j < numberKeys && keysReturned[j] != key.getChave(); j++) {
                        newTree.insereChave(keysReturned[j]);
                    }
                    for (int j = numberKeys - 1; j > 0 && keysReturned[j] != key.getChave(); j--) {
                        newTree.insereChave(keysReturned[j]);
                    }
                }
            } else {
                achou = chaves[i].getAnterior().buscaChave(key, chaves[i].getAnterior());
                if (achou) {
                    if (i == 0) {
                        for (int j = 0; j < numberKeys && keysReturned[j] != key.getChave(); j++) {
                            newTree.insereChave(keysReturned[j]);
                        }
                        for (int j = numberKeys - 1; j > 0 && keysReturned[j] != key.getChave(); j--) {
                            newTree.insereChave(keysReturned[j]);
                        }
                    } else {
                        Nodo nodeWithKey = getNodo(key.getChave());
                        int biggerKey = nodeWithKey.getMaiorChave();
                        for (int j = numberKeys - 1; j > 0 && keysReturned[j] != biggerKey; j--) {
                            newTree.insereChave(keysReturned[j]);
                        }
                        for (int j = 0; j < numberKeys; j++) {
                            if (keysReturned[j] != key.getChave()) {
                                newTree.insereChave(keysReturned[j]);
                            }
                            if (keysReturned[j] == biggerKey) {
                                break;
                            }
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

    private Nodo getNodo(int valorChave) {
        for (int i = 0; i < somaChaves; i++) {
            if (chaves[i].getChave() == valorChave) {
                return this;
            }
        }

        Nodo retorno = null;
        for (int i = 0; i < somaChaves; i++) {
            retorno = chaves[i].getAnterior().getNodo(valorChave);
            if (retorno == null) {
                return chaves[i].getProximo().getNodo(valorChave);
            }
        }
        return retorno;
    }

    private String getTodasChave(Nodo nodo) {
        String chaveRetorno = "";
        for (int i = 0; i < nodo.somaChaves; i++) {
            if (nodo.chaves[i].getAnterior() != null) {
                chaveRetorno += nodo.getTodasChave(nodo.chaves[i].getAnterior()) + " ";
            }

            chaveRetorno += nodo.chaves[i].getChave() + " ";
            if (i == nodo.somaChaves - 1) {
                if (nodo.chaves[i].getProximo() != null) {
                    chaveRetorno += nodo.getTodasChave(nodo.chaves[i].getProximo()) + " ";
                }
            }
        }
        return chaveRetorno;
    }

    public int getSomaChaves() {
        return somaChaves;
    }

    public int getMenorChave() {
        if (ehFolha()) {
            return chaves[0].getChave();
        } else {
            return chaves[0].getAnterior().getMenorChave();
        }
    }

    public int getMaiorChave() {
        if (ehFolha()) {
            return chaves[somaChaves - 1].getChave();
        } else {
            return chaves[somaChaves - 1].getProximo().getMaiorChave();
        }
    }

    // favor entender o que isso faz
    public Chave getSmallerKeySpare() {
        Chave returned = null;
        if (ehFolha()) {
            if (somaChaves > 1) {
                return chaves[0];
            } else {
                return null;
            }
        } else {
            returned = chaves[0].getAnterior().getSmallerKeySpare();
            if (returned == null) {
                returned = chaves[0].getProximo().getSmallerKeySpare();
            }
        }
        return returned;
    }
// favor entender o que isso faz

    public Chave getBiggerKeySpare() {
        Chave returned = null;
        if (ehFolha()) {
            if (somaChaves > 1) {
                return chaves[somaChaves - 1];
            } else {
                return null;
            }
        } else {
            returned = chaves[somaChaves - 1].getProximo().getBiggerKeySpare();
            if (returned == null) {
                returned = chaves[somaChaves - 1].getAnterior().getBiggerKeySpare();
            }
        }
        return returned;
    }

    /* GAMBIARRA - ARRUMAR URGENTE  -- guarda a info se é pai ou não*/
    public static int nivel = 1;
    public static String imprime = "";

    public String imprimeNodo(Nodo nodo, int nivelAnterior) {
        /*String imprime = "";
         int nivel = 1;*/
        int conta = nodo.getSomaChaves();
        Chave[] chaves = nodo.getChaves();
        imprime += "<";
        for (int i = 0; i < 4; i++) {
            imprime += "[";
            if (chaves[i] != null) {
                imprime += chaves[i].getChave() + "";
            } else {
                imprime += "*";
            }
            imprime += "]";
        }
        imprime += ">";
        imprime += "-[" + (nivel + nivelAnterior) + "]\n";

        if (!nodo.ehFolha()) {
            for (int i = 0; i < nodo.somaChaves; i++) {
                imprimeNodo(nodo.chaves[i].getAnterior(), nivel + nivelAnterior);
                if (i == nodo.somaChaves - 1) {
                    imprimeNodo(nodo.chaves[i].getProximo(), nivel + nivelAnterior);
                }
            }
        }
        return imprime;
    }
}
