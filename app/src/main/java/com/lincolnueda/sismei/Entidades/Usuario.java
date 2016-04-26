package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;

/**
 * Created by Lincoln Ueda on 19/08/2015.
 */
public class Usuario implements Serializable{

    private String login;
    private String senha;
    private int codigo;
    private int logado;

    public int getLogado() {
        return logado;
    }

    public void setLogado(int logado) {
        this.logado = logado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}
