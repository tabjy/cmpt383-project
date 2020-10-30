package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BuildStrategies {
    private static final Map<String, Class<? extends IBuildStrategy>> STRATEGY_MAP;

    static {
        STRATEGY_MAP = Map.of( //
                "python", InterpretedLanguageBuildStrategy.class, //
                "javascript", InterpretedLanguageBuildStrategy.class, //
                "php", InterpretedLanguageBuildStrategy.class //
        );
    }

    public static IBuildStrategy forLanguage(String language) throws LanguageNotSupportedException {
        Class<? extends IBuildStrategy> strategy = STRATEGY_MAP.get(language.toLowerCase());
        if (strategy == null) {
            throw new LanguageNotSupportedException(language);
        }

        try {
            return strategy.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
