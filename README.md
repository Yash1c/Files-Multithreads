# 📝 Text File Character Counter

A Java program that counts character occurrences in `.txt` files from a directory using multithreading for optimal performance.

## � Features

- **Multithreaded processing** for efficient file reading
- Character frequency counting (A-Z, case insensitive)
- Execution time measurement
- Optimized for both small (100 files) and large (35,000 files) datasets
- Configurable thread count for optimal performance

## 🛠️ Implementation Details

### Core Logic

1. **File Discovery**: Scans directory for all `.txt` files
2. **Thread Management**:
   - Divides files evenly among threads
   - Each thread processes its assigned files
3. **Character Counting**:
   - Case-insensitive counting (A-Z)
   - Efficient character-to-index conversion
4. **Result Aggregation**:
   - Combines results from all threads
   - Displays final character counts

### Thread Optimization

The program automatically calculates the optimal number of threads based on available processors:
```java
private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
```

## 📊 Performance Considerations

- Tested with both sample (100 files) and production (35,000 files) datasets
- Thread count can be manually adjusted for specific hardware
- Execution time is displayed for performance benchmarking

## 🏁 Usage

1. Place `.txt` files in the specified directory
2. Run the program
3. View character counts and execution time in console

## 📦 Code Structure

```java
public class CharacterCounter {
    // Main components:
    // - File discovery and validation
    // - Thread creation and management
    // - Character counting logic
    // - Result aggregation and display
}
```

## ⏱️ Sample Output

```
Processing 35000 files with 8 threads...
Character counts:
A: 1,234,567
B: 987,654
...
Z: 12,345
Execution time: 4567 ms
```

## 🔧 Technical Notes

- Case-insensitive counting (A-Z treated the same as a-z)
- Non-alphabetic characters are ignored
- Efficient file handling with buffered reading

Optimized for performance on modern multi-core processors.