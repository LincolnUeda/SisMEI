package com.lincolnueda.sismei;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.lincolnueda.sismei.DataBase.CopyDB;


public class actBandoDados extends actBase implements View.OnClickListener {
    private ImageButton btnImportar;
    private ImageButton btnExportar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_banco_dados);

        btnImportar = (ImageButton) findViewById(R.id.btnImportar);
        btnExportar = (ImageButton) findViewById(R.id.btnExportar);

        btnImportar.setOnClickListener(this);
        btnExportar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == btnExportar){
            try {
                CopyDB copia = new CopyDB();
                copia.exportDB();
                Mensagem("Banco de Dados Exportado.");
            }catch (Exception e){
                Mensagem(e.getMessage());
            }
        }
        if (v == btnImportar){
            try {
                CopyDB copia = new CopyDB();
                copia.importDB();
                Mensagem("Banco de Dados Importdado.");
            }catch (Exception e){
                Mensagem(e.getMessage());
            }
        }
    }


    private void Mensagem(String mensagem){
        AlertDialog.Builder msg = new AlertDialog.Builder(this);

        msg.setMessage(mensagem);
        msg.setNeutralButton("OK", null);
        msg.show();
    }



}
