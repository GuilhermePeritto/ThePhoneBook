package com.example.ThePhoneBook.Core;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.Map;

public class ImprimirRelatorio {
    public static void exibirRelatorio(String caminhoRelatorioJasper, Map<String, Object> parametros) {
        try {
            // Carrega o relatório
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(caminhoRelatorioJasper);

            // Preenche o relatório com os dados, se necessário
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, new JREmptyDataSource());

            // Exibe o relatório usando o JasperViewer
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
