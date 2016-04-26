package com.lincolnueda.sismei.Relatorios;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lincolnueda.sismei.DataBase.DataBase;
import com.lincolnueda.sismei.Dominio.RepositorioClienteFornecedor;
import com.lincolnueda.sismei.Entidades.ClienteFornecedor;
import com.lincolnueda.sismei.Entidades.Financeiro;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lincoln Ueda on 23/02/2016.
 */
public class PdfFinanceiro {
    File myFile;
    PdfPCell cell;
    PdfPTable table = new PdfPTable(6);
    private SQLiteDatabase conn;
    private DataBase database;


    public File CreatePDF(Date dataini, Date datafin, String tipo) throws IOException, DocumentException {
        File pdfFolder = null;
        try {
            pdfFolder = Utilidades.CreateDir("Relatorios");
        }catch(Exception e){
            e.printStackTrace();
        }
        myFile = new File(pdfFolder + "/Relatorio Financeiro - " + tipo + ".pdf"); //cria o arquivo no diret?rio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado( Existem outros tamanhos)

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi??o

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);

        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.NORMAL, BaseColor.BLACK);

        //cabe?alho do pedido
        Paragraph p = new Paragraph("Relatorio Financeiro -  " + tipo, f);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(new Paragraph(p));

        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        document.add(new Paragraph(" "));

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);

        String dt = format.format(new Date());

        document.add(new Paragraph("Emiss\u00e3o: " + dt, f2));
        dt = format.format(dataini);
        String dt2 = format.format(datafin);
        document.add(new Paragraph("Per\u00edodo: " + dt + " a " + dt2, f2));


        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.BOLD, BaseColor.BLACK);

        // cabecalho da tabela


        table.setWidths(new int[]{8,40,10,10,8,8});

        p = new Paragraph("Item", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("Descri\u00e7\u00e3o", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);


        p = new Paragraph("Valor", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);


        p = new Paragraph("Venc.", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("Num. Nota", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("Parcela", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);


        Cursor cursor = conn.query("Financeiro",null,"datavenc between ? and ? and tipo = ?",new String[]{String.valueOf(dataini.getTime()), String.valueOf(datafin.getTime()),tipo},null,null,null);
        cursor.moveToFirst();

        int i = 0;
        if (cursor.getCount() > 0) {
            do {

                String report = String.valueOf((i+ 1)); //num item
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);

                report = String.valueOf(cursor.getString(1)); //descricao
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);


                report = String.valueOf(cursor.getLong(3)); // valor
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);


                report = String.valueOf(cursor.getLong(7)); // data de vencimento
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);

                report = String.valueOf(cursor.getInt(2)); //num nota
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);

                report = String.valueOf(cursor.getInt(4) + "/" + cursor.getInt(5)); //num parcela
                cell = new PdfPCell(Phrase.getInstance(report));
                table.addCell(cell);

                i++;

            } while (cursor.moveToNext());
        }

        document.close();

        return myFile;
    }
}
