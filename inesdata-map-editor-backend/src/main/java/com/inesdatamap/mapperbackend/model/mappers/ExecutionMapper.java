package com.inesdatamap.mapperbackend.model.mappers;

import org.mapstruct.Mapper;

import com.inesdatamap.mapperbackend.model.dto.ExecutionDTO;
import com.inesdatamap.mapperbackend.model.jpa.Execution;

/**
 * Mapper interface for converting between {@link Execution} and {@link ExecutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExecutionMapper extends BaseEntityMapper<ExecutionDTO, Execution> {

}
