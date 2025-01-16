package com.inesdatamap.mapperbackend.model.jpa;

import java.io.Serializable;

import com.inesdatamap.mapperbackend.model.enums.DataBaseTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Base Source db entity representation
 *
 * @author gmv
 */
@Getter
@Setter
@Entity
@DiscriminatorValue("DATABASE")
public class DataBaseSource extends DataSource implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2425750964496627584L;

	/**
	 * The connection string for the database source.
	 */
	@Column(name = "connection_string")
	private String connectionString;

	/**
	 * The user name for accessing the database.
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * The password for accessing the database.
	 */
	@Column(name = "password")
	private String password;

	/**
	 * The type of the database.
	 */
	@Column(name = "database_type")
	@Enumerated(EnumType.STRING)
	private DataBaseTypeEnum databaseType;

}
