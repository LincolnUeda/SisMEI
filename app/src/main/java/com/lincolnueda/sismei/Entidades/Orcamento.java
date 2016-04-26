package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lincoln Ueda on 20/08/2015.
 */
public class Orcamento implements Serializable {

    private int codorc;
    private double valtot;
    private  double precofinal;

    public double getPrecofinal() {
        return precofinal;
    }

    public void setPrecofinal(double precofinal) {
        this.precofinal = precofinal;
    }

    private Date dataorc;
    private ClienteFornecedor cliente;
    private ArrayList <Material> listaMateriais = new ArrayList<Material>();

    public int getCodorc() {return codorc; }

    public void setCodorc(int codorc) {this.codorc = codorc;}

    public double getValtot() { return valtot;}

    public void setValtot(double valtot) {
        this.valtot = valtot;
    }

    public Date getDataorc() {return dataorc;}

    public void setDataorc(Date dataorc) {this.dataorc = dataorc; }

    public ClienteFornecedor getCliente() {
        return cliente;
    }

    public void setCliente(ClienteFornecedor cliente) {
        this.cliente = cliente;
    }

    public List<Material> getListaMateriais() {
        return listaMateriais;
    }

    public void setListaMateriais(ArrayList<Material> listaMateriais) {
        this.listaMateriais = listaMateriais;
    }


    public String toString(){ return codorc + "." + cliente.getNomeCliFor();}

}
