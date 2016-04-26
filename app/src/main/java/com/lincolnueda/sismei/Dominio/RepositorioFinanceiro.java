package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Financeiro;

import java.util.Date;

/**
 * Created by Lincoln Ueda on 14/01/2016.
 */
public class RepositorioFinanceiro {

    SQLiteDatabase conn;
    Financeiro financeiro;

    public RepositorioFinanceiro (SQLiteDatabase conn){this.conn = conn;}

    private ContentValues preencheValues(Financeiro finan){
        //preenche  objeto values que será usado para inserir dados no banco
        ContentValues values = new ContentValues();
        values.put("_id",finan.getCodFin());
        values.put("descricao",finan.getDescricao());
        values.put("numnota",finan.getNumnota());
        values.put("valor",finan.getValor());
        values.put("totalparcelas",finan.getTotalparcelas());
        values.put("numparcela",finan.getNumparcela());
        values.put("tipo", finan.getTipo()); // tipo determina se é a pagar ou a receber;
        values.put("datavenc",finan.getDataVenc().getTime());
        values.put("clientefornecedor",finan.getClienteFornecedor().getCodCliFor());
        values.put("codpedido",finan.getCodpedido());

        return values;
    }

    public ArrayAdapter<Financeiro> BuscaFinanceiro(Context context,String tipo,String tipoclifor){

        ArrayAdapter<Financeiro> adpFinan = new ArrayAdapter<Financeiro>(context,android.R.layout.simple_list_item_1);
        Cursor cursor =conn.query("Financeiro",null,"tipo = ?",new String[]{String.valueOf(tipo)},null,null,"_id");
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                Financeiro finan = new Financeiro();
                finan.setCodFin(cursor.getInt(0));
                finan.setDescricao(cursor.getString(1));
                finan.setNumnota(cursor.getInt(2));
                finan.setValor(cursor.getDouble(3));
                finan.setNumparcela(cursor.getInt(4));
                finan.setTotalparcelas(cursor.getInt(5));
                finan.setTipo(cursor.getString(6));
                finan.setDataVenc(new Date(cursor.getLong(7)));

                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cursor.getInt(8),tipoclifor);
               finan.setClienteFornecedor(cliente);

                if (finan.getTipo().equals(tipo)) {
                    adpFinan.add(finan);
                }
            } while (cursor.moveToNext());
        }
        return adpFinan;
    }


    public int BuscaCodigo(Context context,Financeiro finan){
        //Verifica se o codigo do financeiro digitado já existe, para fazer update
        Cursor cursor =conn.query("Financeiro",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if ((cursor.getInt(0) == finan.getCodFin()) && cursor.getString(6).equals(finan.getTipo()))
                    cod = cursor.getInt(0);


            } while (cursor.moveToNext());
        }
        return cod;
    }


    public int BuscaCodigoPedido(Context context,int codpedido,String tipo){
        //Verifica se o codigo do financeiro está relacionado com algum pedido
        Cursor cursor =conn.query("Financeiro",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        double valortotal;
        if (cursor.getCount() > 0) {
            do {
                if ((cursor.getInt(9) == codpedido) && cursor.getString(6).equals(tipo)) {
                    cod = cursor.getInt(0);
                }

            } while (cursor.moveToNext());
        }
        return cod;
    }




    public int BuscaUltimocodigo(Context context){
        //Verifica se o codigo do financeiro digitado já existe, para fazer update
        Cursor cursor =conn.query("Financeiro",null,null,null,null,null,null);
        cursor.moveToLast();
        int cod = 0;
        if (cursor.getCount() > 0) {
                cod = cursor.getInt(0);
        }
        return cod;
    }

    public Financeiro EncontraFinanceiro(SQLiteDatabase conn,int codfinan,String tipo,String tipoclifor){
        //busca o fornecedor do material para preencher o objeto fornecedor
        Financeiro finan = new Financeiro();

        Cursor cursor = conn.query("Financeiro",null,"_id = ? and tipo = ?",new String[]{String.valueOf(codfinan),tipo},null,null,null);
        cursor.moveToFirst();
        do{
            finan.setCodFin(cursor.getInt(0));
            finan.setCodFin(cursor.getInt(0));
            finan.setDescricao(cursor.getString(1));
            finan.setNumnota(cursor.getInt(2));
            finan.setValor(cursor.getDouble(3));
            finan.setNumparcela(cursor.getInt(4));
            finan.setTotalparcelas(cursor.getInt(5));
            finan.setTipo(cursor.getString(6));
            finan.setDataVenc(new Date(cursor.getLong(7)));

            RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
            ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cursor.getInt(1),tipoclifor);
            finan.setClienteFornecedor(cliente);

        }while(cursor.moveToNext());
        //fim da busca fornecedor;
        return financeiro;
    }

    public Financeiro EncontraFinanceiroPedido(SQLiteDatabase conn,int codped){
        Financeiro finan = new Financeiro();

        //busca as contas cadastradas para um pedido
        Cursor cursor = conn.query("Financeiro",null,"codpedido = ?",new String[]{String.valueOf(codped)},null,null,null);
        cursor.moveToFirst();
        int max = cursor.getInt(5);
        finan.setDataVenc(new Date(cursor.getLong(7)));
        int i = 0;
        double valor = 0;
        do{
            finan.setCodFin(cursor.getInt(0));
            finan.setDescricao(cursor.getString(1));
            finan.setNumnota(cursor.getInt(2));
            valor += cursor.getDouble(3);
            finan.setValor(valor);
            finan.setNumparcela(cursor.getInt(4));
            finan.setTotalparcelas(cursor.getInt(5));
            finan.setTipo(cursor.getString(6));


            cursor.moveToNext();
            i++;
        }while( i < max);
        return finan;
    }



    public  void Inserir(Financeiro finan){
        ContentValues values = preencheValues(finan);
        conn.insertOrThrow("Financeiro",null,values);
    }

    public void Alterar(Financeiro finan){
        ContentValues values = preencheValues(finan);
        conn.update("Financeiro", values, "_id = ? and tipo = ?", new String[]{String.valueOf(finan.getCodFin()), String.valueOf(finan.getTipo())});
    }

    public void AlterarWhere(Financeiro finan,String where){
        ContentValues values = preencheValues(finan);
        conn.update("Financeiro", values, "_id = ? and tipo = ? and codpedido = ?", new String[]{String.valueOf(finan.getCodFin()), String.valueOf(finan.getTipo()),where});
    }

    public void Excluir(Financeiro finan){
        conn.delete("Financeiro", "_id = ? and tipo = ?", new String[]{String.valueOf(finan.getCodFin()), String.valueOf(finan.getTipo())});
    }

    public void ExcluirWhere(Financeiro finan){
        conn.delete("Financeiro","codpedido = ?", new String[]{String.valueOf(finan.getCodpedido())});
    }



}
