import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

class Jogador{

    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String uni;
    private int Nasc;
    private String cidade;
    private String estado;

    public Jogador(){
        id = -1;
        nome = "";
        altura = -1;
        peso = -1;
        uni = "";
        Nasc = -1;
        cidade = "";
        estado = "";
    }
    public Jogador(int id, String nome, int altura, int peso, String uni, int Nasc, String cidade, String estado){
        this.id = id;
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
        this.uni = uni;
        this.Nasc = Nasc;
        this.cidade = cidade;
        this.estado = estado;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setAltura(int altura){
        this.altura = altura;
    }
    public void setPeso(int peso){
        this.peso = peso;
    }
    public void setUni (String uni){
        this.uni = uni;
    }
    public void setNasc (int Nasc){
        this.Nasc = Nasc;
    }
    public void setCidade(String cidade){
        this.cidade = cidade;
    }
    public void setEstado (String estado){
        this.estado = estado;
    }
    public int getId (){
        return id;
    }
    public int getAltura (){
        return altura;
    }
    public int getPeso (){
        return peso;
    }
    public int getNasc (){
        return Nasc;
    }
    public String getNome(){
        return nome;
    }
    public String getUni(){
        return uni;
    }
    public String getCidade(){
        return cidade;
    }
    public String getEstado(){
        return estado;
    }
    public void imprimir(){
        String altura ="" +this.altura;
        String peso =""+ this.peso;
        String Nasc = ""+ this.Nasc;
        String nome = this.nome;
        String uni = this.uni;
        String cidade = this.cidade;
        String estado = this.estado;
        if(this.altura == 0){
           altura = "nao informado";
        }
        if(this.peso == 0){
           peso = "nao informado";
        }
        if(this.Nasc == 0){
           Nasc = "nao informado";
        }
        if(this.nome.length()==0){
            nome = "nao informado";
        }
        if(this.uni.length()==0){
            uni = "nao informado";
        }
        if(this.cidade.length()==0){
            cidade = "nao informado";
        }
        if(this.estado.length()==0){
            estado = "nao informado";
        }
        System.out.println(" ## "+nome+" ## "+altura+" ## "+peso+" ## "+Nasc+" ## "+uni+ " ## "+cidade + " ## "+estado+" ##");
    }
    public static ArrayList<Jogador> ler(){
        Arq.openRead("/tmp/players.csv");
        ArrayList<Jogador> listares = new ArrayList<Jogador>();
        while(Arq.hasNext()==true){
            String str = Arq.readLine();
            int j = 0;
            int id = 0;
            while(str.charAt(j) != ','){
                id *=10;
                id += ((int)str.charAt(j)) - 48;
                j++;
            }
            j++;
            String nome = "";
            while(str.charAt(j) != ','){
                nome += str.charAt(j);
                j++;
            }
            j++;
            int altura = 0;
            while(str.charAt(j) != ','){
                altura *=10;
                altura += ((int)str.charAt(j)) - 48;
                j++;
            }
            j++;
            int peso = 0;
            while(str.charAt(j) != ','){
                peso *=10;
                peso += ((int)str.charAt(j)) - 48;
                j++;
            }
            j++;
            String uni = "";
            while(str.charAt(j) != ','){
                uni += str.charAt(j);
                j++;
            }
            j++;
            int Nasc = 0;
            while(str.charAt(j) != ','){
                Nasc *=10;
                Nasc += ((int)str.charAt(j)) - 48;
                j++;
            }
            j++;
            String cidade = "";
            while(str.charAt(j) != ','){
                cidade += str.charAt(j);
                j++;
            }
            j++;
            String estado = "";
            while(j < str.length()){
                estado += str.charAt(j);
                j++;
            }
            listares.add(new Jogador(id,nome,altura, peso, uni, Nasc, cidade, estado));
        }
        Arq.close();
        return listares;
    }
}

class Celula{
    private Jogador elemento;
    private Celula prox;

    //Construtor
    public Celula(){
        this(null);
    }

    public Celula(Jogador p){
        this.elemento = p;
        this.prox = null;
    }

    //gets
    public Jogador getElemento(){
        return this.elemento;
    }
    public Celula getProx(){
        return this.prox;
    }
    //sets
    public void setElemento(Jogador p){
        this.elemento = p;
    }
    public void setProx(Celula prox){
        this.prox = prox;
    }

}

class Pilha{
    private Celula inicio;
    private Celula fim;
    private int tamanho;

    public Pilha(){
     inicio = new Celula();
     fim = inicio;
     tamanho = 0;
    }

    //insert



    public void InsertFinal(Jogador x){

        Celula temp = new Celula(x);
        fim.setProx(temp);
        fim = temp;
        tamanho++;

    }


    //remove



    public void RemoveFinal (){
        Celula temp = inicio;
        while(temp.getProx() != fim){
            temp = temp.getProx();
        }
        System.out.println("(R) " +(fim.getElemento()).getNome());
        fim = temp;
        temp.setProx(null);
        tamanho--;
    }



    //gets
    public Celula getInicio(){
        return this.inicio;
    }
    public int getTamanho(){
        return this.tamanho;
    }

}

public class Principal{

    public static boolean isFIM (String word){
        String end = "FIM";
        boolean result = true;
        int len = end.length();
        if(word.length() == len){
            for(int z = 0; z < len; z++){
                if(end.charAt(z) != word.charAt(z)){
                    result = false;
                    z = len;
                }
            }
        } else{
            result = false;
        }
        return result;
    }
    public static int toNumber(String num){
        int res = 0;
        int j = 0;
        while(j < num.length()){
            res*=10;
            res += ((int) num.charAt(j)) - 48;
            j++;
        }
        return res;
    }
    public static void main (String[] args){
        ArrayList<Jogador> lista = Jogador.ler();
        Pilha list = new Pilha();
        String entrada;
        while(!isFIM(entrada = MyIO.readLine())){
            int num = toNumber(entrada);
            list.InsertFinal(lista.get(num + 1));
        }



        int IorR = toNumber(MyIO.readLine());
        Jogador[] array = new Jogador[3923];
        lista.toArray(array);
        for(int i = 0; i < IorR - 1; i++){
            String metodo = MyIO.readLine();

            if(metodo.charAt(0) == 'R'){
                    list.RemoveFinal();


            } else if (metodo.charAt(0) == 'I'){
                    String[] a = metodo.split(" ");
                    if(a.length > 1){
                            int indr = toNumber(a[1]) + 1;
                            Jogador x = array[indr];
                            list.InsertFinal(x);
                    } else{
                        //System.out.println("GERSON");
                    }
            }

        }
        char m = MyIO.readChar();

            if(m == 'R'){
                    list.RemoveFinal();


            } else if (m == 'I'){
                    //String[] a = metodo.split(" ");
                    //if(a.length > 1){
                            int indr = 80;
                            Jogador x = array[indr];
                            list.InsertFinal(x);
                    //} else{
                        //System.out.println("GERSON");
                    //}
            }

        Celula index = list.getInicio();
        for(int i = 0; i < list.getTamanho(); i++){
            System.out.print("["+i+"]");
            index = index.getProx();
            (index.getElemento()).imprimir();
        }

    }
}
