package com.lincolnueda.sismei.Entidades;

/**
 * Created by Lincoln Ueda on 27/02/2016.
 */
public class Empresa {
    private int codEmp;
    private String nomeEmp;
    private  String cnpjEmp;

    public int getCodEmp() {
        return codEmp;
    }

    public void setCodEmp(int codEmp) {
        this.codEmp = codEmp;
    }

    public String getNomeEmp() {
        return nomeEmp;
    }

    public void setNomeEmp(String nomeEmp) {
        this.nomeEmp = nomeEmp;
    }

    public String getCnpjEmp() {
        return cnpjEmp;
    }

    public void setCnpjEmp(String cnpjEmp) {
        this.cnpjEmp = cnpjEmp;
    }
}
