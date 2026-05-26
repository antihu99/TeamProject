package com.sec.bestreviewer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CommandParser {
    private final static int MIN_TOKENS_NUM = 5; // CMD + 3 of options + at least 1 params
    private final static String SPACE = " ";
    private final static String EMPTY = "";

    public TokenGroup parse(String line) throws IllegalArgumentException {
        String[] r = line.split(",", -1);

//        if (r.length < MIN_TOKENS_NUM) {
//            throw new IllegalArgumentException("wrong command format");
//        }

        String type = r[0];
        List<String> options = Arrays.asList(Arrays.copyOfRange(r, 1, 4));
        List<String> params = Arrays.asList(Arrays.copyOfRange(r, 4, r.length));

        // AND / OR 조건이 포함된 명령인지 확인
        int conditionIdx = getConditionIndex(params);
        if (conditionIdx != -1) {
            String condition = params.get(conditionIdx);
            List<String> firstParams = params.subList(0, conditionIdx);
            List<String> secondOptions = new ArrayList<>();
            secondOptions.add(SPACE);
            secondOptions.addAll(params.subList(conditionIdx + 1, conditionIdx + 3));
            List<String> secondParams = params.subList(conditionIdx + 3, r.length - 4);


            return new TokenGroup(
                    type,
                    getValidList(options), getValidList(firstParams),
                    condition,
                    getValidList(secondOptions), getValidList(secondParams)
            );
        }

        return new TokenGroup(type, getValidList(options), getValidList(params));
    }

    private int getConditionIndex(List<String> params) {
        for (int i = 0; i < params.size(); i++) {
            if ("-a".equals(params.get(i)) || "-o".equals(params.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private List<String> getValidList(List<String> list) {
        List<String> validList = new ArrayList<String>();

        list.forEach(i -> {
            if (!EMPTY.equals(i)) validList.add(i);
        });

        return validList;
    }
}
