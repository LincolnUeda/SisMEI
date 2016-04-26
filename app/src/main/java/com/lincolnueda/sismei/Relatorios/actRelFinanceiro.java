package com.lincolnueda.sismei.Relatorios;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.itextpdf.text.DocumentException;
import com.lincolnueda.sismei.Cadastros.actEditaUsuario;
import com.lincolnueda.sismei.R;
import com.lincolnueda.sismei.actBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class actRelFinanceiro extends actBase implements View.OnClickListener {

    private Button btnRelOK;
    private EditText edtDatIni;
    private EditText edtDatFin;
    private Spinner spnTipoRel;
    private ArrayAdapter<String> adpTipoRel;
    private  Date dataini = new Date();
    private Date datafin = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rel_financeiro);

        edtDatIni = (EditText) findViewById(R.id.edtRelDatIni);
        edtDatFin = (EditText) findViewById(R.id.edtRelDatFin);
        btnRelOK = (Button) findViewById(R.id.btnRelOK);
        spnTipoRel = (Spinner) findViewById(R.id.spnTipoRel);

        adpTipoRel = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
        adpTipoRel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoRel.setAdapter(adpTipoRel);

        adpTipoRel.add("Faturamento");
        adpTipoRel.add("Pagamento");

        ExibeDataListener dataListener = new ExibeDataListener();
        edtDatIni.setOnClickListener(dataListener);
        edtDatFin.setOnClickListener(dataListener);
        edtDatIni.setOnFocusChangeListener(dataListener);
        edtDatFin.setOnFocusChangeListener(dataListener);

        btnRelOK.setOnClickListener(this);



    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_usuario) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    private void exibeData(View v) {
        //exibe um datepicker quando clicar no campo de data
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlgData = new DatePickerDialog(this, new SelecionaDataListener(v), ano, mes, dia);
        dlgData.show();

    }

    @Override
    public void onClick(View v) {
        File myFile = null;
        PdfFinanceiroBruto pdf = new PdfFinanceiroBruto();
        try {

            myFile =  pdf.CreatePDF(dataini,datafin,spnTipoRel.getSelectedItem().toString(),this);
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

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {
        //classe interna, para n?o ter que utilizar o metodo da classe TelaDespesas
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

    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
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

            if (v == edtDatIni) {
                edtDatIni.setText(dt);
                dataini = data;
            }
            if (v == edtDatFin) {
                edtDatFin.setText(dt);
                datafin = data;
            }

        }


    }
}
