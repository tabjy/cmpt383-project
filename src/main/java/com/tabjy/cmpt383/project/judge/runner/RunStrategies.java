package com.tabjy.cmpt383.project.judge.runner;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.judge.builder.CBuildStrategy;
import com.tabjy.cmpt383.project.judge.builder.CppBuildStrategy;
import com.tabjy.cmpt383.project.judge.builder.InterpretedLanguageBuildStrategy;
import com.tabjy.cmpt383.project.models.Language;

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

    public static IRunStrategy forLanguage(Language language) throws LanguageNotSupportedException {
        switch (language) {
            case c:
            case cpp:
                return new NativeRunStrategy();
            case javascript:
                return new NodejsRunStrategy();
            case python:
                return new PythonRunStrategy();
            case java:
                return new JavaRunStrategy();
            default:
                throw new LanguageNotSupportedException(language.name());
        }
    }
}
