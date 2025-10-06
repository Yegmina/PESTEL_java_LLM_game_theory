-- Switch to your personal schema
USE eiakim;

-- Create the table
CREATE TABLE texts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    original_text TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    prompt_text   TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    output_text   TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

