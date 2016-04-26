package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 */
public class Telefone implements Serializable{
    private int codTel;
    private String telefone;
    private String tipotel;
    private ClienteFornecedor clienteFornecedor;

    public int getCodTel() {
        return codTel;
    }

    public void setCodTel(int codTel) {
        this.codTel = codTel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipotel() {
        return tipotel;
    }

    public void setTipotel(String tipotel) {
        this.tipotel = tipotel;
    }

    public ClienteFornecedor getClienteFornecedor() {
        return clienteFornecedor;
    }

    public void setClienteFornecedor(ClienteFornecedor clienteFornecedor) {
        this.clienteFornecedor = clienteFornecedor;
    }

    public String toString(){ return codTel + "." + telefone;}
}
