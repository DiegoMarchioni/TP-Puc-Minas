import java.io.*;

public class Principal {

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

        String[] entrada2 = new String[1000];

        try{
            entrada2 = ler(); // leitura das linhas do arquivo
        }catch(Exception e){
            MyIO.println(e.getMessage());
        }

        ListaDupla lista = new ListaDupla(); 

        for(int i = 0; i < numEntrada; i++){
            Jogador j = new Jogador(entrada2[ids[i]]); // linhas dos ids do pub in -> jogadores
            lista.inserirFim(j);
        }
        
        lista.ordenar();

        

        lista.mostrar();
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
    int id;
    String nome;
    int altura;
    int peso;
    String universidade;
    int anoNascimento;
    String cidadeNascimento;
    String estadoNascimento;

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
        String str = getId() +" ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " +getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento();
        return str;
    }
}

class Celula {
	public Jogador jogador; // Elemento inserido na celula.
    public Celula prox; // Aponta a celula prox.
    public Celula ant;

	public Celula() {
		this(null);
	}

	public Celula(Jogador jogador) {
      this.jogador = jogador;
      this.prox = null;
      this.ant = null;
	}
}

class ListaDupla { // CONTÉM NÓ CABEÇA -> posição 0 é o primeiro elemento inserido (ignora-se o nó)

    private Celula primeiro, ultimo;

    public ListaDupla() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    // INSERÇÕES --------------------------------------------------------

    public void inserirFim(Jogador j) { // igual o inserir da Fila -> fila insere no final
        ultimo.prox = new Celula(j);
        ultimo.prox.ant = ultimo;
        ultimo = ultimo.prox;
    }
    
    //QUICKSORT
    
// Método público para iniciar o QuickSort
public void ordenar() {
    quicksort(primeiro.prox, ultimo);
}

// Método para realizar a troca de dois nós na lista
private void trocar(Celula celula1, Celula celula2) {
    Jogador temp = celula1.jogador;
    celula1.jogador = celula2.jogador;
    celula2.jogador = temp;
}

// Método para realizar o particionamento na lista
private Celula particionar(Celula esq, Celula dir) {
    Jogador pivo = dir.jogador;
    Celula i = esq;
    Celula j = esq;

    while (j != dir) {
        if (j.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) < 0 ||
                (j.jogador.getEstadoNascimento().compareTo(pivo.getEstadoNascimento()) == 0 &&
                        j.jogador.getNome().compareTo(pivo.getNome()) < 0)) {
            trocar(i, j);
            i = i.prox;
        }
        j = j.prox;
    }

    trocar(i, dir);
    return i;
}

// Método principal do QuickSort
private void quicksort(Celula esq, Celula dir) {
    if (esq != null && dir != null && esq != dir && esq != dir.prox) {
        Celula pivo = particionar(esq, dir);
        quicksort(esq, pivo.ant);
        quicksort(pivo.prox, dir);
    }
}
    
    
    // EXTRA -----------------------------------------------------------------

    public void mostrar() {
        int j = 0;
		    for (Celula i = primeiro.prox; i != null; i = i.prox) {
                System.out.println("[" + i.jogador.toString() + "]");
                j++;
		    }
	  }

    public int tamanho() {
        int tamanho = 0;
        for (Celula i = primeiro; i != ultimo; i = i.prox, tamanho++);
        return tamanho;
    }
}
