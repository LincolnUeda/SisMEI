package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 *
 * Classe utilizada para opera��es do Financeiro - Entrada e Saida.
 *
 */
public class Financeiro implements Serializable{
    private int codFin;
    private String descricao;
    private int numnota;
    private double valor;
    private int numparcela;
    private int totalparcelas;
    private int codpedido;

    public int getCodpedido() {
        return codpedido;
    }

    public void setCodpedido(int codpedido) {
        this.codpedido = codpedido;
    }

    public int getTotalparcelas() {
        return totalparcelas;
    }

    public void setTotalparcelas(int totalparcelas) {
        this.totalparcelas = totalparcelas;
    }

    private String tipo;
    private Date dataVenc;
    public Date getDataVenc() {return dataVenc;}

    public void setDataVenc(Date dataVenc) {this.dataVenc = dataVenc;}

    private ClienteFornecedor clienteFornecedor;

    public int getCodFin() {
        return codFin;
    }

    public void setCodFin(int codFin) {
        this.codFin = codFin;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getNumnota() {
        return numnota;
    }

    public void setNumnota(int numnota) {
        this.numnota = numnota;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getNumparcela() {
        return numparcela;
    }

    public void setNumparcela(int numparcela) {
        this.numparcela = numparcela;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ClienteFornecedor getClienteFornecedor() {
        return clienteFornecedor;
    }

    public void setClienteFornecedor(ClienteFornecedor clienteFornecedor) {
        this.clienteFornecedor = clienteFornecedor;
    }

    public String toString(){
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(dataVenc);
        return codFin + "." + descricao + " " + dt;}
}
