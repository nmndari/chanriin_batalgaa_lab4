# Лаборатори 4 — JUnit

- **Оюутны нэр:** Б. Намуундарь
- **Оюутны код:** B232270037

## Орчин

### `java -version`

```
openjdk version "17.0.17" 2025-10-21
OpenJDK Runtime Environment Homebrew (build 17.0.17+0)
OpenJDK 64-Bit Server VM Homebrew (build 17.0.17+0, mixed mode, sharing)
```

### `mvn -version`

```
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 17.0.17, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk@17/17.0.17/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "15.6", arch: "aarch64", family: "mac"
```

## GradeCalculator ба тестүүд

- Код: [`lab04-junit/src/main/java/GradeCalculator.java`](lab04-junit/src/main/java/GradeCalculator.java)
- Тест: [`lab04-junit/src/test/java/GradeCalculatorTest.java`](lab04-junit/src/test/java/GradeCalculatorTest.java)
- Үр дүн: [`lab04-junit/results/mvn-test.txt`](lab04-junit/results/mvn-test.txt)

### Ажиллуулах комманд
```bash
cd lab04-junit
mkdir -p results && mvn test 2>&1 | tee results/mvn-test.txt
```

### Үр дүн
```
Tests run: 40, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Тестийн метод **14** байгаа. Surefire нь `@CsvSource` / `@ValueSource`-ийн мөр бүрийг тусдаа тест гэж тоолдог тул 40 гэж гарч байна. Тест бүр Arrange–Act–Assert бүтэцтэй бөгөөд `@DisplayName`-ээр монгол нэртэй.

| Бүлэг | Шалгасан утгууд |
|---|---|
| Ердийн утга | 95→A, 85→B, 75→C, 65→D, 30→F |
| Хязгаарын утга | 90→A, 89.99→B, 60→D, 59.99→F, 0→F, 100→A |
| `letterGrade` буруу оролт (`assertThrows`) | -1, 101, -0.01, 100.01, NaN |
| `totalScore` зөв нийлбэр | (10, 40, 10, 10, 30)→100, (8, 32.5, 7, 9, 21)→77.5 |
| `totalScore` буруу оролт (`assertThrows`) | att = -5, lab = 41, мөн хэсэг бүрийн сөрөг болон дээд хязгаараас хэтэрсэн утга |

## Мутаци (санаатай унагаах)

`GradeCalculator.letterGrade` доторх `score >= 90` нөхцөлийг зориуд `score > 90` болгож ажиллуулсан:

```bash
mvn test 2>&1 | tee results/mvn-test-mutant.txt
```

Үр дүн ([`lab04-junit/results/mvn-test-mutant.txt`](lab04-junit/results/mvn-test-mutant.txt)):
```
Tests run: 40, Failures: 2, Errors: 0, Skipped: 0
BUILD FAILURE
```

Унасан тестүүд:

| Тест | Мэдээлэл |
|---|---|
| `ninetyIsExactlyA` — "90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)" | `expected: <A> but was: <B>` |
| `letterGradeBoundaries[2]` — `90 оноо -> A` мөр | `expected: <A> but was: <B>` |

90 оноо яг хязгаар дээр байгаа тул `>` болгоход A биш B буцаасан бөгөөд хязгаарын утгыг шалгадаг тестүүд үүнийг илрүүлсэн. 95, 100 зэрэг 90-ээс дээш утгатай тестүүд pass хэвээр байсан нь зөвхөн ердийн утгаар тестлэх нь хангалтгүйг харуулж байна.

Дараа нь `>= 90`-ийг буцааж засаад `mvn test`-ийг дахин ажиллууллсан. (`Tests run: 40, Failures: 0`, `BUILD SUCCESS`).

## Дүгнэлт

`GradeCalculatorTest` класст нийт **14** тестийн метод (`@Test` болон `@ParameterizedTest`) бичсэн бөгөөд `results/mvn-test.txt`-д `Tests run: 40, Failures: 0, Errors: 0, Skipped: 0` гэж гарсан, учир нь Surefire `@CsvSource` / `@ValueSource`-ийн мөр бүрийг тусдаа тест гэж тоолдог. Мутацийн үед `letterGrade` доторх `score >= 90`-ийг `score > 90` болгоход `ninetyIsExactlyA` болон `letterGradeBoundaries`-ийн `90 -> A` мөр гэсэн хоёр тест `expected: <A> but was: <B>` мэдээлэлтэй унаж, `BUILD FAILURE` гарсан. Үүний дараа нөхцөлийг `>= 90` болгож буцаан засаад `mvn test`-ийг дахин ажиллуулж, бүх тест ногоон болсныг `results/mvn-test.txt`-д хадгалсан. Хамгийн сонирхолтой нь мутацийн үед 40 тестээс зөвхөн **2** нь унасан явдал: 95→A, 100→A тестүүд pass хэвээр байсан, учир нь `>` ба `>=` зөвхөн яг 90 дээр л ялгаатай. Хэрэв би 90 оноог тусад нь тестлээгүй бол энэ алдаа огт илрэхгүй байсан.

## Нэмэлт даалгавар - AI-ийн тесттэй харьцуулалт

- AI тест: [`lab04-junit/ai-tests/GradeCalculatorAiTest.java`](lab04-junit/ai-tests/GradeCalculatorAiTest.java)
- Харьцуулалт: [`lab04-junit/ai-tests/mutation-comparison.txt`](lab04-junit/ai-tests/mutation-comparison.txt)
- AI тестийн үр дүн: [`lab04-junit/results/mvn-test-ai.txt`](lab04-junit/results/mvn-test-ai.txt) — `Tests run: 49, Failures: 0, Errors: 0, Skipped: 0`, `BUILD SUCCESS`

(1) Хоёр тестийн suite-ийг ижил 12 мутацид ажиллуулахад AI миний мартсан totalScore дахь NaN шалгалтыг санаж, мөн 79.99, 69.99, ±Infinity, 0.01 болон exception message-ийг зөв шалгасан.
(2) AI шаардлагатай 90, 89.99, 60, 59.99, 0, 100 гэсэн хязгаарын утгуудыг бүгдийг тестэлсэн. Гэхдээ `totalScoreFeedsLetterGrade` тест нь 94 ба 50 гэсэн утга ашигласан тул яг 90 болох тохиолдлыг шалгаж чадаагүй. Мөн AI зөвхөн 2 `@DisplayName` ашигласан бөгөөд хичээлийн шаардлага болох Arrange–Act–Assert бүтэц огт байхгүй. (3) Хамгийн чанартай тест нь `throwsWhenAnyComponentIsNaN` болон хязгаарын утгуудыг шалгасан хүснэгт байсан. Харин зарим тестүүд allMaxGivesHundred-ийн шалгасныг давтаж, шинэ мутаци илрүүлээгүй. — мөр олон байсан ч мутаци бүрийг contains("150") зэрэг message-ийн тестүүд нь мессеж бага зэрэг өөрчлөгдөхөд эмзэг байна. Ерөнхийдөө AI нь олон төрлийн оролт санал болгоход тустай ч давхардсан тест бичих хандлагатай тул үүсгэсэн тестүүдийг шүүж, хэрэгтэй хэсгийг өөрийн тестэд ашиглах нь зөв.