package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 */
public class Endereco implements Serializable{
    private int codEnd;
    private String endereco;
    private String cep;
    private String cidade;
    private String estado;
    private ClienteFornecedor clienteFornecedor;

    public int getCodEnd() {
        return codEnd;
    }

    public void setCodEnd(int codEnd) {
        this.codEnd = codEnd;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ClienteFornecedor getClienteFornecedor() {
        return clienteFornecedor;
    }

    public void setClienteFornecedor(ClienteFornecedor clienteFornecedor) {
        this.clienteFornecedor = clienteFornecedor;
    }


    public String toString(){
        return codEnd + "."+ endereco;
    }
}
