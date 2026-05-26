package com.sec.bestreviewer.util;

import java.nio.channels.Pipe;
import java.util.List;

public class OptionParser {

    private PrimaryOptionEnum primaryOption = PrimaryOptionEnum.NONE;
    private SecondaryOptionEnum secondaryOption = SecondaryOptionEnum.NONE;
    private TertiaryOptionEnum tertiaryOption = TertiaryOptionEnum.NONE;

    public OptionParser(List<String> options) {
        if(options.isEmpty()) return;

        if(options.size() == 3) {
            setPrimaryOption(options.get(0));
            setSecondaryOption(options.get(1));
            setTertiaryOption(options.get(2));
        }

        if(options.size() == 2) {
            setSecondaryOption(options.get(0));
            setTertiaryOption(options.get(1));
        }
    }


    private void setTertiaryOption(String option) {
        if (!option.isEmpty())
            tertiaryOption = TertiaryOptionEnum.fromOption(option);
        else
            tertiaryOption = TertiaryOptionEnum.NONE;
    }

    private void setSecondaryOption(String option) {
        if (!option.isEmpty())
            secondaryOption = SecondaryOptionEnum.fromOption(option);
        else
            secondaryOption = SecondaryOptionEnum.NONE;
    }

    private void setPrimaryOption(String option) {
        if (!option.isEmpty())
            primaryOption = PrimaryOptionEnum.fromOption(option);
        else
            primaryOption = PrimaryOptionEnum.NONE;
    }

    public PrimaryOptionEnum getPrimaryOption() {
        return primaryOption;
    }

    public SecondaryOptionEnum getSecondaryOption() {
        return secondaryOption;
    }

    public TertiaryOptionEnum getTertiaryOption() {
        return tertiaryOption;
    }

}
