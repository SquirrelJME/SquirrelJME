// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib;

/**
 * This interface contains the list of error codes which may occur when an
 * application fails to install.
 *
 * @since 2017/12/30
 */
public interface InstallErrorCodes
{
	/** The applicaton is already installed. */
	public static final int ALREADY_INSTALLED =
		1;

	/** The dependency of an application is in conflict. */
	public static final int APP_INTEGRITY_FAILURE_DEPENDENCY_CONFLICT =
		2;

	/** The dependency of an application is mismatched. */
	public static final int APP_INTEGRITY_FAILURE_DEPENDENCY_MISMATCH =
		3;

	/** The hash sum of an application is invalid. */
	public static final int APP_INTEGRITY_FAILURE_HASH_MISMATCH =
		4;

	/** The application is blacklisted. */
	public static final int APPLICATION_BLACKLISTED =
		5;

	/** An attribute does not match. */
	public static final int ATTRIBUTE_MISMATCH =
		6;

	/** Could not authenticate. */
	public static final int AUTHENTICATION_FAILURE =
		7;

	/** Not authorized to install the application. */
	public static final int AUTHORIZATION_FAILURE =
		8;

	/** The certificate authority has been disabled. */
	public static final int CA_DISABLED =
		9;

	/** Installation cancelled. */
	public static final int CANCELLED =
		10;

	/** Cannot authenticate. */
	public static final int CANNOT_AUTH =
		11;

	/** A dependency eventually depends on itself. */
	public static final int CIRCULAR_COMPONENT_DEPENDENCY =
		12;

	/** The client requesting the program has been blacklisted. */
	public static final int CLIENT_BLACKLISTED =
		13;

	/** Dynamic componenent dependencies limit exceeded. */
	public static final int COMPONENT_DEPS_LIMIT_EXCEEDED =
		14;

	/** There is a conflict in a content handler. */
	public static final int CONTENT_HANDLER_CONFLICT =
		15;

	/** The hash sum of a dependency is mismatched. */
	public static final int CORRUPT_DEPENDENCY_HASH =
		16;

	/** The JAR is malformed. */
	public static final int CORRUPT_JAR =
		17;

	/** The certificate of the provider is malformed. */
	public static final int CORRUPT_PROVIDER_CERT =
		18;

	/** The signature of a JAR is incorrect. */
	public static final int CORRUPT_SIGNATURE =
		19;

	/** The JAR cannot run on this device. */
	public static final int DEVICE_INCOMPATIBLE =
		20;

	/** JAR or JAD has a duplicated key. */
	public static final int DUPLICATED_KEY =
		21;

	/** The certificate authority's key has expired. */
	public static final int EXPIRED_CA_KEY =
		22;

	/** The provider's certficate has expired. */
	public static final int EXPIRED_PROVIDER_CERT =
		23;

	/** Not enough space for installation. */
	public static final int INSUFFICIENT_STORAGE =
		24;

	/** The JAD type is not valid. */
	public static final int INVALID_JAD_TYPE =
		25;

	/** The JAD URL is not valid. */
	public static final int INVALID_JAD_URL =
		26;

	/** The JAR type is not valid. */
	public static final int INVALID_JAR_TYPE =
		27;

	/** The JAR URL is not valid. */
	public static final int INVALID_JAR_URL =
		28;

	/** Manifest contains an invalid key. */
	public static final int INVALID_KEY =
		29;

	/** Cannot load native libraries within a JAR. */
	public static final int INVALID_NATIVE_LIBRARY =
		30;

	/** Payment information incorrect. */
	public static final int INVALID_PAYMENT_INFO =
		31;

	/** The provider certificate is not valid. */
	public static final int INVALID_PROVIDER_CERT =
		32;

	/** The signature is not valid. */
	public static final int INVALID_SIGNATURE =
		33;

	/** The value of a manifest key is not correct. */
	public static final int INVALID_VALUE =
		34;

	/** The version format is not valid. */
	public static final int INVALID_VERSION =
		35;

	/** Failed to read or write the file. */
	public static final int IO_FILE_ERROR =
		36;

	/** Failed to read from or write to network. */
	public static final int IO_NETWORK_ERROR =
		37;

