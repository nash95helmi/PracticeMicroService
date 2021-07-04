package com.practice.modulebatch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collection;

@Configuration
public abstract class AbstractSpringBatchConfiguration extends SimpleBatchConfiguration {
    private BatchConfigurer configurer;

    @Bean
    public abstract DataSource dataSource();

    @Override
    protected BatchConfigurer getConfigurer(Collection<BatchConfigurer> configurers) {
        if (this.configurer != null)
            return this.configurer;

        if (configurers == null || configurers.isEmpty()) {
            if (dataSource() == null) {
                DefaultBatchConfigurer defaultBatchConfigurer = new DefaultBatchConfigurer();
                defaultBatchConfigurer.initialize();
                this.configurer = defaultBatchConfigurer;
                return configurer;
            } else {
                DefaultBatchConfigurer defaultBatchConfigurer = new DefaultBatchConfigurer(dataSource());
                defaultBatchConfigurer.initialize();
                this.configurer = defaultBatchConfigurer;
                return configurer;
            }
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("To use custome BatchConfigurer, the context has to be 1, found "+
                    configurers.size());
        }
        this.configurer = configurers.iterator().next();
        return this.configurer;
    }
}
