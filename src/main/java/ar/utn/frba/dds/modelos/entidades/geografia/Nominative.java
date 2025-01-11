package ar.utn.frba.dds.modelos.entidades.geografia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class Nominative {

    public static Map<String, String> obtenerCoordenadas(String direccion, String altura, String ciudad) {
        String direccionCompleta = direccion + " " + altura + ", " + ciudad;
        String url = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(direccionCompleta, StandardCharsets.UTF_8) + "&format=json&addressdetails=1";
        Map<String, String> coordenadas = new HashMap<>();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.lines().collect(Collectors.joining());
                reader.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response);
                if (rootNode.isArray() && rootNode.size() > 0) {
                    JsonNode firstResult = rootNode.get(0);

                    String latitudSinFormato = firstResult.get("lat").asText();
                    /*String latitudConFormato = latitudSinFormato.replaceAll(",", "."); // Replace all commas with dots
                    coordenadas.put("lat", latitudConFormato);*/
                    String latitudConFormato = String.format(Locale.US, "%.7f", Double.parseDouble(latitudSinFormato.replace(",", ".")));
                    coordenadas.put("lat", latitudConFormato);


                    String longitudSinFormato = firstResult.get("lon").asText();
                    String longitudConFormato = String.format(Locale.US, "%.6f", Double.parseDouble(longitudSinFormato.replace(",", ".")));
                    coordenadas.put("lon", longitudConFormato);
                    /*String longitudConFormato = longitudSinFormato.replaceAll(",", "."); // Replace all commas with dots
                    coordenadas.put("lon", longitudConFormato);*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return coordenadas;
    }

    public static Map<String, String> obtenerDireccion(String latParam, String lonParam) {
        Double lat = Double.valueOf(latParam);
        Double lon = Double.valueOf(lonParam);
        Map<String, String> direccion = new HashMap<>();
        try {
            String urlString = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f", lat, lon);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject address = jsonObject.getJSONObject("address");

                direccion.put("calle", address.optString("road"));
                direccion.put("altura", address.optString("house_number"));
                direccion.put("ciudad", address.optString("city"));
                direccion.put("provincia", address.optString("state"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return direccion;
    }
}
