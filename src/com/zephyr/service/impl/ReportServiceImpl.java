package com.zephyr.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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

import java.io.FileNotFoundException;
import java.util.Optional;


public class ReportServiceImpl implements ReportService {

    private final BoletoRepository boletoRepository;
    private final VueloRepository vueloRepository;
    private final PasajeroRepository pasajeroRepository;

    public ReportServiceImpl() {
        this.boletoRepository = new BoletoRepositoryJDBCImpl();
        this.vueloRepository = new VueloRepositoryJDBCImpl();
        this.pasajeroRepository = new PasajeroRepositoryJDBCImpl();
    }

    @Override
    public String generarBoardingPass(int idBoleto) {

        Optional<Boleto> boletoOpt = boletoRepository.findById(idBoleto);
        if (boletoOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el boleto con ID: "  + idBoleto);
            return null;
        }

        Boleto boleto = boletoOpt.get();

        Optional<Vuelo> vueloOpt = vueloRepository.findVueloDetalladoById(boleto.getIdVuelo());
        if (vueloOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el vuelo con ID: "  + idBoleto);
            return null;
        }

        Vuelo vuelo = vueloOpt.get();

        Optional<Pasajero> pasajeroOpt = pasajeroRepository.findById(boleto.getIdPasajero());
        if (pasajeroOpt.isEmpty()) {
            System.err.println("Error de reporte: No se encontro el  pasajero con ID: "  + boleto.getIdPasajero());
            return null;
        }

        Pasajero pasajero = pasajeroOpt.get();

        String userHome = System.getProperty("user.home");
        String filePath = userHome + "/Downloads/BoardingPass_" + boleto.getCodigoBoleto() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("TARJETA DE EMBARQUE - ZEPHYR")
                    .setBold()
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Pasajero: " + pasajero.getNombres() + " " +  pasajero.getApellidos())
                    .setFontSize(14));

            document.add(new Paragraph(vuelo.getOrigenIata() + " --> " + vuelo.getDestinoIata())
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            document.add(new Paragraph("Código de Boleto: " + boleto.getCodigoBoleto())
                    .setFontSize(12)
                    .setItalic());

            document.add(new Paragraph("Vuelo: " + vuelo.getCodigoVuelo() + " (" + vuelo.getAerolinea() + ")")
                    .setFontSize(14));

            document.add(new Paragraph("Asiento: " + boleto.getAsiento())
                    .setFontSize(16)
                    .setBold());

            document.add(new Paragraph("Puerta: " + (vuelo.getCodigoPuerta() != null ? vuelo.getCodigoPuerta() : "Pendiente"))
                    .setFontSize(14));

            document.add(new Paragraph("Hora de Salida: " + vuelo.getFechaSalida().toString())
                    .setFontSize(12));

            document.close();

            System.out.println("PDF generado exitosamente en: " + filePath);
            return filePath;

        } catch (FileNotFoundException e) {
            System.err.println("Error al crear el PDF (archivo no encontrado): " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
