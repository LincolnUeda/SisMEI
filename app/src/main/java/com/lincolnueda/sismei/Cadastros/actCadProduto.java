package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioProduto;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;

import java.util.ArrayList;


public class actCadProduto extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnSalvarProd;
    private Button btnAdMatProd;
    private Button btnExcluirProd;

    private EditText edtCodrod;
    private EditText edtNomeProd;
    private EditText edtPrecoProd;
    private EditText edtQuantProd;
    private TextView lblCustProd;

    private Bundle bundle;
    private Produto produto = new Produto();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioProduto repositorio;
    private ListView lstMateriaisProd;
    private ArrayAdapter<Material> adpMateriaisProd;
    private ArrayList<Material> listaMat = new ArrayList<Material>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_produto);

        btnSalvarProd = (Button) findViewById(R.id.btnSalvarProd);
        btnAdMatProd = (Button) findViewById(R.id.btnAdMatProd);
        btnExcluirProd = (Button) findViewById(R.id.btnExcluirProd);
        edtCodrod = (EditText) findViewById(R.id.edtCodProd);
        edtNomeProd = (EditText) findViewById(R.id.edtNomeProd);
        edtPrecoProd = (EditText) findViewById(R.id.edtPrecoProd);
       // edtQuantProd = (EditText) findViewById(R.id.edtQuantProd);
        lblCustProd = (TextView) findViewById(R.id.lblCustProd);
        lstMateriaisProd = (ListView) findViewById(R.id.lstMateriaisProd);

        adpMateriaisProd = new ArrayAdapter<Material>(this,android.R.layout.simple_list_item_1);
        lstMateriaisProd.setAdapter(adpMateriaisProd);
        lstMateriaisProd.setOnItemClickListener(this);

        btnSalvarProd.setOnClickListener(this);
        btnAdMatProd.setOnClickListener(this);
        btnExcluirProd.setOnClickListener(this);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("produto")){
                produto = (Produto) bundle.getSerializable("produto");
                ConexaoBanco();
                PreencheCampos();

            }
        }

    }


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioProduto(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }


    private void PreencheCampos() {
        double valortotal = 0;
        if (produto.getCodProd() != 0) {
            edtCodrod.setText(String.valueOf(produto.getCodProd()));
            edtNomeProd.setText(String.valueOf(produto.getNomeProd()));
           // edtQuantProd.setText(String.valueOf(produto.getQuant()));
            edtPrecoProd.setText(String.valueOf(produto.getPrecoProd()));

            //preenche lista de materiais do produto
            if (produto.getListaMateriais().size() > 0){
                int i = 0;
                int max = produto.getListaMateriais().size();
                Material material;
                do{
                    material = produto.getListaMateriais().get(i);

                    adpMateriaisProd.add(material);
                    valortotal += (material.getValunid() * material.getQuant());
                    i ++;
                }while (i < max);
                lblCustProd.setText("Custo Total: " + String.valueOf(valortotal));
            }


        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_produto, menu);
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
    @Override
    public void onClick(View v) {

        if (v == btnSalvarProd){
            Salvar();
        }

        if (v == btnExcluirProd){
            Excluir();
        }
        if (v == btnAdMatProd){
            AdicMateriais();
        }

    }

    private void Salvar(){
        try {
            produto.setCodProd(Integer.parseInt(edtCodrod.getText().toString()));
            produto.setNomeProd(edtNomeProd.getText().toString());
            produto.setPrecoProd(Double.parseDouble(edtPrecoProd.getText().toString()));
            produto.setQuant(0);

        }catch (Exception ex){

            Mensagem("Erro " + ex.getMessage());
        }
        int cod = repositorio.BuscaCodigo(this,produto);
        if (cod == 0){
            try {
                if (produto.getListaMateriais().size() > 0) {
                    repositorio.Inserir(produto);
                    Mensagem("Produto Cadastrado.");
                    Limpar();
                }else{
                    Mensagem("Atenção: \n Não é possivel cadastrar um produto sem materiais.");
                }
            } catch( Exception Ex){
                Mensagem("Erro: " + Ex.getMessage());
            }

        } else {
            if (produto.getListaMateriais().size() > 0) {
            repositorio.Alterar(produto);
            Mensagem("Produto Atualizado.");
            }else{
                Mensagem("Atenção: \n Não é possivel cadastrar um produto sem materiais.");
            }
        }

    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Deseja excluir este Produto?");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(produto);
                Mensagem("Produto Excluido.");
                //Limpar();
                produto = new Produto();
                Limpar();
            }
        });
        msg.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        msg.show();
    }

    private void AdicMateriais(){
        Material material = new Material();
        material.setCodmat(0);
        Intent intent = new Intent(this,actAdicMaterialProd.class);
        intent.putExtra("material", material);
        intent.putExtra("produto", produto);
        startActivityForResult(intent, 1);

    }


    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

    private void Limpar(){
        try {
            produto = new Produto();
            edtCodrod.requestFocus();
            edtCodrod.setText("");
            edtNomeProd.setText("");
            //edtQuantProd.setText("");
            edtPrecoProd.setText("");
            adpMateriaisProd.clear();
            lblCustProd.setText("Custo Total: ");

        }catch (Exception e){
            Mensagem("Erro: " + e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        double valortotal = 0;

        if (requestCode == 1) {

            if(resultCode == 1){
                Material material;
                adpMateriaisProd.clear();
                try {
                    produto = (Produto) data.getSerializableExtra("materialProd");
                    int max = produto.getListaMateriais().size();
                    int i = 0;

                    i = 0;
                    do {
                        if (max != 0) {
                            material = produto.getListaMateriais().get(i);
                            adpMateriaisProd.add(material);
                            valortotal += (material.getValunid() * material.getQuant());
                        }
                        i ++;
                    }while (i < max);
                    lblCustProd.setText("Custo Total: " + String.valueOf(valortotal));


                }catch (Exception e){
                    AlertDialog.Builder msg = new AlertDialog.Builder(this);
                    msg.setMessage("Erro: " + e.getMessage());
                    msg.setNeutralButton("OK", null);
                    msg.show();
                }
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Material material;
        material = adpMateriaisProd.getItem(position);
        Intent intent = new Intent(this,actAdicMaterialProd.class);
        intent.putExtra("material",material);
        intent.putExtra("position",position);
        intent.putExtra("produto",produto);
        startActivityForResult(intent,1);

    }

}
