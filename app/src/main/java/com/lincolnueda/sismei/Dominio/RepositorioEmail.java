package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Email;

/**
 * Created by Lincoln Ueda on 28/08/2015.
 */
public class RepositorioEmail {
    public RepositorioEmail(SQLiteDatabase conn, ClienteFornecedor clifor){this.conn = conn;this.clifor = clifor;}

    private Email email;
    private ClienteFornecedor clifor;
    private SQLiteDatabase conn;
    private DataBase dataBase;

    private ContentValues preencheValues(Email email){
        //preenche  objeto values que será usado para inserir dados no banco
        ContentValues values = new ContentValues();
        values.put("_id",email.getCodEmail());
        values.put("email",email.getEmail());
        values.put("clientefornecedor",clifor.getCodCliFor());
        values.put("tipoclifor",clifor.getTipo());

        return values;
    }
    public ArrayAdapter<Email> BuscaEmail(Context context){
        ArrayAdapter<Email> adpEmail = new ArrayAdapter<Email>(context,android.R.layout.simple_list_item_1);
        int cod = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Email",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(cod),String.valueOf(tipo)},null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                Email email = new Email();
                email.setCodEmail(cursor.getInt(0));
                email.setEmail(cursor.getString(1));

                email.setClienteFornecedor(clifor);

                adpEmail.add(email);

            } while (cursor.moveToNext());

        }
        return adpEmail;

    }

    public int BuscaCodigo(Context context,Email email){
        ClienteFornecedor clifor = email.getClienteFornecedor();
        int cod = 0;
        int codigo = clifor.getCodCliFor();
        String tipo = clifor.getTipo();
        Cursor cursor = conn.query("Email",null,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(codigo),String.valueOf(tipo)},null,null,null);        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                if ( (cursor.getInt(0) == email.getCodEmail() ) && (cursor.getInt(2) == clifor.getCodCliFor()) && cursor.getString(3).equals(clifor.getTipo()) )
                   cod = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        return cod;
    }

    public void Inserir(Email email){
        ContentValues values = preencheValues(email);
        conn.insertOrThrow("Email",null,values);

    }

    public void Alterar(Email email){
        ContentValues values = preencheValues(email);
        conn.update("Email", values, "_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(email.getCodEmail()), String.valueOf(clifor.getCodCliFor()), String.valueOf(clifor.getTipo())});
    }

    public void Excluir(Email email){
        ContentValues values = preencheValues(email);
        conn.delete("Email","_id = ? and clientefornecedor = ? and tipoclifor = ?", new String[]{String.valueOf(email.getCodEmail()),String.valueOf(clifor.getCodCliFor()),String.valueOf(clifor.getTipo())});
    }

}
