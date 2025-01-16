package com.inesdatamap.mapperbackend.properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the {@link SecurityProperties}
 */
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = SecurityProperties.class)
@TestPropertySource(properties = { "security.cors[0].path=/test", "security.cors[0].allowed-origins=allowed-origin-01,allowed-origin-02",
	"security.cors[0].allowed-methods=allowed-method-01,allowed-method-02",
	"security.cors[0].allowed-headers=allowed-header-01,allowed-header-02",
	"security.cors[0].exposed-headers=exposed-header-01,exposed-header-02", })
class SecurityPropertiesTest {

	@Autowired
	private SecurityProperties properties;

	@Test
	void getCorsTest() {
		assertEquals(1, properties.getCors().length);
		assertEquals("/test", properties.getCors()[0].getPath());
	}

	@Test
	void getCorsAllowedOriginsTest() {
		assertArrayEquals(new String[] { "allowed-origin-01", "allowed-origin-02" }, properties.getCors()[0].getAllowedOrigins());
	}

	@Test
	void getCorsAllowedMethodsTest() {
		assertArrayEquals(new String[] { "allowed-method-01", "allowed-method-02" }, properties.getCors()[0].getAllowedMethods());
	}

	@Test
	void getCorsAllowedHeadersTest() {
		assertArrayEquals(new String[] { "allowed-header-01", "allowed-header-02" }, properties.getCors()[0].getAllowedHeaders());
	}

	@Test
	void getCorsExposedHeadersTest() {
		assertArrayEquals(new String[] { "exposed-header-01", "exposed-header-02" }, properties.getCors()[0].getExposedHeaders());
	}

}
