import java.io.*;

public class ArvBin {
    public static void main(String[] args){

        String[] entrada = new String[1000];
        int numEntrada = 0;       
        // Leitura da entrada padrao
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids[] = new int[numEntrada];

        for(int i = 0; i < numEntrada; i++){
            ids[i] = Integer.parseInt(entrada[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[4000];

        try{
            entrada2 = ler(); // leitura das linhas do arquivo
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }

        ArvoreBinaria arvore = new ArvoreBinaria(); 
        long tempo = System.currentTimeMillis();
        for (int i = 0; i < numEntrada; i++) {
            Jogador j = new Jogador(entrada2[ids[i]]);
            arvore.inserir(j);
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        arvore.caminharCentral();
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
            this.cidadeNascimento = (campos[6].isEmpty())? "nao informado": campos[6];
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

    public void setAnoNascimento(int anoNascimento){
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento(){
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
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " + getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + "]";
        return str;
    }
}

class No {
	public Jogador elemento; // Conteudo do no.
	public No esq, dir;  // Filhos da esq e dir.

	public No(Jogador elemento) {
		this(elemento, null, null);
	}

	public No(Jogador elemento, No esq, No dir) {
		this.elemento = elemento;
		this.esq = esq;
		this.dir = dir;
	}
}

class ArvoreBinaria {
	private No raiz; // Raiz da arvore.
    public int comp = 0;

	public ArvoreBinaria() {
		raiz = null;
	}


	public void inserir(Jogador x){
		raiz = inserir(x, raiz);
	}

	private No inserir(Jogador x, No i){
		if (i == null) {
         i = new No(x);
         comp ++;

        } else if (x.getNome().compareTo(i.elemento.getNome()) < 0) {
            i.esq = inserir(x, i.esq);
            comp ++;
        } else if (x.getNome().compareTo(i.elemento.getNome()) > 0) {
            i.dir = inserir(x, i.dir);
            comp ++;
        }

		return i;
    }
    
    public void caminharCentral(){
        caminharCentral(raiz);
    }

    private void caminharCentral(No i){
        if(i != null){
            caminharCentral(i.esq);
            MyIO.println(i.elemento.getNome() + " ");
            caminharCentral(i.dir);
        }
    }
}