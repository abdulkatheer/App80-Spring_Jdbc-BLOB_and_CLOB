package org.katheer.dao;

import org.katheer.dto.Applicant;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicantDaoImpl implements ApplicantDao {
    JdbcTemplate jdbcTemplate;
    LobHandler lobHandler;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    @Override
    public void createApplicant(Applicant applicant) {
        String query = "INSERT INTO applicants VALUES(?,?,?,?)";
        jdbcTemplate.execute(query, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                ps.setInt(1, applicant.getId());
                ps.setString(2, applicant.getName());
                FileInputStream image;
                FileReader resume;

                try {
                    image = new FileInputStream(applicant.getImage());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                try {
                    resume = new FileReader(applicant.getResume());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                lobCreator.setBlobAsBinaryStream(ps, 3, image, (int) applicant.getImage().length());
                lobCreator.setClobAsCharacterStream(ps, 4, resume,
                        (int) applicant.getResume().length());
            }
        });
    }

    @Override
    public Applicant getApplicant(int id) {
        String query = "SELECT * FROM applicants WHERE id = " + id;
        final Applicant[] applicant = new Applicant[1];
        jdbcTemplate.query(query, new AbstractLobStreamingResultSetExtractor() {
            @Override
            protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
                applicant[0] = new Applicant();
                applicant[0].setId(rs.getInt(1));
                applicant[0].setName(rs.getString(2));

                File image = new File("F:/image_out.jpg");
                File resume = new File("F:/resume_out.docx");

                FileOutputStream image_stream = new FileOutputStream(image);
                FileWriter resume_writer = new FileWriter(resume);

                FileCopyUtils.copy(lobHandler.getBlobAsBinaryStream(rs, 3), image_stream);
                FileCopyUtils.copy(lobHandler.getClobAsCharacterStream(rs, 4), resume_writer);

                applicant[0].setImage(image);
                applicant[0].setResume(resume);
            }
        });
        return applicant[0];
    }
}
