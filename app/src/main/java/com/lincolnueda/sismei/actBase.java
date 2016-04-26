package com.lincolnueda.sismei;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lincolnueda.sismei.Cadastros.actEditaUsuario;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioUsuario;
import com.lincolnueda.sismei.Entidades.Usuario;


public class actBase extends ActionBarActivity {

    SQLiteDatabase conn;
    DataBase database;
    Usuario  usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_base);

        ConexaoBD();

    }




    private void ConexaoBD(){
        //conex?o com o banco
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            RepositorioUsuario repositorio = new RepositorioUsuario(conn);
           usuario = repositorio.Usuariologado();
        }

        catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();

        }//tryCacth
    }// ConexaoBD



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        VerificaUsuario(menu);
        return true;
    }


    private void VerificaUsuario(Menu menu){
        if (usuario.getLogado() != 0)
            getMenuInflater().inflate(R.menu.main_menu, menu);
        else
            getMenuInflater().inflate(R.menu.main_menu_2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_usuario) {
            this.finish();
            Intent teste = new Intent(this, actEditaUsuario.class);
            startActivityForResult(teste, 1);

            return true;
        }

        if (id == R.id.action_mainmenu) {
            this.finish();
            Intent teste = new Intent(this, MainActivity.class);
            startActivityForResult(teste, 1);

            return true;
        }

        if (id == R.id.action_sair) {
            DataBase database;
            SQLiteDatabase conn = null;

            try {
                database = new DataBase(this);
                conn = database.getWritableDatabase();

                //repUsuario = new RepositorioUsuario(conn);
            } catch (SQLException ex) {
                AlertDialog.Builder msg = new AlertDialog.Builder(this);
                msg.setMessage("N\u00e3o foi possivel conectar com o banco. Erro: " + ex.getMessage());
                msg.setNeutralButton("OK", null);
                msg.show();
            }

            RepositorioUsuario repositorioUsuario = new RepositorioUsuario(conn);
            Usuario usuario =  repositorioUsuario.Logout();
            usuario.setLogado(0);
            repositorioUsuario.Alterar(usuario);
            finish();
            System.exit(0);

            return true;
        }

        if (id == R.id.action_empresa) {
            this.finish();
            Intent teste = new Intent(this, actEmpresa.class);
            startActivityForResult(teste, 1);

            return true;
        }

        if (id == R.id.action_BancoDados) {
            this.finish();
            Intent bd = new Intent(this, actBandoDados.class);
            startActivityForResult(bd, 1);

            return true;
        }


        if (id == R.id.action_login) {
            this.finish();
            Intent teste = new Intent(this, actLogin.class);
            startActivityForResult(teste, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        if (requestCode == 1) {


        }

    }

}
