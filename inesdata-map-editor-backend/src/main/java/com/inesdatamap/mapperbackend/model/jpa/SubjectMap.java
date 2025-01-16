package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Subject db entity representation
 *
 * @author gmv
 *
 */
@Getter
@Setter
@Entity
@Table(name = "SUBJECT_MAP")
public class SubjectMap extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID for serialization
	 */
	private static final long serialVersionUID = -3164348767737374579L;

	/**
	 * The template associated with the subject.
	 */
	@Column(name = "template")
	private String template;

	/**
	 * The class name associated with the subject.
	 */
	@Column(name = "class_name")
	private String className;
}
