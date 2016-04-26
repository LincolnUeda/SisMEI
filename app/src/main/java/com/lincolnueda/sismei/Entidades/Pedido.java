package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 */
public class Pedido implements Serializable{

    private int codPed;
    private int numNota;
    private EtapaProducao etapaProducao;
    private ClienteFornecedor cliente;
    private ArrayList<Produto> listaProdutos = new ArrayList<Produto>();
    private Date dataped;
    private String Status;
    private Date dataentrega;

    public Date getDataentrega() {return dataentrega;}

    public void setDataentrega(Date dataentrega) {this.dataentrega = dataentrega;}

    public Date getDataped() {return dataped;}

    public void setDataped(Date dataped) {this.dataped = dataped;}

    public ArrayList<Produto> getListaProdutos() {return listaProdutos;}

    public void setListaProdutos(ArrayList<Produto> listaProdutos) {this.listaProdutos = listaProdutos;}

    public int getCodPed() {return codPed;}

    public String getStatus() {return Status;}

    public void setStatus(String status) {Status = status;}

    public void setCodPed(int codPed) {
        this.codPed = codPed;
    }

    public int getNumNota() {
        return numNota;
    }

    public void setNumNota(int numNota) {
        this.numNota = numNota;
    }

    public EtapaProducao getEtapaProducao() {
        return etapaProducao;
    }

    public void setEtapaProducao(EtapaProducao etapaProducao) {this.etapaProducao = etapaProducao;}

    public ClienteFornecedor getCliente() {
        return cliente;
    }

    public void setCliente(ClienteFornecedor cliente) {
        this.cliente = cliente;
    }

    public String toString(){
        return codPed + "." + cliente.getNomeCliFor();
    }


}
