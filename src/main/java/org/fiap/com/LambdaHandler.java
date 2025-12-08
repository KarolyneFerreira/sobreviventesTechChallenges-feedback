package org.fiap.com;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.fiap.com.Services.FeedbackService;

import java.time.LocalDate;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final FeedbackService service = new FeedbackService();

//    @Override
//    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
//        LocalDate inicio = LocalDate.parse((String) input.get("inicio"));
//        LocalDate fim = LocalDate.parse((String) input.get("fim"));
//        return service.sumarizarPorNotas(inicio, fim);
//    }

//    @Override
//    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
//        LocalDate inicio = LocalDate.parse((String) input.get("inicio"));
//        LocalDate fim = LocalDate.parse((String) input.get("fim"));
//
//        // Testes de conectividade
//        service.testarConexaoS3();
//        service.getRepository().testarConexaoRDS();
//
//        return service.sumarizarPorNotas(inicio, fim);
//    }

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        LocalDate inicio = LocalDate.parse((String) input.get("inicio"));
        LocalDate fim = LocalDate.parse((String) input.get("fim"));

        // Testes
        service.getRepository().listarTabelas();
        service.getRepository().listarSchemas();
//        service.getRepository().testarSelect("nome_da_tabela");

        return service.sumarizarPorNotas(inicio, fim);
    }


}


