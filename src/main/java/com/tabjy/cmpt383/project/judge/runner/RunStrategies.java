package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RunStrategies {
    private static final Map<String, Class<? extends IRunStrategy>> STRATEGY_MAP;

    static {
        STRATEGY_MAP = Map.of( //
                "c", NativeRunStrategy.class, //
                "cpp", NativeRunStrategy.class, //
                "javascript", NodejsRunStrategy.class, //
                "python", PythonRunStrategy.class //
        );
    }

    public static IRunStrategy forLanguage(String language) throws LanguageNotSupportedException {
        Class<? extends IRunStrategy> strategy = STRATEGY_MAP.get(language.toLowerCase());
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
