package com.lincolnueda.sismei.Relatorios;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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
import com.lincolnueda.sismei.Dominio.RepositorioEmpresa;
import com.lincolnueda.sismei.Dominio.RepositorioFinanceiro;
import com.lincolnueda.sismei.Entidades.Empresa;
import com.lincolnueda.sismei.Utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Lincoln Ueda on 23/02/2016.
 */
public class PdfFinanceiroBruto{
    File myFile;
    PdfPCell cell;
    PdfPTable table = new PdfPTable(2);
    private SQLiteDatabase conn;
    private DataBase database;
    private Empresa empresa;


    public File CreatePDF(Date dataini, Date datafin, String tipo,Context context) throws IOException, DocumentException {

        File pdfFolder = Utilidades.CreateDir("Relatorios");
        myFile = new File(pdfFolder + "/Relatorio Financeiro - " + tipo + ".pdf"); //cria o arquivo no diret?rio
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A4); // PageSize.A4 cria o documento no tamanho especificado( Existem outros tamanhos)

        PdfWriter.getInstance(document, output);
        document.open();//abre o arquivo para edi??o

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);

        Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 15.0f, Font.NORMAL, BaseColor.BLACK);

        //cabe?alho do pedido
        Paragraph p = null;

        int mesini = dataini.getMonth();
        int mesfin = datafin.getMonth();

        if (tipo.equals("Faturamento")) {
            if(mesini == mesfin)
                p = new Paragraph("RELAT\u00d3RIO MENSAL DAS RECEITAS BRUTAS", f);
            else
                p = new Paragraph("RELAT\u00d3RIO DE RECEITAS BRUTAS", f);

        }else{
            if (mesini == mesfin)
                p = new Paragraph("RELAT\u00d3RIO MENSAL DE PAGAMENTO", f);
            else
                p = new Paragraph("RELAT\u00d3RIO DE PAGAMENTO", f);
        }
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(new Paragraph(p));

        p = new Paragraph("______________________________________________________________________________");
        document.add(p);
        document.add(new Paragraph(" "));

        try {
            database = new DataBase(context);
            conn = database.getWritableDatabase();
        }catch(SQLException ex){

        }

        BuscaEmrpesa();

        p = new Paragraph("CNPJ: " + empresa.getCnpjEmp());
        document.add(p);

        p = new Paragraph("Empreendedor Individual: " + empresa.getNomeEmp());
        document.add(p);


        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);

        String dt = format.format(dataini);
        String dt2 = format.format(datafin);
        document.add(new Paragraph("Per\u00edodo de apura��o: " + dt + " a " + dt2, f2));


        p = new Paragraph("______________________________________________________________________________\n\n");
        document.add(p);


        String tipofin = "";
        if (tipo.equals("Faturamento"))
            tipofin = "receb";
        else
            tipofin = "pagar";


        Cursor cursor = conn.query("Financeiro",null,"datavenc between ? and ? and tipo = ?",new String[]{String.valueOf(dataini.getTime()), String.valueOf(datafin.getTime()),tipofin},null,null,null);
        cursor.moveToFirst();

        table.setTotalWidth(600);
        table.setWidths(new int[]{200,100});

        int i = 0;
        double valorCnota = 0;
        double valorSNota = 0;
        if (cursor.getCount() > 0) {
            do {

                if (cursor.getInt(2) != 0 ) {
                    valorCnota += cursor.getDouble(3); // valor
                }else{
                    valorSNota += cursor.getDouble(3);
                }


            } while (cursor.moveToNext());

        }
        p = new Paragraph("I � Venda de produtos industrializados com dispensa de emiss�o de documento fiscal", f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph(String.format("%.2f",valorSNota), f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("II � Venda de produtos industrializados com documento fiscal emitido", f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph(String.format("%.2f",valorCnota), f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);


        p = new Paragraph("III � Total das receitas com venda de produtos industrializados (I + II)", f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph(String.format("%.2f",valorCnota + valorSNota), f2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);


        format = DateFormat.getDateInstance(DateFormat.LONG);
        dt = format.format(new Date());

        Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 10.0f, Font.NORMAL, BaseColor.BLACK);
        p = new Paragraph("LOCAL E DATA: \nFranca, " + dt, f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);

        table.addCell(cell);

        p = new Paragraph("ASSINATURA DO EMPRES\u00c1RIO\n      \n   \n   \n  \n     \n", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        table.addCell(cell);

        p = new Paragraph("ENCONTRAM-SE ANEXADOS E ESTE RELAT\u00d3RIO:\n" +
                "- Os documentos fiscais comprobat\u00f3rios das entradas de mercadorias e servi\ufffdos tomados referentes ao per\ufffdodo;\n" +
                "- As notas fiscais relativas \u00e0s opera\ufffd\ufffdes ou presta\ufffd\ufffdes realizadas eventualmente emitidas.\n   \n", f3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        cell = new PdfPCell(p);
        cell.setColspan(2);
        table.addCell(cell);

        document.add(table);
        document.close();

        return myFile;
    }


    private void BuscaEmrpesa(){
        try {

            RepositorioEmpresa repositorio = new RepositorioEmpresa(conn);
            empresa = repositorio.BuscarEmpresa();

        }catch(SQLException ex){

        }
    }
}
