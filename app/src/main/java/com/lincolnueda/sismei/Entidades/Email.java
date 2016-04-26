package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 24/08/2015.
 */
public class Email implements Serializable{
    private int codEmail;
    private String email;
    private ClienteFornecedor clienteFornecedor;

    public ClienteFornecedor getClienteFornecedor() {
        return clienteFornecedor;
    }

    public void setClienteFornecedor(ClienteFornecedor clienteFornecedor) {
        this.clienteFornecedor = clienteFornecedor;
    }

    public int getCodEmail() {
        return codEmail;
    }

    public void setCodEmail(int codEmail) {
        this.codEmail = codEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString(){return codEmail + "." + email;}
}
