import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Notifier {
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Notifier";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File("Assets/", ".credentials/sheets.googleapis.com-java-TableData");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    // https://docs.google.com/spreadsheets/d/<spreadsheetId>/edit?usp=sharing
    private String spreadsheetId;

    public Notifier(String id) {
        // Build a new authorized API client service.
        spreadsheetId = id;
    }

    public static void update(String spreadsheetId) {
        Sheets service = null;
        try {
            service = getSheetsService();
        } catch(Exception e) {
            e.printStackTrace();
        }

        String date = null;
        int lineNum = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Assets/readMetadata.txt"));
            date = reader.readLine();
            String s = reader.readLine();
            if(s != null) lineNum = Integer.parseInt(s);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String actualDate = getDate(service, spreadsheetId);
        if(date == null || !date.equals(actualDate)) lineNum = 0;
        String output = "low";
        int maxPriority = 0;
        while (output.equals("low") || output.equals("Low") || output.equals("medium") || output.equals("Medium") || output.equals("high") || output.equals("High")) {
            output = getPriorityAtLine(lineNum, service, spreadsheetId);

            if(output.equals("low") || output.equals("Low")) {
                maxPriority = Math.max(maxPriority, 1);
            } else if(output.equals("medium") || output.equals("Medium")) {
                maxPriority = Math.max(maxPriority, 2);
            } else if(output.equals("high") || output.equals("High")) {
                maxPriority = Math.max(maxPriority, 3);
            }

            lineNum++;
        }

        //send notification of appropriate priority
        System.out.println("Notification priority: " + maxPriority);

        //store values for next chenck
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("Assets/readMetadata.txt"));
            pw.write(actualDate + "\n");
            pw.print(lineNum);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPriorityAtLine(int lineNum, Sheets service, String spreadsheetId) {
        String range = "Sheet1!A" + (lineNum + 3) + ":A" + (lineNum + 3);
        try {
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.size() == 0) {
                System.out.println("No data found.");
            } else {
                return values.get(0).get(0) + "";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDate(Sheets service, String spreadsheetId) {
        String range = "Sheet1!A1:A1";

        try {
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.size() == 0) {
                System.out.println("No data found.");
            } else {
                String date = values.get(0).get(0) + "";
                return date;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream("Assets/sheets/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}