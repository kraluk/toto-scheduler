package com.kraluk.totoscheduler.web.rest.vm;

import com.google.common.base.MoreObjects;

import ch.qos.logback.classic.Logger;

/**
 * View Model object for storing a Logback logger.
 */
public class LoggerVm {

    private String name;

    private String level;

    public LoggerVm(Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

    public LoggerVm() {
        // Empty public constructor used by Jackson.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("name", name)
            .add("level", level)
            .toString();
    }
}
