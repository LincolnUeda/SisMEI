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

import com.lincolnueda.sismei.Cadastros.actCadEndereco;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioEndereco;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Endereco;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaEndereco extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ClienteFornecedor clifor;
    private RepositorioEndereco repositorio;
    private Usuario usuario;
    private DataBase database;
    private SQLiteDatabase conn;
    private Endereco endereco = new Endereco();

    private Button btnCadEndereco;
    private ListView lstEnderecos;
    private ArrayAdapter<Endereco> adpEndereco;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_endereco);

        btnCadEndereco = (Button)findViewById(R.id.btnCadEndereco);
        lstEnderecos = (ListView)findViewById(R.id.lstEnderecos);
        btnCadEndereco.setOnClickListener(this);
        lstEnderecos.setOnItemClickListener(this);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("clifor")){
                try {
                    clifor = (ClienteFornecedor) bundle.getSerializable("clifor");
                    endereco.setClienteFornecedor(clifor);
                    ConexaoBanco();
                    adpEndereco = repositorio.BuscaEndereco(this);
                    lstEnderecos.setAdapter(adpEndereco);
                } catch (Exception ex){
                    AlertDialog.Builder msg = new AlertDialog.Builder(this);
                    msg.setMessage("Erro: " + ex.getMessage());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }
            }


        }


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_endereco, menu);
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



    @Override
    public void onClick(View v) {
        endereco.setCodEnd(0);

        Intent intent = new Intent(this,actCadEndereco.class);
        intent.putExtra("endereco",endereco);
        startActivityForResult(intent,1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        endereco = adpEndereco.getItem(position);
        Intent intent = new Intent(this,actCadEndereco.class);
        intent.putExtra("endereco",endereco);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        endereco = new Endereco();
        adpEndereco = repositorio.BuscaEndereco(this);
        lstEnderecos.setAdapter(adpEndereco);


    }

}
