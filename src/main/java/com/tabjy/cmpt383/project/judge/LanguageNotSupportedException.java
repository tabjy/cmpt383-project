package com.tabjy.cmpt383.project.judge;

import javax.naming.OperationNotSupportedException;

public class LanguageNotSupportedException extends OperationNotSupportedException {

    public LanguageNotSupportedException(String language) {
        super("language '" + language + "' is not supported");
    }
}
