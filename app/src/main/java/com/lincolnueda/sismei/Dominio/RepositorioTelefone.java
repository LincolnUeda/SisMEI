package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Telefone;

/**
 * Created by Lincoln Ueda on 28/08/2015.
 */
public class RepositorioTelefone {
    public RepositorioTelefone(SQLiteDatabase conn, ClienteFornecedor clifor){this.conn = conn;this.clifor = clifor;}

    private Telefone telefone;
    private ClienteFornecedor clifor;
    private SQLiteDatabase conn;
    private DataBase dataBase;

    private ContentValues preencheValues(Telefone telefone){
        //preenche  objeto values que será usado para inserir dados no banco
        ContentValues values = new ContentValues();
        values.put("_id",telefone.getCodTel());
        values.put("telefone",telefone.getTelefone());
        values.put("tipotelefone",telefone.getTipotel());
        values.put("clientefornecedor",clifor.getCodCliFor());
        values.put("tipoclifor",clifor.getTipo());

        return values;
    }
    public ArrayAdapter<Telefone> BuscaTelefone(Context context){
        ArrayAdapter<Telefone> adpTelefone = new ArrayAdapter<Telefone>(context,android.R.layout.simple_list_item_1);
        int cod = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Telefone",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(cod),String.valueOf(tipo)},null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                Telefone telefone = new Telefone();
                telefone.setCodTel(cursor.getInt(0));
                telefone.setTelefone(cursor.getString(1));
                telefone.setTipotel(cursor.getString(2));

                telefone.setClienteFornecedor(clifor);

                adpTelefone.add(telefone);

            } while (cursor.moveToNext());

        }
        return adpTelefone;

    }

    public int BuscaCodigo(Context context,Telefone telefone){
        ClienteFornecedor clifor = telefone.getClienteFornecedor();
        int cod = 0;
        int codigo = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Telefone",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(codigo),String.valueOf(tipo)},null,null,null);        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                if ( (cursor.getInt(0) == telefone.getCodTel() ) && (cursor.getInt(5) == clifor.getCodCliFor()) && cursor.getString(6).equals(clifor.getTipo()) )
                   cod = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return cod;
    }

    public void Inserir(Telefone telefone){
        ContentValues values = preencheValues(telefone);
        conn.insertOrThrow("Telefone",null,values);

    }

    public void Alterar(Telefone telefone){
        ContentValues values = preencheValues(telefone);
        conn.update("Telefone", values, "_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(telefone.getCodTel()), String.valueOf(clifor.getCodCliFor()), String.valueOf(clifor.getTipo())});
    }

    public void Excluir(Telefone telefone){
        ContentValues values = preencheValues(telefone);
        conn.delete("Telefone","_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(telefone.getCodTel()),String.valueOf(clifor.getCodCliFor()),String.valueOf(clifor.getTipo())});
    }

}
