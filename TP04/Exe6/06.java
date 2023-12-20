import java.util.*;
import java.io.*;

public class Arv {
    public static void main(String[] args) {

        String[] entrada1 = new String[1000];

        int numEntrada1 = 0;
        // Leitura da entrada padrao
        do {
            entrada1[numEntrada1] = MyIO.readLine();
        } while (isFim(entrada1[numEntrada1++]) == false);
        numEntrada1--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids1[] = new int[numEntrada1];

        for (int i = 0; i < numEntrada1; i++) {
            ids1[i] = Integer.parseInt(entrada1[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[1000];

        int numEntrada2 = 0;
        // Leitura da entrada padrao
        do {
            entrada2[numEntrada2] = MyIO.readLine();
        } while (isFim(entrada2[numEntrada2++]) == false);
        numEntrada2--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids2[] = new int[numEntrada2];

        for (int i = 0; i < numEntrada2; i++) {
            ids2[i] = Integer.parseInt(entrada2[i]); // transformando id em inteiro
        }

        String[] linhas = new String[4000];

        try {
            linhas = ler(); // leitura das linhas do arquivo
        } catch (Exception e) {
            MyIO.println(e.getMessage());
        }

        ArvoreTrie arvore1 = new ArvoreTrie();
        ArvoreTrie arvore2 = new ArvoreTrie();
        ArvoreTrie arvoreFinal = new ArvoreTrie();

        long tempo = System.currentTimeMillis();

        for (int i = 0; i < numEntrada1; i++) {
            Jogador j = new Jogador(linhas[ids1[i]]);
            MyIO.println(j.getId() + "\t" + j.getNome());
            arvore1.inserir(j.getNome());
        }

        for (int i = 0; i < numEntrada2; i++) {
            Jogador j = new Jogador(linhas[ids2[i]]);
            MyIO.println(j.getId() + "\t" + j.getNome());
            arvore2.inserir(j.getNome());
        }

        arvoreFinal.merge(arvore1, arvore2);

        int numEntrada = 0;
        String[] entrada = new String[1000];

        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        // arvoreFinal.mostrar();

        for (int i = 0; i < numEntrada; i++) {
            try{
                if (arvoreFinal.pesquisar(entrada[i]))
                    MyIO.println(entrada[i] + " SIM");
                else
                    MyIO.println(entrada[i] + " NAO");
            } catch (Exception e){}
        } 

        tempo = (System.currentTimeMillis()- tempo);
        Arq.openWrite("matricula_trie.txt");
        Arq.print("804762\t" + arvoreFinal.comp + "\t" + tempo + "ms");
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
    public char elemento;
    public int tamanho = 255;
    public No[] prox;
    public boolean folha;

    public No(char elemento) {
        this.elemento = elemento;
        prox = new No[tamanho];
        for (int i = 0; i < tamanho; i++)
            prox[i] = null;
        folha = false;
    }

    public static int hash(char x) {
        return (int) x;
    }
}

class ArvoreTrie {
    public No raiz;
    public int comp = 0;

    public ArvoreTrie() {
        raiz = new No('_');
    }

    public void inserir(String s){
        inserir(s, raiz, 0);
    }

    private void inserir(String s, No no, int i){
        if (no.prox[s.charAt(i)] == null) {
            no.prox[s.charAt(i)] = new No(s.charAt(i));

            if (i == s.length() - 1) {
                no.prox[s.charAt(i)].folha = true;
            } else {
                inserir(s, no.prox[s.charAt(i)], i + 1);
            }

        } else if (no.prox[s.charAt(i)].folha == false && i < s.length() - 1) {
            inserir(s, no.prox[s.charAt(i)], i + 1);
        }
    }

    public boolean pesquisar(String s) throws Exception {
        return pesquisar(s, raiz, 0);
    }

    public boolean pesquisar(String s, No no, int i) throws Exception {
        boolean resp;
        if(no.prox[s.charAt(i)] == null){
            resp = false;
            comp ++;
        } else if(i == s.length() - 1){
            resp = (no.prox[s.charAt(i)].folha == true);
            comp ++;
        } else if(i < s.length() - 1 ){
            resp = pesquisar(s, no.prox[s.charAt(i)], i + 1);
            comp ++;
        } else {
            throw new Exception("Erro ao pesquisar!");
        }
        return resp;
    }

    public void merge(ArvoreTrie a1, ArvoreTrie a2){
        this.concatenar(a1);
        this.concatenar(a2);
    }

    public void concatenar(ArvoreTrie x){
        concatenar("", x.raiz);
    }

    public void concatenar(String s, No no) {
        if(no.folha == true){
            this.inserir(s += no.elemento);
        } else {
            for(int i = 0; i < no.tamanho; i++){
                if(no.prox[i] != null){
                    concatenar(s + (no.elemento == '_' ? "": no.elemento), no.prox[i]);
                }
            }
        }
    }

    public void mostrar(){
        mostrar("", raiz);
    }

    public void mostrar(String s, No no) {
        if(no.folha == true){
            System.out.println("Palavra: " + (s + no.elemento));
        } else {
            for(int i = 0; i < no.tamanho; i++){
                if(no.prox[i] != null){
                    MyIO.println(no.prox[i].elemento + " " + i);
                    mostrar(s + no.elemento, no.prox[i]);
                }
            }
        }
    }
}