package com.lincolnueda.sismei.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.EtapaProducao;
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.Entidades.Produto;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lincoln Ueda on 05/01/2016.
 */
public class RepositorioPedido {
    public RepositorioPedido(SQLiteDatabase conn){this.conn = conn;}

    private SQLiteDatabase conn;


    public ArrayAdapter<Pedido> BuscaPedido(Context context){
        ArrayAdapter<Pedido> adppedido = new ArrayAdapter<Pedido>(context,android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("Pedido",null,null,null,null,null,null);
        cursor.moveToFirst();

        do {
            if (cursor.getCount() > 0) {
                Pedido pedido = new Pedido();

                pedido.setCodPed(cursor.getInt(0));

                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cursor.getInt(1), "cli");
                pedido.setCliente(cliente);

                pedido.setNumNota(cursor.getInt(2));

                RepositorioEtapaProducao repEtapa = new RepositorioEtapaProducao(conn);
                EtapaProducao etapa = repEtapa.EncontraEtapa(conn, cursor.getInt(3));
                pedido.setEtapaProducao(etapa);

                pedido.setDataped(new Date(cursor.getLong(4)));
                pedido.setDataentrega(new Date(cursor.getLong(5)));
                pedido.setStatus(cursor.getString(6));


              pedido.setListaProdutos(BuscaProdutoPedido(context, pedido));

                adppedido.add(pedido);

            }
        } while (cursor.moveToNext());
        return adppedido;

    }

    private ArrayList<Produto> BuscaProdutoPedido(Context context,Pedido pedido){
        ArrayList<Produto> listaProdutos = new ArrayList<Produto>();
        int i = 0;

        Cursor cursor = conn.query("ListaPedido", null, "pedido = ?", new String[]{String.valueOf(pedido.getCodPed())}, null, null, null);
        cursor.moveToFirst();


        do{
            int codprod = cursor.getInt(2);
            //busca dados do Produto no banco
            Cursor prod = conn.query("Produto",null,"_id = ?",new String[]{String.valueOf(codprod)},null,null,null);
            prod.moveToFirst();
            do{
                if (prod.getCount() > 0){
                    try {
                        Produto produto = new Produto();
                        produto.setCodProd(prod.getInt(0));
                        produto.setNomeProd(prod.getString(1));
                        produto.setPrecoProd(prod.getDouble(3));
                        produto.setQuant(cursor.getInt(3));// pega quantidade da lista de produtos do pedido

                        RepositorioProduto repprod = new RepositorioProduto(conn);
                        produto.setListaMateriais(repprod.BuscaMateriaisProduto(context, produto));
                        listaProdutos.add(produto); //preenche a lista de produtos
                    }catch (Exception e){

                    }

                }
            }while(prod.moveToNext()); //fim busca materiais

        }while (cursor.moveToNext()); // fim busca itens or�amento

        return listaProdutos;
    }

    public int BuscaCodigo(Context context,Pedido pedido){
        //Verifica se o codigo do orçamento digitado já existe, para fazer update
        Cursor cursor =conn.query("Pedido",null,null,null,null,null,null);
        cursor.moveToFirst();
        int cod = 0;
        if (cursor.getCount() > 0) {
            do {
                if (cursor.getInt(0) == pedido.getCodPed())
                    cod = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return cod;
    }

    private ContentValues PreencheValues(Pedido pedido){
        ContentValues values = new ContentValues();
        values.put("_id",pedido.getCodPed());
        values.put("clientefornecedor",pedido.getCliente().getCodCliFor());
        values.put("numnota",pedido.getNumNota());
        values.put("etapaproducao", pedido.getEtapaProducao().getCodEtapa());
        values.put("data", pedido.getDataped().getTime());
        values.put("dataentrega",pedido.getDataentrega().getTime());
        values.put("status",pedido.getStatus());

        return values;
    }


    private ContentValues PreencheValuesProduto(Produto produto, int codped){
        ContentValues values = new ContentValues();
        values.put("pedido",codped);
        values.put("produto",produto.getCodProd());
        values.put("quantidade",produto.getQuant());

        return values;
    }

    public  void Inserir(Pedido pedido){
        ContentValues values = PreencheValues(pedido);
        conn.insertOrThrow("Pedido", null, values);

        int i = 0;
        int max = pedido.getListaProdutos().size();
        do {
            ContentValues valuesprod = PreencheValuesProduto(pedido.getListaProdutos().get(i), pedido.getCodPed());
            conn.insertOrThrow("ListaPedido",null,valuesprod);
            i ++;
        }while (i < max);
    }


    public void Alterar(Pedido pedido){
        ContentValues values = PreencheValues(pedido);
        conn.update("Pedido", values, "_id = ?", new String[]{String.valueOf(pedido.getCodPed())});
        conn.delete("ListaPedido", "pedido = ?", new String[]{String.valueOf(pedido.getCodPed())});//deleta todos os produtos para cadastrar de novo
        int i = 0;
        int max = pedido.getListaProdutos().size();
        do {
            ContentValues valuesped = PreencheValuesProduto(pedido.getListaProdutos().get(i), pedido.getCodPed());
            conn.insertOrThrow("ListaPedido",null,valuesped);
            i ++;
        }while (i < max);
    }

    public void Excluir(Pedido pedido){

        conn.delete("Pedido", "_id = ?", new String[]{String.valueOf(pedido.getCodPed())});
        conn.delete("ListaPedido", "pedido = ?", new String[]{String.valueOf(pedido.getCodPed())});
    }




}
