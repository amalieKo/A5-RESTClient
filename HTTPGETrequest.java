import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author Amalie Kolsgaard
 */
public class HTTPGETrequest {

    private String BASE_URL; // Base URL (address) of the server

    public static void main(String[]args)
    {
        HTTPGETrequest request = new HTTPGETrequest("104.248.47.74", 80);
        request.doHTTPGET();
    }

    /**
     * Create a HTTP GET
     *
     * @param host The host we connect to
     * @param port The port we conntect to
     */
    private HTTPGETrequest(String host, int port)
    {
        BASE_URL = "http://" + host + ":" + port + "/";
    }

    /**
     * Send an HTTP GET to a specific path on the web server
     */
    private void doHTTPGET()
    {
        // TODO: change path to something correct
        sendHTTP_GETrequest("dkrest/test/get2");
    }

    /**
     *  Send an HTTP GET request
     *  and read the response from the server
     *
     *  @param path Relative path in the API (path from doHTTPGET)
     */
    private void sendHTTP_GETrequest(String path)
    {
        try {
            String url = BASE_URL + path; //   http://104.248.47.74:80/dkrest/test/get
            URL urlObj = new URL(url);
            System.out.println("Sending HTTP GET to " + url);
            HttpURLConnection URLconnection = (HttpURLConnection) urlObj.openConnection();

            URLconnection.setRequestMethod("GET");

            int responseCode = URLconnection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server reached");
                // Response was OK, read the body (data)
                InputStream stream = URLconnection.getInputStream();
                String responseBody = convertStreamToString(stream);
                stream.close();
                System.out.println("Response from the server:");
                System.out.println(responseBody); //Svaret fra serveren som String
                JSONParsing(responseBody); //Sender stringen for JSONParsing
            } else {
                String responseDescription = URLconnection.getResponseMessage();
                System.out.println("Request failed, response code: " + responseCode + " (" + responseDescription + ")");
            }
        } catch (ProtocolException e) {
            System.out.println("Protocol not supported by the server");
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extracts the fields a, b and c from the server`s response
     */
    private void JSONParsing(String responseBody)
    {
        //Skriv ut på denne formen: { "b": 2, "a": 1, "c": 3 }
        System.out.println("--------------------------------------------------");
        System.out.println("JSON format");
        System.out.println("--------------------------------------------------");

        //Del opp responseBody Stringen
        //Extract the data from the JSON string
        String jsonObjectString = responseBody;
        System.out.println("Starting json string: " + responseBody);

        // Let's try to parse it as a JSON object
        try {
            JSONObject jsonObject = new JSONObject(jsonObjectString);
            if (jsonObject.has("b")) {
                String letterB = jsonObject.getString("b");
                System.out.println("The object contains field 'b' with value "
                        + letterB);
            }
            if (jsonObject.has("a"))
            {
                String letterA = jsonObject.getString("a");
                System.out.println("The object contains field 'a' with value "
                        + letterA);
            }
            if(jsonObject.has("c"))
            {
                String letterC = jsonObject.getString("c");
                System.out.println("The object contains field 'c' with value "
                        + letterC);
            }
            /*
            // We can also change some fields in the JSONObject
            jsonObject.put("year", 2018);

            // And if we want to translate a JSON object to a string, we simply
            // use toString() method
            System.out.println("The updated JSON object as a string: "
                    + jsonObject.toString());
            */

            // Now let's try to parse the JSON string as an array.
            // This should raise an exception, because the String does not
            // contain a JSON array
            JSONArray wrongArray = new JSONArray(jsonObjectString);

        } catch (JSONException e) {
            // It is important to always wrap JSON parsing in try/catch
            // If the string is suddently not in the expected format,
            // an exception will be generated
            System.out.println("Got exception in JSON parsing: " + e.getMessage());
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * Read the whole content from an InputStream, return it as a string
     * @param is The inputstream to read the body form
     * @return The whole body as a string
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append('\n');
            }
        } catch (IOException ex) {
            System.out.println("Could not read the data from HTTP response: " + ex.getMessage());
        }
        return response.toString();
    }

    public void sendHTTP_POSTrequest()
    {

    }

}
