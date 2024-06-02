package com.example.emailsenderjob.controller;

import com.example.emailsenderjob.model.Mail;
import com.example.emailsenderjob.model.PuneHrMail;
import com.example.emailsenderjob.repository.MailRepository;
import com.example.emailsenderjob.repository.PuneHrMailRepository;
import com.example.emailsenderjob.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//This is Second Copy
@RestController
@RequestMapping("/api")
public class MailController {

    @Autowired
    private MailService mailService;
    @Autowired
    private MailRepository mailRepository;


    @Autowired
    private PuneHrMailRepository puneHrMailRepository;

    private List<Mail> all = new ArrayList<>();

    String resumePath = "C://Users//Satyam//Downloads//satyamkr%20(1).pdf";
    String subject = "Inquiry Regarding Java Developer Positions";


    String filePath =
           "C:\\Users\\Satyam\\Downloads\\SampleResumes_Tips_Startups\\Startups list and how to apply\\Start Up companies list City wise\\Bangalore.txt";


    @PostMapping("extract")
    public void extractAndStoreIntoArray() throws Exception {
        extractCompaniesAndEmails(filePath);
    }

    public void extractCompaniesAndEmails(String filePath) {
        // Regular expression pattern for matching email addresses
        Pattern emailPattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        // Regular expression pattern for matching company names
        Pattern companyNamePattern = Pattern.compile("\\b\\d+\\.?\\s*([^\\(\\)]+)(?=\\s*\\(.*\\))?\\b");

        int count = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String companyName = "";
            String email = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("@")) {
                    // Extract email address
                    Matcher matcher = emailPattern.matcher(line);
                    if (matcher.find()) {
                        email = matcher.group();
                    }
                    System.out.println("Company No :" + count);

                    // Print company name
                    System.out.println("CompanyName :" + companyName);
                    // Print email
                    System.out.println("Email : " + email);
                    System.out.println(); // Empty line for separation
                    count++;
                } else {
                    // Extract company name and ignore text within parentheses
                    Matcher matcher = companyNamePattern.matcher(line.trim());
                    if (matcher.find()) {
                        companyName = matcher.group(1).trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.asList(all));
    }


    @PostMapping("/send")
    public ResponseEntity<?> sendEmailsToCompany() throws MessagingException, IOException {
        int count = 1;
        for (Mail mail : all) {
            String message =
                    "Dear Hiring Team,\n" +
                            "\n" +
                            "I hope this email finds you well. I'm reaching out to express my interest in potential Java developer positions at '" + mail.getCompanyName() + "'. With extensive experience in Java development, specializing in Spring Boot, Spring Data JPA, and microservices architecture, I am eager to contribute to your team.\n" +
                            "\n" +
                            "Attached is my resume for your review. If there are any openings that align with my skills and experience, I would greatly appreciate your consideration.\n" +
                            "\n" +
                            "Thank you for your time and attention. I look forward to the possibility of discussing how I can contribute '" + mail.getCompanyName() + "'.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "Satyam kumar\n" +
                            "mobile:- 6376784581\n" +
                            "\n";



            mailService.sendEmailWithAttachment(mail.getEmail(), subject, message, resumePath);

            System.out.println("No" + count++ + " email send to : " + mail.getCompanyName() + " " + mail.getEmail());
            System.out.println(" ");


        }

        return ResponseEntity.status(HttpStatus.OK).body("send email completed");
    }

    @PostMapping("/storetodb")
    public ResponseEntity storeToDatabase() {

        try {
            String content = readFile(filePath);
            ArrayList<String> emails = extractEmails(content);
            System.out.println("Extracted Email IDs:");
            for (String email : emails) {
                System.out.println(email);
                PuneHrMail puneHrMail = new PuneHrMail();
                puneHrMail.setEmail(email);
                puneHrMailRepository.save(puneHrMail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Saved!", HttpStatus.OK);
    }

    public static ArrayList<String> extractEmails(String text) {
        Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> emailList = new ArrayList<>();
        while (matcher.find()) {
            emailList.add(matcher.group());
        }
        return emailList;
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }


    @PostMapping("/mailfromdb")
    public ResponseEntity<?> senEmailFromDb500() throws MessagingException, IOException {

        String messagebody="Hello Sir/Madam,\n" +
                "Good Day !!!!\n" +
                " \n"+
                "\n"+
                "I need referral for the drive if possible please refer me for the same as I don't want to miss this opportunity. It will really helpful of you can refer me will wait for your positive response.\n" +
                "\n"+
                "Please find my details below and updated resume is attached. Please do the needful and let me know if anything else is required.\n" +
                "\n"+
                "Name : Nagesh Mathpati\n" +
                "Branch : Information Technology\n" +
                "Year of passout : 2023\n" +
                "Skills : Java Full Stack Developer\n" +
                "Mobile number : 9022811913,9022589409 \n" +
                "Email address : nageshmathpati31@gmail.com \n" +
                "Date of birth : 13/06/2002\n" +
                "\n"+
                "\n"+
                "Thanks & Regards ,\n" +
                "Name : Nagesh Mathpati\n" +
                "Mobile number: 9022811913,9022589409 \n" +
                "Email address: nageshmathpati31@gmail.com";

        List<PuneHrMail> all1 = puneHrMailRepository.findAll();
        int count=1;
        for(PuneHrMail p:all1)
        {
            if(p.getId()<1800)
            {
                continue;
            }
            if(p.getId()>2500)
            {
                break;
            }
            else
            {
                System.out.println(count++ + "email id in db "+ p.getId()+ "    "  + p.getEmail());
                mailService.sendEmailWithAttachment(p.getEmail(),subject,messagebody,resumePath);
            }
        }
        return new ResponseEntity<>("stoped becaues of limit exceed",HttpStatus.OK);
    }
}


/*


Great, thanks for providing your details. Based on the information you've provided, here's a concise email template you can use to inquire about job opportunities:

Subject: Inquiry Regarding Java Developer Positions

Dear Hiring Team,

I hope this email finds you well. I'm reaching out to express my interest in potential Java developer positions at [Company Name]. With extensive experience in Java development, specializing in Spring Boot, Spring Data JPA, and microservices architecture, I am eager to contribute to your team.

Attached is my resume for your review. If there are any openings that align with my skills and experience, I would greatly appreciate your consideration.

Thank you for your time and attention. I look forward to the possibility of discussing how I can contribute to [Company Name].

Best regards,
satym kumar
[Your Contact Information]


 */