import java.io.*;


public class RubroNegra {
    public static void main(String[] args) {

        String[] entrada = new String[1000];
        int numEntrada = 0;
        // Leitura da entrada padrao
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids[] = new int[numEntrada];

        for (int i = 0; i < numEntrada; i++) {
            ids[i] = Integer.parseInt(entrada[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[4000];

        try {
            entrada2 = ler(); // leitura das linhas do arquivo
        } catch (Exception e) {
            MyIO.println(e.getMessage());
        }

        Alvinegra arvore = new Alvinegra();

        for (int i = 0; i < numEntrada; i++) {
            Jogador j = new Jogador(entrada2[ids[i]]);
            arvore.inserir(j);
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM
        long tempo = System.currentTimeMillis();
        for (int i = 0; i < numEntrada; i++) {
            if (arvore.pesquisar(entrada[i]))
                MyIO.println("SIM");
            else
                MyIO.println("NAO");
        }

        tempo = (System.currentTimeMillis()- tempo);
        Arq.openWrite("matricula_alvinegra.txt");
        Arq.print("804762\t" + arvore.comp + "\t" + tempo + "ms");
        Arq.close();

    }

    public static boolean isFim(String s) {
        return (s.length() >= 3 && s.charAt(0) == 'F' && s.charAt(1) == 'I' && s.charAt(2) == 'M');
    }

    public static String[] ler() throws Exception {

        String[] entrada = new String[4000];
        int numEntrada = 0;
        File file = new File("/tmp/players.csv");

        BufferedReader br = new BufferedReader(new FileReader(file));
        // Leitura da entrada padrao
        String lixo = br.readLine();
        do {
            entrada[numEntrada] = br.readLine();
        } while (entrada[numEntrada++] != null);
        numEntrada--;

        br.close();
        return entrada;
    }
}

class Jogador {
    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;

    public Jogador() {
    }

    public Jogador(String linha) {
        String campos[] = linha.split(",");
        this.id = Integer.parseInt(campos[0]);
        this.nome = campos[1];
        this.altura = Integer.parseInt(campos[2]);
        this.peso = Integer.parseInt(campos[3]);
        this.universidade = (campos[4].isEmpty()) ? "nao informado" : campos[4];
        this.anoNascimento = Integer.parseInt(campos[5]);
        if (campos.length > 6) {
            this.cidadeNascimento = (campos[6].isEmpty()) ? "nao informado" : campos[6];
            if (campos.length < 8) {
                this.estadoNascimento = "nao informado";
            } else {
                this.estadoNascimento = campos[7];
            }
        } else {
            this.cidadeNascimento = "nao informado";
            this.estadoNascimento = "nao informado";
        }
    }

    // id,Player,height,weight,collage,born,birth_city,birth_state
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento() {
        return anoNascimento;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    public void setEstadoNascimento(String estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public void clone(Jogador J) {

        this.setId(J.getId());
        this.setCidadeNascimento(J.getCidadeNascimento());
        this.setEstadoNascimento(J.getEstadoNascimento());
        this.setNome(J.getNome());
        this.setAltura(J.getAltura());
        this.setPeso(J.getPeso());
        this.setAnoNascimento(J.getAnoNascimento());
        this.setUniversidade(J.getUniversidade());

    }

    public String toString() {
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## "
                + getAnoNascimento() + " ## " + getUniversidade() + " ## " + getCidadeNascimento() + " ## "
                + getEstadoNascimento() + "]";
        return str;
    }
}

class NoAN {
    public boolean cor;
    public Jogador elemento;
    public NoAN esq, dir;

    public NoAN() {
        this(null);
    }

    public NoAN(Jogador elemento) {
        this(elemento, false, null, null);
    }

    public NoAN(Jogador elemento, boolean cor) {
        this(elemento, cor, null, null);
    }

    public NoAN(Jogador elemento, boolean cor, NoAN esq, NoAN dir) {
        this.cor = cor;
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

class Alvinegra {

    private NoAN raiz; // Raiz da arvore.
    public int comp = 0;

    public Alvinegra() {
        raiz = null;
    }

	public boolean pesquisar(String x) {
        MyIO.print(x + " raiz ");
		return pesquisar(x, raiz);
	}

	private boolean pesquisar(String x, NoAN i) {
        boolean resp;
        if (i == null) {
            resp = false;
            comp ++;

        } else if (x.compareTo(i.elemento.getNome()) == 0) {
            resp = true;
            comp ++;
        } else if (x.compareTo(i.elemento.getNome()) < 0) {
            MyIO.print("esq ");
            comp ++;
            resp = pesquisar(x, i.esq);

        } else {
            comp ++;
            MyIO.print("dir ");
            resp = pesquisar(x, i.dir);
        }
        return resp;
    }

    public void inserir(Jogador elemento){

        // Se a arvore estiver vazia
        if (raiz == null) {
            raiz = new NoAN(elemento, false);

            // Senao, se a arvore tiver um elemento
        } else if (raiz.esq == null && raiz.dir == null) {
            if (raiz.elemento.getNome().compareTo(elemento.getNome()) > 0) {
                raiz.esq = new NoAN(elemento, true);
            } else {
                raiz.dir = new NoAN(elemento, true);
            }

            // Senao, se a arvore tiver dois elementos (raiz e dir)
        } else if (raiz.esq == null) {
            if ((raiz.elemento.getNome().compareTo(elemento.getNome()) > 0)) {
                raiz.esq = new NoAN(elemento);
            } else if ((raiz.dir.elemento.getNome().compareTo(elemento.getNome()) > 0)) {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = elemento;
            } else {
                raiz.esq = new NoAN(raiz.elemento);
                raiz.elemento = raiz.dir.elemento;
                raiz.dir.elemento = elemento;
            }

            raiz.esq.cor = raiz.dir.cor = false;

            // Senao, se a arvore tiver dois elementos (raiz e esq)
        } else if (raiz.dir == null) {

            if ((raiz.elemento.getNome().compareTo(elemento.getNome()) < 0)) {
                raiz.dir = new NoAN(elemento);
            } else if ((raiz.esq.elemento.getNome().compareTo(elemento.getNome()) < 0)) {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = elemento;
            } else {
                raiz.dir = new NoAN(raiz.elemento);
                raiz.elemento = raiz.esq.elemento;
                raiz.esq.elemento = elemento;
            }

            raiz.esq.cor = raiz.dir.cor = false;

            // Senao, a arvore tem tres ou mais elementos
        } else {
            inserir(elemento, null, null, null, raiz);
        }

        raiz.cor = false;
    }

    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) {

        // Se o pai tambem e preto, reequilibrar a arvore, rotacionando o avo
        if (pai.cor == true) {

            // 4 tipos de reequilibrios e acoplamento
            if (pai.elemento.getNome().compareTo(avo.elemento.getNome()) > 0) { // rotacao a esquerda ou direita-esquerda
                if (i.elemento.getNome().compareTo(pai.elemento.getNome()) > 0 ) {
                    avo = rotacaoEsq(avo);
                } else {
                    avo = rotacaoDirEsq(avo);
                }

            } else { // rotacao a direita ou esquerda-direita
                if (i.elemento.getNome().compareTo(pai.elemento.getNome()) < 0 ) {
                    avo = rotacaoDir(avo);
                } else {
                    avo = rotacaoEsqDir(avo);
                }
            }

            if (bisavo == null) {
                raiz = avo;
            } else {
                if ((avo.elemento.getNome().compareTo(bisavo.elemento.getNome()) < 0 )) {
                    bisavo.esq = avo;
                } else {
                    bisavo.dir = avo;
                }
            }

            // reestabelecer as cores apos a rotacao
            avo.cor = false;
            avo.esq.cor = avo.dir.cor = true;
        } // if(pai.cor == true)
    }

    private void inserir(Jogador elemento, NoAN bisavo, NoAN avo, NoAN pai, NoAN i){
        if (i == null) {

            if (elemento.getNome().compareTo(pai.elemento.getNome()) < 0) {
                i = pai.esq = new NoAN(elemento, true);
            } else {
                i = pai.dir = new NoAN(elemento, true);
            }

            if (pai.cor == true) {
                balancear(bisavo, avo, pai, i);
            }

        } else {

            // Achou um 4-no: eh preciso fragmeta-lo e reequilibrar a arvore
            if (i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true) {
                i.cor = true;
                i.esq.cor = i.dir.cor = false;
                if (i == raiz) {
                    i.cor = false;
                } else if (pai.cor == true) {
                    balancear(bisavo, avo, pai, i);
                }
            }
            if (elemento.getNome().compareTo(i.elemento.getNome()) < 0)  {
                inserir(elemento, avo, pai, i, i.esq);
            } else if ((elemento.getNome().compareTo(i.elemento.getNome()) > 0) ) {
                inserir(elemento, avo, pai, i, i.dir);
            }
        }
    }

    private NoAN rotacaoDir(NoAN no) {
        NoAN noEsq = no.esq;
        NoAN noEsqDir = noEsq.dir;

        noEsq.dir = no;
        no.esq = noEsqDir;

        return noEsq;
    }

    private NoAN rotacaoEsq(NoAN no) {
        NoAN noDir = no.dir;
        NoAN noDirEsq = noDir.esq;

        noDir.esq = no;
        no.dir = noDirEsq;
        return noDir;
    }

    private NoAN rotacaoDirEsq(NoAN no) {
        no.dir = rotacaoDir(no.dir);
        return rotacaoEsq(no);
    }

    private NoAN rotacaoEsqDir(NoAN no) {
        no.esq = rotacaoEsq(no.esq);
        return rotacaoDir(no);
    }
}