package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioEmail;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Email;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.Utilidades;
import com.lincolnueda.sismei.actBase;


public class actCadEmail extends actBase implements View.OnClickListener{

    private ClienteFornecedor clifor;
    private Email email;
    private Bundle bundle;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioEmail repositorio;

    private Button btnSalvar;
    private Button btnExcuir;

    private EditText edtCodEmail;
    private EditText edtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_email);

        btnSalvar = (Button)findViewById(R.id.btnSalvarEnd);
        btnExcuir = (Button) findViewById(R.id.btnExcluirEnd);
        btnSalvar.setOnClickListener(this);
        btnExcuir.setOnClickListener(this);

        edtCodEmail = (EditText) findViewById(R.id.edtCodEmail);
        edtEmail = (EditText) findViewById(R.id.edtEmail);


        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("email")){
                email = (Email) bundle.getSerializable("email");
                clifor = email.getClienteFornecedor();
                ConexaoBanco();

                if (email.getCodEmail() ==0)
                    edtCodEmail.setText(String.valueOf(Utilidades.AutoCodigo("Email", "_id", conn,"clientefornecedor = ? and tipoclifor = ?",new String[]{String.valueOf(email.getClienteFornecedor().getCodCliFor()),String.valueOf(email.getClienteFornecedor().getTipo())})));
                PreencheCampos();

            }


        }


    }

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioEmail(conn,clifor);
            //repUsuario = new RepositorioUsuario(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }

    public void Salvar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        email.setCodEmail(Integer.parseInt(edtCodEmail.getText().toString()));
        email.setEmail(edtEmail.getText().toString());

        int cod = repositorio.BuscaCodigo(this,email);
        if (cod == 0){
            try {
                repositorio.Inserir(email);
                msg.setMessage("Email Cadastrado.");
            } catch( Exception Ex){
                msg.setMessage("Erro: " + Ex.getMessage());
            }

        }else{
            repositorio.Alterar(email);
            msg.setMessage("Email Atualizado.");
        }
        msg.setNeutralButton("OK", null);
        msg.show();

    }

    private void Excluir(){
        repositorio.Excluir(email);
    }

    private void PreencheCampos() {
        if (email.getCodEmail() != 0) {
            edtCodEmail.setText(String.valueOf(email.getCodEmail()));
            edtEmail.setText(email.getEmail());

        }
    }




    @Override
    public void onClick(View v) {
        if (v == btnSalvar)
            Salvar();
        if (v ==btnExcuir)
            Excluir();

    }
}
