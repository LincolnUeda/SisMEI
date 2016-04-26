package com.lincolnueda.sismei.Listas_Cadastro;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lincolnueda.sismei.Cadastros.actCadFinanceiro;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioFinanceiro;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Financeiro;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaFinanceiro extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnAddFinan;
    private ListView lstFinanceiro;
    private ArrayAdapter<Financeiro> adpFinanceiro;
    private Financeiro financeiro = new Financeiro();
    private RepositorioFinanceiro repositorio;

    private SQLiteDatabase conn;
    private DataBase database;
    private String tipo;
    private String tipoclifor;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_financeiro);


        btnAddFinan = (Button)findViewById(R.id.btnAddFinan);
        lstFinanceiro = (ListView) findViewById(R.id.lstFinanceiro);
        btnAddFinan.setOnClickListener(this);
        lstFinanceiro.setOnItemClickListener(this);

        Bundle bundle = getIntent().getExtras();//recebe  parametro para saber se é a pagar ou a receber
        if (bundle != null){
            ConexaoBanco();
            //grava usuario para mandar de volta para tela principal:
            if (bundle.containsKey("receb")){
                btnAddFinan.setText("Cadastrar Contas a Receber");
                tipo = "receb";
            }
            if (bundle.containsKey("pagar")){
                btnAddFinan.setText("Cadastrar Contas a Pagar");
                tipo = "pagar";
            }
        }
        //Abre conex�o com Danco de Dados
        ConexaoBanco();

        if (tipo.equals("receb"))
            tipoclifor = "cli";
        else
            tipoclifor = "for";

        adpFinanceiro = repositorio.BuscaFinanceiro(this,tipo,tipoclifor);
        lstFinanceiro.setAdapter(adpFinanceiro);
    }


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioFinanceiro(conn);
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
        getMenuInflater().inflate(R.menu.menu_act_lista_financeiro, menu);
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

    @Override
    public void onClick(View v) {
        //evento do bot�o cadastrar cliente/fornecedor
        financeiro.setCodFin(0);

        ClienteFornecedor clifor = new ClienteFornecedor();
        financeiro.setClienteFornecedor(clifor);
        intent = new Intent(this,actCadFinanceiro.class);
        if (tipo.equals("receb")) {
            financeiro.setTipo("receb");
            intent.putExtra("receb", financeiro);
        }
        if (tipo.equals("pagar")) {
            financeiro.setTipo("pagar");
            intent.putExtra("pagar", financeiro);
        }
        try {
            setResult(1, intent);
            startActivityForResult(intent, 1);
        }catch(Exception e){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel criar o banco. Erro: " + e.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        financeiro = adpFinanceiro.getItem(position);
        intent = new Intent(this,actCadFinanceiro.class);

        if (tipo.equals("receb")) {
            financeiro.setTipo("receb");
            intent.putExtra("receb", financeiro);
        }
        if (tipo.equals("pagar")) {
            financeiro.setTipo("pagar");
            intent.putExtra("pagar", financeiro);
        }
        try {
            setResult(1, intent);
            startActivityForResult(intent, 1);
        }catch(Exception e){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel criar o banco. Erro: " + e.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        financeiro = new Financeiro();
        adpFinanceiro = repositorio.BuscaFinanceiro(this, tipo, tipoclifor);
        lstFinanceiro.setAdapter(adpFinanceiro);

    }
}
