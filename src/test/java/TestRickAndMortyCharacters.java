import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestRickAndMortyCharacters {

    private String apiUrl;

    @BeforeEach
    public void setUp() {
        apiUrl = "https://rickandmortyapi.com/api/episode";
    }

    @Test
    public void testGetRickAndMortyCharacters() {
        List<String> characterInfo = new ArrayList<>(); // Список для збереження інформації про персонажів
        int totalCharacters = 0; // Змінна для збереження загальної кількості персонажів

        try {
            while (apiUrl != null) {
                if (apiUrl.isEmpty()) {
                    break; // Виходимо з циклу, якщо поле "next" порожнє
                }

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                assertEquals(HttpURLConnection.HTTP_OK, responseCode, "Помилка при виконанні запиту. Статус код: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (!jsonResponse.has("info") || jsonResponse.isNull("info") || jsonResponse.getJSONObject("info").isNull("next")) {
                    apiUrl = null; // Виходимо з циклу, якщо поле "next" не існує або має значення null
                } else {
                    apiUrl = jsonResponse.getJSONObject("info").getString("next");
                }

                JSONArray charactersArray = jsonResponse.getJSONArray("results");

                for (int i = 0; i < charactersArray.length(); i++) {
                    JSONObject character = charactersArray.getJSONObject(i);
                    String name = character.getString("name");
                    String airDate = character.getString("air_date");
                    String characterInfoString = String.format("\"name\": \"%s\",\n\"air_date\": \"%s\"", name, airDate);
                    characterInfo.add(characterInfoString); // Додаємо інформацію про персонажа до списку
                }

                totalCharacters += charactersArray.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Виникла помилка при виконанні запиту до API.");
        }

        // Після закінчення тестування виводимо інформацію про всі серії
        System.out.println("Усього знайдено серій: " + totalCharacters);
        System.out.println("Інформація про серії:");
        for (String info : characterInfo) {
            System.out.println(info);
        }
    }
}