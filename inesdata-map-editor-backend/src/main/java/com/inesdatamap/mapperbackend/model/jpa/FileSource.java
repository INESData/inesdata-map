package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import com.inesdatamap.mapperbackend.model.enums.DataFileTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * File Source db entity representation
 *
 * @author gmv
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("FILE")
public class FileSource extends DataSource implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8435966261102175199L;

	/**
	 * The name of the file.
	 */
	@Column(name = "file_name")
	private String fileName;

	/**
	 * The path of the file.
	 */
	@Column(name = "file_path")
	private String filePath;

	/**
	 * The fields of the file or data.
	 */
	@Column(name = "fields")
	private String fields;

	/**
	 * The type of the file.
	 */
	@Column(name = "file_type")
	@Enumerated(EnumType.STRING)
	private DataFileTypeEnum fileType;

}
