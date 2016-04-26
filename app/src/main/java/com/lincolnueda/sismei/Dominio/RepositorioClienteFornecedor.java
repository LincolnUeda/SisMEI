package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.Entidades.ClienteFornecedor;

/**
 * Created by Lincoln Ueda on 24/08/2015.
 *
 * Classe responsavel pelos metodos utilizados para manipula��o de dados de Cliente e Fornecedor no banco de dados.
 */
public class RepositorioClienteFornecedor {

    SQLiteDatabase conn;
    ClienteFornecedor clienteFornecedor;

    public RepositorioClienteFornecedor (SQLiteDatabase conn){this.conn = conn;}


    private ContentValues preencheValues(ClienteFornecedor cliFor){
        //preenche  objeto values que ser� usado para inserir dados no banco
        ContentValues values = new ContentValues();
        values.put("_id",cliFor.getCodCliFor());
        values.put("nome",cliFor.getNomeCliFor());
        values.put("cpfcnpj",cliFor.getCpfCnpj());
        values.put("rginsc",cliFor.getRgInsc());
        values.put("tipo", cliFor.getTipo()); // tipo determina se � cliente ou fornecedor.

        return values;
    }

    public ArrayAdapter<ClienteFornecedor> BuscaCliFor(Context context,String tipo){

        ArrayAdapter<ClienteFornecedor> adpCliFor = new ArrayAdapter<ClienteFornecedor>(context,android.R.layout.simple_list_item_1);
        Cursor cursor =conn.query("ClienteFornecedor",null,"tipo = ?",new String[]{String.valueOf(tipo)},null,null,"_id");
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                ClienteFornecedor clifor = new ClienteFornecedor();
                clifor.setCodCliFor(cursor.getInt(0));
                clifor.setNomeCliFor(cursor.getString(1));
                clifor.setCpfCnpj(cursor.getString(2));
                clifor.setRgInsc(cursor.getString(3));
                clifor.setTipo(cursor.getString(4));

                if (clifor.getTipo().equals(tipo)) {
                    adpCliFor.add(clifor);
                }
            } while (cursor.moveToNext());
        }
        return adpCliFor;
    }

    public int BuscaCodigo(Context context,ClienteFornecedor clifor){
        //Verifica se o codigo do cliente digitado j� existe, para fazer update
        Cursor cursor =conn.query("ClienteFornecedor",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if ((cursor.getInt(0) == clifor.getCodCliFor()) && cursor.getString(4).equals(clifor.getTipo()))
                    cod = cursor.getInt(0);


            } while (cursor.moveToNext());
        }
        return cod;
     }

    public ClienteFornecedor EncontraClienteFornecedor(SQLiteDatabase conn,int codfor,String tipo){
        //busca o fornecedor do material para preencher o objeto fornecedor
        ClienteFornecedor fornecedor = new ClienteFornecedor();

        Cursor buscaforn = conn.query("ClienteFornecedor",null,"_id = ? and tipo = ?",new String[]{String.valueOf(codfor),tipo},null,null,null);
        buscaforn.moveToFirst();
        do{
            fornecedor.setCodCliFor(buscaforn.getInt(0));
            fornecedor.setNomeCliFor(buscaforn.getString(1));
            fornecedor.setCpfCnpj(buscaforn.getString(2));
            fornecedor.setRgInsc(buscaforn.getString(3));
            fornecedor.setTipo(buscaforn.getString(4));
        }while(buscaforn.moveToNext());
        //fim da busca fornecedor;
        return fornecedor;
    }

    public  void Inserir(ClienteFornecedor cliFor){
        ContentValues values = preencheValues(cliFor);
        conn.insertOrThrow("ClienteFornecedor",null,values);
    }

    public void Alterar(ClienteFornecedor clifor){
        ContentValues values = preencheValues(clifor);
        conn.update("ClienteFornecedor", values, "_id = ? and tipo = ?", new String[]{String.valueOf(clifor.getCodCliFor()), String.valueOf(clifor.getTipo())});
    }

    public void Excluir(ClienteFornecedor clifor){
      conn.delete("ClienteFornecedor", "_id = ? and tipo = ?", new String[]{String.valueOf(clifor.getCodCliFor()), String.valueOf(clifor.getTipo())});
    }



}
