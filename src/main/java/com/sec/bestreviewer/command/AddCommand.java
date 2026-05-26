package com.sec.bestreviewer.command;


import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;

import java.util.Collections;
import java.util.List;

public class AddCommand extends Command {
    private final Employee employee;
    private final OptionParser optionParser;

    public AddCommand(OptionParser optionParser, Employee employee) {
        this.employee = employee;
        this.optionParser = optionParser;
    }

    @Override
    public List<String> execute(EmployeeStore employeeStore) {
        employeeStore.add(employee);
        return Collections.emptyList();
    }

}
