package br.com.acmattos.article.docker.camel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolExecutorServiceConfig {
    @Bean(name="threadPoolExecutorService")
    public ExecutorService threadPoolExecutorService() {
        return new ThreadPoolExecutor(
            8, 8, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(64),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Bean(name="esSearchPoolExecutorService")
    public ExecutorService esSearchPoolExecutorService() {
        return new ThreadPoolExecutor(
            16, 16, 60L, TimeUnit.SECONDS,
             new ArrayBlockingQueue<>(64),
             new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Bean(name="esBulkPoolExecutorService")
    public ExecutorService esBulkPoolExecutorService() {
        return new ThreadPoolExecutor(
            2, 2, 60L, TimeUnit.SECONDS,
             new ArrayBlockingQueue<>(8),
             new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
