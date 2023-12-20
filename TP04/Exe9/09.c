#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <err.h>
#include <math.h>

// Definição das constantes
#define MAXTAM    6
#define TAM_HASH  25
#define bool      short
#define true      1
#define false     0
#define TAM_MAX_LINHA 250

bool isFim(char *s){
    return (strlen(s) >= 3 && s[0] == 'F' &&
        s[1] == 'I' && s[2] == 'M');
}

char *replaceChar(char *string, char toSearch, char toReplace)
{
    char *charPtr = strchr(string, toSearch);
    if (charPtr != NULL) *charPtr = toReplace;
    return charPtr;
}

void lerLinha(char *string, int tamanhoMaximo, FILE *arquivo)
{
    fgets(string, tamanhoMaximo, arquivo);
    replaceChar(string, '\r', '\0');
    replaceChar(string, '\n', '\0');
}

//TIPO JOGADOR - CELULA ===================================================================

typedef struct {
    int id;
    int peso;
    int altura;
    char nome[70];
    char universidade[70];
    int anoNascimento;
    char cidadeNascimento[70];
    char estadoNascimento[70];
} Jogador;

Jogador array[MAXTAM+1];   // Elementos da pilha 
int primeiro;            // Remove do indice "primeiro".
int ultimo;             // Insere no indice "ultimo".

typedef struct Celula {
	Jogador jogador;        // Elemento inserido na celula.
	struct Celula* prox; // Aponta a celula prox.
} Celula;

// METODOS JOGADOR --------------------------------------------------------------------------------

void inserirNaoInformado(char *linha, char *novaLinha) {
    int tam = strlen(linha);
    for (int i = 0; i <= tam; i++, linha++) {
        *novaLinha++ = *linha;
        if (*linha == ',' && (*(linha + 1) == ',' || *(linha + 1) == '\0')) {
            strcpy(novaLinha, "nao informado");
            novaLinha += strlen("nao informado");
        }
    }
}

void tirarQuebraDeLinha(char linha[]) {
    int tam = strlen(linha);

    if (linha[tam - 2] == '\r' && linha[tam - 1] == '\n') // Linha do Windows
        linha[tam - 2] = '\0'; // Apaga a linha

    else if (linha[tam - 1] == '\r' || linha[tam - 1] == '\n') // Mac ou Linux
        linha[tam - 1] = '\0'; // Apaga a linha
}

/**
 * @param jogador Ponteiro para o jogador a receber os dados
 * @param linha Linha do CSV. Ex.: "65,Joe Graboski,201,88,,1930,,"
 */
