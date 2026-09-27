import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("GradeCalculator-ийн тестүүд")
class GradeCalculatorTest {

    // ---------- letterGrade: ердийн утгууд ----------

    @ParameterizedTest(name = "{0} оноо -> {1}")
    @CsvSource({
        "95, A",
        "85, B",
        "75, C",
        "65, D",
        "30, F"
    })
    @DisplayName("Ердийн оноонд зөв үсгэн дүн өгөх ёстой")
    void typicalScoresGiveCorrectGrade(double score, String expected) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(score); // Act

        assertEquals(expected, grade); // Assert
    }

    // ---------- letterGrade: хязгаарын утгууд ----------

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void ninetyIsExactlyA() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(90.0); // Act

        assertEquals("A", grade); // Assert
    }

    @Test
    @DisplayName("89.99 оноо B дүн байх ёстой (A-гийн доод хязгаараас яг доор)")
    void justBelowNinetyIsB() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(89.99); // Act

        assertEquals("B", grade); // Assert
    }

    @Test
    @DisplayName("60 оноо яг D дүн байх ёстой (тэнцэх доод хязгаар)")
    void sixtyIsExactlyD() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(60.0); // Act

        assertEquals("D", grade); // Assert
    }

    @Test
    @DisplayName("59.99 оноо F дүн байх ёстой (тэнцэх хязгаараас яг доор)")
    void justBelowSixtyIsF() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(59.99); // Act

        assertEquals("F", grade); // Assert
    }

    @ParameterizedTest(name = "{0} оноо -> {1}")
    @CsvSource({
        "0, F",
        "100, A"
    })
    @DisplayName("Хүчинтэй мужийн хоёр захын утга (0 ба 100) зөв дүн өгөх ёстой")
    void rangeEdgesAreValid(double score, String expected) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(score); // Act

        assertEquals(expected, grade); // Assert
    }

    @ParameterizedTest(name = "{0} оноо -> {1}")
    @CsvSource({"95,A", "90,A", "89.99,B", "80,B", "70,C", "60,D", "59.99,F", "0,F"})
    @DisplayName("Бүх дүнгийн хязгаар дээр letterGrade зөв ажиллах ёстой (parameterized)")
    void letterGradeBoundaries(double score, String expected) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        String grade = calc.letterGrade(score); // Act

        assertEquals(expected, grade); // Assert
    }

    // ---------- letterGrade: буруу оролт ----------

    @ParameterizedTest(name = "{0} оноо -> IllegalArgumentException")
    @ValueSource(doubles = {-1, 101, -0.01, 100.01, Double.NaN})
    @DisplayName("0-100 мужаас гадуурх оноонд IllegalArgumentException шидэх ёстой")
    void outOfRangeScoreThrows(double score) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(score));
    }

    // ---------- totalScore ----------

    @Test
    @DisplayName("Бүх хэсэгт дээд оноо авбал нийлбэр 100 байх ёстой")
    void maxComponentsSumToHundred() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        double total = calc.totalScore(10, 40, 10, 10, 30); // Act

        assertEquals(100.0, total, 1e-9); // Assert
    }

    @Test
    @DisplayName("Хязгаар доторх ердийн оноонуудын нийлбэр зөв гарах ёстой")
    void typicalComponentsSumCorrectly() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        double total = calc.totalScore(8, 32.5, 7, 9, 21); // Act

        assertEquals(77.5, total, 1e-9); // Assert
    }

    @ParameterizedTest(name = "att={0}, lab={1}, quiz1={2}, quiz2={3}, exam={4} -> {5}")
    @CsvSource({
        "10, 40, 10, 10, 30, 100",
        "0,  0,  0,  0,  0,  0",
        "8,  32.5, 7, 9, 21, 77.5",
        "5,  20, 5,  5,  15, 50",
        "10, 0,  0,  0,  0,  10",
        "0,  0,  0,  0,  30, 30"
    })
    @DisplayName("Хязгаар доторх оноонуудын нийлбэр зөв гарах ёстой (parameterized)")
    void totalScoreSumsCorrectly(double att, double lab, double quiz1, double quiz2, double exam,
                                 double expected) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        double total = calc.totalScore(att, lab, quiz1, quiz2, exam); // Act

        assertEquals(expected, total, 1e-9); // Assert
    }

    @Test
    @DisplayName("Ирцийн оноо сөрөг (-5) бол IllegalArgumentException шидэх ёстой")
    void negativeAttendanceThrows() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> calc.totalScore(-5, 40, 10, 10, 30));
    }

    @Test
    @DisplayName("Лабын оноо дээд хязгаараас хэтэрсэн (41) бол IllegalArgumentException шидэх ёстой")
    void labAboveMaxThrows() {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> calc.totalScore(10, 41, 10, 10, 30));
    }

    @ParameterizedTest(name = "att={0}, lab={1}, quiz1={2}, quiz2={3}, exam={4}")
    @CsvSource({
        "11, 40, 10, 10, 30",
        "10, -1, 10, 10, 30",
        "10, 40, 10.5, 10, 30",
        "10, 40, 10, -0.1, 30",
        "10, 40, 10, 10, 31",
        "10, 40, 10, 10, -3"
    })
    @DisplayName("Аль ч хэсгийн оноо мужаас гарвал IllegalArgumentException шидэх ёстой")
    void anyComponentOutOfRangeThrows(double att, double lab, double quiz1, double quiz2, double exam) {
        GradeCalculator calc = new GradeCalculator(); // Arrange

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> calc.totalScore(att, lab, quiz1, quiz2, exam));
    }
}
