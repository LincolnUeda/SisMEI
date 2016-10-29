package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioOrcamento;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Orcamento;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.Relatorios.PdfOrcamento;
import com.lincolnueda.sismei.Utilidades;
import com.lincolnueda.sismei.actBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class actCadOrcamento extends actBase implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnSalvarOrc;
    private Button btnAdicOrc;
    private Button btnExcluirOrc;

    private ImageButton btnPdfOrc;

    private EditText edtCodOrc;
    private EditText edtCodCliente;
    private EditText edtDataOrc;
    private TextView lblNomeCliente;
    private TextView lblTotOrcamento;
    private EditText edtPrecoFinal;

    private Bundle bundle;
    private Orcamento orcamento = new Orcamento();
    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioOrcamento repositorio;
    private ListView lstMateriaisOrc;
    private ArrayAdapter<Material> adpMateriaisOrc;
    private ArrayList<Material> listaMat = new ArrayList<Material>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_orcamento);


        btnSalvarOrc = (Button) findViewById(R.id.btnSalvarOrc);
        btnAdicOrc = (Button) findViewById(R.id.btnAdicOrc);
        btnExcluirOrc = (Button) findViewById(R.id.btnExcluirOrc);
        btnPdfOrc = (ImageButton) findViewById(R.id.btnPdfOrc);
        edtCodOrc = (EditText) findViewById(R.id.edtCodOrcamento);
        edtCodCliente = (EditText) findViewById(R.id.edtCodCliente);
        edtDataOrc = (EditText) findViewById(R.id.edtDataOrc);
        edtPrecoFinal = (EditText) findViewById(R.id.edtPrecoFinal);
        lblNomeCliente = (TextView) findViewById(R.id.lblNomeCliente);
        lstMateriaisOrc = (ListView) findViewById(R.id.lstMateriaisOrc);
        lblTotOrcamento = (TextView) findViewById(R.id.lblTotOrcamento);

        adpMateriaisOrc = new ArrayAdapter<Material>(this,android.R.layout.simple_list_item_1);
        lstMateriaisOrc.setAdapter(adpMateriaisOrc);
        lstMateriaisOrc.setOnItemClickListener(this);

        btnSalvarOrc.setOnClickListener(this);
        btnAdicOrc.setOnClickListener(this);
        btnExcluirOrc.setOnClickListener(this);
        btnPdfOrc.setOnClickListener(this);


        ExibeDataListener dataListener = new ExibeDataListener();
        edtDataOrc.setOnClickListener(dataListener); // configura DatePicker para o campo data
        edtDataOrc.setOnFocusChangeListener(dataListener); // configura DatePicker para o campo data
        NomeCliente nome  = new NomeCliente();
        edtCodCliente.setOnFocusChangeListener(nome);

        btnPdfOrc.setVisibility(View.INVISIBLE);

        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null){
            if (bundle.containsKey("orcamento")){
                orcamento = (Orcamento) bundle.getSerializable("orcamento");
                if (orcamento.getCodorc() != 0)
                    btnPdfOrc.setVisibility(View.VISIBLE);
                else {
                    ConexaoBanco();
                    edtCodOrc.setText(String.valueOf(Utilidades.AutoCodigo("Orcamento", "_id", conn)));
                }
                ConexaoBanco();
                PreencheCampos();
            }
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad__orcamento, menu);
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
            //repUsuario = new RepositorioUsuario(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }


    private void PreencheCampos() {
        double valortotal = 0;
        if (orcamento.getCodorc() != 0) {
            edtCodOrc.setText(String.valueOf(orcamento.getCodorc()));
            edtCodCliente.setText(String.valueOf(orcamento.getCliente().getCodCliFor()));
            edtPrecoFinal.setText(String.valueOf(orcamento.getPrecofinal()));

            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(orcamento.getDataorc());
            edtDataOrc.setText(dt);
            lblNomeCliente.setText(orcamento.getCliente().getNomeCliFor());

            //preenche lista de itens do orçamento
           // orcamento.setListaMateriais();
            if (orcamento.getListaMateriais().size() > 0){
                int i = 0;
                int max = orcamento.getListaMateriais().size();
                Material material = new Material();
                do{
                    material = orcamento.getListaMateriais().get(i);

                    adpMateriaisOrc.add(material);
                    valortotal += (material.getValunid() * material.getQuant());
                    i ++;
                }while (i < max);
                lblTotOrcamento.setText("Custo Total: " + String.format("%.2f",valortotal));
            }


        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnSalvarOrc)
            Salvar();
        if (v ==btnExcluirOrc)
            Excluir();

        if (v == btnAdicOrc){
            AdicMateriais();
        }

        if (v == btnPdfOrc){
            File myFile = null;
           PdfOrcamento pdf = new PdfOrcamento();
            try {
               myFile =  pdf.CreatePDF(orcamento);
            } catch (FileNotFoundException e) {
                Mensagem(e.getMessage());
            } catch (DocumentException e) {
                Mensagem(e.getMessage());
            } catch (Exception e){
                Mensagem(e.getMessage());
            }


            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

    }


    private void Salvar(){
        int teste = 0;
        try {
            orcamento.setCodorc(Integer.parseInt(edtCodOrc.getText().toString()));
            orcamento.setPrecofinal(Double.parseDouble(edtPrecoFinal.getText().toString()));

            RepositorioClienteFornecedor repCliente = new RepositorioClienteFornecedor(conn);
            int codcli = Integer.parseInt(edtCodCliente.getText().toString());
            orcamento.setCliente(repCliente.EncontraClienteFornecedor(conn, codcli, "cli"));

        }catch (Exception ex){

            Mensagem("Erro " + ex.getMessage());
        }
       int cod = repositorio.BuscaCodigo(this,orcamento);
        if (cod == 0){
            try {
                if (orcamento.getListaMateriais().size() > 0) {
                    repositorio.Inserir(orcamento);
                    Mensagem("Orçamento Cadastrado.");
                    Limpar();
                }else{
                    Mensagem("Atenção: \n Não é possivel cadastrar um orçamento sem materiais.");
                }
            } catch( Exception Ex){
                Mensagem("Erro: " + Ex.getMessage());
            }

        } else {
            if (orcamento.getListaMateriais().size() > 0) {
            repositorio.Alterar(orcamento);
            Mensagem("Orçamento Atualizado.");
            }else {
                Mensagem("Atenção: \n Não é possivel cadastrar um orçamento sem materiais.");
            }
        }

    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Deseja excluir este Orcamento?");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(orcamento);
                Mensagem("Orçamento Excluido.");
                //Limpar();
                orcamento = new Orcamento();
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
        Intent intent = new Intent(this,actAdicMaterialOrc.class);
        intent.putExtra("material", material);
        intent.putExtra("orcamento", orcamento);
        try {
            startActivityForResult(intent, 1);
        }catch(Exception e){
            Mensagem("Erro: " + e.getMessage());
        }

    }


    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

    private void Limpar(){
        try {
            orcamento = new Orcamento();
            edtCodOrc.requestFocus();
            edtCodCliente.setText("");
            edtCodOrc.setText("");
            edtDataOrc.setText("");
            edtPrecoFinal.setText("");
            adpMateriaisOrc.clear();
            lblTotOrcamento.setText("Custo Total: ");
            lblNomeCliente.setText("");




            int teste = 1;
        }catch (Exception e){
            Mensagem("Erro: " + e.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        double valortotal = 0;

        if (requestCode == 1) {

            if(resultCode == 1){
                Material material = new Material();
                adpMateriaisOrc.clear();
                try {
                    orcamento = (Orcamento) data.getSerializableExtra("materialOrc");
                    int max = orcamento.getListaMateriais().size();
                    int i = 0;

                    i = 0;
                    do {
                        if (max > 0)
                            material = orcamento.getListaMateriais().get(i);
                            adpMateriaisOrc.add(material);
                            valortotal += (material.getValunid() * material.getQuant());
                        i ++;
                    }while (i < max);
                    lblTotOrcamento.setText("Custo Total: " + String.format("%.2f",valortotal));


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
        material = adpMateriaisOrc.getItem(position);
        Intent intent = new Intent(this,actAdicMaterialOrc.class);
        intent.putExtra("material",material);
        intent.putExtra("position",position);
        intent.putExtra("orcamento",orcamento);
        startActivityForResult(intent,1);

    }


    private void exibeData()
    {
        //exibe um datepicker quando clicar no campo de data
        Calendar calendar  = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlgData = new DatePickerDialog(this,new SelecionaDataListener(),ano,mes,dia);
        dlgData.show();
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener
    {
        //classe interna, para n�o ter que utilizar o metodo da classe TelaDespesas
        @Override
        public void onClick(View v) {
            exibeData();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                exibeData();
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener
    {
        // classe para receber a data selecionada no datepicker
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);

            Date data = calendar.getTime();
            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(data);

            edtDataOrc.setText(dt);
            orcamento.setDataorc(data);


        }
    }


        private class NomeCliente implements View.OnClickListener, View.OnFocusChangeListener
        {

            @Override
            public void onClick(View v) {
                if (edtCodCliente.hasFocus() == false){
                    RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                    int cod = Integer.parseInt(edtCodCliente.getText().toString());
                    ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn,cod, "cli");
                    lblNomeCliente.setText(cliente.getNomeCliFor());
                }

            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (edtCodCliente.hasFocus() == false){
                    RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                    int cod = Integer.parseInt(edtCodCliente.getText().toString());
                    ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn,cod, "cli");
                    lblNomeCliente.setText(cliente.getNomeCliFor());
                }
            }
        }
    }