void setJogador(Jogador *jogador, char linha[]) {
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

void ler (char linhas_corrigidas[][TAM_MAX_LINHA]){
    FILE *players;
    //abrindo o arquivo
    players = fopen("/tmp/players.csv", "r");

    char linhas[4000][TAM_MAX_LINHA];

    int i = 0;
    lerLinha(linhas[0], TAM_MAX_LINHA, players);
    do{
        lerLinha(linhas[i++], TAM_MAX_LINHA, players);
    } while(!feof(players));
    i--;

    for(int i = 0; i < 4000; i++){
        inserirNaoInformado(linhas[i], linhas_corrigidas[i]);
    }

}

void imprimir(Jogador *jogador) {
    printf(" ## %s ## %d ## %d ## %d ## %s ## %s ## %s ##\n",
        jogador->nome,
        jogador->altura,
        jogador->peso,
        jogador->anoNascimento,
        jogador->universidade,
        jogador->cidadeNascimento,
        jogador->estadoNascimento
    );
}

Jogador clone(Jogador *jogador) {
    Jogador retorno;

    retorno.id = jogador->id;
    strcpy(retorno.nome, jogador->nome);
    retorno.altura = jogador->altura;
    retorno.peso = jogador->peso;
    retorno.anoNascimento = jogador->anoNascimento;
    strcpy(retorno.universidade, jogador->universidade);
    strcpy(retorno.cidadeNascimento, jogador->cidadeNascimento);
    strcpy(retorno.estadoNascimento, jogador->estadoNascimento);

    return retorno;
}

// METODOS CELULA --------------------------------------------------------------------------------

Celula* novaCelula(Jogador j) {
   Celula* nova = (Celula*) malloc(sizeof(Celula));
   nova->jogador = j;
   nova->prox = NULL;
   return nova;
}

//LISTA PROPRIAMENTE DITA =======================================================

/**
 * Inicializacoes
 */
void start(){
    primeiro = ultimo = 0;
}

/**
 * Remove um elemento da primeira posicao da fila e movimenta 
 * os demais elementos para o primeiro da mesma.
 * @return resp int elemento a ser removido.
 * @Se a fila estiver vazia.
 */
Jogador remover() {

   //validar remocao
   if (primeiro == ultimo) {
      printf("Erro ao remover!");
      exit(1);
   }

   Jogador resp = array[primeiro];
   primeiro = (primeiro + 1) % MAXTAM;
   return resp;
}

void inserir(Jogador x) {

   //validar insercao
    if (((ultimo + 1) % MAXTAM) == primeiro) {
        remover();
    }

   array[ultimo] = x;
   ultimo = (ultimo + 1) % MAXTAM;
}


void inserirTabela(Jogador x){
}


/**
 * Mostra os array separados por espacos.
 */
void mostrar (){
   int i;
   int j = 0;

    for(i = primeiro; i != ultimo; i = ((i + 1) % MAXTAM)) {
        printf("[%d]", j++);
        imprimir(&array[i]);
    }

}

/**
 * Retorna um bool indicando se a fila esta vazia
 * @return bool indicando se a fila esta vazia
 */
bool isVazia() {
   return (primeiro == ultimo); 
}



// Tabela Hash Indireta
Celula* tabelaHash[TAM_HASH];

// Função de Hash Simples
int hash(int chave) {
    return chave % TAM_HASH;
}

// Inserir na Tabela Hash
void inserirTabelaHash(Jogador jogador) {
    int posicao = hash(jogador.id);

    Celula* nova = novaCelula(jogador);

    if (tabelaHash[posicao] == NULL) {
        tabelaHash[posicao] = nova;
    } else {
        Celula* atual = tabelaHash[posicao];
        while (atual->prox != NULL) {
            atual = atual->prox;
        }
        atual->prox = nova;
    }
}



// Função para mostrar a tabela hash
void mostrarTabelaHash() {
    for (int i = 0; i < TAM_HASH; i++) {
        printf("Posição %d:", i);
        Celula* atual = tabelaHash[i];
        while (atual != NULL) {
            imprimir(&(atual->jogador));
            atual = atual->prox;
        }
        printf("\n");
    }
}


// Função para pesquisar um jogador na Tabela Hash por nome
Celula* pesquisarTabelaHash(char nome[]) {
    for (int i = 0; i < TAM_HASH; i++) {
        Celula* atual = tabelaHash[i];

        while (atual != NULL) {
            if (strcmp(atual->jogador.nome, nome) == 0) {
                return atual;  // Jogador encontrado
            }
            atual = atual->prox;
        }
    }

    return NULL;  // Jogador não encontrado
}


// Função para imprimir informações de um jogador encontrado ou não
void imprimirResultadoPesquisa(Celula* resultado) {
    if (resultado != NULL) {
        printf("Jogador encontrado:\n");
        imprimir(&(resultado->jogador));
    } else {
        printf("Jogador não encontrado.\n");
    }
}

// MAIN --------------------------------------------------------------------------------

int main(int argc, char** argv) {
  // Inicialização da Tabela Hash
    for (int i = 0; i < TAM_HASH; i++) {
        tabelaHash[i] = NULL;
    }

   char entrada_id[1000][10];
   int numEntrada_id = 0;
   do{
      lerLinha(entrada_id[numEntrada_id], 10, stdin);
   }while (isFim(entrada_id[numEntrada_id++]) == false); // pegar primeiros ids
   numEntrada_id--;

   int entrada_inteiro[1000];

   for(int i = 0; i < 1000; i++){
      sscanf(entrada_id[i], "%d", &entrada_inteiro[i]); // transformação para inteiros
   }

   char saida[4000][TAM_MAX_LINHA];
   Jogador _Jogadores[1000];

   ler(saida); // leitura do arquivo completo

   start(); // inicio da lista

    // Inserção na Tabela Hash
    for(int i = 0; i < numEntrada_id; i++) {
        setJogador(&_Jogadores[i], saida[entrada_inteiro[i]]);
        inserirTabelaHash(_Jogadores[i]);
    }

    //mostrarTabelaHash();


    char *linha = (char*) malloc (100 * sizeof(char));
    lerLinha(linha, 100, stdin);

    for (int i = 0; isFim(linha) == false; i++)
    {
        Celula *encontrado = pesquisarTabelaHash(linha);
        if(encontrado == NULL){
          printf("%s NAO\n", linha);
        } else {
          printf("%s SIM\n", linha);
        }
        lerLinha(linha, 100, stdin);
    }


}
