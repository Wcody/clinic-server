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
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Wcke
 * @description
 *              <p>
 *              患者年龄定时更新任务
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
     * 每天凌晨1点执行，更新所有患者的age字段
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePatientAge() {
        log.info("开始执行患者年龄更新任务");
        try {
            String sql = "SELECT id, ageType, firstAge, lastAge, createdTime FROM bq_patient WHERE deleted = 0";
            List<PatientAgeData> patients = jdbcTemplate.query(sql, new PatientAgeRowMapper());

            int total = patients.size();
            int success = 0;
            int failed = 0;

            for (PatientAgeData patient : patients) {
                try {
                    // 计算需要更新的值
                    calculateAgeFields(patient);

                    // 生成并执行更新SQL
                    String updateSql = buildUpdateSql(patient);
                    jdbcTemplate.update(updateSql);
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
     * 计算年龄相关字段
     */
    private void calculateAgeFields(PatientAgeData patient) {
        LocalDateTime createdTime = patient.getCreatedTime();
        if (createdTime == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate createdDate = createdTime.toLocalDate();
        Integer ageType = patient.getAgeType();

        if (ageType == null) {
            return;
        }

        if (ageType == 1) {
            // 年龄类型为"年"
            Period period = Period.between(createdDate, today);
            int years = period.getYears();
            int months = period.getMonths();

            patient.setFirstAgePlus(years);
            patient.setLastAgePlus(months);

            // 计算当前年龄
            int currentFirstAge = (patient.getFirstAge() != null ? patient.getFirstAge() : 0) + years;
            int currentLastAge = (patient.getLastAge() != null ? patient.getLastAge() : 0) + months;

            // 月数超过12转为年
            if (currentLastAge >= 12) {
                currentFirstAge += currentLastAge / 12;
                currentLastAge = currentLastAge % 12;
            }

            patient.setAge(BQDateUtils.formatAge(currentFirstAge, currentLastAge, ageType));

        } else if (ageType == 2) {
            // 年龄类型为"月"
            long totalDays = ChronoUnit.DAYS.between(createdDate, today);
            int months = (int) (totalDays / 30);
            int days = (int) (totalDays % 30);

            patient.setFirstAgePlus(months);
            patient.setLastAgePlus(days);

            // 计算当前年龄
            int currentFirstAge = (patient.getFirstAge() != null ? patient.getFirstAge() : 0) + months;
            int currentLastAge = (patient.getLastAge() != null ? patient.getLastAge() : 0) + days;

            // 天数超过30转为月
            if (currentLastAge >= 30) {
                currentFirstAge += currentLastAge / 30;
                currentLastAge = currentLastAge % 30;
            }

            patient.setAge(BQDateUtils.formatAge(currentFirstAge, currentLastAge, ageType));
        }
    }

    /**
     * 生成更新SQL
     */
    private String buildUpdateSql(PatientAgeData patient) {
        return String.format(
                "UPDATE bq_patient SET firstAgePlus = %d, lastAgePlus = %d, age = '%s' WHERE id = %d",
                patient.getFirstAgePlus(),
                patient.getLastAgePlus(),
                patient.getAge(),
                patient.getId());
    }

    /**
     * 患者年龄数据内部类
     */
    private static class PatientAgeData {
        private Long id;
        private Integer ageType;
        private Integer firstAge;
        private Integer lastAge;
        private LocalDateTime createdTime;
        private Integer firstAgePlus;
        private Integer lastAgePlus;
        private String age;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getAgeType() {
            return ageType;
        }

        public void setAgeType(Integer ageType) {
            this.ageType = ageType;
        }

        public Integer getFirstAge() {
            return firstAge;
        }

        public void setFirstAge(Integer firstAge) {
            this.firstAge = firstAge;
        }

        public Integer getLastAge() {
            return lastAge;
        }

        public void setLastAge(Integer lastAge) {
            this.lastAge = lastAge;
        }

        public LocalDateTime getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
        }

        public Integer getFirstAgePlus() {
            return firstAgePlus;
        }

        public void setFirstAgePlus(Integer firstAgePlus) {
            this.firstAgePlus = firstAgePlus;
        }

        public Integer getLastAgePlus() {
            return lastAgePlus;
        }

        public void setLastAgePlus(Integer lastAgePlus) {
            this.lastAgePlus = lastAgePlus;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    /**
     * RowMapper
     */
    private static class PatientAgeRowMapper implements RowMapper<PatientAgeData> {
        @Override
        public PatientAgeData mapRow(ResultSet rs, int rowNum) throws SQLException {
            PatientAgeData data = new PatientAgeData();
            data.setId(rs.getLong("id"));
            data.setAgeType(rs.getObject("ageType") != null ? rs.getInt("ageType") : null);
            data.setFirstAge(rs.getObject("firstAge") != null ? rs.getInt("firstAge") : null);
            data.setLastAge(rs.getObject("lastAge") != null ? rs.getInt("lastAge") : null);
            data.setCreatedTime(
                    rs.getTimestamp("createdTime") != null ? rs.getTimestamp("createdTime").toLocalDateTime() : null);
            return data;
        }
    }
}
