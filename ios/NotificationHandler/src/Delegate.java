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

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.Drive;

import java.io.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delegate {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Notification_Delegate";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new File("credentials/drive-notification-delegate");

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
     * at ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream("Assets/drive/client_secret.json");
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
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Drive service = getDriveService();
        String oldToken = null;


        //get new pageToken from old
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Assets/PageToken.txt"));
            oldToken = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String token;
        if(oldToken == null) {
            token = service.changes().getStartPageToken().execute().getStartPageToken();
        } else {
            token = oldToken;
        }

        //get the fileId of the file we care about
        String fileId = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Assets/FileId.txt"));
            fileId = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        while(true) {
            // Begin with our last saved start token for this user or the
            // current token from getStartPageToken()
            String pageToken = token;
            while (pageToken != null) {
                ChangeList changes = service.changes().list(pageToken)
                        .execute();
                for (Change change : changes.getChanges()) {
                    //notify if a change is in the relevant file
                    if(change.getFileId().equals(fileId)) {
                        System.out.println("Change found");
                        Notifier.update(fileId);
                    }
                }
                if (changes.getNewStartPageToken() != null) {
                    // Last page, save this token for the next polling interval
                    token = changes.getNewStartPageToken();
                }
                pageToken = changes.getNextPageToken();

            }
            if(!token.equals(oldToken)) {
                try {
                    PrintWriter pw = new PrintWriter(new FileWriter("Assets/PageToken.txt"));
                    pw.write(token);
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
