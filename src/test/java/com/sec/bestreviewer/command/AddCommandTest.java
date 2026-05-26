package com.sec.bestreviewer.command;

import com.sec.bestreviewer.store.Employee;
import com.sec.bestreviewer.store.EmployeeStore;
import com.sec.bestreviewer.util.OptionParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AddCommandTest {

    @Test
    public void testExecuteSingle_ShouldReturnEmptyListAndNotAddEmployee() {
        // Arrange
        Employee mockEmployee = mock(Employee.class);
        OptionParser mockParser = mock(OptionParser.class);
        EmployeeStore mockStore = mock(EmployeeStore.class);
        AddCommand addCommand = new AddCommand(mockParser, mockEmployee);

        // Act
        List<String> result = addCommand.executeSingle(mockStore);

        // Assert
        assertEquals(0, result.size());
        verify(mockStore, never()).add(mockEmployee);
    }

    @Test
    public void testExecuteAndOr_ShouldReturnEmptyListAndNotAddEmployee() {
        // Arrange
        Employee mockEmployee = mock(Employee.class);
        OptionParser mockParser = mock(OptionParser.class);
        EmployeeStore mockStore = mock(EmployeeStore.class);
        AddCommand addCommand = new AddCommand(mockParser, mockEmployee);

        // Act
        List<String> result = addCommand.executeAndOr(mockStore);

        // Assert
        assertEquals(0, result.size());
        verify(mockStore, never()).add(mockEmployee);
    }
}