// Import required classes from JavaMail and Java standard libraries
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import javax.mail.search.FlagTerm;
import java.util.Arrays; // Needed for displaying the 'from' array as a string

public class GetMail {

    public static void main(String[] args) {
        // Check if the minimum required arguments are provided
        if (args.length < 3) {
            System.out.println("Usage: java GetMail <server> <user> <password> [email_number]");
            return;
        }
        
        // Assign command-line arguments to variables
        String host = args[0];
        String user = args[1];
        String password = args[2];
        
        // Check if a specific email number is provided
        boolean fetchSingle = args.length == 4;
        int fetchIndex = fetchSingle ? Integer.parseInt(args[3]) - 1 : -1;
    
        try {
            // Set mail protocol to IMAPS (secure IMAP)
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
    
            // Create a mail session, no authentication handler needed
            Session session = Session.getDefaultInstance(props, null);
            // Get the mail store and connect using credentials
            Store store = session.getStore("imaps");
            store.connect(host, user, password);

            // Open the INBOX folder in read-only mode
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
    
            // Search for unread messages only
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            
             // If a specific email number was given, fetch and display that one
            if (fetchSingle) {
                if (fetchIndex >= 0 && fetchIndex < messages.length) {
                    Message message = messages[fetchIndex];
                    // Display subject and sender
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("From: " + Arrays.toString(message.getFrom()));
                    System.out.println("Content:\n");
    
                     // Display the content (only handles plain text)
                    Object content = message.getContent();
                    if (content instanceof String) {
                        System.out.println((String) content);
                    } else {
                        // For non-text content 
                        System.out.println("[Non-text content]");
                    }
                } else {
                    // If the given email number is out of range
                    System.out.println("Invalid email number.");
                }
            } else {
                // If no specific email number is given, list all unread messages
                for (int i = 0; i < messages.length; i++) {
                    Message message = messages[i];
                    // Print index, subject, and sender's email
                    System.out.println((i + 1) + ". " + message.getSubject() + " (" + message.getFrom()[0] + ")");
                }
            }
            
            // Close the inbox and disconnect from the store
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            // Catch and print any errors
            e.printStackTrace();
        }
    }
}    
