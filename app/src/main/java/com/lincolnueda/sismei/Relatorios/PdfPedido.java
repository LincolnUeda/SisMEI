package com.lincolnueda.sismei.Relatorios;

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
import com.lincolnueda.sismei.Entidades.Pedido;
import com.lincolnueda.sismei.Entidades.Produto;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;

/**
 * Created by Lincoln Ueda on 17/02/2016.
 */


public class PdfPedido {
    File myFile;
    PdfPCell cell;
    PdfPTable table = new PdfPTable(4);

    public File CreatePDF(Pedido pedido) throws IOException, DocumentException {

        File pdfFolder = Utilidades.CreateDir("Pedidos");
        myFile = new File(pdfFolder + "/Pedido No " + pedido.getCodPed() + ".pdf"); //cria o arquivo no diretorio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi??o

        Font f = new Font(Font.FontFamily.TIMES_ROMAN,20.0f,Font.BOLD, BaseColor.BLACK);

        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,15.0f,Font.NORMAL, BaseColor.BLACK);

        //cabe?alho do pedido
        Paragraph p = new Paragraph("Pedido No " + String.valueOf(pedido.getCodPed()),f);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(new Paragraph(p));

        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        document.add(new Paragraph(" "));

        p = new Paragraph("Cliente: " + pedido.getCliente().getNomeCliFor(),f2);
        document.add(p);

        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dt = format.format(pedido.getDataped());
        document.add(new Paragraph("Emiss\u00e3o: " + dt,f2));

        dt = format.format(pedido.getDataentrega());
        document.add(new Paragraph("Entrega: " + dt,f2));

        p = new Paragraph("\n\n");
        document.add(p);

        Font f3 = new Font(Font.FontFamily.TIMES_ROMAN,15.0f,Font.BOLD, BaseColor.BLACK);
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

        p = new Paragraph("Valor Unit.", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        //produtos do pedido
        int max = pedido.getListaProdutos().size();
        int i = 0;
        do{
            Produto produto = pedido.getListaProdutos().get(i);

            String report = String.valueOf((i+ 1));
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            report = String.valueOf(produto.getNomeProd());
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            report = String.valueOf(produto.getQuant());
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);

            report = String.valueOf(String.format("%.2f",(produto.getPrecoProd() * produto.getQuant())));
            cell = new PdfPCell(Phrase.getInstance(report));
            table.addCell(cell);



            i ++;
        }while ( i < max);

        i = 0;
        max = pedido.getListaProdutos().size();
        double valortotal = 0;
        Produto prod = new Produto();
        do{
            prod = pedido.getListaProdutos().get(i);
            valortotal += (prod.getPrecoProd() * prod.getQuant());
            i ++;
        }while (i < max);

        p = new Paragraph("Total",f2);
        cell = new PdfPCell(p);
        cell.setColspan(3);
        table.addCell(cell);
        String s = String.format("%.2f",valortotal);
        p = new Paragraph(s);
        cell = new PdfPCell(p);
        table.addCell(cell);
        // document.add(p);

        document.add(table);

        document.close();

        return myFile;
    }

    private void PreencheCellCab(Paragraph p){
        cell = new PdfPCell(p);
        cell.setColspan(4);
        table.addCell(cell);
    }

}
