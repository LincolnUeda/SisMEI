package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioTelefone;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Telefone;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actCadTelefone extends actBase implements View.OnClickListener{

    private ClienteFornecedor clifor;
    private Telefone telefone;
    private Bundle bundle;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioTelefone repositorio;

    private Button btnSalvar;
    private Button btnExcuir;

    private EditText edtCodTel;
    private EditText edtTelefone;
    private EditText edtTipoTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_telefone);

        btnSalvar = (Button)findViewById(R.id.btnSalvarTel);
        btnExcuir = (Button) findViewById(R.id.btnExcluirTel);
        btnSalvar.setOnClickListener(this);
        btnExcuir.setOnClickListener(this);

        edtCodTel = (EditText) findViewById(R.id.edtCodTelefone);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtTipoTel = (EditText) findViewById(R.id.edtTipoTelefone);


        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("telefone")){
                telefone = (Telefone) bundle.getSerializable("telefone");
                clifor = telefone.getClienteFornecedor();
                ConexaoBanco();
                PreencheCampos();

            }


        }


    }


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioTelefone(conn,clifor);
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
        telefone.setCodTel(Integer.parseInt(edtCodTel.getText().toString()));
        telefone.setTelefone(edtTelefone.getText().toString());
        telefone.setTipotel(edtTipoTel.getText().toString());


        int cod = repositorio.BuscaCodigo(this,telefone);
        if (cod == 0){
            try {
                repositorio.Inserir(telefone);
                msg.setMessage("Telefone Cadastrado.");
            } catch( Exception Ex){
                msg.setMessage("Erro: " + Ex.getMessage());
            }

        }else{
            repositorio.Alterar(telefone);
            msg.setMessage("Telefone Atualizado.");
        }
        msg.setNeutralButton("OK", null);
        msg.show();

    }

    private void Excluir(){
        repositorio.Excluir(telefone);
    }

    private void PreencheCampos() {
        if (telefone.getCodTel() != 0) {
            edtCodTel.setText(String.valueOf(telefone.getCodTel()));
            edtTelefone.setText(telefone.getTelefone());
            

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
