package org.katheer.dao;

import org.katheer.dto.Applicant;

public interface ApplicantDao {
    void createApplicant(Applicant applicant);
    Applicant getApplicant(int id);
}
