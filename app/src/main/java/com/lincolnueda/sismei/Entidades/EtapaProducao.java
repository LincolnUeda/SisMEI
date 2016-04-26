package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 */
public class EtapaProducao implements Serializable{

    private int codEtapa;
    private String nomeEtapa;

    public int getCodEtapa() {
        return codEtapa;
    }

    public void setCodEtapa(int codEtapa) {
        this.codEtapa = codEtapa;
    }

    public String getNomeEtapa() {
        return nomeEtapa;
    }

    public void setNomeEtapa(String nomeEtapa) {
        this.nomeEtapa = nomeEtapa;
    }

    @Override
    public String toString(){
        return codEtapa + "." + nomeEtapa;
    }
}
