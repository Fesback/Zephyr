package com.zephyr.service.impl;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.layout.element.Image;

//fuentes
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.zephyr.domain.Boleto;
import com.zephyr.domain.Pasajero;
import com.zephyr.domain.Vuelo;
import com.zephyr.repository.BoletoRepository;
import com.zephyr.repository.PasajeroRepository;
import com.zephyr.repository.VueloRepository;
import com.zephyr.repository.impl.BoletoRepositoryJDBCImpl;
import com.zephyr.repository.impl.PasajeroRepositoryJDBCImpl;
import com.zephyr.repository.impl.VueloRepositoryJDBCImpl;
import com.zephyr.service.ReportService;
import com.itextpdf.layout.properties.TextAlignment;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class ReportServiceImpl implements ReportService {

    private final BoletoRepository boletoRepository;
    private final VueloRepository vueloRepository;
    private final PasajeroRepository pasajeroRepository;

    //colores
    private static final Color COLOR_PRIMARIO = new DeviceRgb(0, 82, 204);
    private static final Color COLOR_TEXTO_BLANCO = new DeviceRgb(255, 255, 255);
    private static final Color COLOR_TEXTO_GRIS = new DeviceRgb(138, 138, 138);

    public ReportServiceImpl() {
        this.boletoRepository = new BoletoRepositoryJDBCImpl();
        this.vueloRepository = new VueloRepositoryJDBCImpl();
        this.pasajeroRepository = new PasajeroRepositoryJDBCImpl();
    }

    @Override
    public String generarBoardingPass(int idBoleto) {

        Optional<Boleto> boletoOpt = boletoRepository.findById(idBoleto);
        if (boletoOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el boleto con ID: " + idBoleto);
            return null;
        }

        Boleto boleto = boletoOpt.get();

        Optional<Vuelo> vueloOpt = vueloRepository.findVueloDetalladoById(boleto.getIdVuelo());
        if (vueloOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el vuelo con ID: " + idBoleto);
            return null;
        }

        Vuelo vuelo = vueloOpt.get();

        Optional<Pasajero> pasajeroOpt = pasajeroRepository.findById(boleto.getIdPasajero());
        if (pasajeroOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el  pasajero con ID: " + boleto.getIdPasajero());
            return null;
        }

        Pasajero pasajero = pasajeroOpt.get();

        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Downloads/BoardingPass_" + boleto.getCodigoBoleto() + ".pdf";

        PageSize boardingPassSize = new PageSize(595, 283);

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            pdf.setDefaultPageSize(boardingPassSize);

            Document document = new Document(pdf);
            document.setMargins(10, 10, 10, 10);

            PdfFont zapfDingbats = PdfFontFactory.createFont(StandardFonts.ZAPFDINGBATS);

            Table tablaPrincipal = new Table(UnitValue.createPercentArray(new float[]{75f, 25f}))
                    .useAllAvailableWidth();
            tablaPrincipal.setBorder(Border.NO_BORDER);

            Cell celdaIzquierda = new Cell();
            celdaIzquierda.setBorder(Border.NO_BORDER);
            celdaIzquierda.setPadding(10);

            celdaIzquierda.setVerticalAlignment(VerticalAlignment.TOP);

            Table tablaHeader = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                    .useAllAvailableWidth()
                    .setBackgroundColor(COLOR_PRIMARIO);

            Paragraph titulo = new Paragraph("BOARDING PASS")
                    .setFontColor(COLOR_TEXTO_BLANCO).setBold().setFontSize(14);
            Paragraph aerolinea = new Paragraph(vuelo.getAerolinea())
                    .setFontColor(COLOR_TEXTO_BLANCO).setFontSize(12).setTextAlignment(TextAlignment.RIGHT);

            tablaHeader.addCell(crearCeldaSimple(titulo, Border.NO_BORDER).setPadding(5));
            tablaHeader.addCell(crearCeldaSimple(aerolinea, Border.NO_BORDER).setPadding(5));
            celdaIzquierda.add(tablaHeader);


            Table tablaVuelo = new Table(UnitValue.createPercentArray(new float[]{40f, 20f, 40f}))
                    .useAllAvailableWidth().setMarginTop(10);

            tablaVuelo.addCell(crearCeldaSimple(
                    new Paragraph(vuelo.getOrigenIata()).setBold().setFontSize(36), Border.NO_BORDER));

            //  simbolo
            tablaVuelo.addCell(crearCeldaSimple(
                    new Paragraph("\u2794")
                            .setFont(zapfDingbats)
                            .setFontSize(24)
                            .setTextAlignment(TextAlignment.CENTER), Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            tablaVuelo.addCell(crearCeldaSimple(
                    new Paragraph(vuelo.getDestinoIata()).setBold().setFontSize(36).setTextAlignment(TextAlignment.RIGHT), Border.NO_BORDER));

            celdaIzquierda.add(tablaVuelo);


            Table tablaDetalles = new Table(UnitValue.createPercentArray(new float[]{50f, 20f, 30f}))
                    .useAllAvailableWidth().setMarginTop(10);
            tablaDetalles.addCell(crearCeldaDetalle("PASAJERO", pasajero.getNombres() + " " + pasajero.getApellidos()));
            tablaDetalles.addCell(crearCeldaDetalle("VUELO", vuelo.getCodigoVuelo()));
            tablaDetalles.addCell(crearCeldaDetalle("ASIENTO", boleto.getAsiento()));
            celdaIzquierda.add(tablaDetalles);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy HH:mm");

            Table tablaDetalles2 = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                    .useAllAvailableWidth().setMarginTop(5); // <-- Reducido de 10 a 5
            tablaDetalles2.addCell(crearCeldaDetalle("PUERTA", vuelo.getCodigoPuerta() != null ? vuelo.getCodigoPuerta() : "---"));
            tablaDetalles2.addCell(crearCeldaDetalle("FECHA/HORA", vuelo.getFechaSalida().format(formatter)));
            celdaIzquierda.add(tablaDetalles2);



            Barcode128 barcode = new Barcode128(pdf);
            barcode.setCode(boleto.getCodigoBoleto());
            Image barcodeImage = new Image(barcode.createFormXObject(pdf));
            barcodeImage.setHeight(40f);


            Cell celdaBarcode = new Cell();
            celdaBarcode.setBorder(Border.NO_BORDER);
            celdaBarcode.setTextAlignment(TextAlignment.CENTER);


            barcodeImage.setWidth(UnitValue.createPercentValue(90));

            celdaBarcode.add(barcodeImage.setMarginTop(5));
            celdaIzquierda.add(celdaBarcode);


            Cell celdaDerecha = new Cell();
            celdaDerecha.setBorderLeft(new DashedBorder(COLOR_TEXTO_GRIS, 1f));
            celdaDerecha.setBorder(Border.NO_BORDER);
            celdaDerecha.setPadding(10);
            celdaDerecha.setVerticalAlignment(VerticalAlignment.TOP);

            Table tablaHeaderTalon = new Table(UnitValue.createPercentArray(new float[]{100f}))
                    .useAllAvailableWidth().setBackgroundColor(COLOR_PRIMARIO);
            tablaHeaderTalon.addCell(crearCeldaSimple(
                    new Paragraph(vuelo.getAerolinea()).setFontColor(COLOR_TEXTO_BLANCO).setFontSize(10).setBold(), Border.NO_BORDER).setPadding(5));
            celdaDerecha.add(tablaHeaderTalon);

            celdaDerecha.add(crearCeldaDetalle("VUELO", vuelo.getCodigoVuelo()));
            celdaDerecha.add(crearCeldaDetalle("ASIENTO", boleto.getAsiento()));
            celdaDerecha.add(crearCeldaDetalle("PASAJERO", pasajero.getNombres()));

            Table tablaOrigenDestinoTalon = new Table(UnitValue.createPercentArray(new float[]{50f, 50f}))
                    .useAllAvailableWidth().setMarginTop(10);
            tablaOrigenDestinoTalon.addCell(crearCeldaDetalle("DE", vuelo.getOrigenIata()));
            tablaOrigenDestinoTalon.addCell(crearCeldaDetalle("A", vuelo.getDestinoIata()));
            celdaDerecha.add(tablaOrigenDestinoTalon);

            celdaDerecha.add(crearCeldaDetalle("FECHA/HORA", vuelo.getFechaSalida().format(formatter)));

            Image barcodeImageTalon = new Image(barcode.createFormXObject(pdf));
            barcodeImageTalon.setWidth(UnitValue.createPercentValue(100));
            barcodeImageTalon.setHeight(30f);
            celdaDerecha.add(barcodeImageTalon.setMarginTop(10));

            tablaPrincipal.addCell(celdaIzquierda);
            tablaPrincipal.addCell(celdaDerecha);

            document.add(tablaPrincipal);
            document.close();

            System.out.println("PDF Profesional (v2.2) generado exitosamente en: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("Error al generar el PDF profesional: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private Cell crearCeldaSimple(Paragraph p, Border border) {
        Cell cell = new Cell().add(p);
        cell.setBorder(border);
        return cell;
    }

    private Cell crearCeldaDetalle(String titulo, String valor) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setPadding(0).setMarginTop(8);

        Paragraph pTitulo = new Paragraph(titulo)
                .setFontColor(COLOR_TEXTO_GRIS)
                .setFontSize(8)
                .setBold();

        Paragraph pValor = new Paragraph(valor)
                .setFontColor(new DeviceRgb(0, 0, 0))
                .setBold();

        cell.add(pTitulo);
        cell.add(pValor);
        return cell;
    }
}