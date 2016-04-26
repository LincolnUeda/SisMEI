package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Endereco;

/**
 * Created by Lincoln Ueda on 28/08/2015.
 */
public class RepositorioEndereco {
    public RepositorioEndereco(SQLiteDatabase conn,ClienteFornecedor clifor){this.conn = conn;this.clifor = clifor;}

    private Endereco endereco;
    private ClienteFornecedor clifor;
    private SQLiteDatabase conn;
    private DataBase dataBase;

    private ContentValues preencheValues(Endereco endereco){
        //preenche  objeto values que será usado para inserir dados no banco
        ContentValues values = new ContentValues();
        values.put("_id",endereco.getCodEnd());
        values.put("endereco",endereco.getEndereco());
        values.put("cep",endereco.getCep());
        values.put("cidade",endereco.getCidade());
        values.put("estado",endereco.getEstado());
        values.put("clientefornecedor",clifor.getCodCliFor());
        values.put("tipoclifor",clifor.getTipo());

        return values;
    }
    public ArrayAdapter<Endereco> BuscaEndereco(Context context){
        ArrayAdapter<Endereco> adpEndereco = new ArrayAdapter<Endereco>(context,android.R.layout.simple_list_item_1);
        int cod = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Endereco",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(cod),String.valueOf(tipo)},null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                Endereco endereco = new Endereco();
                endereco.setCodEnd(cursor.getInt(0));
                endereco.setEndereco(cursor.getString(1));
                endereco.setCep(cursor.getString(2));
                endereco.setCidade(cursor.getString(3));
                endereco.setEstado(cursor.getString(4));
                endereco.setClienteFornecedor(clifor);

                adpEndereco.add(endereco);

            } while (cursor.moveToNext());

        }
        return adpEndereco;

    }

    public int BuscaCodigo(Context context,Endereco endereco){
        ClienteFornecedor clifor = endereco.getClienteFornecedor();
        int cod = 0;
        int codigo = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Endereco",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(codigo),String.valueOf(tipo)},null,null,null);        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                if ( (cursor.getInt(0) == endereco.getCodEnd() ) && (cursor.getInt(5) == clifor.getCodCliFor()) && cursor.getString(6).equals(clifor.getTipo()) )
                   cod = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return cod;
    }

    public void Inserir(Endereco endereco){
        ContentValues values = preencheValues(endereco);
        conn.insertOrThrow("Endereco",null,values);

    }

    public void Alterar(Endereco endereco){
        ContentValues values = preencheValues(endereco);
        conn.update("Endereco", values, "_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(endereco.getCodEnd()), String.valueOf(clifor.getCodCliFor()), String.valueOf(clifor.getTipo())});
    }

    public void Excluir(Endereco endereco){
        ContentValues values = preencheValues(endereco);
        conn.delete("Endereco","_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(endereco.getCodEnd()),String.valueOf(clifor.getCodCliFor()),String.valueOf(clifor.getTipo())});
    }

}
