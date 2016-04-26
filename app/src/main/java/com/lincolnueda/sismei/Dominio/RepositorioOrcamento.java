package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Orcamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lincoln Ueda on 03/09/2015.
 */
public class RepositorioOrcamento {
    public RepositorioOrcamento(SQLiteDatabase conn){this.conn = conn;}

    private SQLiteDatabase conn;
    private Orcamento orcamento;


    public ArrayAdapter<Orcamento>  BuscaOrcamento(Context context){
        ArrayAdapter<Orcamento> adpOrcamento = new ArrayAdapter<Orcamento>(context,android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("Orcamento",null,null,null,null,null,null);
        cursor.moveToFirst();

        do {
            if (cursor.getCount() > 0) {
                Orcamento orcamento = new Orcamento();

                orcamento.setCodorc(cursor.getInt(0));

                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cursor.getInt(1), "cli");
                orcamento.setCliente(cliente);
                
                orcamento.setValtot(cursor.getDouble(2));
                orcamento.setDataorc(new Date(cursor.getLong(3)));
                orcamento.setPrecofinal(cursor.getDouble(4));
                orcamento.setListaMateriais(BuscaMateriaisOrcamento(context, orcamento));

                adpOrcamento.add(orcamento);

            }
        } while (cursor.moveToNext());
        return adpOrcamento;

    }


    private ArrayList<Material> BuscaMateriaisOrcamento(Context context,Orcamento orcamento){
         ArrayList<Material> listaMateriais = new ArrayList<Material>();
        int i = 0;

        Cursor cursor = conn.query("MaterialOrcamento",null,"orcamento = ?",new String[]{String.valueOf(orcamento.getCodorc())},null,null,null);
        cursor.moveToFirst();
        int codmat = cursor.getInt(1);

        do{
            //busca dados do material no banco
            Cursor mat = conn.query("Material",null,"_id = ?",new String[]{String.valueOf(codmat)},null,null,null);
            mat.moveToFirst();
            do{
                if (mat.getCount() > 0){
                    try {
                        Material material = new Material();
                        material.setCodmat(mat.getInt(0));
                        material.setNomemat(mat.getString(1));
                        material.setCormat(mat.getString(2));
                        material.setUnidmed(mat.getString(3));
                        material.setQuant(cursor.getDouble(2));//quantidade cadastrada no orçamento
                        material.setValunid(mat.getDouble(5));
                        RepositorioClienteFornecedor repForn = new RepositorioClienteFornecedor(conn);
                        ClienteFornecedor fornecedor = repForn.EncontraClienteFornecedor(conn, mat.getInt(6), "for"); // busca fornecedor no banco
                        material.setFornecedor(fornecedor);

                        listaMateriais.add(material); //preenche a lista de materiais
                    }catch (Exception e){

                    }

                }
            }while(mat.moveToNext()); //fim busca materiais

        }while (cursor.moveToNext()); // fim busca itens or�amento

        return listaMateriais;
    }

    public int BuscaCodigo(Context context,Orcamento orcamento){
        //Verifica se o codigo do orçamento digitado já existe, para fazer update
        Cursor cursor =conn.query("Orcamento",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if (cursor.getInt(0) == orcamento.getCodorc())
                    cod = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return cod;
    }


    private ContentValues PreencheValues(Orcamento orcamento){
        ContentValues values = new ContentValues();
        values.put("_id",orcamento.getCodorc());
        values.put("cliente",orcamento.getCliente().getCodCliFor());
        values.put("valortotal", orcamento.getValtot());
        values.put("data",orcamento.getDataorc().getTime());
        values.put("precofinal",orcamento.getPrecofinal());
        return values;
    }

    private ContentValues PreencheValuesMaterial(Material mat, int codorc){
        ContentValues values = new ContentValues();
        values.put("material", mat.getCodmat());
        values.put("quantidadematerial",mat.getQuant());
        values.put("orcamento",codorc);
        return values;
    }

    public  void Inserir(Orcamento orcamento){
        ContentValues values = PreencheValues(orcamento);
        conn.insertOrThrow("Orcamento", null, values);

        int i = 0;
        int max = orcamento.getListaMateriais().size();
        do {
            ContentValues valuesmat = PreencheValuesMaterial(orcamento.getListaMateriais().get(i), orcamento.getCodorc());
            conn.insertOrThrow("MaterialOrcamento",null,valuesmat);
            i ++;
        }while (i < max);
    }

    public void Alterar(Orcamento orcamento){
        ContentValues values = PreencheValues(orcamento);
        conn.update("Orcamento", values, "_id = ?", new String[]{String.valueOf(orcamento.getCodorc())});
        conn.delete("MaterialOrcamento", "orcamento = ?", new String[]{String.valueOf(orcamento.getCodorc())});//deleta todos os materiais para cadastrar de novo
        int i = 0;
        int max = orcamento.getListaMateriais().size();
        do {
            ContentValues valuesmat = PreencheValuesMaterial(orcamento.getListaMateriais().get(i), orcamento.getCodorc());
            conn.insertOrThrow("MaterialOrcamento",null,valuesmat);
            i ++;
        }while (i < max);
    }

    public void Excluir(Orcamento orcamento){
       // ContentValues values = PreencheValues(orcamento);
        conn.delete("Orcamento", "_id = ?", new String[]{String.valueOf(orcamento.getCodorc())});
        conn.delete("MaterialOrcamento","orcamento = ?", new String[]{String.valueOf(orcamento.getCodorc())});
    }


}
