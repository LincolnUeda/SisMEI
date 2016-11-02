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
import com.lincolnueda.sismei.Dominio.RepositorioOrcamento;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Orcamento;
import com.lincolnueda.sismei.R;

import java.util.ArrayList;

//tela para adicionar materiais no orçamento

public class actAdicMaterialOrc extends Activity implements View.OnClickListener{

    private EditText edtAdCodMat;
    private EditText edtAdQuant;
    private Button btnAddMat;
    private Button btnExAdMat;
    private TextView lblNomeMaterial;

    private Bundle bundle;
    private Orcamento orcamento = new Orcamento();
    private Material material = new Material();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioOrcamento repositorio;
    private ArrayList<Material> ListaMaterial = new ArrayList<Material>();
    private int posicao = -1;
    private  Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_adic_material);
        int teste = 0;


        edtAdCodMat = (EditText) findViewById(R.id.edtAdCodMat);
        edtAdQuant = (EditText) findViewById(R.id.edtAdQuant);
        btnAddMat = (Button) findViewById(R.id.btnAddMat);
        btnExAdMat = (Button) findViewById(R.id.btnExAdMat);
        lblNomeMaterial = (TextView) findViewById(R.id.lblNomeMaterial);

        btnAddMat.setOnClickListener(this);
        btnExAdMat.setOnClickListener(this);
        btnExAdMat.setVisibility(View.INVISIBLE);

        NomeMaterial nome  = new NomeMaterial(this);
        edtAdCodMat.setOnFocusChangeListener(nome);

        setResult(0, intent);
        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null) {
            if (bundle.containsKey("material")) {
                material = (Material) bundle.getSerializable("material");
                orcamento = (Orcamento) bundle.getSerializable("orcamento");
                if (bundle.containsKey("position")) {
                    posicao = bundle.getInt("position");
                    btnExAdMat.setVisibility(View.VISIBLE);
                }
                ConexaoBanco();
                PreencheCampos();

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {

        if (v == btnAddMat){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            try {
                RepositorioMaterial repMat = new RepositorioMaterial(conn);
                int cod = Integer.parseInt(edtAdCodMat.getText().toString());
                Material material = repMat.EncontraMaterial(conn, cod);
                //altera a quantidade do objeto para a quantidade digitada
                if (material.getCodmat() == 0)
                    msg.setMessage("Não existe material cadastrado com o código " + edtAdCodMat.getText().toString());
                else {
                    material.setQuant(Double.parseDouble(edtAdQuant.getText().toString()));

                    if (posicao == -1) {
                        orcamento.getListaMateriais().add(material);
                        msg.setMessage("Material Adicionado.");

                    } else {
                        orcamento.getListaMateriais().set(posicao, material);
                        msg.setMessage("Material Atualizado.");
                    }
                    intent.putExtra("materialOrc", orcamento);
                    setResult(1, intent);
                    LimpaCampos();
                }
            }catch (Exception e){
                msg.setMessage("Erro" + e.getMessage());
            }
            msg.setNeutralButton("OK", null);
            msg.show();

        }

        if (v == btnExAdMat){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            try {
                orcamento.getListaMateriais().remove(posicao);
            }catch (Exception e){
                msg.setMessage(e.getMessage());
            }

            intent.putExtra("flagExc", 1);
            intent.putExtra("materialOrc", orcamento);
            setResult(1, intent);

            LimpaCampos();
            msg.setMessage("Material Excluído.");
            msg.setNeutralButton("OK", null);
            msg.show();

        }

    }

    private void LimpaCampos(){
        edtAdCodMat.setText("");
        edtAdQuant.setText("");
        lblNomeMaterial.setText("");
    }

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioOrcamento(conn);
            //repUsuario = new RepositorioUsuario(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }
    private void PreencheCampos() {
        if (material.getCodmat() != 0) {
            edtAdCodMat.setText(String.valueOf(material.getCodmat()));
            edtAdQuant.setText(String.valueOf(material.getQuant()));

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
            if (edtAdCodMat.hasFocus() == false){
                RepositorioMaterial repMat = new RepositorioMaterial(conn);
                int cod = Integer.parseInt(edtAdCodMat.getText().toString());
                Material material = repMat.EncontraMaterial(conn, cod);
                if (material.getCodmat() > 0)
                    lblNomeMaterial.setText(material.getNomemat());
                else{
                    AlertDialog.Builder msg = new AlertDialog.Builder(this.context);
                    msg.setMessage("Não existe material cadastrado com o código " + edtAdCodMat.getText().toString());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }

            }
        }

    }

}
