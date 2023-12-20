#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <err.h>

#define MAXTAM 5
#define bool short
#define true 1
#define false 0

#define TAM_MAX_LINHA 250

bool isFim(char *s)
{
    return (strlen(s) >= 3 && s[0] == 'F' &&
            s[1] == 'I' && s[2] == 'M');
}

char *replaceChar(char *string, char toSearch, char toReplace)
{
    char *charPtr = strchr(string, toSearch);
    if (charPtr != NULL)
        *charPtr = toReplace;
    return charPtr;
}

void lerLinha(char *string, int tamanhoMaximo, FILE *arquivo)
{
    fgets(string, tamanhoMaximo, arquivo);
    replaceChar(string, '\r', '\0');
    replaceChar(string, '\n', '\0');
}

typedef struct Jogador {
    int id;
    int altura;
    int peso;
    int anoNascimento;
    char nome[100];
    char universidade[100];
    char cidadeNascimento[100];
    char estadoNascimento[100];
} Jogador;

typedef struct CelulaDupla {
	Jogador elemento;        // Elemento inserido na celula.
	struct CelulaDupla* prox; // Aponta a celula prox.
    struct CelulaDupla* ant;  // Aponta a celula anterior.
} CelulaDupla;

void inserirNaoInformado(char *linha, char *novaLinha)
{
    int tam = strlen(linha);
    for (int i = 0; i <= tam; i++, linha++)
    {
        *novaLinha++ = *linha;
        if (*linha == ',' && (*(linha + 1) == ',' || *(linha + 1) == '\0'))
        {
            strcpy(novaLinha, "nao informado");
            novaLinha += strlen("nao informado");
        }
    }
}

void tirarQuebraDeLinha(char linha[])
{
    int tam = strlen(linha);

    if (linha[tam - 2] == '\r' && linha[tam - 1] == '\n') // Linha do Windows
        linha[tam - 2] = '\0';                            // Apaga a linha

    else if (linha[tam - 1] == '\r' || linha[tam - 1] == '\n') // Mac ou Linux
        linha[tam - 1] = '\0';                                 // Apaga a linha
}

void setJogador(Jogador *jogador, char linha[])
{
    char novaLinha[TAM_MAX_LINHA];
    tirarQuebraDeLinha(linha);
    inserirNaoInformado(linha, novaLinha);

    jogador->id = atoi(strtok(novaLinha, ","));
    strcpy(jogador->nome, strtok(NULL, ","));
    jogador->altura = atoi(strtok(NULL, ","));
    jogador->peso = atoi(strtok(NULL, ","));
    strcpy(jogador->universidade, strtok(NULL, ","));
    jogador->anoNascimento = atoi(strtok(NULL, ","));
    strcpy(jogador->cidadeNascimento, strtok(NULL, ","));
    strcpy(jogador->estadoNascimento, strtok(NULL, ","));
}

void ler(char linhas_corrigidas[][TAM_MAX_LINHA])
{
    FILE *players;
    //abrindo o arquivo
    players = fopen("/tmp/players.csv", "r");

    char linhas[4000][TAM_MAX_LINHA];

    int i = 0;
    lerLinha(linhas[0], TAM_MAX_LINHA, players);
    do
    {
        lerLinha(linhas[i++], TAM_MAX_LINHA, players);
    } while (!feof(players));
    i--;

    for (int i = 0; i < 4000; i++)
    {
        inserirNaoInformado(linhas[i], linhas_corrigidas[i]);
    }
}

typedef struct No {
    Jogador *elemento;
    int nivel; //Numero de niveis abaixo do no
	struct No *esq, *dir;
} No;

No* novoNo(Jogador *elemento) {
   No* novo = (No*) malloc(sizeof(No));
   novo->elemento = elemento;
   novo->esq = NULL;
   novo->dir = NULL;
   return novo;
}

No* raiz;

void start() {
    raiz = NULL;
}

