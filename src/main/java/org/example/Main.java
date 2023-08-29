package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int linhas, colunas;
        double[][] matriz;
        Scanner sc = new Scanner(System.in);

        System.out.print("Informe a quantidade de linhas da sua matriz: ");
        linhas = Integer.parseInt(sc.nextLine());

        System.out.print("Informe a quantidade de colunas da sua matriz: ");
        colunas = Integer.parseInt(sc.nextLine());

        matriz = new double[linhas][colunas];

        for(int i = 0; i<linhas; i++){
            for(int j = 0; j<colunas; j++){
                System.out.printf("Valor do elemento na posicao a%d%d"+":", i+1, j+1);
                matriz[i][j] = Double.parseDouble(sc.nextLine());
            }
        }

        System.out.println("Determinante da matriz informada: "+ calcularDeterminante(matriz, linhas, colunas));

        escalonarMatriz(matriz, linhas, colunas);

    }


    public static void escalonarMatriz(double[][] matriz, int linhas, int colunas){

        while(!verificaMatrizEscalonada(matriz, linhas, colunas)){
            System.out.println("no while");
            System.out.println(matrizToString(matriz));

            //1 mandar linhas zeradas para baixo
            if(!verificaPosicaoLinhasZeradas(matriz, linhas, colunas))
                trocaLinhasZeradas(matriz, linhas);

            //2 Verificar e corrigir pivos
            int[] pivos = getPosicaoPivos(matriz);
            if(!verificaPivos(pivos)){
                for(int i = 0; i<pivos.length; i++){
                    for(int j = i; j<pivos.length; j++){
                        if(j == i)
                            continue;
                        if(pivos[i] > pivos[j]) {
                            trocaLinhas(matriz, i, j);
                            pivos = getPosicaoPivos(matriz);
                            break;
                        }
                    }
                }
            }
            for(int i = 0; i<pivos.length; i++){
                if(!isZerada(matriz[i]) && pivos[i] == -1){
                    matriz[i] = colocarPivoEmLinha(matriz[i]);
                    pivos = getPosicaoPivos(matriz);
                }
            }

            //3 verificar se é pivo e zerar coluna
            for(int i = 0; i<pivos.length; i++){
                if(pivos[i] == -1 )
                    continue;
                System.out.println("Verificando pivo na linha: "+i+" -> "+pivos[i]);
                if(!colunaIsZerada(getColuna(matriz, pivos[i]), i)){
                    zerarColuna(pivos[i], i, matriz);
                }

                //verificar se coluna está zerada

            }




            System.out.println("paia");
        }

        System.out.println("Deu certo");
        System.out.println(matrizToString(matriz));

    }

    public static double[] colocarPivoEmLinha(double[] linha){
        double multiplicador = 0;
        for(int i = 0; i<linha.length; i++){
            if(linha[i] != 0 && multiplicador == 0){
                multiplicador = 1/(linha[i]);
            }
            if(linha[i] != 0){
                linha[i] *=multiplicador;
            }
        }
        return linha;
    }

    public static void zerarColuna(int coluna, int linhaParaIgnorar, double[][] matriz){
        for(int i = 0; i<matriz.length; i++){
            if(i == linhaParaIgnorar)
                continue;

            System.out.println("somando linhas"+i+" com "+linhaParaIgnorar+" multiplicador: "+(-matriz[i][coluna]));
            matriz[i] = somarLinhaComOutra(matriz[i], matriz[linhaParaIgnorar], -matriz[i][coluna]);
        }
    }

    public static double[] somarLinhaComOutra(double[] linha1, double[] linha2, double multiplicador){
        for(int i = 0; i<linha1.length; i++){
            linha1[i] += linha2[i]*multiplicador;
        }
        return linha1;
    }

    public static boolean colunaIsZerada(double[] coluna, int linhaParaIgnorar){
        coluna[linhaParaIgnorar] = 0;
        for(double d : coluna){
            if(d != 0)
                return false;
        }
        return true;
    }

    public static double[] getColuna(double[][] matriz, int nmrColuna){
        double[] coluna = new double[matriz.length];
        for(int i = 0; i<matriz.length; i++){
            coluna[i] = matriz[i][nmrColuna];
        }
        return coluna;
    }

    public static String matrizToString(double[][] matriz){
       String out = "" ;
       for(double[] linha : matriz){
           out+="[ ";
           for(double elemento : linha){
               out+=elemento+" ";
           }
           out+="]\n";
       }
       return out;
    }

    public static double[][] trocaLinhas(double[][] matriz, int linha1, int linha2){
        double[] linha1Reserva = matriz[linha1];
        matriz[linha1] = matriz[linha2];
        matriz[linha2] = linha1Reserva;
        return matriz;
    }

    public static void trocaLinhasZeradas(double[][] matriz, int linhas){
        for(int i = 0; i<linhas; i++){
            if(!isZerada(matriz[i]))
                continue;

            matriz = trocaLinhas(matriz, i, linhas-1);
            System.out.println("Trocando linha "+i+" com linha "+(linhas-1));

        }
    }

    public static boolean verificaPosicaoLinhasZeradas(double[][] matriz, int linhas, int colunas){
        boolean achouZerada = false;
        for(int i = 0; i<linhas; i++){
            if(isZerada(matriz[i]))
                achouZerada = true;
            else if(!isZerada(matriz[i]) && achouZerada)
                return false;
        }
        return true;
    }


    public static int[] getPosicaoPivos(double[][] matriz){
        //indica em qual coluna está o pivo da linha i
        int[] pivos = new int[matriz.length];
        for(int i = 0; i<matriz.length; i++){
            for(int j = 0; j<matriz[i].length; j++){
                if(matriz[i][j] != 1 && matriz[i][j] != 0) {
                    pivos[i] = -1;
                    break;
                }
                if(matriz[i][j] == 1) {
                    pivos[i] = j;
                    break;
                }
                pivos[i] = -1;
            }
        }
        return pivos;
    }

    public static boolean verificaPivos(int[] pivos){
        int pivoAtual = pivos[0];
        for(int i = 1; i<pivos.length; i++){
            if(pivos[i] <= pivoAtual && pivos[i] != -1)
                return false;
        }
        return true;
    }

    public static boolean verificaMatrizEscalonada(double[][] matriz, int linhas, int colunas){
        //1 verificar se há linhas zeradas acima de linhas não zeradas
        if(!verificaPosicaoLinhasZeradas(matriz, linhas, colunas))
            return false;
        //FIM 1
        //==========================================================


        //2 verificar se os pivos seguem a ordem certa

        //indica em qual coluna está o pivo da linha i
        int[] pivos = getPosicaoPivos(matriz);
        if(!verificaPivos(pivos))
            return false;


        //FIM 2
        //========================================

        //Inicio 3
        //verificar se todas as linhas não zeradas possuem pivo
        for(double[] linha : matriz){
            for(double elemento : linha){
                if(isZerada(linha))
                    break;
                if(elemento == 1)
                    break;
                else if(elemento == 0)
                    continue;
                else
                    return false;
            }
        }



        // 4 -> verificar se todas as colunas com pivos são zeradas


        return true;

    }

    public static boolean isZerada(double[] linha){
        for(int i = 0; i<linha.length; i++){
            if(linha[i] != 0)
                return false;
        }
        return true;
    }

    public static double calcularDeterminante(double[][] matriz, int linhas, int colunas){
        if (linhas == 2){
            return (matriz[0][0]*matriz[1][1]) - (matriz[0][1]*matriz[1][0]);
        }

        double[][] novaMatriz;
        double determinante = 0;
        for(int i = 0; i<colunas; i++){
            novaMatriz = gerarMatrizMenor(linhas, colunas, matriz, i);
            determinante+=matriz[0][i]*( Math.pow(-1,(1)+(i+1) )*calcularDeterminante(novaMatriz, linhas-1, colunas-1));
        }

        return determinante;

    }

    public static double[][] gerarMatrizMenor(int linhas, int colunas, double[][] matriz, int colunaAtual){
        double[][] novaMatriz = new double[linhas-1][colunas-1];
        for(int i = 1; i<linhas; i++){
            int coluna = 0;
            for(int j = 0; j<colunas; j++){
                if(j == colunaAtual)
                    continue;

                novaMatriz[i-1][coluna] = matriz[i][j];
                coluna++;
            }
        }
        return novaMatriz;
    }

}