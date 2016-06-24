// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This enumeration contains the many ways which applications can be failed
 * to be installed.
 *
 * @since 2016/06/24
 */
public enum InstallErrorCodes
{
	/** The applicaton is already installed. */
	ALREADY_INSTALLED,

	/** The dependency of an application is in conflict. */
	APP_INTEGRITY_FAILURE_DEPENDENCY_CONFLICT,

	/** The dependency of an application is mismatched. */
	APP_INTEGRITY_FAILURE_DEPENDENCY_MISMATCH,

	/** The hash sum of an application is invalid. */
	APP_INTEGRITY_FAILURE_HASH_MISMATCH,

	/** The application is blacklisted. */
	APPLICATION_BLACKLISTED,

	/** An attribute does not match. */
	ATTRIBUTE_MISMATCH,

	/** Could not authenticate. */
	AUTHENTICATION_FAILURE,

	/** Not authorized to install the application. */
	AUTHORIZATION_FAILURE,

	/** The certificate authority has been disabled. */
	CA_DISABLED,

	/** Installation cancelled. */
	CANCELLED,

	/** Cannot authenticate. */
	CANNOT_AUTH,

	/** A dependency eventually depends on itself. */
	CIRCULAR_COMPONENT_DEPENDENCY,

	/** The client requesting the program has been blacklisted. */
	CLIENT_BLACKLISTED,

	/** Dynamic componenent dependencies limit exceeded. */
	COMPONENT_DEPS_LIMIT_EXCEEDED,

	/** There is a conflict in a content handler. */
	CONTENT_HANDLER_CONFLICT,

	/** The hash sum of a dependency is mismatched. */
	CORRUPT_DEPENDENCY_HASH,

	/** The JAR is malformed. */
	CORRUPT_JAR,

	/** The certificate of the provider is malformed. */
	CORRUPT_PROVIDER_CERT,

	/** The signature of a JAR is incorrect. */
	CORRUPT_SIGNATURE,

	/** The JAR cannot run on this device. */
	DEVICE_INCOMPATIBLE,

	/** JAR or JAD has a duplicated key. */
	DUPLICATED_KEY,

	/** The certificate authority's key has expired. */
	EXPIRED_CA_KEY,

	/** The provider's certficate has expired. */
	EXPIRED_PROVIDER_CERT,

	/** Not enough space for installation. */
	INSUFFICIENT_STORAGE,

	/** The JAD type is not valid. */
	INVALID_JAD_TYPE,

	/** The JAD URL is not valid. */
	INVALID_JAD_URL,

	/** The JAR type is not valid. */
	INVALID_JAR_TYPE,

	/** The JAR URL is not valid. */
	INVALID_JAR_URL,

	/** Manifest contains an invalid key. */
	INVALID_KEY,

	/** Cannot load native libraries within a JAR. */
	INVALID_NATIVE_LIBRARY,

	/** Payment information incorrect. */
	INVALID_PAYMENT_INFO,

	/** The provider certificate is not valid. */
	INVALID_PROVIDER_CERT,

	/** The signature is not valid. */
	INVALID_SIGNATURE,

	/** The value of a manifest key is not correct. */
	INVALID_VALUE,

	/** The version format is not valid. */
	INVALID_VERSION,

	/** Failed to read or write the file. */
	IO_FILE_ERROR,

	/** Failed to read from or write to network. */
	IO_NETWORK_ERROR,

	/** The JAD URL has moved. */
	JAD_MOVED,

	/** The JAD was not found. */
	JAD_NOT_FOUND,

	/** The server hosting the JAD was not found. */
	JAD_SERVER_NOT_FOUND,

	/** JAR class file verification failed. */
	JAR_CLASSES_VERIFICATION_FAILED,

	/** The JAR is locked. */
	JAR_IS_LOCKED,

	/** The JAR was not found. */
	JAR_NOT_FOUND,

	/** The server containin the JAR was not found. */
	JAR_SERVER_NOT_FOUND,

	/** The JAR size is not correctly matched. */
	JAR_SIZE_MISMATCH,

	/** The name of a LIBlet collides with another LIBlet. */
	LIBLET_NAMESPACE_COLLISION,

	/** The configuration (MIDP, etc.) is missing from the manifest. */
	MISSING_CONFIGURATION,

	/** The hash code of a dependency is missing. */
	MISSING_DEPENDENCY_HASH,

	/** The JAD URL of a dependency is missing. */
	MISSING_DEPENDENCY_JAD_URL,

	/** The JAR size has not been specified. */
	MISSING_JAR_SIZE,

	/** The URL to the JAR has not been specified. */
	MISSING_JAR_URL,

	/** The profile (CLDC, etc.) is missing from the manifest. */
	MISSING_PROFILE,

	/** The provider certificate is missing. */
	MISSING_PROVIDER_CERT,

	/** No suite name was specified. */
	MISSING_SUITE_NAME,

	/** No vendor was specified. */
	MISSING_VENDOR,

	/** No version was specified. */
	MISSING_VERSION,

	/** The suite to be installed is newer than the current one. */
	NEW_VERSION,

	/** Not an error. */
	NO_ERROR,

	/** Another error occured. */
	OTHER_ERROR,

	/** Authetication with a proxy is required. */
	PROXY_AUTH,

	/** The push registration is not in the {@code MIDlet-n} attribute. */
	PUSH_CLASS_FAILURE,

	/** The push registration is already taken. */
	PUSH_DUP_FAILURE,

	/** The format of a push registration is not valid. */
	PUSH_FORMAT_FAILURE,

	/** The protocol of a push registration has failed. */
	PUSH_PROTO_FAILURE,

	/** The certificate has been revoked. */
	REVOKED_CERT,

	/** The name of the suite mismatches. */
	SUITE_NAME_MISMATCH,

	/** There are more properties than the available amount of memory. */
	TOO_MANY_PROPS,

	/** Trusted suites cannot be overwritten. */
	TRUSTED_OVERWRITE_FAILURE,

	/** Not authorized. */
	UNAUTHORIZED,

	/** Not authorized to install suits. */
	UNAUTHORIZED_INSTALL,

	/** Unknown certificate authority. */
	UNKNOWN_CA,

	/** Unknown certificate status. */
	UNKNOWN_CERT_STATUS,

	/** Certificate not supported. */
	UNSUPPORTED_CERT,

	/** Certificate character encoding not supported. */
	UNSUPPORTED_CHAR_ENCODING,

	/** Payment information not supported. */
	UNSUPPORTED_PAYMENT_INFO,

	/** The payment suite is not trusted. */
	UNTRUSTED_PAYMENT_SUITE,

	/** Vendor mismatched. */
	VENDOR_MISMATCH,

	/** Version mismatched. */
	VERSION_MISMATCH,

	/** End. */
	;
}

