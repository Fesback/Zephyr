package com.zephyr.service;

public interface EmailService {
    void enviarEmailConAdjunto(String destinatario, String asunto, String cuerpoMensaje, String rutaArchivoAdjunto);
}
