package com.lincolnueda.sismei;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.lincolnueda.sismei.Cadastros.actCadFinanceiro;
import com.lincolnueda.sismei.Cadastros.actCadPedido;


public class actTabCadPedido extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tab_cad_pedido);



        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Pedido");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("finaneiro");


        tab1.setIndicator("Pedido");
        tab1.setContent(new Intent(this, actCadPedido.class));

        tab2.setIndicator("Financeiro");
        tab2.setContent(new Intent(this, actCadFinanceiro.class));


        tabHost.addTab(tab1);
        tabHost.addTab(tab2);



    }


}
