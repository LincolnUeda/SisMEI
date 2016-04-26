package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lincolnueda.sismei.Entidades.Empresa;

/**
 * Created by Lincoln Ueda on 21/08/2015.
 *
 * Classe responsavel pelos metodos referentes a operacoes da classe Empresa com o banco de dados.
 *
 *
 */
public class RepositorioEmpresa {
    private SQLiteDatabase conn;
    private Empresa empresa;

    public RepositorioEmpresa(SQLiteDatabase conn){this.conn = conn;} //construtor da classe. recebe a conexao feita com o banco

    public Empresa BuscarEmpresa() {
        //metodo para buscar empresa no banco de dados.
        Cursor cursor = conn.query("Empresa", null, null, null, null, null, "_id");
        Empresa empresa = new Empresa();
        empresa.setCodEmp(0);

        //retornar valores do banco
        if (cursor.getCount() > 0) {
            cursor.moveToFirst(); //move para o primeiro registro do banco
            do {
                empresa.setCodEmp(cursor.getInt(0));
                empresa.setNomeEmp(cursor.getString(1));
                empresa.setCnpjEmp(cursor.getString(2));

            } while (cursor.moveToNext());
        }
        return empresa;
    }


    public void Inserir( Empresa empresa){

        ContentValues values = new ContentValues(); //este objeto eh o que sera mandado para o metodo de update.
        values.put("empresa",empresa.getNomeEmp());
        values.put("cnpj",empresa.getCnpjEmp());


        conn.insertOrThrow("Empresa",null,values);
    }

    public void Alterar( Empresa empresa){

        ContentValues values = new ContentValues(); //este objeto eh o que sera mandado para o metodo de update.
        values.put("empresa",empresa.getNomeEmp());
        values.put("cnpj",empresa.getCnpjEmp());


        conn.update("Empresa",values,"_id = ?",new String[]{String.valueOf(empresa.getCodEmp())});
    }

}
