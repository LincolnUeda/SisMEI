package com.lincolnueda.sismei.Entidades;


/**
 * Created by Lincoln Ueda on 28/12/2015.
 * Classe usada nos orçamentos, para sobrescrever o metodo toString e exibir o valor total do material.
 *
 */
public class MaterialOrcamento extends Material{

    // pad with " " to the left to the given length (n)
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    @Override
    public String toString(){
        //(String.format("%10s", "howto").replace(' ', '*'));

        return   padLeft(String.valueOf(codmat),4) + "." + padLeft(nomemat,10) +" "+ padLeft(String.valueOf(quant),6) +" "+ padLeft(unidmed,3) + " "+padLeft(String.valueOf((quant * valunid)), 6);
    }
}
