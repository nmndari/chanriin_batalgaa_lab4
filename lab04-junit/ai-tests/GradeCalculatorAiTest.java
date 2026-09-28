// AI-аар (Claude) үүсгэсэн тест.
// Ажлуулахдаа түр src/test/java фолдерт хийж байгаад ажлуулсан.
// Ажлуулсны дараа буцаан ai-tests фолдерт хийсэн. Ойлгомжтой байх үүднээс ai-tests фолдерт хийсэн.
// Үр дүнг results/mvn-test-ai.txt файлд хадгалсан.
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorAiTest {

    private GradeCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new GradeCalculator();
    }

    @Nested
    @DisplayName("letterGrade")
    class LetterGradeTests {

        @ParameterizedTest(name = "score {0} -> {1}")
        @CsvSource({
                "100, A",
                "95, A",
                "90, A",
                "89.99, B",
                "89, B",
                "85, B",
                "80, B",
                "79.99, C",
                "79, C",
                "75, C",
                "70, C",
                "69.99, D",
                "69, D",
                "65, D",
                "60, D",
                "59.99, F",
                "59, F",
                "30, F",
                "0.01, F",
                "0, F"
        })
        void returnsCorrectLetterForValidScores(double score, String expected) {
            assertEquals(expected, calc.letterGrade(score));
        }

        @ParameterizedTest(name = "invalid score {0}")
        @ValueSource(doubles = {-0.01, -1, -100, 100.01, 101, 1000,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
        void throwsForOutOfRangeScores(double score) {
            assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(score));
        }

        @Test
        void throwsForNaN() {
            assertThrows(IllegalArgumentException.class, () -> calc.letterGrade(Double.NaN));
        }

        @Test
        void exceptionMessageContainsScore() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> calc.letterGrade(150));
            assertTrue(ex.getMessage().contains("150"));
        }
    }

    @Nested
    @DisplayName("totalScore")
    class TotalScoreTests {

        @Test
        void sumsAllComponents() {
            assertEquals(85.0, calc.totalScore(10, 35, 8, 7, 25), 1e-9);
        }

        @Test
        void allZerosGivesZero() {
            assertEquals(0.0, calc.totalScore(0, 0, 0, 0, 0), 1e-9);
        }

        @Test
        void allMaxGivesHundred() {
            assertEquals(100.0, calc.totalScore(10, 40, 10, 10, 30), 1e-9);
        }

        @Test
        void handlesFractionalValues() {
            assertEquals(71.25, calc.totalScore(9.5, 30.25, 7.5, 6.0, 18.0), 1e-9);
        }

        @ParameterizedTest(name = "att={0}, lab={1}, q1={2}, q2={3}, exam={4}")
        @CsvSource({
                // attendance out of range
                "-1, 20, 5, 5, 15",
                "10.01, 20, 5, 5, 15",
                // lab out of range
                "5, -0.1, 5, 5, 15",
                "5, 40.01, 5, 5, 15",
                // quiz1 out of range
                "5, 20, -1, 5, 15",
                "5, 20, 11, 5, 15",
                // quiz2 out of range
                "5, 20, 5, -1, 15",
                "5, 20, 5, 10.5, 15",
                // exam out of range
                "5, 20, 5, 5, -1",
                "5, 20, 5, 5, 31"
        })
        void throwsWhenAnyComponentOutOfRange(double att, double lab, double q1, double q2, double exam) {
            assertThrows(IllegalArgumentException.class,
                    () -> calc.totalScore(att, lab, q1, q2, exam));
        }

        @Test
        void throwsWhenAnyComponentIsNaN() {
            assertAll(
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> calc.totalScore(Double.NaN, 20, 5, 5, 15)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> calc.totalScore(5, Double.NaN, 5, 5, 15)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> calc.totalScore(5, 20, Double.NaN, 5, 15)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> calc.totalScore(5, 20, 5, Double.NaN, 15)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> calc.totalScore(5, 20, 5, 5, Double.NaN))
            );
        }

        @Test
        void labAcceptsValueAboveOtherComponentMaxes() {
            // lab max is 40, so 35 must be accepted even though it's > 10 and > 30
            assertEquals(35.0, calc.totalScore(0, 35, 0, 0, 0), 1e-9);
        }

        @Test
        void examAcceptsValueUpToThirty() {
            assertEquals(30.0, calc.totalScore(0, 0, 0, 0, 30), 1e-9);
        }

        @Test
        void exceptionMessageNamesTheOffendingComponent() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> calc.totalScore(5, 20, 5, 5, 35));
            assertTrue(ex.getMessage().contains("Шалгалт"));
        }
    }

    @Test
    @DisplayName("totalScore and letterGrade integrate correctly")
    void totalScoreFeedsLetterGrade() {
        double total = calc.totalScore(10, 38, 9, 9, 28); // 94
        assertEquals("A", calc.letterGrade(total));

        total = calc.totalScore(5, 20, 5, 5, 15); // 50
        assertEquals("F", calc.letterGrade(total));
    }
}
