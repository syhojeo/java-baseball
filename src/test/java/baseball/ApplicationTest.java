package baseball;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

class ApplicationTest extends NsTest {

    @ParameterizedTest
    @DisplayName("입력받은 값을 분석하여 Strike, ball 개수 집계 기능 테스트")
    @CsvSource(value = {"123, 456, 0, 0", "123, 123, 3, 0", "145, 415, 1, 2", "567, 756, 0, 3"})
    void analyzeInputNumberTest(String inputNumber, String setNumber, int strike, int ball) {

        Map<String, Integer> resultMap = Application.analyzeInputNumber(inputNumber, setNumber);

        assertThat(resultMap.get("strike")).isEqualTo(strike);
        assertThat(resultMap.get("ball")).isEqualTo(ball);
    }

    @ParameterizedTest
    @DisplayName("입력한 수가 유효하지 않을때 Exception이 발생하는지 통합 테스트")
    @ValueSource(strings = {"가나다", "122", "가가나나다다라마마바123124", "", "  ", "123"})
    void checkInputNumberValidationTest(String inputNumber) {

        boolean isTestSuccess = false;

        try {
            Application.checkInputNumberValidation(inputNumber);
        } catch (IllegalArgumentException e) {
            isTestSuccess = true;
        }

        assertThat(isTestSuccess).isTrue();
    }

    @DisplayName("입력된 수가 1 ~ 9 사이의 값이 아닐 경우 Exception 발생하는지 테스트")
    @Test
    void isNumberTest() {

        String inputNumber = "만23";
        boolean isTestSuccess = false;

        try {
            Application.isNumber(inputNumber);
        } catch (IllegalArgumentException e) {
            isTestSuccess = true;
        }

        assertThat(isTestSuccess).isTrue();
    }

    @DisplayName("입력된 수가 중복되어 있는 경우 Exception 발생하는지 테스트")
    @Test
    void checkDuplicationTest() {

        String inputNumber = "112";
        boolean isTestSuccess = false;

        try {
            Application.checkDuplication(inputNumber);
        } catch (IllegalArgumentException e) {
            isTestSuccess = true;
        }

        assertThat(isTestSuccess).isTrue();
    }

    @DisplayName("입력된 수가 3이 아닐경우 Exception 발생하는지 테스트")
    @Test
    void checkLengthTest() {

        String inputNumber = "1234";
        boolean isTestSuccess = false;

        try {
            Application.checkLength(inputNumber);
        } catch (IllegalArgumentException e) {
            //pass
            isTestSuccess = true;
        }

        assertThat(isTestSuccess).isTrue();
    }

    @DisplayName("숫자 입력 기능 테스트")
    @Test
    void inputNumberFromPlayerTest() {

        String inputString = "456";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(inputStream);

        String inputNumber = Application.inputNumberFromPlayer();

        assertThat(inputNumber).isEqualTo(inputString);

    }

    @DisplayName("1 ~ 9 사이의 값으로 중복 없이 설정되는지 테스트")
    @Test
    void setNumberTest() {

        int testNumber = 1000;
        boolean result = false;
        String number;

        for (int tryNumber = 0; tryNumber < testNumber; tryNumber++) {
            number = Application.setNumber();
            result = checkSetNumber(number);
            if (result == false) {
                break;
            }
        }

        assertThat(result).isTrue();
    }

    private boolean checkSetNumber(String number) {

        if (number.length() != 3) {
            return false;
        }

        char first = number.charAt(0);
        char second = number.charAt(1);
        char third = number.charAt(2);

        if (first == second || first == third || second == third) {
            return false;
        }

        boolean digitCheckResult = true;
        digitCheckResult = isDigitExceptZero(first);
        digitCheckResult = isDigitExceptZero(second);
        digitCheckResult = isDigitExceptZero(third);

        if (digitCheckResult == false) {
            return false;
        }

        return true;
    }

    private boolean isDigitExceptZero(char digit) {

        if (digit > '0' && digit <= '9') {
            return true;
        } else {
            return false;
        }
    }


    @Test
    void 게임종료_후_재시작() {
        assertRandomNumberInRangeTest(
                () -> {
                    run("246", "135", "1", "597", "589", "2");
                    assertThat(output()).contains("낫싱", "3스트라이크", "1볼 1스트라이크", "3스트라이크", "게임 종료");
                },
                1, 3, 5, 5, 8, 9
        );
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("1234"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
