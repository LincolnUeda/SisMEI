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

import com.lincolnueda.sismei.Cadastros.actClienteFornecedor;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioUsuario;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaCliFor extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnAddCliFor;
    private ListView lstCliFor;
    private ArrayAdapter<ClienteFornecedor>adpCliFor;
    private Usuario usuario;
    private ClienteFornecedor clifor = new ClienteFornecedor();
    private RepositorioClienteFornecedor repositorio;
    private RepositorioUsuario repUsuario;
    private SQLiteDatabase conn;
    private DataBase database;
    private String tipo;
    private  Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_cli_for);

        btnAddCliFor = (Button)findViewById(R.id.btnAddFinan);
        lstCliFor = (ListView) findViewById(R.id.lstCliFor);
        btnAddCliFor.setOnClickListener(this);
        lstCliFor.setOnItemClickListener(this);

        Bundle bundle = getIntent().getExtras();//recebe usuario e parametro para saber se � cliente ou fornecedor
        if (bundle != null){
            ConexaoBanco();
            usuario = repUsuario.Usuariologado();
            //grava usuario para mandar de volta para tela principal:
           if (bundle.containsKey("cliente")){
                btnAddCliFor.setText("Cadastrar Cliente");
                tipo = "cli";
            }
            if (bundle.containsKey("fornecedor")){
                btnAddCliFor.setText("Cadastrar Fornecedor");
                tipo = "for";
            }
        }
        //Abre conex�o com Danco de Dados
        ConexaoBanco();
        adpCliFor = repositorio.BuscaCliFor(this,tipo);
        lstCliFor.setAdapter(adpCliFor);


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_cli_for, menu);
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
            repositorio = new RepositorioClienteFornecedor(conn);
            repUsuario = new RepositorioUsuario(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }

    @Override
    public void onClick(View v) {
        //evento do bot�o cadastrar cliente/fornecedor
        clifor.setCodCliFor(0);
        intent = new Intent(this,actClienteFornecedor.class);
        if (tipo.equals("cli")) {
            clifor.setTipo("cli");
            intent.putExtra("cliente", clifor);
        }
        if (tipo.equals("for")) {
            clifor.setTipo("for");
            intent.putExtra("fornecedor", clifor);
        }
        setResult(1, intent);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clifor = adpCliFor.getItem(position);
        intent = new Intent(this,actClienteFornecedor.class);

        if (tipo.equals("cli"))
            intent.putExtra("cliente",clifor);
        if (tipo.equals("for"))
            intent.putExtra("fornecedor",clifor);

        intent.putExtra("usuario", usuario);
        setResult(1, intent);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        clifor = new ClienteFornecedor();
        adpCliFor = repositorio.BuscaCliFor(this,tipo);
        lstCliFor.setAdapter(adpCliFor);

    }
}
