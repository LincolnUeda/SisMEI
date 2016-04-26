package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 *
 * Classe utilizada para Clientes e Fornecedores.
 */
public class ClienteFornecedor implements Serializable{
    private int codCliFor;
    private String nomeCliFor;
    private String cpfCnpj;
    private String rgInsc;
    private String tipo;

    public int getCodCliFor() {
        return codCliFor;
    }

    public void setCodCliFor(int codCliFor) {
        this.codCliFor = codCliFor;
    }

    public String getNomeCliFor() {
        return nomeCliFor;
    }

    public void setNomeCliFor(String nomeCliFor) {
        this.nomeCliFor = nomeCliFor;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRgInsc() {
        return rgInsc;
    }

    public void setRgInsc(String rgInsc) {
        this.rgInsc = rgInsc;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString(){
        return codCliFor + "." + nomeCliFor;
    }
}
