public class Principal {
    public static void main(String[] args){
        int x = MyIO.readInt();
        for(int i = 0; i < x; i++){
            int linhas = MyIO.readInt(), colunas = MyIO.readInt(), mat[][] = new int[linhas][colunas];
            for(int a = 0; a < linhas; a++){
                for(int b = 0; b < colunas; b++){
                    mat[a][b] = MyIO.readInt();
                }
            }
            Matriz matriz = new Matriz(linhas,colunas, mat);
            matriz.mostrarDiagonalPrincipal();
            matriz.mostrarDiagonalSecundaria();

            int linhas2 = MyIO.readInt(), colunas2 = MyIO.readInt(), mat2[][] = new int[linhas][colunas];
            for(int a = 0; a < linhas2; a++){
                for(int b = 0; b < colunas2; b++){
                    mat2[a][b] = MyIO.readInt();
                }
            }
            Matriz matriz2 = new Matriz(linhas,colunas, mat2);

            matriz.soma(matriz2);
            matriz.multiplicacao(matriz2);

            //matriz.mostrar();
            //matriz2.mostrar();
        }

    }
}




class Celula {
    public int elemento;
    public Celula inf, sup, esq, dir;

    public Celula() {
        this(0, null, null, null, null);
    }

    public Celula(int elemento) {
        this(elemento, null, null, null, null);
    }

    public Celula(int elemento, Celula inf, Celula sup, Celula esq, Celula dir) {
        this.elemento = elemento;
        this.inf = inf;
        this.sup = sup;
        this.esq = esq;
        this.dir = dir;
    }
}

class Matriz {
    private Celula inicio;
    private int linhas, colunas;

    public Matriz() {
        this(3, 3);
    }

    public Matriz(int linhas, int colunas) {
        this.linhas = linhas;
        this.colunas = colunas;
        int tam1 = 0, tam2 = 0;

        inicio = new Celula(0);

        for (Celula j = inicio; tam2 < colunas - 1; j = j.dir) {
            j.dir = new Celula(0);
            j.dir.esq = j;
            tam2++;
        }
        tam2 = 0;

        for (Celula i = inicio; tam1 < linhas - 1; i = i.inf) {
            i.inf = new Celula(0);
            for (Celula j = i, k = i.inf; tam2 < colunas - 1; j = j.dir, k = k.dir) {
                k.dir = new Celula(0);
                k.dir.esq = k;
                k.dir.sup = j.dir;
                j.dir.inf = k.dir;
                tam2++;
            }
            tam2 = 0;
            tam1++;
        }
    }
    public Matriz(int[][] mat) {
        this(3, 3, mat);
    }

    public Matriz(int linhas, int colunas, int[][] mat) {
        this.linhas = linhas;
        this.colunas = colunas;
        int tam1 = 0, tam2 = 0;

        inicio = new Celula(mat[0][0]);

        for (Celula j = inicio; tam2 < colunas - 1; j = j.dir) {
            j.dir = new Celula(mat[0][tam2 + 1]);
            j.dir.esq = j;
            tam2++;
        }
        tam2 = 0;

        for (Celula i = inicio; tam1 < linhas - 1; i = i.inf) {
            i.inf = new Celula(mat[tam1 + 1][0]);
            for (Celula j = i, k = i.inf; tam2 < colunas - 1; j = j.dir, k = k.dir) {
                k.dir = new Celula(mat[tam1+1][tam2+1]);
                k.dir.esq = k;
                k.dir.sup = j.dir;
                j.dir.inf = k.dir;
                tam2++;
            }
            tam2 = 0;
            tam1++;
        }
    }
    public void mostrar(){
        Celula cel = inicio;

        for(int i = 0; i < linhas; i++){
            MyIO.print(cel.elemento + " ");
            for(int j = 0; j < colunas-1; j++){
                cel = cel.dir;
                MyIO.print(cel.elemento + " ");
            }
            MyIO.println("");
            for(int x = 0; x < colunas-1; x++){
                cel = cel.esq;
            }
            cel = cel.inf;
        }
    }

    public void soma (Matriz m) {

        Matriz mat = new Matriz(this.linhas, m.colunas);

        Celula c1 = m.inicio;
        Celula c2 = this.inicio;
        Celula c3 = mat.inicio;

        for(int i = 0; i < linhas; i++){
            c3.elemento = c1.elemento + c2.elemento;
            for(int j = 0; j < colunas-1; j++){
                c1 = c1.dir;
                c2 = c2.dir;
                c3 = c3.dir;
                c3.elemento = c1.elemento + c2.elemento;
            }
            for(int x = 0; x < colunas-1; x++){
                c1 = c1.esq;
                c2 = c2.esq;
                c3 = c3.esq;
            }
            c1 = c1.inf;
            c2 = c2.inf;
            c3 = c3.inf;
        }
 
        mat.mostrar();
    }

    public void multiplicacao (Matriz m){
        
        if (isQuadrada(m)) {

            Matriz mat = new Matriz(this.linhas, m.colunas);

            Celula c1 = this.inicio;
            Celula c2 = m.inicio;
            Celula c3 = mat.inicio;

            for(int i = 0; i < linhas; i++){
                for(int j = 0; j < colunas; j++){
                    c3.elemento += c1.elemento * c2.elemento;
                    while(c1.dir != null && c2.inf != null){
                        c1 = c1.dir;
                        c2 = c2.inf;
                        c3.elemento += c1.elemento * c2.elemento;
                    }
                    while(c1.esq != null && c2.sup != null){
                        c1 = c1.esq;
                        c2 = c2.sup;
                    }
                    if(c2.dir != null)
                        c2 = c2.dir;
                    else j = colunas;

                    if(c3.dir != null){
                        c3 = c3.dir;
                    } else {
                        while(c3.esq != null)
                            c3 = c3.esq;
                        if(c3.inf != null)
                            c3 = c3.inf;
                    }
                }
                if(c1.inf != null)
                    c1 = c1.inf;
                while(c2.esq != null){
                    c2 = c2.esq;
                }
            }   

            mat.mostrar();
        }
    }

    public boolean isQuadrada(){
       return (this.linhas == this.colunas);
    }

    public boolean isQuadrada(Matriz m){
        return (this.linhas == m.linhas && this.colunas == m.colunas);
     }

    public void mostrarDiagonalPrincipal() {
        Celula c3 = inicio;
        if (isQuadrada()) {
            MyIO.print(c3.elemento + " ");
            for(int i = 1; i < colunas; i++){
                if(c3.dir != null && c3.dir.inf != null){
                    c3 = c3.dir.inf;
                    MyIO.print(c3.elemento + " ");
                }
            }
        }
        MyIO.println("");
    }

    public void mostrarDiagonalSecundaria() {
        Celula c3 = inicio;
        if (isQuadrada()) {
            while(c3.dir != null){
                c3 = c3.dir;
            }
            MyIO.print(c3.elemento + " ");
            while(c3.esq != null && c3.esq.inf != null){
                c3 = c3.esq.inf;
                MyIO.print(c3.elemento + " ");
            }
        }
        MyIO.println("");
    }
}