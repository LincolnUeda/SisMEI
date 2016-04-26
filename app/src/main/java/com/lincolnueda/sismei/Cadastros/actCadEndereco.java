package com.lincolnueda.sismei.Cadastros;

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
import com.lincolnueda.sismei.Dominio.RepositorioEndereco;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Endereco;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actCadEndereco extends actBase implements View.OnClickListener{

    private ClienteFornecedor clifor;
    private Endereco endereco;
    private Bundle bundle;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioEndereco repositorio;

    private Button btnSalvar;
    private Button btnExcuir;

    private EditText edtCodEnd;
    private EditText edtEndereco;
    private EditText edtCep;
    private EditText edtCidade;
    private EditText edtEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_endereco);

        btnSalvar = (Button)findViewById(R.id.btnSalvarEnd);
        btnExcuir = (Button) findViewById(R.id.btnExcluirEnd);
        btnSalvar.setOnClickListener(this);
        btnExcuir.setOnClickListener(this);

        edtCodEnd = (EditText) findViewById(R.id.edtCodEndereco);
        edtEndereco = (EditText) findViewById(R.id.edtEndereco);
        edtCep = (EditText) findViewById(R.id.edtCep);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtEstado = (EditText) findViewById(R.id.edtEstado);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("endereco")){
                endereco = (Endereco) bundle.getSerializable("endereco");
                clifor = endereco.getClienteFornecedor();
                ConexaoBanco();
                PreencheCampos();

            }


        }


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_endereco, menu);
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


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioEndereco(conn,clifor);
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
        endereco.setCodEnd(Integer.parseInt(edtCodEnd.getText().toString()));
        endereco.setEndereco(edtEndereco.getText().toString());
        endereco.setCep(edtCep.getText().toString());
        endereco.setCidade(edtCidade.getText().toString());
        endereco.setEstado(edtEstado.getText().toString());

        int cod = repositorio.BuscaCodigo(this,endereco);
        if (cod == 0){
            try {
                repositorio.Inserir(endereco);
                msg.setMessage("Endere\u00e7o Cadastrado.");
            } catch( Exception Ex){
                msg.setMessage("Erro: " + Ex.getMessage());
            }

        }else{
            repositorio.Alterar(endereco);
            msg.setMessage("Endere\u00e7o Atualizado.");
        }
        msg.setNeutralButton("OK", null);
        msg.show();

    }

    private void Excluir(){
        repositorio.Excluir(endereco);
    }

    private void PreencheCampos() {
        if (endereco.getCodEnd() != 0) {
            edtCodEnd.setText(String.valueOf(endereco.getCodEnd()));
            edtEndereco.setText(endereco.getEndereco());
            edtCep.setText(endereco.getCep());
            edtCidade.setText(endereco.getCidade());
            edtEstado.setText(endereco.getEstado());
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
