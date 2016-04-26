package com.lincolnueda.sismei.Relatorios;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by Lincoln Ueda on 13/02/2016.
 */
public class PDFTeste {


    File myFile;

    public File CreatePDF()throws FileNotFoundException,DocumentException {

        File pdfFolder = Utilidades.CreateDir("PastaTeste");
        myFile = new File(pdfFolder + "/TestePDF" + ".pdf"); //cria o arquivo no diret�rio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado( Existem outros tamanhos)

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi��o


        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("Teste de Pdf"));
        cell.setColspan(3);

        table.addCell(cell);

        int i = 1;
        do{
            cell = new PdfPCell(new Phrase("coluna " + i));
            cell.setColspan(1);
            table.addCell(cell);
            i++;
    }while (i <=12);

        document.add(table);


        /*document.add(new Paragraph("Teste de arquivos PDF"));
        document.add(new Paragraph("Linha 1"));
        document.add(new Paragraph("Linha 2"));
        document.add(new Paragraph("Linha 3"));
        document.add(new Paragraph("Linha 4"));*/

        document.close();

        return myFile;
    }


    /*private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }*/


}


