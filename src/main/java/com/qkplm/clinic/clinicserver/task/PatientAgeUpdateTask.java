/*
* 版权声明 Copyright (c) 2026。
* 版权所有者： [九维无纸化病案管理系统]
* 首创日期： 2026年4月23日
*/
package com.qkplm.clinic.clinicserver.task;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qkplm.clinic.libcommon.utils.BQDateUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Wcke
 * @description
 *              <p>
 *              患者年龄定时更新任务：每日凌晨1点根据 birthDate 重新计算 firstAge/lastAge/ageType/age
 *              </p>
 * @datetime 2026-4-23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PatientAgeUpdateTask {

    private final JdbcTemplate jdbcTemplate;

    /**
     * 应用启动时执行一次
     */
    @PostConstruct
    public void init() {
        log.info("应用启动，执行患者年龄更新任务");
        // updatePatientAge();
    }

    /**
     * 每天凌晨1点执行，根据 birthDate 重新计算所有患者的年龄字段
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePatientAge() {
        log.info("开始执行患者年龄更新任务");
        try {
            String sql = "SELECT id, birthDate FROM bq_patient WHERE deleted = 0 AND birthDate IS NOT NULL";
            List<PatientAgeData> patients = jdbcTemplate.query(sql, new PatientAgeRowMapper());

            int total = patients.size();
            int success = 0;
            int failed = 0;

            for (PatientAgeData patient : patients) {
                try {
                    calculateAgeFields(patient);
                    jdbcTemplate.update(buildUpdateSql(patient));
                    success++;
                } catch (Exception e) {
                    log.error("更新患者年龄失败，id={}", patient.getId(), e);
                    failed++;
                }
            }

            log.info("患者年龄更新任务完成，总数={}, 成功={}, 失败={}", total, success, failed);
        } catch (Exception e) {
            log.error("患者年龄更新任务执行异常", e);
        }
    }

    /**
     * 根据 birthDate 计算 firstAge/lastAge/ageType/age。
     * 满1岁用"年/月"模式（ageType=1），不足1岁用"月/天"模式（ageType=2）。
     */
    private void calculateAgeFields(PatientAgeData patient) {
        LocalDate birthDate = patient.getBirthDate();
        if (birthDate == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        int years = period.getYears();

        if (years >= 1) {
            patient.setAgeType(1);
            patient.setFirstAge(years);
            patient.setLastAge(period.getMonths());
        } else {
            long totalMonths = ChronoUnit.MONTHS.between(birthDate, today);
            long remainDays = ChronoUnit.DAYS.between(birthDate.plusMonths(totalMonths), today);
            patient.setAgeType(2);
            patient.setFirstAge((int) totalMonths);
            patient.setLastAge((int) remainDays);
        }

        patient.setAge(BQDateUtils.formatAge(patient.getFirstAge(), patient.getLastAge(), patient.getAgeType()));
    }

    private String buildUpdateSql(PatientAgeData patient) {
        return String.format(
                "UPDATE bq_patient SET firstAge = %d, lastAge = %d, ageType = %d, age = '%s' WHERE id = %d",
                patient.getFirstAge(),
                patient.getLastAge(),
                patient.getAgeType(),
                patient.getAge(),
                patient.getId());
    }

    private static class PatientAgeData {
        private Long id;
        private LocalDate birthDate;
        private Integer ageType;
        private Integer firstAge;
        private Integer lastAge;
        private String age;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
        public Integer getAgeType() { return ageType; }
        public void setAgeType(Integer ageType) { this.ageType = ageType; }
        public Integer getFirstAge() { return firstAge; }
        public void setFirstAge(Integer firstAge) { this.firstAge = firstAge; }
        public Integer getLastAge() { return lastAge; }
        public void setLastAge(Integer lastAge) { this.lastAge = lastAge; }
        public String getAge() { return age; }
        public void setAge(String age) { this.age = age; }
    }

    private static class PatientAgeRowMapper implements RowMapper<PatientAgeData> {
        @Override
        public PatientAgeData mapRow(ResultSet rs, int rowNum) throws SQLException {
            PatientAgeData data = new PatientAgeData();
            data.setId(rs.getLong("id"));
            java.sql.Date sqlDate = rs.getDate("birthDate");
            if (sqlDate != null) {
                data.setBirthDate(sqlDate.toLocalDate());
            }
            return data;
        }
    }
}
