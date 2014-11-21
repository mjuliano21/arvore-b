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

            // matar a referencia velha da folha que subiu e substituir a copia da
            //referencia atual dessa folha para a referencia velha dela mesma
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

    public static int nivel = 1;
    public static String imprime = "";

    public String imprimeNodo(Nodo nodo, int nivelAnterior) {

        int conta = nodo.getSomaChaves();
        Chave[] chaves = nodo.getChaves();
        for (int i = 0; i < 4; i++) {
            if (chaves[i] != null) {
                imprime += chaves[i].getChave() + "";
            } else {
                imprime += "*";
            }
            imprime += ",";
        }
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
