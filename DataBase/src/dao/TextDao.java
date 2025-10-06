package dao;

import entity.Text;
import datasource.MariaDbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TextDao {

    public List<Text> getAllTexts() {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "SELECT id, model_name, original_text, prompt_text, output_text FROM texts";
        List<Text> texts = new ArrayList<Text>();

        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String modelName = rs.getString("model_name");
                String originalText = rs.getString("original_text");
                String promptText = rs.getString("prompt_text");
                String outputText = rs.getString("output_text");
                
                Text text = new Text(id, modelName, originalText, promptText, outputText);
                texts.add(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return texts;
    }

    public Text getText(int id) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "SELECT id, model_name, original_text, prompt_text, output_text FROM texts WHERE id=?";
        
        Text text = null;
        int count = 0;

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                count++;
                String modelName = rs.getString("model_name");
                String originalText = rs.getString("original_text");
                String promptText = rs.getString("prompt_text");
                String outputText = rs.getString("output_text");
                
                text = new Text(id, modelName, originalText, promptText, outputText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (count == 1) {
            return text;
        } else {
            return null;
        }
    }

    public void persist(Text text) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "INSERT INTO texts (model_name, original_text, prompt_text, output_text) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, text.getModelName());
            ps.setString(2, text.getOriginalText());
            ps.setString(3, text.getPromptText());
            ps.setString(4, text.getOutputText());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Text text) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "UPDATE texts SET model_name=?, original_text=?, prompt_text=?, output_text=? WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, text.getModelName());
            ps.setString(2, text.getOriginalText());
            ps.setString(3, text.getPromptText());
            ps.setString(4, text.getOutputText());
            ps.setInt(5, text.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        Connection conn = MariaDbConnection.getConnection();
        String sql = "DELETE FROM texts WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
