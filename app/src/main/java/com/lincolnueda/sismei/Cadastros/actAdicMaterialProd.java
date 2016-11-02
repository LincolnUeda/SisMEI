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
import com.lincolnueda.sismei.Dominio.RepositorioMaterial;
import com.lincolnueda.sismei.Dominio.RepositorioProduto;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.R;

import java.util.ArrayList;


public class actAdicMaterialProd extends Activity implements View.OnClickListener{

    private EditText edtAdCodProd;
    private EditText edtAdQuantProd;
    private Button btnSalvarAdProd;
    private Button btnExAdProd;
    private TextView lblNomeMaterial;

    private Bundle bundle;
    private Produto produto = new Produto();
    private Material material = new Material();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioProduto repositorio;
    private ArrayList<Material> ListaMaterial = new ArrayList<Material>();
    private int posicao = -1;
    private Intent intent = new Intent();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_adic_material_prod);
         int teste = 0;


        edtAdCodProd = (EditText) findViewById(R.id.edtCodAdProd);
        edtAdQuantProd = (EditText) findViewById(R.id.edtAdQuantProd);
        btnSalvarAdProd = (Button) findViewById(R.id.btnSalvarAdProd);
        btnExAdProd = (Button) findViewById(R.id.btnExAdProd);
        lblNomeMaterial = (TextView) findViewById(R.id.lblNomeMatProd);

        btnSalvarAdProd.setOnClickListener(this);
        btnExAdProd.setOnClickListener(this);
        btnExAdProd.setVisibility(View.INVISIBLE);

        NomeMaterial nome = new NomeMaterial(this);
        edtAdCodProd.setOnFocusChangeListener(nome);

        setResult(0, intent);
        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null) {
            if (bundle.containsKey("material")) {
                material = (Material) bundle.getSerializable("material");
                produto = (Produto) bundle.getSerializable("produto");
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

        if (v == btnSalvarAdProd){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            try {
                RepositorioMaterial repMat = new RepositorioMaterial(conn);
                int cod = Integer.parseInt(edtAdCodProd.getText().toString());
                Material material = repMat.EncontraMaterial(conn, cod);
                //altera a quantidade do objeto para a quantidade digitada
                if (material.getCodmat() == 0)
                    msg.setMessage("Não existe material cadastrado com o código " + edtAdCodProd.getText().toString());
                else {
                    material.setQuant(Double.parseDouble(edtAdQuantProd.getText().toString()));

                    if (posicao == -1) {
                        produto.getListaMateriais().add(material);
                        msg.setMessage("Material Adicionado.");

                    } else {
                        produto.getListaMateriais().set(posicao, material);
                        msg.setMessage("Material Atualizado.");
                    }
                    intent.putExtra("materialProd", produto);
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
                produto.getListaMateriais().remove(posicao);
            }catch (Exception e){
                msg.setMessage(e.getMessage());
            }

            intent.putExtra("flagExc", 1);
            intent.putExtra("materialProd", produto);
            setResult(1, intent);

            LimpaCampos();
            msg.setMessage("Material Excluído.");
            msg.setNeutralButton("OK", null);
            msg.show();

        }

    }


    private void LimpaCampos(){
        edtAdCodProd.setText("");
        edtAdQuantProd.setText("");
        lblNomeMaterial.setText("");
    }

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioProduto(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }
    private void PreencheCampos() {
        if (material.getCodmat() != 0) {
            edtAdCodProd.setText(String.valueOf(material.getCodmat()));
            edtAdQuantProd.setText(String.valueOf(material.getQuant()));

            lblNomeMaterial.setText(material.getNomemat());
        }
    }

    private class NomeMaterial implements View.OnClickListener, View.OnFocusChangeListener {
        public NomeMaterial(Context context){ this.context = context;}
        private Context context;
        @Override
        public void onClick(View v) {
            BuscaNome();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            BuscaNome();
        }

        private void BuscaNome(){
            if (edtAdCodProd.hasFocus() == false){
                RepositorioMaterial repMat = new RepositorioMaterial(conn);
                int cod = Integer.parseInt(edtAdCodProd.getText().toString());
                Material material = repMat.EncontraMaterial(conn, cod);
                if (material.getCodmat() > 0)
                    lblNomeMaterial.setText(material.getNomemat());
                else {
                    AlertDialog.Builder msg = new AlertDialog.Builder(this.context);
                    msg.setMessage("Não existe material cadastrado com o código " + edtAdCodProd.getText().toString());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }
            }
        }
    }
}
