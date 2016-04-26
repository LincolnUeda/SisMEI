package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Material;

/**
 * Created by Lincoln Ueda on 02/09/2015.
 */
public class RepositorioMaterial {
    public RepositorioMaterial(SQLiteDatabase conn){this.conn = conn;}


    private Material material;
    private ClienteFornecedor fornecedor;
    private SQLiteDatabase conn;
    private DataBase database;


    private ContentValues PreencheValues(Material mat){
        ContentValues values = new ContentValues();
        values.put("_id",mat.getCodmat());
        values.put("nomematerial",mat.getNomemat());
        values.put("cormaterial",mat.getCormat());
        values.put("unidmedida",mat.getUnidmed());
        values.put("quantidade",mat.getQuant());
        values.put("valorunid",mat.getValunid());
        values.put("clientefornecedor", mat.getFornecedor().getCodCliFor());
        return values;
    }


    public ArrayAdapter<Material> BuscaMaterial(Context context){
        ArrayAdapter<Material> adpMaterial = new ArrayAdapter<Material>(context,android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("Material",null,null,null,null,null,null);
        cursor.moveToFirst();
        do{
            material = new Material();
            fornecedor = new ClienteFornecedor();
            if (cursor.getCount() > 0){
                material.setCodmat(cursor.getInt(0));
                material.setNomemat(cursor.getString(1));
                material.setCormat(cursor.getString(2));
                material.setUnidmed(cursor.getString(3));
                material.setQuant(cursor.getDouble(4));
                material.setValunid(cursor.getDouble(5));
                RepositorioClienteFornecedor repForn = new RepositorioClienteFornecedor(conn);
               fornecedor =  repForn.EncontraClienteFornecedor(conn,cursor.getInt(6),"for"); // busca fornecedor no banco
                material.setFornecedor(fornecedor);
                adpMaterial.add(material);
            }


        }while (cursor.moveToNext());
        return adpMaterial;

    } // fim BuscaMaterial


    public Material EncontraMaterial(SQLiteDatabase conn,int codfor){
        //busca o material
        Material material = new Material();
        ClienteFornecedor fornecedor;
        Cursor buscamat = conn.query("Material",null,"_id = ?",new String[]{String.valueOf(codfor)},null,null,null);
        buscamat.moveToFirst();
        do{
            material.setCodmat(buscamat.getInt(0));
            material.setNomemat(buscamat.getString(1));
            material.setCormat(buscamat.getString(2));
            material.setUnidmed(buscamat.getString(3));
            material.setQuant(buscamat.getDouble(4));
            material.setValunid(buscamat.getDouble(5));
            RepositorioClienteFornecedor repForn = new RepositorioClienteFornecedor(conn);
            fornecedor =  repForn.EncontraClienteFornecedor(conn,buscamat.getInt(6),"for"); // busca fornecedor no banco
            material.setFornecedor(fornecedor);
        }while(buscamat.moveToNext());
        //fim da busca material;
        return material;
    }//fim encontramaterial





    public int BuscaCodigo(Context context,Material material){
        //Verifica se o codigo do cliente digitado j� existe, para fazer update
        Cursor cursor =conn.query("Material",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if (cursor.getInt(0) == material.getCodmat())
                    cod = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return cod;
    }


    public  void Inserir(Material material){
        ContentValues values = PreencheValues(material);
        conn.insertOrThrow("Material", null, values);
    }

    public void Alterar(Material material){
        ContentValues values = PreencheValues(material);
        conn.update("Material", values, "_id = ?", new String[]{String.valueOf(material.getCodmat())});
    }


    public void Excluir( Material material){
        //ContentValues values = PreencheValues(material);
        conn.delete("Material", "_id = ?", new String[]{String.valueOf(material.getCodmat())});

    }



}
