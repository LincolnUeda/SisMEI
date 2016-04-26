package com.lincolnueda.sismei.Relatorios;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lincolnueda.sismei.Entidades.Material;
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Lincoln Ueda on 19/02/2016.
 */
public class PdfListaMateriaisPedido {
    File myFile;
    PdfPCell cell;
    PdfPTable table = new PdfPTable(3);

    public File CreatePDF(ArrayList<String> listaMaterial,Pedido pedido) throws IOException, DocumentException {

        File pdfFolder = Utilidades.CreateDir("Pedidos");
        myFile = new File(pdfFolder + "/ListaMateriais Pedido No " + pedido.getCodPed() + ".pdf"); //cria o arquivo no diret?rio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado( Existem outros tamanhos)

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi??o

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);

        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.NORMAL, BaseColor.BLACK);

        //cabe?alho do pedido
        Paragraph p = new Paragraph("Lista de Materias sem Estoque - Pedido No " + String.valueOf(pedido.getCodPed()), f);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(new Paragraph(p));

        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        document.add(new Paragraph(" "));

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(pedido.getDataped());
        document.add(new Paragraph("Emiss\u00e3o: " + dt,f2));


        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.BOLD, BaseColor.BLACK);




        //lista materiais
        int max = listaMaterial.size();
        int i = 0;
        do {
           // Material material = listaMaterial.get(i);
            String report =  listaMaterial.get(i);  //(i + 1) + Utilidades.padRight(material.getNomemat(), 20) + " " + Utilidades.padRight(String.valueOf(material.getQuant()), 4);
            document.add(new Paragraph(report));
            i++;
        } while (i < max);

        document.close();

        return myFile;
    }
}
