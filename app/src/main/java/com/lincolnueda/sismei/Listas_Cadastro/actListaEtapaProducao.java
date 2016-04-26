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

import com.lincolnueda.sismei.Cadastros.actCadEtapaProducao;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioEtapaProducao;
import com.lincolnueda.sismei.Entidades.EtapaProducao;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaEtapaProducao extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnCadEtapaProducao;
    private ListView lstEtapaProducao;
    private DataBase database;
    private SQLiteDatabase conn;
    private EtapaProducao etapa;
    private RepositorioEtapaProducao repositorio;
    private ArrayAdapter<EtapaProducao> adpEtapaProducao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_etapa_producao);

        btnCadEtapaProducao = (Button) findViewById(R.id.btnCadEtapaProducao);
        btnCadEtapaProducao.setOnClickListener(this);
        lstEtapaProducao = (ListView) findViewById(R.id.lstEtapaProducao);
        ConexaoBanco();
        adpEtapaProducao = repositorio.BuscaEtapa(this);
        lstEtapaProducao.setAdapter(adpEtapaProducao);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_etapa_producao, menu);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etapa = adpEtapaProducao.getItem(position);
        Intent intent = new Intent(this,actCadEtapaProducao.class);
        intent.putExtra("etapa",etapa);
        startActivityForResult(intent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        etapa = new EtapaProducao();
        adpEtapaProducao = repositorio.BuscaEtapa(this);
        lstEtapaProducao.setAdapter(adpEtapaProducao);

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

    @Override
    public void onClick(View v) {
        etapa = new EtapaProducao();
        etapa.setCodEtapa(0);
        Intent intent = new Intent(this,actCadEtapaProducao.class);
        intent.putExtra("etapa",etapa);
        startActivityForResult(intent,1);

    }
}
