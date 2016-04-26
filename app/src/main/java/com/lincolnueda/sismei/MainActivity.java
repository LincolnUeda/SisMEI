package com.lincolnueda.sismei;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.lincolnueda.sismei.Cadastros.actEditaUsuario;
import com.lincolnueda.sismei.DataBase.CopyDB;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioUsuario;
import com.lincolnueda.sismei.Entidades.Usuario;
import com.lincolnueda.sismei.Listas_Cadastro.actListaCliFor;
import com.lincolnueda.sismei.Listas_Cadastro.actListaEtapaProducao;
import com.lincolnueda.sismei.Listas_Cadastro.actListaFinanceiro;
import com.lincolnueda.sismei.Listas_Cadastro.actListaMateriais;
import com.lincolnueda.sismei.Listas_Cadastro.actListaOrcamento;
import com.lincolnueda.sismei.Listas_Cadastro.actListaPedido;
import com.lincolnueda.sismei.Listas_Cadastro.actListaProduto;
import com.lincolnueda.sismei.Relatorios.PDFTeste;
import com.lincolnueda.sismei.Relatorios.actRelFinanceiro;

import java.io.File;
import java.io.FileNotFoundException;


public class MainActivity extends actBase  implements View.OnClickListener{

    private DataBase database;
    private SQLiteDatabase conn;
    private Usuario usuario;

   // private Button btnTeste;
    private ImageButton btnCliente;
    private ImageButton btnFornecedor;
    private ImageButton btnMateriais;
    private ImageButton btnOrcamento;
    private ImageButton btnProduto;
    private ImageButton btnPedido;
    private ImageButton btnEtapaProd;
    private ImageButton btnContasPagar;
    private ImageButton btnContasReceber;
   // private ImageButton btnPDF;
    private ImageButton btnRelatorio;
    private TextView lblteste;
    private int novousuario = 0;

    private LinearLayout layMain;

 // importa/exporta Banco de Dados
    private ImageButton btnBackup;
    private ImageButton btnImporta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // btnTeste  = (ImageButton) findViewById(R.id.btnTeste);
        btnCliente = (ImageButton) findViewById(R.id.btnCliente);
        btnFornecedor = (ImageButton) findViewById(R.id.btnFornecedor);
        btnMateriais = (ImageButton) findViewById(R.id.btnMaterial);
        btnOrcamento = (ImageButton) findViewById(R.id.btnOrcamento);
        btnProduto = (ImageButton) findViewById(R.id.btnProduto);
        btnPedido = (ImageButton) findViewById(R.id.btnPedido);
        btnEtapaProd = (ImageButton) findViewById(R.id.btnEtapaProd);
        btnContasPagar = (ImageButton) findViewById(R.id.btnContasPagar);
        btnContasReceber = (ImageButton) findViewById(R.id.btnContasReceber);
       // btnPDF = (ImageButton) findViewById(R.id.btnPDF);

        btnRelatorio = (ImageButton) findViewById(R.id.btnRelatorio);

        lblteste = (TextView) findViewById(R.id.lblteste);

       // btnBackup = (ImageButton) findViewById(R.id.btnBackup);
       // btnImporta = (ImageButton) findViewById(R.id.btnImporta);

        layMain = (LinearLayout) findViewById(R.id.Lay_Main);
        layMain.setVisibility(View.VISIBLE);



        ConexaoBD();
        if ((usuario.getLogado() == 0) && (novousuario == 0) )
            SemUsuario();

       // btnTeste.setOnClickListener(this);
        btnCliente.setOnClickListener(this);
        btnFornecedor.setOnClickListener(this);
        btnMateriais.setOnClickListener(this);
        btnOrcamento.setOnClickListener(this);
        btnProduto.setOnClickListener(this);
        btnPedido.setOnClickListener(this);
        btnEtapaProd.setOnClickListener(this);
        btnContasPagar.setOnClickListener(this);
        btnContasReceber.setOnClickListener(this);
        btnRelatorio.setOnClickListener(this);


