package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actEditaUsuario extends actBase {


    private EditText edtLogin;
    private EditText edtSenha;
    private EditText edtConfirmaSenha;
    private Button btnSalvar;
    private SQLiteDatabase conn;


    private Usuario usuario;
    private RepositorioUsuario repositorio;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edita_usuario);

        //recuperar referencia dos objetos na activity
        edtLogin = (EditText) findViewById(R.id.edtAltLogin);
        edtSenha = (EditText) findViewById(R.id.edtAltSenha);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmaSenha);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);


        DataBase database = new DataBase(this);
        conn = database.getWritableDatabase();
        repositorio = new RepositorioUsuario(conn);
        usuario = repositorio.Usuariologado();
    }




    public void OnClick (View v){
        String senha = edtSenha.getText().toString();
        String confirma = edtConfirmaSenha.getText().toString();
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        if (senha.equals(confirma)){

            usuario.setLogin(edtLogin.getText().toString());
            usuario.setSenha(edtSenha.getText().toString());
            repositorio.Alterar(usuario);

            //cria intent com usuario que fez login e envia para tela principal:
            Intent intent = new Intent();
            setResult(1, intent);
            msg.setMessage("Usu\u00e1rio e Senha altrados.");
            msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            msg.show();


        }else {

            msg.setMessage("As senhas digitadas n\u00e3o s\u00e3o iguais.\nPor favor,digite novamente.");
            msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    edtSenha.setText("");
                    edtConfirmaSenha.setText("");
                    dialog.dismiss();
                }
            });
            msg.show();
        }





    }


}
