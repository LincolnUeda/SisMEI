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

import com.lincolnueda.sismei.Cadastros.actCadOrcamento;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioOrcamento;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Orcamento;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaOrcamento extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Orcamento orcamento = new Orcamento();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioOrcamento repositorio;

    private ArrayAdapter<Orcamento> adpOrcamento;
    private Button btnCadOrcamento;
    private ListView lstOrcamento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_orcamento);

        btnCadOrcamento = (Button) findViewById(R.id.btnCadOrcamento);
        lstOrcamento = (ListView) findViewById(R.id.lstOrcamento);
        ConexaoBanco();

        adpOrcamento = repositorio.BuscaOrcamento(this);
        lstOrcamento.setAdapter(adpOrcamento);
        btnCadOrcamento.setOnClickListener(this);
        lstOrcamento.setOnItemClickListener(this);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_orcamento, menu);
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

    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioOrcamento(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }


    @Override
    public void onClick(View v) {
        int debug = 0;
        orcamento.setCodorc(0);

        Intent intent = new Intent(this,actCadOrcamento.class);
        intent.putExtra("orcamento",orcamento);
        startActivityForResult(intent,1);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        orcamento = adpOrcamento.getItem(position);
        Intent intent = new Intent(this,actCadOrcamento.class);
        intent.putExtra("orcamento",orcamento);
        startActivityForResult(intent,1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        orcamento = new Orcamento();
        adpOrcamento = repositorio.BuscaOrcamento(this);
        lstOrcamento.setAdapter(adpOrcamento);

    }
}
