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