bool pesquisarRec(char* x, No* i) {
    bool resp;
    if (i == NULL) {
        resp = false;

    } else if (strcmp(x, i->elemento->nome) == 0) {
        resp = true;
        printf("SIM\n");
    } else if (strcmp(x, i->elemento->nome) < 0) {
        printf("esq ");
        resp = pesquisarRec(x, i->esq);
    } else {
        printf("dir ");
        resp = pesquisarRec(x, i->dir);
    }
    return resp;
}

void pesquisar(char* x) {
    printf("%s raiz ", x);
    if(pesquisarRec(x, raiz) == false) printf("NAO\n");
}

int getNivel(No *no) {
    return (no == NULL) ? 0 : no->nivel;
}

void setNivel(No *no) {
    no->nivel = 1 + fmax(getNivel(no->esq),getNivel(no->dir));
}

No* rotacionarDir(No *no) {
    No* noEsq = no->esq;
    No* noEsqDir = noEsq->dir;

    noEsq->dir = no;
    no->esq = noEsqDir;

    setNivel(no);
    setNivel(noEsq);

    return noEsq;
}

No* rotacionarEsq(No *no) {
    No *noDir = no->dir;
    No *noDirEsq = noDir->esq;

    noDir->esq = no;
    no->dir = noDirEsq;

    setNivel(no);
    setNivel(noDir);

    return noDir;
}

No* balancear(No *no){

    if(no != NULL) {
        int fator = getNivel(no->dir) - getNivel(no->esq);

    //Se balanceada
        if (abs((long)fator) <= 1){

            setNivel(no);

    //Se desbalanceada para a direita
        } else if (fator == 2){

            int fatorFilhoDir = getNivel(no->dir->dir) - getNivel(no->dir->esq);

            //Se o filho a direita tambem estiver desbalanceado
            if (fatorFilhoDir == -1) {
                no->dir = rotacionarDir(no->dir);
            }
            no = rotacionarEsq(no);

            //Se desbalanceada para a esquerda
        }   else if (fator == -2){

            int fatorFilhoEsq = getNivel(no->esq->dir) - getNivel(no->esq->esq);

            //Se o filho a esquerda tambem estiver desbalanceado
            if (fatorFilhoEsq == 1) {
                no->esq = rotacionarEsq(no->esq);
            }
            no = rotacionarDir(no);

        }
    }

    return no;
}

void inserirRec(Jogador* x, No** i) {
    if (*i == NULL) {
        *i = novoNo(x);

    } else if (strcmp(x->nome , (*i)->elemento->nome ) < 0) {
        inserirRec(x, &((*i)->esq));

    } else if (strcmp(x->nome , (*i)->elemento->nome) > 0) {
        inserirRec(x, &((*i)->dir));

    } else {
        errx(1, "Erro ao inserir!");
    }

    *i = balancear(*i);
}

void caminharPreRec(No* i) {
   if (i != NULL) {
      printf("%s\n", i->elemento->nome);
      caminharPreRec(i->esq);
      caminharPreRec(i->dir);
   }
}

void caminharPre() {
   caminharPreRec(raiz);
}

void inserir(Jogador *x) {
   inserirRec(x, &raiz);
}

int main(int argc, char **argv)
{

    char entrada[1000][10];
    int numEntrada = 0;
    do
    {
        lerLinha(entrada[numEntrada], 10, stdin);
    } while (isFim(entrada[numEntrada++]) == false); // pegar primeiros ids
    numEntrada--;

    int entrada_inteiro[1000];

    for (int i = 0; i < 1000; i++)
    {
        sscanf(entrada[i], "%d", &entrada_inteiro[i]); // transformação para inteiros
    }

    char saida[4000][TAM_MAX_LINHA];

    ler(saida); // leitura do arquivo completo

    start(); // inicio da lista

    for (int i = 0; i < numEntrada; i++)
    {
        Jogador *j = (Jogador*) malloc (sizeof(Jogador));
        setJogador(j, saida[entrada_inteiro[i]]); // criação dos jogadores e inserção na lista
        inserir(j);
    }

    char *linha = (char*) malloc (100 * sizeof(char));
    lerLinha(linha, 100, stdin);

    for (int i = 0; isFim(linha) == false; i++)
    {
        pesquisar(linha);
        lerLinha(linha, 100, stdin);
    }

    // caminharPre();

}
