package com._oormthonUNIV.newnew.global.properties;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class OpenApiProperties {

    private final Environment env;
    private final String OPENAI_API_KEY;

    private OpenApiProperties(Environment env) {
        this.env = env;
        this.OPENAI_API_KEY = env.getProperty("openai.api.key");
        System.out.println("OPENAI_API_KEY: " + OPENAI_API_KEY);
    }

    public String getOPENAI_API_KEY() {
        return OPENAI_API_KEY;
    }

}
