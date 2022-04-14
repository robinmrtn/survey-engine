package com.roal.survey_engine.security.config;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class HashidsConfig {

    @Bean
    @Qualifier("surveyHashids")
    public Hashids surveyHashids() {
        return new Hashids("survey", 9);
    }

    @Bean
    @Qualifier("campaignHashids")
    public Hashids campaignHashids() {
        return new Hashids("campaign", 9);
    }

    @Bean
    @Qualifier("responseHashids")
    public Hashids responseHashids() {
        return new Hashids("response", 9);
    }

    @Bean
    @Qualifier("userHashids")
    public Hashids userHashids() {
        return new Hashids("user", 9);
    }

    @Bean
    @Qualifier("workspaceHashids")
    public Hashids workspaceHashids() {
        return new Hashids("workspace", 9);
    }

    @Bean
    @Primary
    public Hashids elementHashids() {
        return new Hashids("element", 9);
    }

}
