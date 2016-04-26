package com.lincolnueda.sismei.Cadastros;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioFinanceiro;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Financeiro;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class actCadFinanceiro extends actBase implements View.OnClickListener {



    private EditText edtCodFinan;
    private EditText edtDescFinan;
    private TextView lblParcAtual;
    private EditText edtDataVencFinan;
    private EditText edtCliFinan;
    private EditText edtNotaFinan;
    private EditText edtParcela;
    private EditText edtValorFinan;

    private Button btnSalvarFinan;
    private Button btnExcluirFinan;
    private TextView lblNomeCliFinan;
    private  TextView lblCliFinan;


    private DataBase database;
    private SQLiteDatabase conn;
    private RepositorioFinanceiro repositorio;
    private Financeiro financeiro = new Financeiro();
    private ClienteFornecedor clifor;
    private Bundle bundle;
    private String tipoCliFor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_financeiro);

        edtCodFinan = (EditText) findViewById(R.id.edtCodFinan);
        edtDescFinan = (EditText) findViewById(R.id.edtDescFinan);
        lblParcAtual = (TextView) findViewById(R.id.lblParcAtual);
        edtCliFinan = (EditText) findViewById(R.id.edtCliFinan);
        edtDataVencFinan = (EditText) findViewById(R.id.edtDataVencFinan);
        edtNotaFinan = (EditText) findViewById(R.id.edtNotaFinan);
        edtParcela = (EditText) findViewById(R.id.edtParcela);
        edtValorFinan = (EditText) findViewById(R.id.edtValorFinan);


        btnSalvarFinan = (Button) findViewById(R.id.btnSalvarFinan);
        btnExcluirFinan = (Button)findViewById(R.id.btnExcluirFinan);

        btnSalvarFinan.setOnClickListener(this);
        btnExcluirFinan.setOnClickListener(this);

        lblNomeCliFinan = (TextView) findViewById(R.id.lblNomeCliFinan);
        lblCliFinan = (TextView) findViewById(R.id.lblCliFinan);

        ExibeDataListener dataListener = new ExibeDataListener();
        edtDataVencFinan.setOnClickListener(dataListener); // configura DatePicker para o campo data
        edtDataVencFinan.setOnFocusChangeListener(dataListener); // configura DatePicker para o campo data
        NomeCliente nome  = new NomeCliente();
        edtCliFinan.setOnFocusChangeListener(nome);


        bundle = getIntent().getExtras();//recebe parametro para saber se é a pagar ou a receber
        if (bundle != null){

            if (bundle.containsKey("receb") ||(bundle.containsKey("pagar")) ) {
                if (bundle.containsKey("receb")) {

                    lblCliFinan.setText("Cód.Cliente");
                    financeiro = (Financeiro) bundle.getSerializable("receb");
                    tipoCliFor = "cli";

                }
                if (bundle.containsKey("pagar")) {

                    lblCliFinan.setText("Cód.Fornecedor");
                    financeiro = (Financeiro) bundle.getSerializable("pagar");
                    tipoCliFor = "for";
                }
                PreencheCampos();
            }
        }
        ConexaoBanco();







    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_cad_financeiro, menu);
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

    @Override
    public void onClick(View v) {
        if (v == btnSalvarFinan){
            Salvar();
        }
        if ( v == btnExcluirFinan){
            Excluir();
        }

    }





    private void Salvar(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        try{
            financeiro.setCodFin(Integer.parseInt(edtCodFinan.getText().toString()));
            financeiro.setDescricao(edtDescFinan.getText().toString());
            financeiro.setCodpedido(0);

            String nota = edtNotaFinan.getText().toString();
            if (nota.isEmpty())
                nota = "0";

            financeiro.setNumnota(Integer.parseInt(nota));
           // financeiro.setNumparcela(Integer.parseInt(edtParcAtual.getText().toString()));
            financeiro.setTotalparcelas(Integer.parseInt(edtParcela.getText().toString()));
            financeiro.setValor(Double.parseDouble(edtValorFinan.getText().toString()));

            RepositorioClienteFornecedor repCliente = new RepositorioClienteFornecedor(conn);
            int codcli = Integer.parseInt(edtCliFinan.getText().toString());
            financeiro.setClienteFornecedor(repCliente.EncontraClienteFornecedor(conn, codcli, tipoCliFor));


            /*metodo para verificar no banco o codigo digitado pelo usuario,
            para decidir se será feito insert ou update.*/
            int cod = repositorio.BuscaCodigo(this,financeiro);
            if (cod == 0) {

                //caso a conta tenha mais de 1 parcela, o sistema irá cadastrá-las automaticamente.
                if( financeiro.getTotalparcelas() > 1) {
                    CadastrarParcelas();
                }else {
                    int numparc = 1;
                    financeiro.setNumparcela(numparc);
                    repositorio.Inserir(financeiro);
                }

                if (financeiro.getTipo().equals("receb"))
                    Mensagem("Recebimento Cadastrado.");
                else
                    Mensagem("Pagamento Cadastrado.");
            }else {
                    repositorio.Alterar(financeiro);

                if (financeiro.getTipo().equals("receb"))
                    Mensagem("recebimento Atualiizado.");
                else
                    Mensagem("Pagamento Atualizado.");
            }
            // Limpar();
        }catch (Exception ex){
            msg.setMessage(" Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }

    }

    private void Excluir(){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        if (financeiro.getTipo().equals("receb"))
            msg.setMessage("Deseja excluir este Recebimento?");
        else
            msg.setMessage("Deseja excluir este Pagamento?");

        msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(financeiro);
                if (financeiro.getTipo().equals("receb"))
                    Mensagem("Recebimento excluido.");
                else
                    Mensagem("Pagamento excluido.");

                dialog.dismiss();
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

    private void CadastrarParcelas(){
        int numparc = 1;
        double valortotal = financeiro.getValor();
        do{

            financeiro.setValor(valortotal / financeiro.getTotalparcelas());
            financeiro.setNumparcela(numparc);
            String desc = financeiro.getDescricao();
            financeiro.setDescricao(desc + "( Parcela " + financeiro.getNumparcela() + "/" + financeiro.getTotalparcelas() + ")");
            repositorio.Inserir(financeiro);

            Date data = financeiro.getDataVenc();
            numparc= financeiro.getNumparcela();
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.add(Calendar.MONTH, 1);

            int codfinan = financeiro.getCodFin();
            numparc++;
            codfinan++;
            financeiro.setCodFin(codfinan);
            financeiro.setDataVenc(cal.getTime());


        }while (numparc <=financeiro.getTotalparcelas());

    }



    public void ConexaoBanco(){
        //Abre conex�o com Danco de Dados
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioFinanceiro(conn);

        }catch(SQLException ex){
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK",null);
            msg.show();
        }
    }


    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

    private void PreencheCampos(){
        if (financeiro.getCodFin() != 0) {
            edtCodFinan.setText(String.valueOf(financeiro.getCodFin()));
            edtDescFinan.setText(financeiro.getDescricao().toString());
            edtParcela.setText(String.valueOf(financeiro.getTotalparcelas()));
            edtNotaFinan.setText(String.valueOf(financeiro.getNumnota()));
            edtValorFinan.setText(String.valueOf(financeiro.getValor()));

            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(financeiro.getDataVenc());
            edtDataVencFinan.setText(dt);
            lblParcAtual.setText((String.valueOf(financeiro.getNumparcela())));

            edtCliFinan.setText(String.valueOf(financeiro.getClienteFornecedor().getCodCliFor()));
            lblNomeCliFinan.setText(financeiro.getClienteFornecedor().getNomeCliFor());

        }
    }

    private void Limpar(){
        edtCodFinan.setText("");
        edtDescFinan.setText("");
        edtParcela.setText("");
        edtNotaFinan.setText("");
        edtDataVencFinan.setText("");
        edtValorFinan.setText("");
        lblParcAtual.setText("");
        lblNomeCliFinan.setText("");
        edtCliFinan.setText("");
        clifor = new ClienteFornecedor();

        if (bundle.containsKey("receb")) {
            financeiro.setTipo("receb");
            clifor.setTipo("cli");
        }else {
            clifor.setTipo("for");
            financeiro.setTipo("pagar");
        }
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

            edtDataVencFinan.setText(dt);
            financeiro.setDataVenc(data);
        }
    }


    private class NomeCliente implements View.OnClickListener, View.OnFocusChangeListener
    {

        @Override
        public void onClick(View v) {
            if (edtCliFinan.hasFocus() == false){
                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                int cod = Integer.parseInt(edtCliFinan.getText().toString());
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn,cod,tipoCliFor);
                lblNomeCliFinan.setText(cliente.getNomeCliFor());
            }

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (edtCliFinan.hasFocus() == false){
                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                int cod = Integer.parseInt(edtCliFinan.getText().toString());
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn,cod, tipoCliFor);
                lblNomeCliFinan.setText(cliente.getNomeCliFor());
            }
        }
    }





}
