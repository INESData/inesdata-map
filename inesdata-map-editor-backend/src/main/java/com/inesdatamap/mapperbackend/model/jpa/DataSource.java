package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import com.inesdatamap.mapperbackend.model.enums.DataSourceTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * DataSource db entity representation
 *
 * @author gmv
 */
@Getter
@Setter
@Entity
@Table(name = "DATA_SOURCE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class DataSource extends BaseEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4183830263918699985L;

	/**
	 * The name of the data source.
	 */
	@Column(name = "name")
	private String name;

	/**
	 * The type of the data source.
	 */
	@Column(name = "type", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	protected DataSourceTypeEnum type;
}
