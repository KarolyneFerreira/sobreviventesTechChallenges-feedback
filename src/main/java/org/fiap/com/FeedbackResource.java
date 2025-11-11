package org.fiap.com;



import org.fiap.com.Services.FeedbackService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;

@Path("/feedback")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeedbackResource {

    @Inject
    FeedbackService service;

    @GET
    @Path("/relatorio-semanal")
    public Response gerarRelatorioSemanal() {
        // 📆 Calcula intervalo da semana anterior (segunda a domingo)
        LocalDate hoje = LocalDate.now();
        LocalDate fim = hoje.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate inicio = fim.minusDays(6);

        Map<String, Object> resultado = service.sumarizarPorNotas(inicio, fim);

        return Response.ok(resultado).build();
    }
}
