package com.lincolnueda.sismei.Listas_Cadastro;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.lincolnueda.sismei.Cadastros.actCadTelefone;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioTelefone;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Telefone;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaTelefone extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ClienteFornecedor clifor;
    private RepositorioTelefone repositorio;
    private Usuario usuario;
    private DataBase database;
    private SQLiteDatabase conn;
    private Telefone telefone = new Telefone();

    private Button btnCadTelefone;
    private ListView lstTelefone;
    private ArrayAdapter<Telefone> adpTelefone;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_telefone);

        btnCadTelefone = (Button)findViewById(R.id.btnCadTelefone);
        lstTelefone = (ListView)findViewById(R.id.lstTelefone);
        btnCadTelefone.setOnClickListener(this);
        lstTelefone.setOnItemClickListener(this);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("clifor")){
                try {
                    clifor = (ClienteFornecedor) bundle.getSerializable("clifor");
                    telefone.setClienteFornecedor(clifor);
                    ConexaoBanco();
                    adpTelefone = repositorio.BuscaTelefone(this);
                    lstTelefone.setAdapter(adpTelefone);
                } catch (Exception ex){
                    AlertDialog.Builder msg = new AlertDialog.Builder(this);
                    msg.setMessage("Erro: " + ex.getMessage());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }
            }


        }


    }


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioTelefone(conn,clifor);
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
        telefone.setCodTel(0);

        Intent intent = new Intent(this,actCadTelefone.class);
        intent.putExtra("telefone",telefone);
        startActivityForResult(intent,1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        telefone = adpTelefone.getItem(position);
        Intent intent = new Intent(this,actCadTelefone.class);
        intent.putExtra("telefone",telefone);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        telefone = new Telefone();
        adpTelefone = repositorio.BuscaTelefone(this);
        lstTelefone.setAdapter(adpTelefone);


    }

}
