package bin;

public class ArvoreB {
    // atributos
    private Nodo nod;
    private int ordem;

    // construtor
    public ArvoreB(int ordem) {
        this.ordem = ordem;
        this.nod = new Nodo(this.ordem);
    }
    // inserir chave
    private Chave insereChave(Chave novaChave, Nodo nodo) {
        Chave chaveRetorno = null;

        if (nodo.ehFolha()) {
            /*
             Se for uma Folha, apenas insere nela
             */
            chaveRetorno = nodo.insereChave(novaChave);
        } else {
            /*
             * Caso não for ele procura a folha recursivamente
             */
            Chave[] chaves = nodo.getChaves();
            int contaChaves = nodo.getSomaChaves();
            for (int i = 0; i < contaChaves; i++) {
                if (novaChave.getChave() < chaves[i].getChave()) {
                    chaveRetorno = insereChave(novaChave, chaves[i].getAnterior());
                    break;
                } else if (i == contaChaves - 1) {
                    chaveRetorno = insereChave(novaChave, chaves[i].getProximo());
                    break;
                }
            }

            if (chaveRetorno != null) {
                chaveRetorno = nodo.insereChave(chaveRetorno);
            }
        }

        return chaveRetorno;
    }
    // insere chave
    public void insereChave(int chave) {
        Chave novaChave = new Chave(chave);
        novaChave = insereChave(novaChave, nod);
        if (novaChave != null) {
            Nodo novoNodo = new Nodo(ordem);
            novoNodo.insereChave(novaChave);
            nod = novoNodo;
        }
    }
 // busca chave
    public boolean buscaChave(int value) {
        return nod.buscaChave(new Chave(value), nod);
    }
 // remove chave
    public void removeChave(int key) {
        nod.removeKeyHard(new Chave(key));
    }

    public String imprimeChave() {
        String retorno = "";

        String[] nodosComNivel = nod.imprimeNodo(nod, 0).split("\n");
        int nivelMax = 0;
        String[][] niveis = new String[10][100];
        int[] contaNiveis = new int[10];
        for (int i = 0; i < nodosComNivel.length; i++) {
            String[] divisao = nodosComNivel[i].split("-");
            int nivel = Integer.parseInt("" + divisao[divisao.length - 1].charAt(1)) - 1;
            niveis[nivel][contaNiveis[nivel]++] = divisao[0];
            if (nivel > nivelMax) {
                nivelMax = nivel;
            }
        }

        String tabInicial = "";
        for (int i = 0; i <= nivelMax; i++) {
            tabInicial += "";
            retorno += "\n";
            for (int j = 0; j < contaNiveis[i]; j++) {
                retorno += tabInicial + niveis[i][j] + "\t\t";
            }
        }

        nod.nivel = 1;
        nod.imprime = "";
        return retorno;
    }
    
    public String imprimeChaveGravacao() {
        String retorno = "";

        String[] nodosComNivel = nod.imprimeNodo(nod, 0).split("\n");
        int nivelMax = 0;
        String[][] niveis = new String[10][100];
        int[] contaNiveis = new int[10];
        for (int i = 0; i < nodosComNivel.length; i++) {
            String[] divisao = nodosComNivel[i].split("-");
            int nivel = Integer.parseInt("" + divisao[divisao.length - 1].charAt(1)) - 1;
            niveis[nivel][contaNiveis[nivel]++] = divisao[0];
            if (nivel > nivelMax) {
                nivelMax = nivel;
            }
        }

        String tabInicial = "";
        for (int i = 0; i <= nivelMax; i++) {
            tabInicial += "";

            for (int j = 0; j < contaNiveis[i]; j++) {
                retorno += tabInicial + niveis[i][j];
            }
        }

        nod.nivel = 1;
        nod.imprime = "";
        return retorno;
    }

   
}
