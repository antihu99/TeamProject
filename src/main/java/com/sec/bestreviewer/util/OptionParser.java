package com.sec.bestreviewer.util;

import java.nio.channels.Pipe;
import java.util.List;

public class OptionParser {
    private static final int PRIMARY_OPTION_INDEX = 0;
    private static final int SECONDARY_OPTION_INDEX = 1;
    private static final int TERTIARY_OPTION_INDEX = 2;

    private PrimaryOptionEnum primaryOption = PrimaryOptionEnum.NONE;
    private SecondaryOptionEnum secondaryOption = SecondaryOptionEnum.NONE;
    private TertiaryOptionEnum tertiaryOption = TertiaryOptionEnum.NONE;

    public OptionParser(List<String> options) {
        if (options.isEmpty()) {
            return;
        }

        if (options.size() >= 3) {
            setPrimaryOption(options.get(PRIMARY_OPTION_INDEX));
            setSecondaryOption(options.get(SECONDARY_OPTION_INDEX));
            setTertiaryOption(options.get(TERTIARY_OPTION_INDEX));
            return;
        }

        parseCompactOptions(options);
    }

    private void parseCompactOptions(List<String> options) {
        for (String option : options) {
            if (isBlank(option)) {
                continue;
            }

            if (isPrimaryOption(option)) {
                setPrimaryOption(option);
                continue;
            }

            if (isTertiaryOption(option)) {
                setTertiaryOption(option);
                continue;
            }

            setSecondaryOption(option);
        }
    }

    private void setTertiaryOption(String option) {
        if (!isBlank(option))
            tertiaryOption = TertiaryOptionEnum.fromOption(option);
        else
            tertiaryOption = TertiaryOptionEnum.NONE;
    }

    private void setSecondaryOption(String option) {
        if (!isBlank(option))
            secondaryOption = SecondaryOptionEnum.fromOption(option);
        else
            secondaryOption = SecondaryOptionEnum.NONE;
    }

    private void setPrimaryOption(String option) {
        if (!isBlank(option))
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

    private boolean isPrimaryOption(String option) {
        return PrimaryOptionEnum.PRINT.getOption().equals(option);
    }

    private boolean isTertiaryOption(String option) {
        for (TertiaryOptionEnum tertiary : TertiaryOptionEnum.values()) {
            if (tertiary == TertiaryOptionEnum.NONE) {
                continue;
            }

            if (tertiary.getOption().equals(option)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlank(String option) {
        return option == null || option.trim().isEmpty();
    }

}
