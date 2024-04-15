// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This enumeration contains the many ways which applications can be failed
 * to be installed.
 *
 * @since 2016/06/24
 */
@Api
@SuppressWarnings("FieldNamingConvention")
public enum InstallErrorCodes
{
	/** The applicaton is already installed. */
	@Api
	ALREADY_INSTALLED,

	/** The dependency of an application is in conflict. */
	@Api
	APP_INTEGRITY_FAILURE_DEPENDENCY_CONFLICT,

	/** The dependency of an application is mismatched. */
	@Api
	APP_INTEGRITY_FAILURE_DEPENDENCY_MISMATCH,

	/** The hash sum of an application is invalid. */
	@Api
	APP_INTEGRITY_FAILURE_HASH_MISMATCH,

	/** The application is blacklisted. */
	@Api
	APPLICATION_BLACKLISTED,

	/** An attribute does not match. */
	@Api
	ATTRIBUTE_MISMATCH,

	/** Could not authenticate. */
	@Api
	AUTHENTICATION_FAILURE,

	/** Not authorized to install the application. */
	@Api
	AUTHORIZATION_FAILURE,

	/** The certificate authority has been disabled. */
	@Api
	CA_DISABLED,

	/** Installation cancelled. */
	@Api
	CANCELLED,

	/** Cannot authenticate. */
	@Api
	CANNOT_AUTH,

	/** A dependency eventually depends on itself. */
	@Api
	CIRCULAR_COMPONENT_DEPENDENCY,

	/** The client requesting the program has been blacklisted. */
	@Api
	CLIENT_BLACKLISTED,

	/** Dynamic componenent dependencies limit exceeded. */
	@Api
	COMPONENT_DEPS_LIMIT_EXCEEDED,

	/** There is a conflict in a content handler. */
	@Api
	CONTENT_HANDLER_CONFLICT,

	/** The hash sum of a dependency is mismatched. */
	@Api
	CORRUPT_DEPENDENCY_HASH,

	/** The JAR is malformed. */
	@Api
	CORRUPT_JAR,

	/** The certificate of the provider is malformed. */
	@Api
	CORRUPT_PROVIDER_CERT,

	/** The signature of a JAR is incorrect. */
	@Api
	CORRUPT_SIGNATURE,

	/** The JAR cannot run on this device. */
	@Api
	DEVICE_INCOMPATIBLE,

	/** JAR or JAD has a duplicated key. */
	@Api
	DUPLICATED_KEY,

	/** The certificate authority's key has expired. */
	@Api
	EXPIRED_CA_KEY,

	/** The provider's certficate has expired. */
	@Api
	EXPIRED_PROVIDER_CERT,

	/** Not enough space for installation. */
	@Api
	INSUFFICIENT_STORAGE,

	/** The JAD type is not valid. */
	@Api
	INVALID_JAD_TYPE,

	/** The JAD URL is not valid. */
	@Api
	INVALID_JAD_URL,

	/** The JAR type is not valid. */
	@Api
	INVALID_JAR_TYPE,

	/** The JAR URL is not valid. */
	@Api
	INVALID_JAR_URL,

	/** Manifest contains an invalid key. */
	@Api
	INVALID_KEY,

	/** Cannot load native libraries within a JAR. */
	@Api
	INVALID_NATIVE_LIBRARY,

	/** Payment information incorrect. */
	@Api
	INVALID_PAYMENT_INFO,

	/** The provider certificate is not valid. */
	@Api
	INVALID_PROVIDER_CERT,

	/** The signature is not valid. */
	@Api
	INVALID_SIGNATURE,

	/** The value of a manifest key is not correct. */
	@Api
	INVALID_VALUE,

	/** The version format is not valid. */
	@Api
	INVALID_VERSION,

	/** Failed to read or write the file. */
	@Api
	IO_FILE_ERROR,

	/** Failed to read from or write to network. */
	@Api
	IO_NETWORK_ERROR,

