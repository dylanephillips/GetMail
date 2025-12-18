/**
 * title: GetMail.java
 * description: A Java program that connects to an IMAP email server and allows the user to
 *              either list unread emails or retrieve a specific unread email by number.
 * date: July 21, 2025
 * author: Dylan Phillips
 * copyright: 2025 Dylan Phillips
 */

/**
 * I declare that this assignment is my own work and that all material previously written or published
 * in any source by any other person has been duly acknowledged in the assignment.
 * I have not submitted this work, or a significant part thereof, previously as part of any academic program.
 * In submitting this assignment I give permission to copy it for assessment purposes only.
 *
 * <H1>GetMail Email Client</H1>
 *
 * <H3>Purpose and Description</H3>
 *
 * <P>
 * This program connects to a secure IMAP server (such as Gmail),
 * authenticates using user credentials provided via command-line arguments,
 * and then lists unread messages in the inbox.
 * The user may optionally retrieve the full content of one of the unread emails by providing
 * its number from the list.
 * </P>
 *
 * <P>
 * The program supports two modes:
 * </P>
 * <UL>
 *   <LI><b>List Mode:</b> Display all unread emails with their subject and sender.</LI>
 *   <LI><b>Read Mode:</b> Retrieve the content of one specific unread email using its number.</LI>
 * </UL>
 *
 * <H3>Classes</H3>
 *
 * <UL>
 *   <LI><b>GetMail</b>: Main class containing logic to connect to the email server,
 *       authenticate the user, search for unread messages, and display email data.</LI>
 * </UL>
 *
 * <H3>Key Methods</H3>
 *
 * <UL>
 *   <LI><b>public static void main(String[] args)</b> - Parses user credentials and command-line input,
 *       connects to the server, searches for unread emails, and either lists them or retrieves one.</LI>
 * </UL>
 *
 * <H3>Compiling and Running</H3>
 *
 * <DL>
 *   <DT>Step 1: Compile the program</DT>
 *   <DD><code>javac -cp .:../javax.mail.jar:../activation.jar GetMail.java</code></DD>
 *
 *   <DT>Step 2: Run the program to list unread emails</DT>
 *   <DD><code>java -cp .:../javax.mail.jar:../activation.jar GetMail imap.gmail.com your_email@gmail.com your_password</code></DD>
 *
 *   <DT>Step 3: Run the program to retrieve a specific unread email</DT>
 *   <DD><code>java -cp .:../javax.mail.jar:../activation.jar GetMail imap.gmail.com your_email@gmail.com your_password 3</code></DD>
 * </DL>
 *
 * <H3>Test Plan</H3>
 *
 * <P>
 * 1. Provide correct server, email, and password — should list unread emails.<br/>
 * 2. Provide correct server, email, password, and a valid email number — should print the email content.<br/>
 * 3. Provide a number outside of the range — should display \"Invalid email number.\"<br/>
 * 4. Provide fewer than 3 arguments — should show usage instructions.<br/>
 * 5. Provide invalid credentials — should catch and display authentication error.<br/>
 * </P>
 */

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