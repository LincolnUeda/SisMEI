package com.lincolnueda.sismei.Entidades;

import java.io.Serializable;
import com.lincolnueda.sismei.Utilidades;



/**
 * Created by Lincoln Ueda on 20/08/2015.
 */
public class Material implements Serializable {
    protected int codmat;
    protected String nomemat;
    protected String cormat;
    protected String unidmed;
    protected double quant;
    protected double valunid;
    protected ClienteFornecedor fornecedor;

    public int getCodmat() {
        return codmat;
    }

    public void setCodmat(int codmat) {
        this.codmat = codmat;
    }

    public String getNomemat() {
        return nomemat;
    }

    public void setNomemat(String nomemat) {
        this.nomemat = nomemat;
    }

    public String getCormat() {
        return cormat;
    }

    public void setCormat(String cormat) {
        this.cormat = cormat;
    }

    public String getUnidmed() {
        return unidmed;
    }

    public void setUnidmed(String unidmed) {
        this.unidmed = unidmed;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public double getValunid() {
        return valunid;
    }

    public void setValunid(double valunid) {
        this.valunid = valunid;
    }

    public ClienteFornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(ClienteFornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }


    @Override
    public String toString() {

        return String.format("%4s",String.valueOf(codmat).trim()) +"."+String.format("%-10s",String.valueOf(nomemat).trim())+ " " + Utilidades.padRight(cormat,10) +  "."+Utilidades.padLeft(String.valueOf(quant), 6)+" "+
                Utilidades.padLeft(unidmed, 3)/*+" "+Utilidades.padLeft(String.valueOf((quant * valunid)), 6)*/;
    }



}

