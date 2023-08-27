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
            //1 mandar linhas zeradas para baixo
            if(!verificaPosicaoLinhasZeradas(matriz, linhas, colunas))
                trocaLinhasZeradas(matriz, linhas);

            System.out.println("paia");
        }

        System.out.println("Deu certo");

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

    public static boolean verificaMatrizEscalonada(double[][] matriz, int linhas, int colunas){
        //1 verificar se há linhas zeradas acima de linhas não zeradas
        if(!verificaPosicaoLinhasZeradas(matriz, linhas, colunas))
            return false;
        //FIM 1
        //==========================================================


        //2 verificar se os pivos seguem a ordem certa

        //indica em qual coluna está o pivo da linha i
        int[] pivos = new int[linhas];
        for(int i = 0; i<linhas; i++){
            for(int j = 0; j<colunas; j++){
                if(matriz[i][j] == 1) {
                    pivos[i] = j;
                    break;
                }
                pivos[i] = -1;
            }
        }

        int pivoAtual = pivos[0];
        for(int i = 1; i<linhas; i++){
            if(pivos[i] <= pivoAtual && pivos[i] != -1)
                return false;
        }

        //FIM 2
        //========================================


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