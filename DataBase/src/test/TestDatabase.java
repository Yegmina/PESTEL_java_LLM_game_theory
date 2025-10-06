package test;

import dao.TextDao;
import entity.Text;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

public class TestDatabase {

    private static final String MODEL_NAME = "test:8b";
    private static final Random random = new Random();
    private static final String[] LOREM_IPSUM_WORDS = {
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", 
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor", 
        "incididunt", "ut", "labore", "et", "dolore", "magna", 
        "aliqua", "enim", "minim", "veniam", "quis", "nostrud", 
        "exercitation", "ullamco", "laboris", "nisi", "aliquip", 
        "ex", "ea", "commodo", "consequat"
    };

    public static void main(String[] args) {
        System.out.println("=== Starting Database Test ===\n");

        TextDao textDao = new TextDao();

        // Test 1: Insert some random texts
        System.out.println("1. Inserting random texts...");
        testInsertTexts(textDao);

        // Test 2: Retrieve and display all texts
        System.out.println("\n2. Retrieving all texts from database...");
        testGetAllTexts(textDao);

        // Test 3: Get a specific text by ID (get the first one)
        System.out.println("\n3. Testing retrieval of single text...");
        testGetSingleText(textDao);

        System.out.println("\n=== Database Test Completed ===");
    }

    private static void testInsertTexts(TextDao textDao) {
        // Insert 3-5 random texts
        int numberOfTexts = 3 + random.nextInt(3); // 3 to 5 texts
        
        for (int i = 1; i <= numberOfTexts; i++) {
            String originalText = generateRandomText(10, 20);
            String promptText = generateRandomText(5, 10);
            String outputText = generateRandomText(15, 25);
            
            Text text = new Text(MODEL_NAME, originalText, promptText, outputText);
            textDao.persist(text);
            
            System.out.println("   Inserted text " + i + ":");
            System.out.println("     Model: " + MODEL_NAME);
            System.out.println("     Original: " + truncateText(originalText, 50));
            System.out.println("     Prompt: " + truncateText(promptText, 30));
            System.out.println("     Output: " + truncateText(outputText, 50));
        }
        
        System.out.println("   ✓ Successfully inserted " + numberOfTexts + " texts");
    }

    private static void testGetAllTexts(TextDao textDao) {
        List<Text> allTexts = textDao.getAllTexts();
        
        if (allTexts.isEmpty()) {
            System.out.println("   No texts found in database.");
            return;
        }
        
        System.out.println("   Found " + allTexts.size() + " texts in database:");
        System.out.println("   " + "-".repeat(100));
        
        for (Text text : allTexts) {
            System.out.println("   ID: " + text.getId());
            System.out.println("   Model: " + text.getModelName());
            System.out.println("   Original: " + truncateText(text.getOriginalText(), 60));
            System.out.println("   Prompt: " + truncateText(text.getPromptText(), 40));
            System.out.println("   Output: " + truncateText(text.getOutputText(), 60));
            System.out.println("   " + "-".repeat(100));
        }
    }

    private static void testGetSingleText(TextDao textDao) {
        List<Text> allTexts = textDao.getAllTexts();
        
        if (allTexts.isEmpty()) {
            System.out.println("   No texts available to test single retrieval.");
            return;
        }
        
        // Get the first text's ID
        int firstId = allTexts.get(0).getId();
        Text text = textDao.getText(firstId);
        
        if (text != null) {
            System.out.println("   ✓ Successfully retrieved text with ID " + firstId);
            System.out.println("     Model: " + text.getModelName());
            System.out.println("     Original: " + truncateText(text.getOriginalText(), 40));
        } else {
            System.out.println("   ✗ Failed to retrieve text with ID " + firstId);
        }
    }

    private static String generateRandomText(int minWords, int maxWords) {
        int wordCount = minWords + random.nextInt(maxWords - minWords + 1);
        StringBuilder text = new StringBuilder();
        
        for (int i = 0; i < wordCount; i++) {
            if (i > 0) {
                text.append(" ");
            }
            String word = LOREM_IPSUM_WORDS[random.nextInt(LOREM_IPSUM_WORDS.length)];
            // Capitalize first word
            if (i == 0) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
            text.append(word);
        }
        
        // Add punctuation
        text.append(random.nextBoolean() ? "." : "!");
        
        return text.toString();
    }

    private static String truncateText(String text, int maxLength) {
        if (text == null) {
            return "null";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
