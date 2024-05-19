package com.kidchang.lingopress.press;

import com.kidchang.lingopress.press.entity.LearnedPress;
import com.kidchang.lingopress.press.entity.LearnedPressContentLine;
import com.kidchang.lingopress.press.entity.Press;
import com.kidchang.lingopress.press.entity.PressContentLine;
import com.kidchang.lingopress.user.User;

public class PressSteps {
    public static LearnedPressContentLine getNewLearnedPressContentLineOnlyMemo(LearnedPress newLearnedPress, PressContentLine newPressContentLine, int lineNumber, Press newPress, User newUser) {
        return LearnedPressContentLine.builder()
                .learnedPress(newLearnedPress)
                .pressContentLine(newPressContentLine)
                .isCorrect(null)
                .lineNumber(lineNumber)
                .press(newPress)
                .user(newUser)
                .userTranslatedLine(null)
                // 메모 저장
                .memo("memo")
                .build();
    }

    public static LearnedPress getNewLearnedPress(User newUser, Press newPress) {
        return LearnedPress.builder()
                .user(newUser)
                .press(newPress)
                .build();
    }

    public static PressContentLine getNewPressContentLine(Press newPress) {
        return PressContentLine.builder()
                .id(1L)
                .lineText("hello")
                .lineNumber(1)
                .press(newPress)
                .build();
    }

    public static Press getNewPress(Long pressId) {
        return Press.builder()
                .title("test")
                .id(pressId)
                .build();
    }
}