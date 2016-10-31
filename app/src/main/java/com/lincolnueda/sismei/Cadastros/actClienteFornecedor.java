package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioUsuario;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.Listas_Cadastro.actListaEmail;
import com.lincolnueda.sismei.Listas_Cadastro.actListaTelefone;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.Listas_Cadastro.actListaEndereco;
import com.lincolnueda.sismei.Utilidades;
import com.lincolnueda.sismei.actBase;


public class actClienteFornecedor extends actBase implements OnClickListener{

    private EditText edtCodCliFor;
    private EditText edtNomCiFor;
    private EditText edtRgInsc;
    private EditText edtCnpjCpf;
    private Button btnSalvar;
    private Button btnExcluir;
    private Button btnEndereco;
    private Button btnTelefone;
    private Button btnEmail;
    private TextView lblCodCliFor;
    private TextView lblNomeCliFor;
    private TextView lblRgInsc;
    private TextView lblCnpjCpf;

    private Usuario usuario;
    private Bundle bundle;

    private  ClienteFornecedor clifor = new ClienteFornecedor();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioClienteFornecedor repositorio;
    private RepositorioUsuario repUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cliente_fornecedor);

        ConexaoBanco();

        edtCodCliFor = (EditText) findViewById(R.id.edtCodCliFor);
        edtNomCiFor = (EditText) findViewById(R.id.edtNomeCliFor);
        edtRgInsc = (EditText) findViewById(R.id.edtRgInsc);
        edtCnpjCpf = (EditText) findViewById(R.id.edtCnpjCpf);

        btnSalvar = (Button) findViewById(R.id.btnSalvarCliFor);
        btnExcluir = (Button)findViewById(R.id.btnExcluirCliFor);
        btnEndereco = (Button) findViewById(R.id.btnEndereco);
        btnTelefone = (Button) findViewById(R.id.btnTelefone);
        btnEmail = (Button) findViewById(R.id.btnEmail);

        lblCodCliFor = (TextView) findViewById(R.id.lblCodCliFor);
        lblNomeCliFor = (TextView) findViewById(R.id.lblNomCliFor);
        lblRgInsc = (TextView) findViewById(R.id.lblRgInsc);
        lblCnpjCpf = (TextView) findViewById(R.id.lblCnpjCpf);

        btnSalvar.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);
        btnEndereco.setOnClickListener(this);
        btnTelefone.setOnClickListener(this);
        btnEmail.setOnClickListener(this);

        bundle = getIntent().getExtras();//recebe parametro para saber se � cliente ou fornecedor
        if (bundle != null){
            usuario = repUsuario.Usuariologado();

           if (bundle.containsKey("cliente") ||(bundle.containsKey("fornecedor")) ) {
            if (bundle.containsKey("cliente")) {

                lblCodCliFor.setText("Cód. Cliente");
                lblNomeCliFor.setText("Nome Cliente");
                lblRgInsc.setText("RG/Insc. Estadual Cliente");
                lblCnpjCpf.setText("CPF/CNPJ Cliente");
                clifor = (ClienteFornecedor) bundle.getSerializable("cliente");
            }

            if (bundle.containsKey("fornecedor")) {
                lblCodCliFor.setText("Cód. Fornecedor");
                lblNomeCliFor.setText("Nome Fornecedor");
                lblRgInsc.setText("RG/Insc. Estadual Fornecedor");
                lblCnpjCpf.setText("CPF/CNPJ Fornecedor");
                clifor = (ClienteFornecedor) bundle.getSerializable("fornecedor");
            }
            if (clifor.getCodCliFor() == 0){
                ConexaoBanco();
                edtCodCliFor.setText(String.valueOf(Utilidades.AutoCodigo("ClienteFornecedor", "_id", conn,"tipo",new String[]{String.valueOf(clifor.getTipo())})));
            }

            PreencheCampos();
        }
        }

    }


    public void ConexaoBanco(){
        //Abre conex�o com Danco de Dados
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioClienteFornecedor(conn);
            repUsuario = new RepositorioUsuario(conn);

        }catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }
    }

    private void Salvar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        try{
            clifor.setCodCliFor(Integer.parseInt(edtCodCliFor.getText().toString()));
            clifor.setNomeCliFor(edtNomCiFor.getText().toString());
            clifor.setRgInsc(edtRgInsc.getText().toString());
            clifor.setCpfCnpj(edtCnpjCpf.getText().toString());
            /*metodo para verificar no banco o codigo digitado pelo usuario,
            para decidir se ser� feito insert ou update.*/
            int cod = repositorio.BuscaCodigo(this,clifor);
            if (cod == 0) {
                repositorio.Inserir(clifor);
                if (clifor.getTipo().equals("cli"))
                    Mensagem("Cliente Cadastrado.");
                else
                    Mensagem("Fornecedor Cadastrado.");
            }else {
                repositorio.Alterar(clifor);
                if (clifor.getTipo().equals("cli"))
                    Mensagem("Cliente Atualiizado.");
                else
                    Mensagem("Fornecedor Atualizado.");
            }
           // Limpar();
        }catch (Exception ex){
            msg.setMessage(" Erro ao inserir Dados: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }

    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        if (clifor.getTipo().equals("cli"))
            msg.setMessage("Deseja excluir este cliente?");
        else
            msg.setMessage("Deseja excluir este fornecedor?");

        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(clifor);
                if (clifor.getTipo().equals("cli"))
                    Mensagem("Cliente excluido.");
                else
                    Mensagem("Fornecedor excluido.");

                dialog.dismiss();
                Limpar();
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

    private void PreencheCampos(){
        if (clifor.getCodCliFor() != 0) {
            edtCodCliFor.setText(String.valueOf(clifor.getCodCliFor()));
            edtNomCiFor.setText(clifor.getNomeCliFor().toString());
            edtRgInsc.setText(clifor.getRgInsc().toString());
            edtCnpjCpf.setText(clifor.getCpfCnpj().toString());
        }
    }

    private void Limpar(){
        edtCodCliFor.setText("");
        edtNomCiFor.setText("");
        edtRgInsc.setText("");
        edtCnpjCpf.setText("");
        clifor = new ClienteFornecedor();
        if (bundle.containsKey("cliente"))
            clifor.setTipo("cli");
        else
            clifor.setTipo("for");

    }

    @Override
    public void onClick(View v) {
        if (v == btnSalvar){
            Salvar();
        }

        if (v == btnExcluir){
            Excluir();
        }

        if (v == btnEndereco){
            if( clifor.getCodCliFor() != 0) {
                Intent endereco = new Intent(this,actListaEndereco.class);
                endereco.putExtra("clifor", clifor);
                startActivity(endereco);
            }else{
                if (clifor.getTipo().equals("cli"))
                Mensagem("Para cadastrar um endereço, você deve primeiro cadastrar o Cliente.");
                else
                    Mensagem("Para cadastrar um endereço, você deve primeiro cadastrar o Fornecedor.");
            }

        }

        if (v == btnTelefone){
            if( clifor.getCodCliFor() != 0) {
                Intent endereco = new Intent(this,actListaTelefone.class);
                endereco.putExtra("clifor", clifor);
                startActivity(endereco);
            }else{
                if (clifor.getTipo().equals("cli"))
                    Mensagem("Para cadastrar um telefone, você deve primeiro cadastrar o Cliente.");
                else
                    Mensagem("Para cadastrar um telefone, você deve primeiro cadastrar o Fornecedor.");
            }

        }

        if (v == btnEmail){
            if( clifor.getCodCliFor() != 0) {
                Intent endereco = new Intent(this,actListaEmail.class);
                endereco.putExtra("clifor", clifor);
                startActivity(endereco);
            }else{
                if (clifor.getTipo().equals("cli"))
                    Mensagem("Para cadastrar um email, você deve primeiro cadastrar o Cliente.");
                else
                    Mensagem("Para cadastrar um email, você deve primeiro cadastrar o Fornecedor.");
            }
        }

    }
}
