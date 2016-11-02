package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Produto;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lincoln Ueda on 29/12/2015.
 */
public class RepositorioProduto {
    public RepositorioProduto(SQLiteDatabase conn){this.conn = conn;}
    
    private SQLiteDatabase conn;
    private Produto produto;


    public ArrayAdapter<Produto> BuscaProduto(Context context){
        ArrayAdapter<Produto> adpProduto = new ArrayAdapter<Produto>(context,android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("Produto",null,null,null,null,null,null);
        cursor.moveToFirst();

        do {
            if (cursor.getCount() > 0) {
                Produto produto = new Produto();

                produto.setCodProd(cursor.getInt(0));
                produto.setNomeProd(cursor.getString(1));
                produto.setPrecoProd(cursor.getDouble(3));
                produto.setQuant(cursor.getInt(4));

                produto.setListaMateriais(BuscaMateriaisProduto(context, produto));

                adpProduto.add(produto);

            }
        } while (cursor.moveToNext());
        return adpProduto;

    }

    public ArrayList<Material> BuscaMateriaisProduto(Context context,Produto produto){
        ArrayList<Material> listaMateriais = new ArrayList<Material>();
        int i = 0;

        Cursor cursor = conn.query("ListaMaterial", null, "produto = ?", new String[]{String.valueOf(produto.getCodProd())}, null, null, null);
        cursor.moveToFirst();


        do{
            int codmat = cursor.getInt(2);
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
                        material.setQuant(cursor.getDouble(3));//quantidade cadastrada no produto
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

    public int BuscaCodigo(Context context,Produto produto){
        //Verifica se o codigo do orçamento digitado já existe, para fazer update
        Cursor cursor =conn.query("Produto",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if (cursor.getInt(0) == produto.getCodProd())
                    cod = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return cod;
    }


    public Produto EncontraProduto(SQLiteDatabase conn,Context context,int codprod){
        //busca o material
        Produto produto = new Produto();
        ClienteFornecedor fornecedor;
        Cursor buscaprod = conn.query("Produto",null,"_id = ?",new String[]{String.valueOf(codprod)},null,null,null);
       if(buscaprod.getCount() > 0){ //verifica se encontrou algum produto com o codigo digitado
            buscaprod.moveToFirst();
            do {
                produto.setCodProd(buscaprod.getInt(0));
                produto.setNomeProd(buscaprod.getString(1));
                produto.setPrecoProd(buscaprod.getDouble(3));
                produto.setQuant(buscaprod.getInt(4));

                produto.setListaMateriais(BuscaMateriaisProduto(context, produto));

            } while (buscaprod.moveToNext());
            //fim da busca produto;
            return produto;
        }else{
           //não encontrou produto
            produto.setCodProd(0);
           return produto;
        }
    }//fim encontraproduto


    private ContentValues PreencheValues(Produto produto){
        ContentValues values = new ContentValues();
        values.put("_id",produto.getCodProd());
        values.put("nomeproduto",produto.getNomeProd());
        values.put("customaterial",produto.getCustomaterial());
        values.put("valorproduto",produto.getPrecoProd());
        values.put("quantidade", produto.getQuant());
        
        return values;
    }

    private ContentValues PreencheValuesMaterial(Material mat, int codprod){
        ContentValues values = new ContentValues();
        values.put("produto", codprod);
        values.put("material", mat.getCodmat());
        values.put("quantidadematerial",mat.getQuant());
       
        return values;
    }



    public  void Inserir(Produto produto){
        ContentValues values = PreencheValues(produto);
        conn.insertOrThrow("Produto", null, values);

        int i = 0;
        int max = produto.getListaMateriais().size();
        do {
            ContentValues valuesmat = PreencheValuesMaterial(produto.getListaMateriais().get(i), produto.getCodProd());
            conn.insertOrThrow("ListaMaterial",null,valuesmat);
            i ++;
        }while (i < max);
    }

    public void Alterar(Produto produto){
        ContentValues values = PreencheValues(produto);
        conn.update("Produto", values, "_id = ?", new String[]{String.valueOf(produto.getCodProd())});
        conn.delete("ListaMaterial", "produto = ?", new String[]{String.valueOf(produto.getCodProd())});//deleta todos os materiais para cadastrar de novo
        int i = 0;
        int max = produto.getListaMateriais().size();
        do {
            ContentValues valuesmat = PreencheValuesMaterial(produto.getListaMateriais().get(i), produto.getCodProd());
            conn.insertOrThrow("ListaMaterial",null,valuesmat);
            i ++;
        }while (i < max);
    }

    public void Excluir(Produto produto){
        // ContentValues values = PreencheValues(produto);
        conn.delete("Produto", "_id = ?", new String[]{String.valueOf(produto.getCodProd())});
        conn.delete("ListaMaterial", "produto = ?", new String[]{String.valueOf(produto.getCodProd())});
    }


}
