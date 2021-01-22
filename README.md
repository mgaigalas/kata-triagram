# Kata14
Exercise based on: [Kata14: Tom Swift Under the Milkwood](http://codekata.com/kata/kata14-tom-swift-under-the-milkwood/)
## Usage

```
mvn clean package && java -jar target/kata-triagram-1.0-SNAPSHOT-jar-with-dependencies.jar <input_file_path> <output_file_path>
```

## Notes
1. not Thread-Safe
2. at the moment discards punctuation
3. to run against Sonar (localhost, no security) with coverage report execute:
```
mvn clean verify -Dtest.coverage.skip=false sonar:sonar -Dsonar.host.url=http://localhost:9000
```