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
import com.lincolnueda.sismei.Dominio.RepositorioEtapaProducao;
import com.lincolnueda.sismei.Entidades.EtapaProducao;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.Utilidades;
import com.lincolnueda.sismei.actBase;


public class actCadEtapaProducao extends actBase implements View.OnClickListener{

    private EtapaProducao etapa;
    private Button btnSalvarEtapa;
    private Button btnExcluirEtapa;
    private EditText edtCodEtapa;
    private EditText edtNomeEtapa;
    private Bundle bundle;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioEtapaProducao repositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_etapa_producao);

        edtCodEtapa = (EditText) findViewById(R.id.edtCodEtapa);
        edtNomeEtapa = (EditText) findViewById(R.id.edtNomeEtapa);
        btnSalvarEtapa = (Button) findViewById(R.id.btnSalvarEtapa);
        btnExcluirEtapa = (Button) findViewById(R.id.btnExcluirEtapa);

        btnSalvarEtapa.setOnClickListener(this);
        btnExcluirEtapa.setOnClickListener(this);

        ConexaoBanco();

        bundle = getIntent().getExtras();//recebe parametro para saber se � cliente ou fornecedor
        if (bundle != null) {
            if (bundle.containsKey("etapa")){
                etapa = (EtapaProducao) bundle.getSerializable("etapa");
                if (etapa.getCodEtapa() == 0)
                    edtCodEtapa.setText(String.valueOf(Utilidades.AutoCodigo("EtapaProducao", "_id", conn)));
                PreencheCampos();
            }
        }
    }



    private void Salvar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        try{
            etapa.setCodEtapa(Integer.parseInt(edtCodEtapa.getText().toString()));
            etapa.setNomeEtapa(edtNomeEtapa.getText().toString());

            int cod = repositorio.BuscaCodigo(this,etapa);
            if (cod ==0) {
                repositorio.Inserir(etapa);
                Mensagem("Etapa cadastrada.");
            }else {
                repositorio.Alterar(etapa);
                Mensagem("Etapa alterada.");
            }



            Limpar();
            etapa = new EtapaProducao();

        }catch (Exception ex){
            msg.setMessage(" Erro ao inserir Dados: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }
    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Deseja excluir esta Etapa?");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(etapa);
                Mensagem("Etapa Excluida.");
                Limpar();
                etapa = new EtapaProducao();
            }
        });
        msg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        msg.show();

    }

    private void PreencheCampos(){
        if (etapa.getCodEtapa() != 0){
            edtCodEtapa.setText(String.valueOf(etapa.getCodEtapa()));
            edtNomeEtapa.setText(etapa.getNomeEtapa());
        }
    }

    private void Limpar(){
        edtCodEtapa.setText("");
        edtNomeEtapa.setText("");
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
            repositorio = new RepositorioEtapaProducao(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }





   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_etapa_producao, menu);
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
    }
*/
    @Override
    public void onClick(View v) {
        if (v == btnSalvarEtapa)
            Salvar();
        if(v == btnExcluirEtapa)
            Excluir();

    }
}
