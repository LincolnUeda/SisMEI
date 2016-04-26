package com.lincolnueda.sismei.Relatorios;

import android.app.AlertDialog;
import android.graphics.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfBody;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lincolnueda.sismei.Cadastros.actCadOrcamento;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Orcamento;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

/**
 * Created by Lincoln Ueda on 15/02/2016.
 */
public class PdfOrcamento {
    File myFile;
    PdfPCell cell;
    PdfPTable table = new PdfPTable(3);


    public File CreatePDF(Orcamento orcamento) throws IOException, DocumentException {

        File pdfFolder = Utilidades.CreateDir("Orcamentos");
        myFile = new File(pdfFolder + "/Orcamento No " + orcamento.getCodorc() + ".pdf"); //cria o arquivo no diret�rio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado( Existem outros tamanhos)

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi��o

        Font f = new Font(Font.FontFamily.TIMES_ROMAN,20.0f,Font.BOLD, BaseColor.BLACK);

        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,15.0f,Font.NORMAL, BaseColor.BLACK);

        //cabe�alho do or�amento
        Paragraph p = new Paragraph("Or\u00e7amento No " + String.valueOf(orcamento.getCodorc()),f);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(new Paragraph(p));

        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        document.add(new Paragraph(" "));

        p = new Paragraph("Cliente: " + orcamento.getCliente().getNomeCliFor(),f2);
        document.add(p);

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(orcamento.getDataorc());
        document.add(new Paragraph("Data: " + dt,f2));
        p = new Paragraph("Valor do Or\u00e7amento: R$ " + String.format("%.2f",orcamento.getPrecofinal()),f2);
        document.add(p);


        Font f3 = new Font(Font.FontFamily.TIMES_ROMAN,15.0f,Font.BOLD, BaseColor.BLACK);
        p = new Paragraph("\nObserva\u00e7\u00e3o: Este or\u00e7amento \u00e9 valido por 30 dias a partir da data de emiss\u00e3o.",f3);
        document.add(p);

        //materiais do or�amento

        p = new Paragraph("\n\n");
        document.add(p);

        table.setWidths(new int[]{8,40,20});

        p = new Paragraph("Item", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("Produto", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("Quant.", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        int max = orcamento.getListaMateriais().size();
        int i = 0;
        do{
            Material material = orcamento.getListaMateriais().get(i);
            String report = String.valueOf((i+ 1));
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            report = String.valueOf(material.getNomemat());
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            report = String.valueOf(material.getQuant() + " " + material.getUnidmed());
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            //String report = material.toString();
            //document.add(new Paragraph(report));
            i ++;
        }while ( i < max);


        document.add(table);
        document.close();

        return myFile;
    }


}
