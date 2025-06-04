import java.io.File; // For file handling
import java.io.IOException; // For handling IO exceptions
import java.nio.charset.StandardCharsets; // For UTF-8 encoding
import java.nio.file.Files; // For reading file contents
import java.util.ArrayList; // For dynamic lists
import java.util.Arrays; // For array utilities
import java.util.Collections; // For empty immutable collections
import java.util.HashMap; // For character counting
import java.util.List; // For lists
import java.util.Map; // For key-value data structures
import java.util.concurrent.Callable; // For concurrent tasks
import java.util.concurrent.ExecutionException; // For handling task errors
import java.util.concurrent.ExecutorService; // For managing threads
import java.util.concurrent.Executors; // For creating thread pools
import java.util.concurrent.Future; // For asynchronous task results

public class Main {
    
    // Class to store the result and statistics
    private static class ProcessingResult {
        Map<Character, Integer> charCount; // Letter count
        int processedFiles; // Number of files processed
        int errorFiles; // Number of files with errors

        // Constructor for ProcessingResult
        public ProcessingResult(Map<Character, Integer> charCount, int processedFiles, int errorFiles) {
            this.charCount = charCount;
            this.processedFiles = processedFiles;
            this.errorFiles = errorFiles;
        }
    }

    // Here is the original code that was commented out, which shows the files in the "All" directory.

//     public static void Show() {
//         File dir = new File("All");
//         System.out.println("Files in directory: " + dir.getAbsolutePath());
        
//         if (!dir.exists()) {
//             System.out.println("Directory does not exist.");
//             return;
//         }
        
//         File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
//         if (files == null || files.length == 0) {
//             System.out.println("No .txt files found. Is the ZIP extracted?");
//         } else {
//             for (File file : files) {
//                 System.out.println(file.getName());
//             }
//         }
//     }
// }

    public static void main(String[] args) {
        System.out.println("-------------------------------------------------------");
        System.out.println("Current dir: " + System.getProperty("user.dir")); // Show current working directory
        System.out.println("-------------------------------------------------------");

        File dir = new File("All"); // Directory containing files
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt")); // Filter .txt files
        
        if (files == null || files.length == 0) { // Check if there are files
            System.out.println("No .txt files found. Is the ZIP extracted?");
            return; // Exit if no files
        }

        System.out.println("Number of .txt files to process: " + files.length);

        int numThreads = Runtime.getRuntime().availableProcessors(); // Get available CPU cores
        System.out.println("Using " + numThreads + " threads");
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads); // Create thread pool
        List<Future<ProcessingResult>> futures = new ArrayList<>(); // List to store task results

        long start = System.currentTimeMillis(); // Start timer

        // Process files in batches
        int batchSize = (int) Math.ceil((double) files.length / numThreads); // Calculate batch size
        for (int i = 0; i < files.length; i += batchSize) { // Loop through batches
            int end = Math.min(i + batchSize, files.length); // End index for batch
            File[] batch = Arrays.copyOfRange(files, i, end); // Create batch
            futures.add(executor.submit(() -> processFilesBatch(Arrays.asList(batch)))); // Submit batch for processing
        }

        // Consolidate results
        Map<Character, Integer> totalCount = new HashMap<>(); // Final letter count
        int totalProcessed = 0; // Total successfully processed files
        int totalErrors = 0; // Total error files
        
        for (Future<ProcessingResult> future : futures) { // For each batch result
            try {
                ProcessingResult result = future.get(); // Get result
                result.charCount.forEach((k, v) -> totalCount.merge(k, v, Integer::sum)); // Merge counts
                totalProcessed += result.processedFiles; // Update processed count
                totalErrors += result.errorFiles; // Update error count
            } catch (InterruptedException | ExecutionException e) { // Handle batch errors
                System.err.println("Error processing batch: " + e.getMessage());
                totalErrors += batchSize; // Count entire batch as error
            }
        }

        long end = System.currentTimeMillis(); // End timer
        executor.shutdown(); // Shut down thread pool

        System.out.println("\nProcessing summary:");
        System.out.println("Total files: " + files.length);
        System.out.println("Successfully processed: " + totalProcessed);
        System.out.println("Files with errors: " + totalErrors);
        System.out.println("Time elapsed: " + (end - start) + " ms\n");

        System.out.println("Contagem consolidada de letras:");
        for (char c = 'A'; c <= 'Z'; c++) { // Loop A-Z
            System.out.printf("%c: %d\n", c, totalCount.getOrDefault(c, 0)); // Print count or 0
        }
    }

    // Process a batch of files and return results
    private static ProcessingResult processFilesBatch(List<File> files) {
        Map<Character, Integer> localCount = new HashMap<>(); // Local letter count
        int processed = 0; // Number of files processed successfully
        int errors = 0; // Number of files with errors
        
        for (File file : files) { // For each file
            Map<Character, Integer> fileCount = countCharsInFile(file); // Count letters in file
            if (!fileCount.isEmpty()) { // If successful
                fileCount.forEach((key, value) -> localCount.merge(key, value, Integer::sum)); // Merge counts
                processed++; // Increment processed
            } else {
                errors++; // Increment errors
            }
        }
        return new ProcessingResult(localCount, processed, errors); // Return result
    }

    // Count letters A-Z in a single file
    public static Map<Character, Integer> countCharsInFile(File file) {
        Map<Character, Integer> charCount = new HashMap<>(); // Letter count map
        try {
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8) // Read file content
                           .toUpperCase(); // Convert to uppercase
            
            for (char c : content.toCharArray()) { // For each character
                if (c >= 'A' && c <= 'Z') { // If letter A-Z
                    charCount.put(c, charCount.getOrDefault(c, 0) + 1); // Increment count
                }
            }
            return charCount; // Return count
        } catch (IOException e) { // Handle read errors
            System.err.println("Error reading file: " + file.getName());
            return Collections.emptyMap(); // Return empty result
        }
    }
}
