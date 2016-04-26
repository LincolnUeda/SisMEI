package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioMaterial;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actCadMaterial extends actBase implements View.OnClickListener{
    private Material material;
    private ClienteFornecedor fornecedor;
    private Bundle bundle;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioMaterial repositorio;

    private Button btnSalvar;
    private Button btnExcluir;
    private EditText edtcodMaterial;
    private EditText edtNomeMaterial;
    private EditText edtCorMaterial;
    private EditText edtUnidMedidaMaterial;
    private EditText edtPrecoMaterial;
    private EditText edtQuantidadeMaterial;
    private EditText edtFornecedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_material);

        edtcodMaterial = (EditText) findViewById(R.id.edtCodMaterial);
        edtNomeMaterial = (EditText) findViewById(R.id.edtNomeMaterial);
        edtCorMaterial = (EditText) findViewById(R.id.edtCorMaterial);
        edtUnidMedidaMaterial = (EditText) findViewById(R.id.edtUnidMedida);
        edtPrecoMaterial = (EditText) findViewById(R.id.edtPrecoMaterial);
        edtQuantidadeMaterial = (EditText) findViewById(R.id.edtQuantidadeMaterial);
        edtFornecedor = (EditText) findViewById(R.id.edtCodFornecedor);

        btnSalvar = (Button) findViewById(R.id.btnSalvarMaterial);
        btnExcluir = (Button) findViewById(R.id.btnExcluirMaterial);
        btnSalvar.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);
        ConexaoBanco();

        bundle = getIntent().getExtras();//recebe parametro para saber se � cliente ou fornecedor
        if (bundle != null) {
            if (bundle.containsKey("material")){
                material = (Material) bundle.getSerializable("material");
                    PreencheCampos();
                }
            }
        }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_material, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void PreencheCampos(){
        if (material.getCodmat() != 0){
            edtcodMaterial.setText(String.valueOf(material.getCodmat()));
            edtNomeMaterial.setText(material.getNomemat());
            edtCorMaterial.setText(material.getCormat());
            edtUnidMedidaMaterial.setText(material.getUnidmed());
            edtPrecoMaterial.setText(String.valueOf(material.getValunid()));
            edtQuantidadeMaterial.setText(String.valueOf(material.getQuant()));
            edtFornecedor.setText(String.valueOf(material.getFornecedor().getCodCliFor()));
        }
    }

    private void Limpar(){
        edtcodMaterial.setText("");
        edtNomeMaterial.setText("");
        edtCorMaterial.setText("");
        edtUnidMedidaMaterial.setText("");
        edtPrecoMaterial.setText("");
        edtQuantidadeMaterial.setText("");
        edtFornecedor.setText("");
    }


    private void Salvar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        try{
            material.setCodmat(Integer.parseInt(edtcodMaterial.getText().toString()));
            material.setNomemat(edtNomeMaterial.getText().toString());
            material.setCormat(edtCorMaterial.getText().toString());
            material.setUnidmed(edtUnidMedidaMaterial.getText().toString());
            material.setQuant(Double.parseDouble(edtQuantidadeMaterial.getText().toString()));
            material.setValunid(Double.parseDouble(edtPrecoMaterial.getText().toString()));
            int codfor = Integer.parseInt(edtFornecedor.getText().toString());

            RepositorioClienteFornecedor repforn = new RepositorioClienteFornecedor(conn);
            ClienteFornecedor forn = repforn.EncontraClienteFornecedor(conn,codfor,"for");
            material.setFornecedor(forn);
            int cod = repositorio.BuscaCodigo(this,material);
            if (cod ==0) {
                repositorio.Inserir(material);
                Mensagem("Material cadastrado.");
            }else {
                repositorio.Alterar(material);
                Mensagem("Material alterado.");
            }



            Limpar();
            material = new Material();

    }catch (Exception ex){
        msg.setMessage(" Erro ao inserir Dados: " + ex.getMessage());
        msg.setNeutralButton("OK",null);
        msg.show();
    }
    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Deseja excluir este Material?");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(material);
                Mensagem("Material Excluido.");
                Limpar();
                material = new Material();
            }
        });
        msg.setNegativeButton("N\u00e3o", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        msg.show();

    }

    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioMaterial(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }




    @Override
    public void onClick(View v) {
        if (v == btnSalvar)
            Salvar();
        if(v == btnExcluir)
             Excluir();
    }
}
