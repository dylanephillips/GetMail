# GetMail – Java IMAP Email Client

## Overview
GetMail is a Java command-line application that connects to a secure IMAP email server (such as Gmail).  
It allows users to list unread emails in their inbox or retrieve the full content of a specific unread message by number.

---

## Features
- Secure IMAP (IMAPS) email connection
- Lists unread emails with sender and subject
- Retrieves the content of a selected unread email
- Handles invalid input and authentication errors

---

## Program Modes
- **List Mode** – Displays all unread emails with their subject and sender
- **Read Mode** – Retrieves the content of one specific unread email using its number

---

## Classes
- **GetMail** – Main class that handles server connection, authentication, message searching, and output

---

## Key Method
- **`public static void main(String[] args)`**  
  Parses command-line arguments, connects to the IMAP server, searches unread emails, and either lists them or retrieves a selected message.

---

## Requirements
- Java JDK 8 or higher
- JavaMail API (`javax.mail.jar`)
- Java Activation Framework (`activation.jar`)
- IMAP-enabled email account

---

## Compiling
```bash
javac -cp .:../javax.mail.jar:../activation.jar GetMail.java
Running
List unread emails
bash
Copy code
java -cp .:../javax.mail.jar:../activation.jar GetMail imap.gmail.com your_email@gmail.com your_password
Retrieve a specific unread email
bash
Copy code
java -cp .:../javax.mail.jar:../activation.jar GetMail imap.gmail.com your_email@gmail.com your_password 3
Test Plan
Provide valid server, email, and password — unread emails are listed

Provide valid credentials and a valid email number — email content is displayed

Provide an invalid email number — displays “Invalid email number.”

Provide fewer than 3 arguments — usage instructions are shown

Provide invalid credentials — authentication error is handled gracefully

Notes
Use an app-specific password when connecting to Gmail.

The application currently supports plain text email content only.

Author
Dylan Phillips
July 21, 2025
