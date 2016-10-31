package com.lincolnueda.sismei;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioFinanceiro;
import com.lincolnueda.sismei.Dominio.RepositorioPedido;

import java.io.File;

/**
 * Created by Lincoln Ueda on 28/12/2015.
 */
public class Utilidades {


    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static  String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }


    public static int AutoCodigo(String table, String column,SQLiteDatabase conn){
        //Busca  o ultimo codigo da tabela e retorna o numero seguinte
        int cod = 0;
        try {
            Cursor codigo = conn.query(table, new String[]{column}, null, null, null, null, column + " DESC");
            codigo.moveToFirst();
            cod = codigo.getInt(0);
            cod += 1;
        }catch(Exception e){

        }
        if(cod == 0)
                cod = 1;
        return cod;
    }

    public static int AutoCodigo(String table, String column,SQLiteDatabase conn,String where,String whereArg[]){
        /*Busca  o ultimo codigo da tabela e retorna o numero seguinte,
        de acordo com a condição recebida
        Parâmetros:
        where: colunas que fazem parte da condição
        where Arg: vetor com valores que as colunas devem ter para satisfazer a condição

        */
        int cod = 0;
        try {
            Cursor codigo = conn.query(table, new String[]{column}, where + "= ?",whereArg, null, null, column + " DESC");
            codigo.moveToFirst();
            cod = codigo.getInt(0);
            cod += 1;
        }catch(Exception e){

        }
        if(cod == 0)
            cod = 1;
        return cod;
    }

    public static  File CreateDir(String pasta) {
        //criar diretorio para salvar arquivos
        String teste = "";
        File sismei = new File(Environment.getExternalStorageDirectory(), "/SisMEI/");
        if (!sismei.exists())
            sismei.mkdir();

        File pdfFolder = new File(Environment.getExternalStorageDirectory(), "/SisMEI/" + pasta);
        if (!pdfFolder.exists())
            pdfFolder.mkdir();
        return pdfFolder;
    }
}
