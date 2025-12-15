package org.fiap.com;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.fiap.com.Services.FeedbackService;
import org.jboss.logging.Logger;

import java.time.LocalDate;

@ApplicationScoped
public class LambdaHandler implements RequestHandler<Object, String> {

    private static final Logger LOG = Logger.getLogger(LambdaHandler.class);

    @Inject
    FeedbackService service;

    @Override
    public String handleRequest(Object input, Context context) {

        LOG.info("🚀 Lambda acionada pelo EventBridge");
        LOG.infof("📥 Payload recebido: %s", input);

        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusDays(7);

        LOG.infof("📅 Intervalo de busca -> Início: %s | Fim: %s", inicio, fim);

        service.gerarCsvSemanal(inicio, fim);

        LOG.info("✅ Execução da Lambda finalizada com sucesso");

        return "OK";
    }
}
