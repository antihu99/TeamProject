package com.sec.bestreviewer;

import com.sec.bestreviewer.command.CombinationEnum;
import org.junit.jupiter.api.Test;

import static com.spun.util.Asserts.assertEqual;
import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {
    @Test
    void 옵션이_없는_CMD를_잘파싱하는지_확인() {
        String line = "ADD, , , ,08951033,QDJPTOJ KIM,CL3,010-3240-5443,19800308,ADV";

        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);


        assertEquals("ADD", tokens.getType());

        String[] options = {" ", " ", " "};
        assertArrayEquals(options, tokens.getOptions().toArray());

        String[] params = {"08951033", "QDJPTOJ KIM", "CL3", "010-3240-5443", "19800308", "ADV"};
        assertArrayEquals(params, tokens.getParams().toArray());
    }

    @Test
    void 옵션이_있는_CMD를_잘파싱하는지_확인() {
        String line = "SCH,-p, , ,phoneNum,010-2742-2901";

        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);

        System.out.println(tokens.getType() + " / '" + tokens.getOptions() + "' / " + tokens.getParams());

        assertEquals("SCH", tokens.getType());

        String[] options = {"-p", " ", " "};
        assertArrayEquals(options, tokens.getOptions().toArray());

        String[] params = {"phoneNum", "010-2742-2901"};
        assertArrayEquals(params, tokens.getParams().toArray());
    }

    @Test
    void AndOr_커맨들를_잘_파싱하는지_확인() {
        String line = "SCH,-p,-l, ,name,KIM,-a, , ,cl,CL4";

        CommandParser commandParser = new CommandParser();
        TokenGroup tokens = commandParser.parse(line);

        assertEquals("SCH", tokens.getType());

        String[] firstOptions = {"-p", "-l", " "};
        assertArrayEquals(firstOptions, tokens.getFirstOptions().toArray());

        String[] firstParams = {"name", "KIM"};
        assertArrayEquals(firstParams, tokens.getFirstParams().toArray());

        assertEquals("-a", tokens.getCombination());
        assertEquals(CombinationEnum.AND, tokens.getCombinationEnum());

        String[] secondOptions = {" ", " ", " "};
        assertArrayEquals(secondOptions, tokens.getSecondOptions().toArray());

        String[] secondParams = {"cl", "CL4"};
        assertArrayEquals(secondParams, tokens.getSecondParams().toArray());
    }

}
