package com.lincolnueda.sismei.Cadastros;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TabActivity;
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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.DocumentException;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Dominio.RepositorioEtapaProducao;
import com.lincolnueda.sismei.Dominio.RepositorioFinanceiro;
import com.lincolnueda.sismei.Dominio.RepositorioMaterial;
import com.lincolnueda.sismei.Dominio.RepositorioPedido;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Financeiro;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.Relatorios.PdfListaMateriaisPedido;
import com.lincolnueda.sismei.Relatorios.PdfPedido;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class actCadPedido extends TabActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText edtCodPed;
    private EditText edtDataPed;
    private EditText edtDataPedEnt;
    private EditText edtCodClientePed;
    private EditText edtEtapa;
    private TextView lblTotPedido;
    private Button btnSalvarPed;
    private Button btnAdicProd;
    private Button btnExcluirPed;
    private ImageButton btnPdfPed;
    private ListView lstProdutoPed;
    private ArrayAdapter<Produto> adpProdutoPed;
    private ArrayList<Produto> listaProduto = new ArrayList<Produto>();
    private TextView lblNomeClientePed;
    private Spinner spnStatus;
    private ArrayAdapter<String> adpStatus;

    //aba Financeiro
    private EditText edtNumNota;
    private EditText edtNumParcPed;
    private EditText edtDataVencPed;

    private TabHost tabHost;
    double valortotal = 0;

    private DataBase database;
    private SQLiteDatabase conn;
    private Bundle bundle;
    private Pedido pedido;
    private Financeiro financeiro = new Financeiro();
    private RepositorioPedido repositorio;
    private RepositorioFinanceiro repFinan;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cad_pedido);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);

        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("tab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Pedido");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Financeiro");
        tabHost.addTab(spec);

        btnSalvarPed = (Button) findViewById(R.id.btnSalvarPed);
        btnAdicProd = (Button) findViewById(R.id.btnAdicProd);
        btnExcluirPed = (Button) findViewById(R.id.btnExcluirPed);
        btnPdfPed = (ImageButton) findViewById(R.id.btnPdfPed);

        //try {
            edtCodPed = (EditText) findViewById(R.id.edtCodPed);
       // } catch (Exception e) {
           // Mensagem(e.getMessage());
       // }
        edtCodClientePed = (EditText) findViewById(R.id.edtCodClientePed);
        edtDataPed = (EditText) findViewById(R.id.edtDataPed);
        edtDataPedEnt = (EditText) findViewById(R.id.edtDataPedEnt);
        edtEtapa = (EditText) findViewById(R.id.edtEtapaPed);
        lstProdutoPed = (ListView) findViewById(R.id.lstProdutoPed);
        lblNomeClientePed = (TextView) findViewById(R.id.lblNomeClientePed);
        lblTotPedido = (TextView) findViewById(R.id.lblTotPedido);
        spnStatus = (Spinner) findViewById(R.id.spnStatusPed);

        //aba financeiro
        edtNumNota = (EditText) findViewById(R.id.edtNumNFPed);
        edtDataVencPed = (EditText) findViewById(R.id.edtDataVencPed);
        edtNumParcPed = (EditText) findViewById(R.id.edtNumParcPed);

        adpStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adpStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adpStatus);

        adpStatus.add("Aberto");
        adpStatus.add("Encerrado");


        adpProdutoPed = new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1);
        lstProdutoPed.setAdapter(adpProdutoPed);
        lstProdutoPed.setOnItemClickListener(this);

        btnSalvarPed.setOnClickListener(this);
        btnAdicProd.setOnClickListener(this);
        btnExcluirPed.setOnClickListener(this);
        btnPdfPed.setOnClickListener(this);

        ExibeDataListener dataListener = new ExibeDataListener();
        edtDataPed.setOnClickListener(dataListener); // configura DatePicker para o campo data
        edtDataPed.setOnFocusChangeListener(dataListener); // configura DatePicker para o campo data

        edtDataPedEnt.setOnClickListener(dataListener); // configura DatePicker para o campo data
        edtDataPedEnt.setOnFocusChangeListener(dataListener); // configura DatePicker para o campo data

        edtDataVencPed.setOnClickListener(dataListener); // configura DatePicker para o campo data
        edtDataVencPed.setOnFocusChangeListener(dataListener); // configura DatePicker para o campo data


        NomeCliente nome = new NomeCliente();
        edtCodClientePed.setOnFocusChangeListener(nome);


        btnPdfPed.setVisibility(View.INVISIBLE);
        bundle = getIntent().getExtras();//recebe parametro
        if (bundle != null) {
            if (bundle.containsKey("pedido")) {
                pedido = (Pedido) bundle.getSerializable("pedido");
                if (pedido.getCodPed() != 0)
                    btnPdfPed.setVisibility(View.VISIBLE);
                else {
                    ConexaoBanco();
                    edtCodPed.setText(String.valueOf(Utilidades.AutoCodigo("Pedido", "_id", conn)));
                }
                ConexaoBanco();
                PreencheCampos();
            }
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {

        if (v == btnSalvarPed) {
            Salvar();
        }

        if (v == btnExcluirPed) {
            Excluir();
        }
        if (v == btnAdicProd) {
            AdicProduto();
        }

        if (v == btnPdfPed) {
            File myFile = null;
            PdfPedido pdf = new PdfPedido();
            try {
                myFile = pdf.CreatePDF(pedido);
            } catch (FileNotFoundException e) {
                Mensagem(e.getMessage());
            } catch (DocumentException e) {
                Mensagem(e.getMessage());
            } catch (Exception e) {
                Mensagem(e.getMessage());
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        double valortotal = 0;

        if (requestCode == 1) {

            if (resultCode == 1) {
                Produto produto;
                adpProdutoPed.clear();
                try {
                    pedido = (Pedido) data.getSerializableExtra("ProdutoPed");
                    int max = pedido.getListaProdutos().size();
                    int i;

                    i = 0;
                    do {
                        if (max != 0) {
                            produto = pedido.getListaProdutos().get(i);
                            adpProdutoPed.add(produto);
                            valortotal += (produto.getPrecoProd() * produto.getQuant());
                        }
                        i++;
                    } while (i < max);
                    lblTotPedido.setText("Valor Total: " + String.valueOf(valortotal));


                } catch (Exception e) {
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
        Produto produto;
        produto = adpProdutoPed.getItem(position);
        Intent intent = new Intent(this, actAdicProd.class);
        intent.putExtra("produto", produto);
        intent.putExtra("position", position);
        intent.putExtra("pedido", pedido);
        startActivityForResult(intent, 1);

    }


    public void ConexaoBanco() {
        try {
            database = new DataBase(this);
            conn = database.getWritableDatabase();
            repositorio = new RepositorioPedido(conn);
            repFinan = new RepositorioFinanceiro(conn);
        } catch (SQLException ex) {
            AlertDialog.Builder msg = new AlertDialog.Builder(this);
            msg.setMessage("Não foi possivel conectar com o banco. Erro: " + ex.getMessage());
            msg.setNeutralButton("OK", null);
            msg.show();
        }
    }


    private void PreencheCampos() {

        if (pedido.getCodPed() != 0) {
            edtCodPed.setText(String.valueOf(pedido.getCodPed()));
            edtCodClientePed.setText(String.valueOf(pedido.getCliente().getCodCliFor()));
            edtEtapa.setText(String.valueOf(pedido.getEtapaProducao().getCodEtapa()));
            edtNumNota.setText(String.valueOf(pedido.getNumNota()));

            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(pedido.getDataped());
            edtDataPed.setText(dt);

            dt = format.format(pedido.getDataentrega());
            edtDataPedEnt.setText(dt);

            lblNomeClientePed.setText(pedido.getCliente().getNomeCliFor());

            financeiro = repFinan.EncontraFinanceiroPedido(conn, pedido.getCodPed());
            edtNumParcPed.setText(String.valueOf(financeiro.getTotalparcelas()));

            dt = format.format(financeiro.getDataVenc());
            edtDataVencPed.setText(dt);


            //preenche lista de itens do orçamento
            // orcamento.setListaMateriais();


            if (pedido.getListaProdutos().size() > 0) {
                int i = 0;
                int max = pedido.getListaProdutos().size();
                Produto produto = new Produto();
                do {
                    produto = pedido.getListaProdutos().get(i);

                    adpProdutoPed.add(produto);
                    valortotal += (produto.getPrecoProd() * produto.getQuant());
                    i++;
                } while (i < max);
                lblTotPedido.setText("Valor Total: " + String.format("%.2f", valortotal));
            }

            String cat = pedido.getStatus();
            // dt = format.format(financeiro.getDataVenc());
            // edtDataPedEnt.setText(dt);

            if (cat.equals("Aberto"))
                spnStatus.setSelection(0);
            else
                spnStatus.setSelection(1);


        }
    }


    private void Salvar() {
        int teste = 0;
        if (edtDataVencPed.getText().toString().equals("") && edtNumParcPed.getText().toString().equals("")) {
            Mensagem("Aten\u00e7\u00e3o\n Campos Data de Vencimento e N\u00famero de Parcelas s\u00e3o orbrigat\u00f3rios!");
        } else {
            try {

                pedido.setCodPed(Integer.parseInt(edtCodPed.getText().toString()));
                String nota = edtNumNota.getText().toString();
                if (nota.isEmpty()) //caso nao tenha sido digitado o codigo da nota,
                    nota = "0";    // ele sera gravado como zero
                pedido.setNumNota(Integer.parseInt(nota));
                pedido.setStatus(spnStatus.getSelectedItem().toString());

                //configura etapa de produção do pedido
                RepositorioEtapaProducao repEtapa = new RepositorioEtapaProducao(conn);
                int codetapa = Integer.parseInt(edtEtapa.getText().toString());
                pedido.setEtapaProducao(repEtapa.EncontraEtapa(conn, codetapa));

                //configura cliente do pedido
                RepositorioClienteFornecedor repCliente = new RepositorioClienteFornecedor(conn);
                int codcli = Integer.parseInt(edtCodClientePed.getText().toString());
                pedido.setCliente(repCliente.EncontraClienteFornecedor(conn, codcli, "cli"));

            } catch (Exception ex) {

                Mensagem("Erro " + ex.getMessage());
            }
            //verifica se o código do pedido já existe
            int cod = repositorio.BuscaCodigo(this, pedido);
            if (cod == 0) {
                try {
                    if (pedido.getListaProdutos().size() > 0) {
                        repositorio.Inserir(pedido);
                        SalvarFinanceiro(); //cadastra no sistema recebimento referente ao pedido
                        RetiraEstoque(); // retira do estoque de materiais a quantidade usada o pedido
                        Mensagem("Pedido Cadastrado.");
                        Limpar();
                    } else {
                        Mensagem("Atenção: \n Não é possivel cadastrar um pedido sem produtos.");
                    }
                } catch (Exception Ex) {
                    Mensagem("Erro: " + Ex.getMessage());
                }

            } else {
                if (pedido.getListaProdutos().size() > 0) {
                    repositorio.Alterar(pedido);
                    SalvarFinanceiro();
                    Mensagem("Pedido Atualizado.");
                } else {
                    Mensagem("Atenção: \n Não é possivel cadastrar um pedido sem produtos.");
                }
            }

        }
    }

    private void SalvarFinanceiro() {
        financeiro.setTotalparcelas(Integer.parseInt(edtNumParcPed.getText().toString()));
        financeiro.setNumnota(pedido.getNumNota());
        int max = pedido.getListaProdutos().size();
        int i = 0;
        valortotal = 0;
        do {
            Produto produto = pedido.getListaProdutos().get(i);
            valortotal += (produto.getPrecoProd() * produto.getQuant());
            i++;
        } while (i < max);
        financeiro.setValor(valortotal);

        int cod = repFinan.BuscaCodigoPedido(this, pedido.getCodPed(), "receb");
        if (cod == 0)
            financeiro.setCodFin(repFinan.BuscaUltimocodigo(this) + 1);
        else
            financeiro.setCodFin(cod);

        financeiro.setClienteFornecedor(pedido.getCliente());
        financeiro.setTipo("receb");
        financeiro.setCodpedido(pedido.getCodPed());

        try {

            /*metodo para verificar no banco o codigo digitado pelo usuario,
            para decidir se será feito insert ou update.*/
            int codped = repFinan.BuscaCodigoPedido(this, pedido.getCodPed(), "receb");
            if (codped == 0) {
                cod = repFinan.BuscaCodigo(this, financeiro);
                if (cod == 0) {

                    //caso a conta tenha mais de 1 parcela, o sistema irá cadastrá-las automaticamente.
                    if (financeiro.getTotalparcelas() > 1) {
                        CadastrarParcelas();
                    } else {
                        financeiro.setDescricao("Pedido nº " + pedido.getCodPed());
                        int numparc = 1;
                        financeiro.setNumparcela(numparc);
                        repFinan.Inserir(financeiro);
                    }

                }
            } else {
                repFinan.AlterarWhere(financeiro, String.valueOf(pedido.getCodPed()));

            }
            // Limpar();
        } catch (Exception ex) {
            Mensagem("Erro: " + ex.getMessage());
        }

    }

    private void RetiraEstoque() {
        final Pedido pedido2 = pedido;
        //metodo retira do estoque a quantidade de cada material que será usada para produzir o pedido

        //Mensagem que sera exibida caso algum material seja insuficiente
        String msg = "Atenção: \n Materiais no estoque são insuficientes para este pedido.";
        msg += "\n Foi gerado um arquivo PDF com os materiais e a quantidade necessária.\n Pressione OK para visualisar";

        final ArrayList<String> listaMaterial = new ArrayList<>();
        int flagmsg = 0;

        int maxped = pedido.getListaProdutos().size();
        int i = 0;
        do { //percorre lista d produtos
            Produto prod = pedido.getListaProdutos().get(i);
            int maxprod = prod.getListaMateriais().size();
            int j = 0;
            int cont = 0;
            do {//percorre lista de materiais do produto

                Material mat = prod.getListaMateriais().get(j);
                double quantusada = mat.getQuant() * prod.getQuant();

                RepositorioMaterial repMat = new RepositorioMaterial(conn);
                Material mat2 = repMat.EncontraMaterial(conn, mat.getCodmat()); //material cadastrado no sistema,
                // com quantidade do estoque
                if (mat2.getQuant() > quantusada) {
                    mat2.setQuant(mat2.getQuant() - quantusada);//retira a quantidade usada no pedido;
                } else {
                    double diferenca = (quantusada - mat2.getQuant());
                    Material mat3 = new Material();
                    mat3 = mat2;
                    mat3.setQuant(diferenca);

                    listaMaterial.add(mat3.getCodmat() + "."
                            + Utilidades.padRight(mat3.getNomemat(), 20) + " "
                            + Utilidades.padRight(String.valueOf(mat3.getQuant()), 4)
                            + " " + mat3.getUnidmed());

                    mat2.setQuant(0);
                    flagmsg = 1;
                }

                repMat.Alterar(mat2);
                j++;

            } while (j < maxprod);
            i++;

        } while (i < maxped);

        if (flagmsg == 1) {
            AlertDialog.Builder msgSemMat = new AlertDialog.Builder(this);
            msgSemMat.setMessage(msg);

            //botao OK ira gerar um arquivo PDF com os materiais insuficientes e a quantidade necessaria
            msgSemMat.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //abrir pdf
                    File myFile = null;
                    PdfListaMateriaisPedido pdf = new PdfListaMateriaisPedido();
                    try {
                        myFile = pdf.CreatePDF(listaMaterial, pedido2);
                    } catch (FileNotFoundException e) {
                        Mensagem(e.getMessage());
                    } catch (DocumentException e) {
                        Mensagem(e.getMessage());
                    } catch (Exception e) {
                        Mensagem(e.getMessage());
                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);


                }
            });
            msgSemMat.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            msgSemMat.show();
        }

    }

    private void CadastrarParcelas() {
        //metodo ira cadasttrar as parcelas do pagamento do pedido
        int numparc = 1;
        double valortotal = financeiro.getValor();
        do {

            financeiro.setValor(valortotal / financeiro.getTotalparcelas());
            financeiro.setNumparcela(numparc);
            financeiro.setDescricao("Pedido nº " + pedido.getCodPed() + "(Parcela "
                    + financeiro.getNumparcela() + "/" + financeiro.getTotalparcelas() + ")");

            repFinan.Inserir(financeiro);

            Date data = financeiro.getDataVenc();
            numparc = financeiro.getNumparcela();
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.add(Calendar.MONTH, 1);

            int codfinan = financeiro.getCodFin();
            numparc++;
            codfinan++;
            financeiro.setCodFin(codfinan);
            financeiro.setDataVenc(cal.getTime());


        } while (numparc <= financeiro.getTotalparcelas());

    }


    private void Excluir() {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage("Deseja excluir este pedido?");
        msg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                repositorio.Excluir(pedido);
                financeiro.setCodpedido(pedido.getCodPed());
                repFinan.ExcluirWhere(financeiro);
                Mensagem("Pedido Excluido.");
                //Limpar();
                pedido = new Pedido();
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

    private void AdicProduto() {
        Produto produto = new Produto();
        produto.setCodProd(0);
        Intent intent = new Intent(this, actAdicProd.class);
        intent.putExtra("produto", produto);
        intent.putExtra("pedido", pedido);
        try {
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Mensagem("Erro: " + e.getMessage());
        }

    }


    private void Mensagem(String mensagem) {
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }

    private void Limpar() {
        try {
            pedido = new Pedido();
            edtCodPed.requestFocus();
            edtCodPed.setText("");
            edtCodClientePed.setText("");
            edtDataPed.setText("");
            edtDataPedEnt.setText("");
            edtNumNota.setText("");
            edtEtapa.setText("");
            adpProdutoPed.clear();
            lblTotPedido.setText("Valor Total: ");
            lblNomeClientePed.setText("");
            spnStatus.setSelection(0);


            int teste = 1;
        } catch (Exception e) {
            Mensagem("Erro: " + e.getMessage());
        }
    }


    private void exibeData(View v) {
        //exibe um datepicker quando clicar no campo de data
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlgData = new DatePickerDialog(this, new SelecionaDataListener(v), ano, mes, dia);
        dlgData.show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("actCadPedido Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        //classe interna, para n�o ter que utilizar o metodo da classe TelaDespesas
        @Override
        public void onClick(View v) {
            exibeData(v);

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                exibeData(v);
            }
        }
    }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {
        // classe para receber a data selecionada no datepicker
        public SelecionaDataListener(View v) {
            this.v = v;
        }

        private View v;

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);

            Date data = calendar.getTime();
            DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dt = format.format(data);

            if (v == edtDataPed) {
                edtDataPed.setText(dt);
                pedido.setDataped(data);
            }
            if (v == edtDataPedEnt) {
                edtDataPedEnt.setText(dt);
                pedido.setDataentrega(data);
            }

            if (v == edtDataVencPed) {
                edtDataVencPed.setText(dt);
                financeiro.setDataVenc(data);
            }

        }
    }


    private class NomeCliente implements View.OnClickListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {
            if (edtCodClientePed.hasFocus() == false) {
                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                int cod = Integer.parseInt(edtCodClientePed.getText().toString());
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cod, "cli");
                lblNomeClientePed.setText(cliente.getNomeCliFor());
            }

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (edtCodClientePed.hasFocus() == false) {
                RepositorioClienteFornecedor repCli = new RepositorioClienteFornecedor(conn);
                int cod = Integer.parseInt(edtCodClientePed.getText().toString());
                ClienteFornecedor cliente = repCli.EncontraClienteFornecedor(conn, cod, "cli");
                lblNomeClientePed.setText(cliente.getNomeCliFor());
            }
        }
    }

}
