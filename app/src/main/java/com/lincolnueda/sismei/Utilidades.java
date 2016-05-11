package com.lincolnueda.sismei;

import android.os.Environment;

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
