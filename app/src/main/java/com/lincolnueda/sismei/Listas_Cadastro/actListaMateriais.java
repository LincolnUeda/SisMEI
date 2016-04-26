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

import com.lincolnueda.sismei.Cadastros.actCadMaterial;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioMaterial;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaMateriais extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{
    private Material material;
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioMaterial repositorio;
    private ArrayAdapter<Material> adpMaterial;

    private Button btnCadMaterial;
    private ListView lstMaterial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_materiais);

        btnCadMaterial = (Button)findViewById(R.id.btnCadMaterial);
        lstMaterial = (ListView) findViewById(R.id.lstMaterial);
        ConexaoBanco();
        adpMaterial = repositorio.BuscaMaterial(this);
        lstMaterial.setAdapter(adpMaterial);

        btnCadMaterial.setOnClickListener(this);
        lstMaterial.setOnItemClickListener(this);


    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_lista_materiais, menu);
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
            repositorio = new RepositorioMaterial(conn);
           } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }


    @Override
    public void onClick(View v) {
        material = new Material();
        material.setCodmat(0);
        Intent intent = new Intent(this,actCadMaterial.class);
        intent.putExtra("material",material);
        startActivityForResult(intent,1);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        material = adpMaterial.getItem(position);
        Intent intent = new Intent(this,actCadMaterial.class);
        intent.putExtra("material",material);
        startActivityForResult(intent,1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        material = new Material();
        adpMaterial = repositorio.BuscaMaterial(this);
        lstMaterial.setAdapter(adpMaterial);

    }
}
