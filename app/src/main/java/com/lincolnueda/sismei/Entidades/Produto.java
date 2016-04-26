package com.lincolnueda.sismei.Entidades;

import com.lincolnueda.sismei.Utilidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lincoln Ueda on 19/08/2015.

 */
public class Produto implements Serializable{

    private int codProd;
    private String nomeProd;
    private double customaterial;
    private double precoProd;
    private double quant;

    public ArrayList<Material> getListaMateriais() {
        return listaMateriais;
    }

    public void setListaMateriais(ArrayList<Material> listaMateriais) {
        this.listaMateriais = listaMateriais;
    }

    private ArrayList<Material> listaMateriais = new ArrayList<Material>();

    public int getCodProd() {
        return codProd;
    }

    public void setCodProd(int codProd) {
        this.codProd = codProd;
    }

    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    public double getCustomaterial() {
        return customaterial;
    }

    public void setCustomaterial(double customaterial) {
        this.customaterial = customaterial;
    }

    public double getPrecoProd() {
        return precoProd;
    }

    public void setPrecoProd(double precoProd) {
        this.precoProd = precoProd;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }


    public String toString(){
        return Utilidades.padRight(String.valueOf(codProd),4) + "." + Utilidades.padRight(nomeProd,20) + " " + Utilidades.padRight(String.valueOf(quant),4);
    }



}