	/** The JAD URL has moved. */
	public static final int JAD_MOVED =
		38;

	/** The JAD was not found. */
	public static final int JAD_NOT_FOUND =
		39;

	/** The server hosting the JAD was not found. */
	public static final int JAD_SERVER_NOT_FOUND =
		40;

	/** JAR class file verification failed. */
	public static final int JAR_CLASSES_VERIFICATION_FAILED =
		41;

	/** The JAR is locked. */
	public static final int JAR_IS_LOCKED =
		42;

	/** The JAR was not found. */
	public static final int JAR_NOT_FOUND =
		43;

	/** The server containin the JAR was not found. */
	public static final int JAR_SERVER_NOT_FOUND =
		44;

	/** The JAR size is not correctly matched. */
	public static final int JAR_SIZE_MISMATCH =
		45;

	/** The name of a LIBlet collides with another LIBlet. */
	public static final int LIBLET_NAMESPACE_COLLISION =
		46;

	/** The configuration (MIDP, etc.) is missing from the manifest. */
	public static final int MISSING_CONFIGURATION =
		47;

	/** The hash code of a dependency is missing. */
	public static final int MISSING_DEPENDENCY_HASH =
		48;

	/** The JAD URL of a dependency is missing. */
	public static final int MISSING_DEPENDENCY_JAD_URL =
		49;

	/** The JAR size has not been specified. */
	public static final int MISSING_JAR_SIZE =
		50;

	/** The URL to the JAR has not been specified. */
	public static final int MISSING_JAR_URL =
		51;

	/** The profile (CLDC, etc.) is missing from the manifest. */
	public static final int MISSING_PROFILE =
		52;

	/** The provider certificate is missing. */
	public static final int MISSING_PROVIDER_CERT =
		53;

	/** No suite name was specified. */
	public static final int MISSING_SUITE_NAME =
		54;

	/** No vendor was specified. */
	public static final int MISSING_VENDOR =
		55;

	/** No version was specified. */
	public static final int MISSING_VERSION =
		56;

	/** The suite to be installed is newer than the current one. */
	public static final int NEW_VERSION =
		57;

	/** Not an error. */
	public static final int NO_ERROR =
		58;

	/** Another error occured. */
	public static final int OTHER_ERROR =
		59;

	/** Authetication with a proxy is required. */
	public static final int PROXY_AUTH =
		60;

	/** The push registration is not in the {@code MIDlet-n} attribute. */
	public static final int PUSH_CLASS_FAILURE =
		61;

	/** The push registration is already taken. */
	public static final int PUSH_DUP_FAILURE =
		62;

	/** The format of a push registration is not valid. */
	public static final int PUSH_FORMAT_FAILURE =
		63;

	/** The protocol of a push registration has failed. */
	public static final int PUSH_PROTO_FAILURE =
		64;

	/** The certificate has been revoked. */
	public static final int REVOKED_CERT =
		65;

	/** The name of the suite mismatches. */
	public static final int SUITE_NAME_MISMATCH =
		66;

	/** There are more properties than the available amount of memory. */
	public static final int TOO_MANY_PROPS =
		67;

	/** Trusted suites cannot be overwritten. */
	public static final int TRUSTED_OVERWRITE_FAILURE =
		68;

	/** Not authorized. */
	public static final int UNAUTHORIZED =
		69;

	/** Not authorized to install suits. */
	public static final int UNAUTHORIZED_INSTALL =
		70;

	/** Unknown certificate authority. */
	public static final int UNKNOWN_CA =
		71;

	/** Unknown certificate status. */
	public static final int UNKNOWN_CERT_STATUS =
		72;

	/** Certificate not supported. */
	public static final int UNSUPPORTED_CERT =
		73;

	/** Certificate character encoding not supported. */
	public static final int UNSUPPORTED_CHAR_ENCODING =
		74;

	/** Payment information not supported. */
	public static final int UNSUPPORTED_PAYMENT_INFO =
		75;

	/** The payment suite is not trusted. */
	public static final int UNTRUSTED_PAYMENT_SUITE =
		76;

	/** Vendor mismatched. */
	public static final int VENDOR_MISMATCH =
		77;

	/** Version mismatched. */
	public static final int VERSION_MISMATCH =
		78;
}

