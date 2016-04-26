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

import com.lincolnueda.sismei.Cadastros.actCadProduto;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioProduto;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaProduto extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Produto produto = new Produto();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioProduto repositorio;

    private Button btnCadProduto;
    private ListView lstProduto;
    private ArrayAdapter<Produto> adpProduto;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_produto);

        btnCadProduto = (Button) findViewById(R.id.btnCadProduto);
        lstProduto = (ListView) findViewById(R.id.lstProduto);
        ConexaoBanco();

        adpProduto = repositorio.BuscaProduto(this);
        lstProduto.setAdapter(adpProduto);
        btnCadProduto.setOnClickListener(this);
        lstProduto.setOnItemClickListener(this);


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_produto, menu);
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
            repositorio = new RepositorioProduto(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }

    @Override
    public void onClick(View v) {
        produto.setCodProd(0);
        Intent intent = new Intent(this,actCadProduto.class);
        intent.putExtra("produto",produto);
        startActivityForResult(intent,1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        produto = adpProduto.getItem(position);
        Intent intent = new Intent(this,actCadProduto.class);
        intent.putExtra("produto",produto);
        startActivityForResult(intent,1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        produto = new Produto();
        adpProduto = repositorio.BuscaProduto(this);
        lstProduto.setAdapter(adpProduto);

    }

}