        lblteste.setText(usuario.getLogin());

//        btnBackup.setOnClickListener(this);
       // btnImporta.setOnClickListener(this);
       // btnPDF.setOnClickListener(this);

    }

      //----------------------------------------------------------------------------------------------
    //Metodos criados pelo desenvolvedor:

    private void ConexaoBD(){
        //conex�o com o banco
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            RepositorioUsuario repositorio = new RepositorioUsuario(conn);
            int quant = repositorio.Varredura(); //apenas conta quantos usuarios existem no banco.
            if (quant == 0) {
                AlertDialog.Builder msg = new AlertDialog.Builder(this);
                msg.setMessage("Voc\u00ea n\u00e3o possui usu\u00e1rio cadastrado. Um usuario padr\u00e3o foi criado.\nUsu\u00e1rio: admin\nSenha:admin123");
                msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       SemUsuario();

                    }
                });
                msg.show();
                //se n�o houver usuario cadastrado, ser� criado usuario padr�o.
                ContentValues values = new ContentValues();
                values.put("login","admin");
                values.put("senha","admin123");
                values.put("logado",0);
                conn.insertOrThrow("Usuario", null, values);
                usuario = repositorio.Usuariologado();
                novousuario = 1;
            }else{
               usuario = repositorio.Usuariologado();
            }
        }
        catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("N\u00e3o foi possivel criar o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();

        }//tryCacth
    }// ConexaoBD

    public void SemUsuario(){
        layMain.setVisibility(View.INVISIBLE);
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        //mostra mensagem de usuario n�o logado
        if (novousuario == 0) {
            msg.setMessage("Nenhum usuário logado no sistema.\nPor favor, realize o login.");
            msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent login = new Intent(MainActivity.this, actLogin.class);
                    startActivityForResult(login, 1);
                    dialog.dismiss();
                }
            });
            msg.show();
        }else{
            Intent login = new Intent(MainActivity.this, actLogin.class);
            startActivityForResult(login, 1);
        }
    }

    //------------------------------------------------------------------------------------------


    @Override
    public void onClick(View v) {

        int teste1 = 0;

        if (v == btnCliente) {
            Intent cliente = new Intent(this, actListaCliFor.class);
           cliente.putExtra("cliente","cliente");
            startActivityForResult(cliente, 1);

        }
        if (v == btnFornecedor){
           Intent fornecedor = new Intent(this, actListaCliFor.class);
            fornecedor.putExtra("fornecedor","fornecedor");
            startActivityForResult(fornecedor, 1);
        }
        if (v == btnMateriais){
            Intent material = new Intent(this, actListaMateriais.class);
            startActivityForResult(material, 1);
        }


        if (v == btnOrcamento){
            Intent orcamento = new Intent(this, actListaOrcamento.class);
            startActivityForResult(orcamento, 1);
        }

        if (v == btnProduto){
            Intent produto = new Intent(this, actListaProduto.class);
            startActivityForResult(produto, 1);
        }

        if (v == btnPedido){
            Intent pedido = new Intent(this, actListaPedido.class);
            startActivityForResult(pedido, 1);
        }

        if (v == btnEtapaProd){
                int teste = 0;
                Intent etapa = new Intent(this, actListaEtapaProducao.class);
                startActivityForResult(etapa, 1);

        }

        if (v == btnContasPagar) {
            Intent pagar = new Intent(this, actListaFinanceiro.class);
            pagar.putExtra("pagar","pagar");
            startActivityForResult(pagar, 1);

        }
        if (v == btnContasReceber){
            Intent receber = new Intent(this, actListaFinanceiro.class);
            receber.putExtra("receb","receb");
            startActivityForResult(receber, 1);
        }

        if (v == btnRelatorio){
            Intent relatorio = new Intent(this, actRelFinanceiro.class);

            startActivityForResult(relatorio, 1);
        }



        if (v == btnBackup){
            CopyDB copia = new CopyDB();
            copia.exportDB();
        }
        if (v == btnImporta){
            CopyDB copia = new CopyDB();
            copia.importDB();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        if (requestCode == 1) {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            RepositorioUsuario repositorio = new RepositorioUsuario(conn);
                usuario = repositorio.Usuariologado();
                lblteste.setText(usuario.getLogin());
               // btnTeste.setVisibility(View.VISIBLE);
            if (usuario.getLogado() == 1)
                    layMain.setVisibility(View.VISIBLE);
            }else {
                    SemUsuario();


            }

        }

}
