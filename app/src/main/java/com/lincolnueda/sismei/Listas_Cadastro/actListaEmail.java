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

import com.lincolnueda.sismei.Cadastros.actCadEmail;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioEmail;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Email;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;


public class actListaEmail extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ClienteFornecedor clifor;
    private RepositorioEmail repositorio;
    private Usuario usuario;
    private DataBase database;
    private SQLiteDatabase conn;
    private Email email = new Email();

    private Button btnCadEmail;
    private ListView lstEmails;
    private ArrayAdapter<Email> adpEmail;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lista_email);

        btnCadEmail = (Button)findViewById(R.id.btnCadEmail);
        lstEmails = (ListView)findViewById(R.id.lstEmail);
        btnCadEmail.setOnClickListener(this);
        lstEmails.setOnItemClickListener(this);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("clifor")){
                try {
                    clifor = (ClienteFornecedor) bundle.getSerializable("clifor");
                    email.setClienteFornecedor(clifor);
                    ConexaoBanco();
                    adpEmail = repositorio.BuscaEmail(this);
                    lstEmails.setAdapter(adpEmail);
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
            repositorio = new RepositorioEmail(conn,clifor);
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
        email.setCodEmail(0);

        Intent intent = new Intent(this,actCadEmail.class);
        intent.putExtra("email",email);
        startActivityForResult(intent,1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        email = adpEmail.getItem(position);
        Intent intent = new Intent(this,actCadEmail.class);
        intent.putExtra("email",email);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        email = new Email();
        adpEmail = repositorio.BuscaEmail(this);
        lstEmails.setAdapter(adpEmail);


    }

}
