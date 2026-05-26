package com.sec.bestreviewer.command;

import com.sec.bestreviewer.CommandFactory;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import com.sec.bestreviewer.util.Pair;

import java.util.Collections;
import java.util.List;

public class CountCommand extends Command {
    @Override
    public List<String> execute(EmployeeStore employeeStore) {
        return Collections.singletonList(CommandFactory.CMD_CNT + "," + String.valueOf(employeeStore.count()));
    }
}
