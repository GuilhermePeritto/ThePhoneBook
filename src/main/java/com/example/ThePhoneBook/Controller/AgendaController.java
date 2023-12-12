package com.example.ThePhoneBook.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.page.MonthPage;
import com.example.ThePhoneBook.Model.Contato;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AgendaController {

    // Método para calcular e exibir aniversários na agenda
    // Método para calcular e exibir aniversários na agenda
    public void Calcular(List<Contato> listaContatos, MonthPage agenda) {
        // Criar um conjunto para rastrear as datas já processadas
        Set<LocalDate> datasProcessadas = new HashSet<>();

        // Itera sobre a lista de contatos
        for (Contato contato : listaContatos) {
            LocalDate dataNascimento = contato.getDataNascimento();

            // Verifica se a data de nascimento é válida
            if (dataNascimento != null) {
                // Obtém o mês e o dia do aniversário
                int mesAniversario = dataNascimento.getMonthValue();
                int diaAniversario = dataNascimento.getDayOfMonth();

                // Itera por cada ano no intervalo desejadoadm
                for (int ano = 1900; ano <= 2100; ano++) {
                    // Cria a entrada para o aniversário do ano atual
                    LocalDate aniversarioAtual = LocalDate.of(ano, mesAniversario, diaAniversario);

                    // Se o aniversário já passou este ano, usa o próximo ano
                    if (LocalDate.now().isAfter(aniversarioAtual)) {
                        aniversarioAtual = aniversarioAtual.plusYears(1);
                    }

                    // Verifica se a data já foi processada
                    if (!datasProcessadas.contains(aniversarioAtual)) {
                        // Adiciona a entrada para o aniversário
                        Entry<String> entry = new Entry<>(contato.getDescricao());
                        entry.changeStartDate(aniversarioAtual);
                        entry.changeEndDate(aniversarioAtual);
                        entry.setLocation("Aniversário de " + contato.getDescricao());

                        // Adiciona a entrada à MonthPage
                        agenda.getCalendarSources().get(0).getCalendars().get(0).addEntry(entry);

                        // Adiciona a data ao conjunto de datas processadas
                        datasProcessadas.add(aniversarioAtual);
                    }
                }
            }
        }
    }


}