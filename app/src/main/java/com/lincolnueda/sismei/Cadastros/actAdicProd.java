package com.lincolnueda.sismei.Cadastros;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioPedido;
import com.lincolnueda.sismei.Dominio.RepositorioProduto;
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.R;

import java.util.ArrayList;


public class actAdicProd extends Activity implements View.OnClickListener {


    private EditText edtAdCodProd;
    private EditText edtAdQuantProd;
    private Button btnAddProd;
    private Button btnExAdProd;
    private TextView lblNomeProduto;

    private Bundle bundle;
    private Pedido pedido = new Pedido();
    private  Produto produto = new Produto();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioPedido repositorio;
    private ArrayList<Produto> ListaMaterial = new ArrayList<Produto>();
    private int posicao = -1;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_adic_prod);

        edtAdCodProd = (EditText) findViewById(R.id.edtAdCodProd);
        edtAdQuantProd = (EditText) findViewById(R.id.edtAdQuantProd);
        btnAddProd = (Button) findViewById(R.id.btnAddProd);
        btnExAdProd = (Button) findViewById(R.id.btnExAdProd);
        lblNomeProduto = (TextView) findViewById(R.id.lblNomeProduto);

        btnAddProd.setOnClickListener(this);
        btnExAdProd.setOnClickListener(this);
        btnExAdProd.setVisibility(View.INVISIBLE);

        NomeProduto nome  = new NomeProduto(this);
        edtAdCodProd.setOnFocusChangeListener(nome);

        setResult(0, intent);
        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null) {
            if (bundle.containsKey("produto")) {
                produto = (Produto) bundle.getSerializable("produto");
                pedido = (Pedido) bundle.getSerializable("pedido");
                if (bundle.containsKey("position")) {
                    posicao = bundle.getInt("position");
                    btnExAdProd.setVisibility(View.VISIBLE);
                }
                ConexaoBanco();
                PreencheCampos();

            }

        }
    }



    @Override
    public void onClick(View v) {
        if (v == btnAddProd){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            try {
                RepositorioProduto repProd = new RepositorioProduto(conn);
                int cod = Integer.parseInt(edtAdCodProd.getText().toString());
                Produto produto = repProd.EncontraProduto(conn, this, cod);
                if (produto.getCodProd() == 0){ //verifica se o produto existe
                    msg.setMessage(" Não existe produto cadastrado com o código " + edtAdCodProd.getText().toString());
                }else {
                    //altera a quantidade do objeto para a quantidade digitada
                    produto.setQuant(Double.parseDouble(edtAdQuantProd.getText().toString()));

                    if (posicao == -1) {
                        pedido.getListaProdutos().add(produto);
                        msg.setMessage("Produto Adicionado.");

                    } else {
                        pedido.getListaProdutos().set(posicao, produto);
                        msg.setMessage("Produto Atualizado.");
                    }
                    intent.putExtra("ProdutoPed", pedido);
                    setResult(1, intent);
                    LimpaCampos();
                }
            }catch (Exception e){
                msg.setMessage("Erro" + e.getMessage());
            }
            msg.setNeutralButton("OK", null);
            msg.show();

        }

        if (v == btnExAdProd){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            try {
                pedido.getListaProdutos().remove(posicao);
            }catch (Exception e){
                msg.setMessage(e.getMessage());
            }

            intent.putExtra("flagExc", 1);
            intent.putExtra("ProdutoPed", pedido);
            setResult(1, intent);

            LimpaCampos();
            msg.setMessage("Produto Excluído.");
            msg.setNeutralButton("OK", null);
            msg.show();

        }

    }

    private void LimpaCampos(){
        edtAdCodProd.setText("");
        edtAdQuantProd.setText("");
        lblNomeProduto.setText("");
    }

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioPedido(conn);
            //repUsuario = new RepositorioUsuario(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }
    private void PreencheCampos() {
        if (produto.getCodProd() != 0) {
            edtAdCodProd.setText(String.valueOf(produto.getCodProd()));
            edtAdQuantProd.setText(String.valueOf(produto.getQuant()));

            lblNomeProduto.setText(produto.getNomeProd());
        }
    }




    private class NomeProduto implements View.OnClickListener, View.OnFocusChangeListener {

        public NomeProduto(Context context){ this.context = context;}

        private Context context;

        @Override
        public void onClick(View v) {BuscaNome();}

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            BuscaNome();
        }

        private void BuscaNome(){
            if (edtAdCodProd.hasFocus() == false){
                RepositorioProduto repProd = new RepositorioProduto(conn);
                int cod = Integer.parseInt(edtAdCodProd.getText().toString());
                Produto produto = repProd.EncontraProduto(conn,context,cod);
                if (produto.getCodProd() > 0)
                    lblNomeProduto.setText(produto.getNomeProd());
                else{
                    AlertDialog.Builder msg = new AlertDialog.Builder(this.context);
                    msg.setMessage(" Não existe produto cadastrado com o código " + edtAdCodProd.getText().toString());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }


            }
        }

    }
}
