package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Entidades.EtapaProducao;

/**
 * Created by Lincoln Ueda on 11/01/2016.
 */
public class RepositorioEtapaProducao {

    public RepositorioEtapaProducao( SQLiteDatabase conn){ this.conn = conn;}

    private SQLiteDatabase conn;
    private EtapaProducao etapa;
    private DataBase database;


    private ContentValues PreencheValues(EtapaProducao etapa) {
        ContentValues values = new ContentValues();
        values.put("_id",etapa.getCodEtapa());
        values.put("nomeetapa",etapa.getNomeEtapa());
        return values;
    }

    public ArrayAdapter<EtapaProducao> BuscaEtapa(Context context) {
        ArrayAdapter<EtapaProducao> adpEtapa = new ArrayAdapter<EtapaProducao>(context, android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("EtapaProducao", null, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            EtapaProducao etapa = new EtapaProducao();
            if (cursor.getCount() > 0) {
                etapa.setCodEtapa(cursor.getInt(0));
                etapa.setNomeEtapa(cursor.getString(1));
                adpEtapa.add(etapa);
            }
        } while (cursor.moveToNext());
        return adpEtapa;
    }


    public EtapaProducao EncontraEtapa(SQLiteDatabase conn,int codetapa) {
        EtapaProducao etapa = new EtapaProducao();
        Cursor cursor = conn.query("EtapaProducao", null, "_id = ?",new String[]{String.valueOf(codetapa)}, null, null, null);
        cursor.moveToFirst();
        do {

            if (cursor.getCount() > 0) {
                etapa.setCodEtapa(cursor.getInt(0));
                etapa.setNomeEtapa(cursor.getString(1));
            }
        } while (cursor.moveToNext());
        return etapa;
    }

    public int BuscaCodigo(Context context,EtapaProducao etapa){
        //Verifica se o codigo do cliente digitado j� existe, para fazer update
        Cursor cursor =conn.query("EtapaProducao",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if (cursor.getInt(0) == etapa.getCodEtapa())
                    cod = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return cod;
    }


    public  void Inserir(EtapaProducao etapa){
        ContentValues values = PreencheValues(etapa);
        conn.insertOrThrow("EtapaProducao", null, values);
    }

    public void Alterar(EtapaProducao etapa){
        ContentValues values = PreencheValues(etapa);
        conn.update("EtapaProducao", values, "_id = ?", new String[]{String.valueOf(etapa.getCodEtapa())});
    }

    public void Excluir( EtapaProducao etapa){
        //ContentValues values = PreencheValues(material);
        conn.delete("EtapaProducao", "_id = ?", new String[]{String.valueOf(etapa.getCodEtapa())});

    }

}
