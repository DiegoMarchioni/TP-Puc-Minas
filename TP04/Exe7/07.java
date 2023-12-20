
import java.io.*;

public class Arv {

    public static void main(String[] args){
        String[] entrada = new String[1000];
        int numEntrada = 0, numEntrada2 = 0;       
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

        tabelaHash tabela = new tabelaHash();

        for(int i = 0; i < numEntrada; i++){
            Jogador j = new Jogador(entrada2[ids[i]]); // linhas dos ids do pub in -> jogadores
            tabela.inserirTab(j);
        }

        String[] entrada3 = new String[1000];
        // Leitura da entrada padrao
        do {
            entrada3[numEntrada2] = MyIO.readLine();
            //System.out.println(entrada3[numEntrada2]);
        } while (isFim(entrada3[numEntrada2++]) == false);
        numEntrada2--; // Desconsiderar ultima linha contendo a palavra FIM

        long tempo = System.currentTimeMillis();

        for(int i = 0; i < numEntrada2; i++){
            MyIO.print(entrada3[i] + " ");
            if(tabela.procurar(entrada3[i]) == null){
                MyIO.println("NAO");
            } else {
                MyIO.println("SIM");
            }
        }

        tempo = (System.currentTimeMillis()- tempo);
        Arq.openWrite("matricula_hashReserva.txt");
        Arq.print("804762\t" + tabela.comp + "\t" + tempo + "ms");
        Arq.close();  

        //tabela.mostrar();
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

/**
 * arvoreBinaria
 */ 
class tabelaHash {
    public Jogador[] tabela;
    public Jogador[] areaDeReserva;
    public int qtdAreaReserva;
    public int comp = 0;

    public tabelaHash(){
        tabela = new Jogador[21];
        areaDeReserva = new Jogador[9];
    }

    public void inserirTab(Jogador jogador){
        if(tabela[hash(jogador.altura)]  == null){
            tabela[hash(jogador.altura)] = jogador;
        } else {
            try{
                inserirAreaReserva(jogador);
            } catch(Exception e){

            }
        }
    }

    private void inserirAreaReserva(Jogador jogador) throws Exception{
        for(int i = 0; i < 9; i++){
            if(areaDeReserva[i] == null){
                areaDeReserva[i] = jogador;
                break;
            }
        }
    }
    
    public void mostrar(){
        for(int i = 0; i < 21; i++){
            MyIO.println(tabela[i] + "");
        }
        MyIO.println("---------------");
        for(int i = 0; i < 9; i++){
            MyIO.println(areaDeReserva[i] + "");
        }
    }



    public Jogador procurar(String nome){
        for(int i = 0; i < 21; i++){
            if(tabela[i] != null && nome.compareTo(tabela[i].nome) == 0){
                comp ++;
                return tabela[i];
            }
        }
        for(int i = 0; i < 9; i++){
            comp ++;
            if(nome.compareTo(areaDeReserva[i].nome) == 0){
                return areaDeReserva[i];
            }
        }
        return null;
    }

    public int hash(int tamanho){
        return tamanho % 21;
    }
}   



class No{
    public Jogador elemento;
    public No dir;
    public No esq;
    //Construtor vazio
    public No(){
        this(null);
    }
    //Construtor 
    public No(Jogador jogador){
        elemento = jogador;
        dir = null;
        esq = null;
    }

}

//class Jogador
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
        String str = " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " +getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + " ##";
        return str;
    }
}