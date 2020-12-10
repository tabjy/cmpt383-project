package com.tabjy.cmpt383.project.judge.builder;

import com.tabjy.cmpt383.project.judge.LanguageNotSupportedException;
import com.tabjy.cmpt383.project.models.Language;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class BuildStrategies {

    public static IBuildStrategy forLanguage(Language language) throws LanguageNotSupportedException {
        switch (language) {
            case c:
                return new CBuildStrategy();
            case cpp:
                return new CppBuildStrategy();
            case java:
                return new JavaBuildStrategy();
            case javascript:
            case python:
                return new InterpretedLanguageBuildStrategy();
            default:
                throw new LanguageNotSupportedException(language.name());
        }
    }
}
