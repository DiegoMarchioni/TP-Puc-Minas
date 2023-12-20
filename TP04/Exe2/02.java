import java.io.*;

public class ArvBin2 {
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

        ArvoreArvore arvore = new ArvoreArvore();

        int[] v = { 7, 3, 11, 1, 5, 9, 12, 0, 2, 4, 6, 8, 10, 13, 14 };

        for (int i = 0; i < v.length; i++) {
            arvore.inserir(v[i]);
        }

        for (int i = 0; i < numEntrada; i++) {
            Jogador j = new Jogador(entrada2[ids[i]]);
            if (arvore.pesquisar(j.getNome(), false) == false)
                arvore.inserirNome(j);
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        // arvore.caminharPre();
        // arvore.raiz.esq.arvoreSecundaria.caminharPre();
        long tempo = System.currentTimeMillis();
        for (int i = 0; i < numEntrada; i++) {
            arvore.pesquisar(entrada[i], true);
        }

        tempo = (System.currentTimeMillis()- tempo);
        Arq.openWrite("matricula_arvoreBinaria.txt");
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

class No {
    public int id; // Conteudo do no.
    public No esq, dir; // Filhos da esq e dir
    public ArvoreBinaria arvoreSecundaria;

    public No(int id) {
        this(id, null, null);
    }

    public No(int id, No esq, No dir) {
        this.id = id;
        this.esq = esq;
        this.dir = dir;
        this.arvoreSecundaria = new ArvoreBinaria();
    }
}

class No2 {
    public String nome; // Conteudo do no.
    public No2 esq, dir; // Filhos da esq e dir

    public No2(String nome) {
        this(nome, null, null);
    }

    public No2(String nome, No2 esq, No2 dir) {
        this.nome = nome;
        this.esq = esq;
        this.dir = dir;
    }
}

class ArvoreArvore {

    public No raiz; // Raiz da arvore
    public int comp = 0;

    public ArvoreArvore() {
        raiz = null;
    }

    public void caminharPre() {
        caminharPre(raiz);
    }

    private void caminharPre(No i) {
        if (i != null) {
            System.out.println(i.id + " "); // Conteudo do no.
            caminharPre(i.esq); // mod15s da esquerda.
            caminharPre(i.dir); // mod15s da direita.
        }
    }

    public void inserir(int id) {
        raiz = inserir(id, raiz);
    }

    private No inserir(int x, No i) {
        if (i == null) {
            comp ++;
            i = new No(x);

        } else if (x < i.id) {
            comp ++;
            i.esq = inserir(x, i.esq);

        } else if (x > i.id) {
            comp ++;
            i.dir = inserir(x, i.dir);
        }

        return i;
    }

    public boolean pesquisar(String x, boolean print) {

        if (print) {
            comp ++;
            MyIO.print(x + " raiz ");
        }

        boolean resp = pesquisar(x, raiz, print);

        if (resp == false && print){
            comp ++;
            MyIO.println("NAO");
        }

        return resp;

    }

    private boolean pesquisar(String x, No i, boolean print) {
        boolean resp;

        if (i == null) {
            comp ++;
            resp = false;
        } else if (i.arvoreSecundaria.pesquisar(x, print)) {
            comp ++;
            resp = true;
        } else {
            if (print){
                comp ++;
                MyIO.print("esq ");
            }
            resp = pesquisar(x, i.esq, print);

            if (resp == false) {
                comp ++;
                if (print){
                    MyIO.print("dir ");
                    comp ++;
                }
                resp = pesquisar(x, i.dir, print);
            }
        }

        return resp;
    }

    public void inserirNome(Jogador jogador) {
        inserirNome(jogador, raiz);
    }

    private void inserirNome(Jogador x, No i) {

        int mod15 = x.getAltura() % 15;

        if (mod15 == i.id) {
            i.arvoreSecundaria.inserir(x.getNome());

        } else if (mod15 < i.id) {
            inserirNome(x, i.esq);

        } else if (mod15 > i.id) {
            inserirNome(x, i.dir);
        }
    }
}

class ArvoreBinaria {
    private No2 raiz; // Raiz da arvore.

    public ArvoreBinaria() {
        raiz = null;
    }

    public void caminharPre() {
        caminharPre(raiz);
    }

    private void caminharPre(No2 i) {
        if (i != null) {
            System.out.println(i.nome + " "); // Conteudo do no.
            caminharPre(i.esq); // Elementos da esquerda.
            caminharPre(i.dir); // Elementos da direita.
        }
    }

    public void inserir(String x) {
        raiz = inserir(x, raiz);
    }

    private No2 inserir(String x, No2 i) {
        if (i == null) {
            i = new No2(x);
        } else if (x.compareTo(i.nome) < 0) {
            i.esq = inserir(x, i.esq);

        } else if (x.compareTo(i.nome) > 0) {
            i.dir = inserir(x, i.dir);
        }
        return i;
    }

    public boolean pesquisar(String x, boolean print) {
        return pesquisar(x, raiz, print);
    }

    private boolean pesquisar(String x, No2 i, boolean print) {

        boolean resp;
        
        if (i == null) {
            resp = false;
        } else if (x.compareTo(i.nome) == 0) {
            resp = true;
            MyIO.println("SIM");
        } else {
            if (print) MyIO.print("ESQ ");
            resp = pesquisar(x, i.esq, print);

            if (resp == false) {
                if (print)
                    MyIO.print("DIR ");
                resp = pesquisar(x, i.dir, print);
            }
        }

        return resp;
    }
}