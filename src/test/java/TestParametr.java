import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.math.BigDecimal; // Доданий імпорт для BigDecimal
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class TestParametr {

    @ParameterizedTest
    @ValueSource(strings = {"USD", "EUR", "GBP"})
    void test_currency_exchange_rate(String currency) {
        try {
            URL url = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=" + currency + "&json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            assertEquals(HttpURLConnection.HTTP_OK, responseCode, "Помилка при виконанні запиту. Статус код: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            assertTrue(jsonArray.length() > 0, "Дані про валюту не знайдено");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currencyData = jsonArray.getJSONObject(i);
                assertNotNull(currencyData.getString("txt"), "Назва валюти не знайдена");

                // Приводимо значення rate до типу BigDecimal
                BigDecimal rateValue = new BigDecimal(currencyData.get("rate").toString());
                assertNotNull(rateValue);

                assertNotNull(currencyData.getString("cc"), "Код валюти не знайдений");

                System.out.println("Курс " + currencyData.getString("txt") + " (" + currencyData.getString("cc") + "): " + rateValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Виникла помилка при виконанні тесту.");
        }
    }
}