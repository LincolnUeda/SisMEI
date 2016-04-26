package com.lincolnueda.sismei;

import android.app.AlertDialog;
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
import com.lincolnueda.sismei.DataBase.ScriptSql;
import com.lincolnueda.sismei.Dominio.RepositorioEmpresa;
import com.lincolnueda.sismei.Entidades.Empresa;


public class actEmpresa extends actBase implements View.OnClickListener {
    private Button btnSalvarEmpresa;
    private EditText edtNomeEmpresa;
    private EditText edtCNPJEmpresa;
    private RepositorioEmpresa repositorio;
    private DataBase database;
    private SQLiteDatabase conn;
    private Empresa empresa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_empresa);

        btnSalvarEmpresa = (Button) findViewById(R.id.btnSalvarEmpresa);
        edtNomeEmpresa = (EditText) findViewById(R.id.edtNomeEmpresa);
        edtCNPJEmpresa = (EditText) findViewById(R.id.edtCNPJEmp);


        btnSalvarEmpresa.setOnClickListener(this);
        ConexaoBanco();
        PreencheCampos();


    }

    private void PreencheCampos(){
        empresa = repositorio.BuscarEmpresa();
        if (empresa.getCodEmp() != 0){
            edtNomeEmpresa.setText(empresa.getNomeEmp());
            edtCNPJEmpresa.setText(empresa.getCnpjEmp());
        }

    }


    public void ConexaoBanco(){
        //Abre conex?o com Danco de Dados
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioEmpresa(conn);



        }catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }
    }

    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }


    @Override
    public void onClick(View v) {
        Empresa empteste = repositorio.BuscarEmpresa();
        String mensagem;
        if(empteste.getCodEmp() == 0){
            empresa.setNomeEmp(edtNomeEmpresa.getText().toString());
            empresa.setCnpjEmp(edtCNPJEmpresa.getText().toString());
            repositorio.Inserir(empresa);
            mensagem = "Empresa Salva";

        }else {
            empresa.setNomeEmp(edtNomeEmpresa.getText().toString());
            empresa.setCnpjEmp(edtCNPJEmpresa.getText().toString());
            repositorio.Alterar(empresa);
            mensagem = "Dados Atualizados";
        }
        Mensagem(mensagem);

    }
}
