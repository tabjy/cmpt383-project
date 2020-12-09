package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.models.Language;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BuildStrategies {
    private static final Map<String, Class<? extends IBuildStrategy>> STRATEGY_MAP;

    static {
        STRATEGY_MAP = Map.of( //
                "c", CBuildStrategy.class, //
                "cpp", CppBuildStrategy.class, //
                "javascript", InterpretedLanguageBuildStrategy.class, //
                "python", InterpretedLanguageBuildStrategy.class //
        );
    }

    public static IBuildStrategy forLanguage(Language language) throws LanguageNotSupportedException {
        switch (language) {
            case c:
                return new CBuildStrategy();
            case cpp:
                return new CppBuildStrategy();
            case javascript:
            case python:
                return new InterpretedLanguageBuildStrategy();
            default:
                throw new LanguageNotSupportedException(language.name());
        }
    }
}