	/** The JAD URL has moved. */
	@Api
	JAD_MOVED,

	/** The JAD was not found. */
	@Api
	JAD_NOT_FOUND,

	/** The server hosting the JAD was not found. */
	@Api
	JAD_SERVER_NOT_FOUND,

	/** JAR class file verification failed. */
	@Api
	JAR_CLASSES_VERIFICATION_FAILED,

	/** The JAR is locked. */
	@Api
	JAR_IS_LOCKED,

	/** The JAR was not found. */
	@Api
	JAR_NOT_FOUND,

	/** The server containin the JAR was not found. */
	@Api
	JAR_SERVER_NOT_FOUND,

	/** The JAR size is not correctly matched. */
	@Api
	JAR_SIZE_MISMATCH,

	/** The name of a LIBlet collides with another LIBlet. */
	@Api
	LIBLET_NAMESPACE_COLLISION,

	/** The configuration (MIDP, etc.) is missing from the manifest. */
	@Api
	MISSING_CONFIGURATION,

	/** The hash code of a dependency is missing. */
	@Api
	MISSING_DEPENDENCY_HASH,

	/** The JAD URL of a dependency is missing. */
	@Api
	MISSING_DEPENDENCY_JAD_URL,

	/** The JAR size has not been specified. */
	@Api
	MISSING_JAR_SIZE,

	/** The URL to the JAR has not been specified. */
	@Api
	MISSING_JAR_URL,

	/** The profile (CLDC, etc.) is missing from the manifest. */
	@Api
	MISSING_PROFILE,

	/** The provider certificate is missing. */
	@Api
	MISSING_PROVIDER_CERT,

	/** No suite name was specified. */
	@Api
	MISSING_SUITE_NAME,

	/** No vendor was specified. */
	@Api
	MISSING_VENDOR,

	/** No version was specified. */
	@Api
	MISSING_VERSION,

	/** The suite to be installed is newer than the current one. */
	@Api
	NEW_VERSION,

	/** Not an error. */
	@Api
	NO_ERROR,

	/** Another error occured. */
	@Api
	OTHER_ERROR,

	/** Authetication with a proxy is required. */
	@Api
	PROXY_AUTH,

	/** The push registration is not in the {@code MIDlet-n} attribute. */
	@Api
	PUSH_CLASS_FAILURE,

	/** The push registration is already taken. */
	@Api
	PUSH_DUP_FAILURE,

	/** The format of a push registration is not valid. */
	@Api
	PUSH_FORMAT_FAILURE,

	/** The protocol of a push registration has failed. */
	@Api
	PUSH_PROTO_FAILURE,

	/** The certificate has been revoked. */
	@Api
	REVOKED_CERT,

	/** The name of the suite mismatches. */
	@Api
	SUITE_NAME_MISMATCH,

	/** There are more properties than the available amount of memory. */
	@Api
	TOO_MANY_PROPS,

	/** Trusted suites cannot be overwritten. */
	@Api
	TRUSTED_OVERWRITE_FAILURE,

	/** Not authorized. */
	@Api
	UNAUTHORIZED,

	/** Not authorized to install suits. */
	@Api
	UNAUTHORIZED_INSTALL,

	/** Unknown certificate authority. */
	@Api
	UNKNOWN_CA,

	/** Unknown certificate status. */
	@Api
	UNKNOWN_CERT_STATUS,

	/** Certificate not supported. */
	@Api
	UNSUPPORTED_CERT,

	/** Certificate character encoding not supported. */
	@Api
	UNSUPPORTED_CHAR_ENCODING,

	/** Payment information not supported. */
	@Api
	UNSUPPORTED_PAYMENT_INFO,

	/** The payment suite is not trusted. */
	@Api
	UNTRUSTED_PAYMENT_SUITE,

	/** Vendor mismatched. */
	@Api
	VENDOR_MISMATCH,

	/** Version mismatched. */
	@Api
	VERSION_MISMATCH,

	/** End. */
	;
}

