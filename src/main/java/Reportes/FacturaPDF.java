package Reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FacturaPDF {

    public static void generarFactura(
            int idVenta,
            String cliente,
            String dni,
            JTable tabla,
            double total) {

        try {

            JFileChooser chooser = new JFileChooser();

            chooser.setDialogTitle("Guardar factura");

            chooser.setSelectedFile(
                    new File("Factura_" + idVenta + ".pdf"));

            chooser.setFileFilter(
                    new FileNameExtensionFilter(
                            "Archivos PDF",
                            "pdf"));

            int opcion = chooser.showSaveDialog(null);

            if (opcion != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File archivo = chooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".pdf")) {
                archivo = new File(
                        archivo.getAbsolutePath() + ".pdf");
            }

            Document documento = new Document(
                    PageSize.A4,
                    40,
                    40,
                    40,
                    40);

            PdfWriter.getInstance(
                    documento,
                    new FileOutputStream(archivo));

            documento.open();

            Font titulo =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            22);

            Font subtitulo =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            16);

            Font texto =
                    FontFactory.getFont(
                            FontFactory.HELVETICA,
                            12);

            Font totalFont =
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD,
                            16);

            Paragraph empresa =
                    new Paragraph(
                            "DAVSOL ECO SYSTEMS",
                            titulo);

            empresa.setAlignment(Element.ALIGN_CENTER);

            documento.add(empresa);

            Paragraph sistema =
                    new Paragraph(
                            "Termas solares a precio de infarto",
                            texto);

            sistema.setAlignment(Element.ALIGN_CENTER);

            documento.add(sistema);

            documento.add(new Paragraph(" "));

            Paragraph factura =
                    new Paragraph(
                            "FACTURA DE VENTA",
                            subtitulo);

            factura.setAlignment(Element.ALIGN_CENTER);

            documento.add(factura);

            documento.add(new Paragraph(" "));

            String fecha =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm:ss")
                            .format(new Date());

            documento.add(
                    new Paragraph(
                            "Factura N°: " + idVenta,
                            texto));

            documento.add(
                    new Paragraph(
                            "Fecha: " + fecha,
                            texto));

            documento.add(new Paragraph(" "));

            documento.add(
                    new Paragraph(
                            "Cliente: " + cliente,
                            texto));

            documento.add(
                    new Paragraph(
                            "DNI/RUC: " + dni,
                            texto));

            documento.add(new Paragraph(" "));

            PdfPTable tablaPDF =
                    new PdfPTable(4);

            tablaPDF.setWidthPercentage(100);

            tablaPDF.setWidths(
                    new float[]{
                        1.2f,
                        4f,
                        2f,
                        2f
                    });

            PdfPCell c1 =
                    new PdfPCell(
                            new Phrase("CANT"));

            PdfPCell c2 =
                    new PdfPCell(
                            new Phrase("PRODUCTO"));

            PdfPCell c3 =
                    new PdfPCell(
                            new Phrase("PRECIO"));

            PdfPCell c4 =
                    new PdfPCell(
                            new Phrase("SUBTOTAL"));

            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c4.setBackgroundColor(BaseColor.LIGHT_GRAY);

            tablaPDF.addCell(c1);
            tablaPDF.addCell(c2);
            tablaPDF.addCell(c3);
            tablaPDF.addCell(c4);

            for (int i = 0; i < tabla.getRowCount(); i++) {

                tablaPDF.addCell(
                        tabla.getValueAt(i, 0).toString());

                tablaPDF.addCell(
                        tabla.getValueAt(i, 1).toString());

                tablaPDF.addCell(
                        tabla.getValueAt(i, 2).toString());

                tablaPDF.addCell(
                        tabla.getValueAt(i, 3).toString());
            }

            documento.add(tablaPDF);

            documento.add(new Paragraph(" "));

            Paragraph totalP =
                    new Paragraph(
                            "TOTAL: S/. "
                            + String.format("%.2f", total),
                            totalFont);

            totalP.setAlignment(
                    Element.ALIGN_RIGHT);

            documento.add(totalP);

            documento.add(new Paragraph(" "));

            documento.add(
                    new Paragraph(
                            "Gracias por confiar en DAVSOL ECO SYSTEMS"));

            documento.add(
                    new Paragraph(
                            "Ica - Perú"));

            documento.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Factura generada correctamente.");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al generar factura:\n"
                    + e.getMessage());

            e.printStackTrace();
        }
    }
}