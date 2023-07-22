//Цей тест дозволяє перевірити,
// чи працює API для отримання курсу обміну валют
// та чи повертає воно очікувані дані про курси валют.

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestSpecialforeveryone {

    @Test
    void Test_usd_parameterized() {
        String[] currencies = {"USD", "EUR", "GBP"};

        for (String currency : currencies) {
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
}