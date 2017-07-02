package com.kraluk.totoscheduler.cucumber.stepdefs;

import com.kraluk.totoscheduler.TotoSchedulerApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = TotoSchedulerApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
