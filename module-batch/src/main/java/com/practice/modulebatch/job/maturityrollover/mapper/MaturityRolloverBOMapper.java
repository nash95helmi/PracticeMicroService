package com.practice.modulebatch.job.maturityrollover.mapper;

import com.practice.modulebase.util.HibernateIdGenerator;
import com.practice.modulebatch.job.maturityrollover.bo.MaturityRolloverBO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.Date;

public class MaturityRolloverBOMapper implements FieldSetMapper<MaturityRolloverBO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaturityRolloverBOMapper.class);

    private MaturityRolloverBO header;
    private MaturityRolloverBO footer;

    @Override
    public MaturityRolloverBO mapFieldSet(FieldSet fieldSet) throws BindException {
        MaturityRolloverBO bo = new MaturityRolloverBO();
        LOGGER.info("Step1MaturityRolloverReader fieldsetCount : {}",fieldSet.getFieldCount());
        LOGGER.info("Step1MaturityRolloverReader fieldreadString[0] : {}",fieldSet.readString(0));

        if (!StringUtils.containsAny(fieldSet.readString(0), "H", "F")) {
            bo.setId(HibernateIdGenerator.UUID());
            bo.setPolicyNumber(fieldSet.readString(1));
            bo.setTransactionType(fieldSet.readString(2));
            bo.setTransactionRefNo(fieldSet.readString(3));
            bo.setCompletionDate(fieldSet.readString(4));
            bo.setStatus(fieldSet.readString(5));
            bo.setCreatedBy("BatchMRO");
            bo.setDateCreated(new Date());
        }
        return bo;
    }
}
