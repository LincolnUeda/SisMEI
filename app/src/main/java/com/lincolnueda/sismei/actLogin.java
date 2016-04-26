package com.lincolnueda.sismei;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioUsuario;
import com.lincolnueda.sismei.Entidades.Usuario;


public class actLogin extends Activity implements View.OnClickListener{

   // public actLogin(SQLiteDatabase conn){this.conn = conn;} // construtor da classe(deve receber a conex
   // �o feita na actMain


    private EditText edtLogin;
    private EditText edtSenha;
    private Button btnOK;
    private Button btnVoltar;
    private SQLiteDatabase conn;
    private DataBase dataBase;

    private RepositorioUsuario repositorioUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        //recuperar objetos da tela
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        btnOK.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);

        ConexaoBD();




    }



    @Override
    public void onClick(View v) {

        if (v == btnOK){
            repositorioUsuario = new RepositorioUsuario(conn);
            //envia login e senha digitados para buscar usuario no banco
            Usuario usuario = new Usuario();
             usuario = repositorioUsuario.BuscarUsuario(edtLogin.getText().toString(),edtSenha.getText().toString());

            if (usuario.getLogin() != null){

                //cria intent com usuario que fez login e envia para tela principal:
                Intent intent = new Intent();
                usuario.setLogado(1);
                repositorioUsuario.Alterar(usuario);
                setResult(1,intent);
                finish();

            }else{
                AlertDialog.Builder msg = new AlertDialog.Builder(this);
                msg.setMessage("Usuario ou senha invalidos!");
                msg.setNeutralButton("OK",null);
                msg.show();
            }

        }
        if (v == btnVoltar){
            finish();
        }
    }


    private void ConexaoBD(){
        try {
            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

           // ContentValues values = new ContentValues();
           // values.put("login","adm");
           // values.put("senha","123");
           // conn.insertOrThrow("Usuario", null, values);


        }
        catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();

        }
    }



}
