package com.example.emailsenderjob.repository;

import com.example.emailsenderjob.model.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long>
{

}