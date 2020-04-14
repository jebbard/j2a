/**
 *
 * {@link J2AConfiguration}.java
 *
 * @author Jens Ebert
 *
 * @date 11.02.2020
 *
 */
package com.github.j2a.core.config;

import java.util.Set;

/**
 * {@link J2AConfiguration}
 *
 */
public class J2AConfiguration {
	private final Set<String> applicationPackages;

	public J2AConfiguration(Set<String> applicationPackages) {
		this.applicationPackages = applicationPackages;
	}

	public boolean isApplicationPackage(String packageToCheck) {
		if (packageToCheck == null || packageToCheck.isBlank()) {
			throw new IllegalArgumentException("Package to check must not be null or blank");
		}

		String cleanedPackage = packageToCheck.toLowerCase().trim();

		return applicationPackages.stream().anyMatch(p -> cleanedPackage.startsWith(p));
	}
}
