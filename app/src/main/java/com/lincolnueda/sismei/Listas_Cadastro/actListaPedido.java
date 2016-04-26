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

import com.lincolnueda.sismei.Cadastros.actCadPedido;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioPedido;
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;
import com.lincolnueda.sismei.actTabCadPedido;


public class actListaPedido extends actBase implements View.OnClickListener , AdapterView.OnItemClickListener{


    private Button btnCadPedido;
    private ListView lstPedido;
    private ArrayAdapter<Pedido> adpPedido;
    private DataBase database;
    private RepositorioPedido repositorio;
    private SQLiteDatabase conn;
    private  Pedido pedido = new Pedido();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_pedido);


        btnCadPedido = (Button) findViewById(R.id.btnCadPedido);
        lstPedido = (ListView) findViewById(R.id.lstPedido);
        ConexaoBanco();

        adpPedido = repositorio.BuscaPedido(this);
        lstPedido.setAdapter(adpPedido);
        lstPedido.setOnItemClickListener(this);
        btnCadPedido.setOnClickListener(this);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_pedido, menu);
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
            repositorio = new RepositorioPedido(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }



    @Override
    public void onClick(View v) {

        pedido.setCodPed(0);

        //Intent intent = new Intent(this,actCadPedido.class);
        Intent intent = new Intent(this,actCadPedido.class);
        intent.putExtra("pedido",pedido);
        startActivityForResult(intent,1);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pedido = adpPedido.getItem(position);
        Intent intent = new Intent(this,actCadPedido.class);
        intent.putExtra("pedido",pedido);
        startActivityForResult(intent,1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        pedido = new Pedido();
        adpPedido = repositorio.BuscaPedido(this);
        lstPedido.setAdapter(adpPedido);

    }
}
