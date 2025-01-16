package com.inesdatamap.mapperbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inesdatamap.mapperbackend.utils.Constants;

/**
 * Configuration class for BCryptPasswordEncoder.
 *
 * This configuration class sets up a {@link BCryptPasswordEncoder} bean with a specified strength parameter. BCrypt is a widely supported
 * algorithm for password hashing that is deliberately slow to mitigate password cracking attacks.
 */
@Configuration
public class PasswordEncoderConfig {

	/**
	 * Creates a {@link PasswordEncoder} bean using {@link BCryptPasswordEncoder}.
	 *
	 * The {@code BCryptPasswordEncoder} is configured with a strength of 16, which affects the computational cost of the hashing algorithm.
	 * The strength should be tuned to balance security and performance. The default strength is 10.
	 *
	 * @return a {@link PasswordEncoder} configured with a strength of 16.
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(Constants.NUMBER_16);
	}

}
