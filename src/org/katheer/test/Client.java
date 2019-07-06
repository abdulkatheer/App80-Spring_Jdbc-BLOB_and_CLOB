package org.katheer.test;

import org.katheer.dao.ApplicantDao;
import org.katheer.dto.Applicant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class Client {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("org/katheer/resource" +
                "/applicationContext.xml");
        ApplicantDao applicantDao = (ApplicantDao) context.getBean("applicantDao");

        /*
        //Insert
        Applicant applicant1 = new Applicant();
        applicant1.setId(111);
        applicant1.setName("AAA");
        applicant1.setImage(new File("F:/image.jpg"));
        applicant1.setResume(new File("F:/resume.docx"));

        applicantDao.createApplicant(applicant1);
         */

        /*
        //Read
        Applicant applicant = applicantDao.getApplicant(111);
        System.out.println("ID      : " + applicant.getId());
        System.out.println("Name    : " + applicant.getName());
        System.out.println("Image   : " + applicant.getImage().getAbsolutePath());
        System.out.println("Resume  : " + applicant.getResume().getAbsolutePath());
         */
    }
}
