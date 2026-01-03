package org.fiap.com.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class AwsClientProducer {

    @Produces
    @ApplicationScoped
    public S3Client s3Client() {
        return S3Client.create();
    }
}
