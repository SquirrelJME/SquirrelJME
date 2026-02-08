// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Provides {@link RuntimeShelf#vmDescription(int)}.
 *
 * @since 2026/01/16
 */
public enum InfoVmDescription
	implements SpecificField
{
	/** Unspecified. */
	UNSPECIFIED("Unspecified",
		VMDescriptionType.UNSPECIFIED),
	
	/** The VM version. */
	VM_VERSION("VM Version",
		VMDescriptionType.VM_VERSION),
	
	/** The VM name. */
	VM_NAME("VM Name",
		VMDescriptionType.VM_NAME),
	
	/** The VM Vendor. */
	VM_VENDOR("VM Vendor",
		VMDescriptionType.VM_VENDOR),
	
	/** The VM E-mail. */
	VM_EMAIL("VM E-Mail",
		VMDescriptionType.VM_EMAIL),
	
	/** The VM URL. */
	VM_URL("VM URL",
		VMDescriptionType.VM_URL),
	
	/** The executable path of the VM. */
	EXECUTABLE_PATH("Executable Path",
		VMDescriptionType.EXECUTABLE_PATH),
	
	/** The operating system name. */
	OS_NAME("OS Name",
		VMDescriptionType.OS_NAME),
	
	/** The operating system version. */
	OS_VERSION("OS Version",
		VMDescriptionType.OS_VERSION),
	
	/** The operating system architecture. */
	OS_ARCH("OS Architecture",
		VMDescriptionType.OS_ARCH),
	
	/** The current virtual machine security policy. */
	VM_SECURITY_POLICY("Security Policy Details",
		VMDescriptionType.VM_SECURITY_POLICY),
	
	/** Single lines of legal text and copyrights used for ports. */
	THIRD_PARTY_LEGAL_LINE("3rd Party Legal Line",
		VMDescriptionType.THIRD_PARTY_LEGAL_LINE),
	
	/** Full document of legal text, with entire licenses. */
	THIRD_PARTY_LEGAL_DOCUMENT("3rd Party Legal Document",
		VMDescriptionType.THIRD_PARTY_LEGAL_DOCUMENT),
	
	/** The path separator used. */
	PATH_SEPARATOR("Path Separator",
		VMDescriptionType.PATH_SEPARATOR),
	
	/** The virtual machine info. */
	VM_INFO("VM Info",
		VMDescriptionType.VM_INFO),
	
	/** Unknown. */
	DEFAULT_DIR_UNKNOWN("Directory: Unknown",
		VMDescriptionType.DEFAULT_DIR_UNKNOWN),
	
	/** The cache directory. */
	DEFAULT_DIR_CACHE("Directory: Cache",
		VMDescriptionType.DEFAULT_DIR_CACHE),
	
	/** The config directory. */
	DEFAULT_DIR_CONFIG("Directory: Config",
		VMDescriptionType.DEFAULT_DIR_CONFIG),
	
	/** The data directory. */
	DEFAULT_DIR_DATA("Directory: Data",
		VMDescriptionType.DEFAULT_DIR_DATA),
	
	/** The state directory. */
	DEFAULT_DIR_STATE("Directory: State",
		VMDescriptionType.DEFAULT_DIR_STATE),
	
	/** The native library directory. */
	DEFAULT_DIR_NATIVES("Directory: Natives",
		VMDescriptionType.DEFAULT_DIR_NATIVES),
	
	/** Executable directory. */
	DEFAULT_DIR_EXEC("Directory: Executable",
		VMDescriptionType.DEFAULT_DIR_EXEC),
	
	/** Temporary directory. */
	DEFAULT_DIR_TEMPORARY("Directory: Temporary",
		VMDescriptionType.DEFAULT_DIR_TEMPORARY),
	
	/** The libraries directory. */
	DEFAULT_DIR_LIBRARIES("Directory: Libraries",
		VMDescriptionType.DEFAULT_DIR_LIBRARIES),
	
	/** The non-volatile storage directory. */
	DEFAULT_DIR_BUCKET_DATA("Directory: Bucket: Data",
		VMDescriptionType.DEFAULT_DIR_BUCKET_DATA),
	
	/** The extra bucket directory. */
	DEFAULT_DIR_BUCKET_EXTRA("Directory: Bucket: Extra",
		VMDescriptionType.DEFAULT_DIR_BUCKET_EXTRA),
	
	/** The number of default directory types. */
	DEFAULT_DIR_NUM_TYPES("Directory: Reserved 11",
		VMDescriptionType.DEFAULT_DIR_NUM_TYPES),
	
	/** Default directory reserved: 12. */
	DEFAULT_DIR_RESERVED_12("Directory: Reserved 12",
		VMDescriptionType.DEFAULT_DIR_RESERVED_12),
	
	/** Default directory reserved: 13. */
	DEFAULT_DIR_RESERVED_13("Directory: Reserved 13",
		VMDescriptionType.DEFAULT_DIR_RESERVED_13),
	
	/** Default directory reserved: 14. */
	DEFAULT_DIR_RESERVED_14("Directory: Reserved 14",
		VMDescriptionType.DEFAULT_DIR_RESERVED_14),
	
	/** Default directory reserved: 15. */
	DEFAULT_DIR_RESERVED_15("Directory: Reserved 15",
		VMDescriptionType.DEFAULT_DIR_RESERVED_15),
	
	/** Default directory reserved: 16. */
	DEFAULT_DIR_RESERVED_16("Directory: Reserved 16",
		VMDescriptionType.DEFAULT_DIR_RESERVED_16),
	
	/** Default directory reserved: 17. */
	DEFAULT_DIR_RESERVED_17("Directory: Reserved 17",
		VMDescriptionType.DEFAULT_DIR_RESERVED_17),
	
	/** Default directory reserved: 18. */
	DEFAULT_DIR_RESERVED_18("Directory: Reserved 18",
		VMDescriptionType.DEFAULT_DIR_RESERVED_18),
	
	/** Default directory reserved: 19. */
	DEFAULT_DIR_RESERVED_19("Directory: Reserved 19",
		VMDescriptionType.DEFAULT_DIR_RESERVED_19),
	
	/** Default directory reserved: 20. */
	DEFAULT_DIR_RESERVED_20("Directory: Reserved 20",
		VMDescriptionType.DEFAULT_DIR_RESERVED_20),
	
	/** The number of reserved directories. */
	DEFAULT_DIR_NUM_RESERVED("Directory: Reserved Count",
		VMDescriptionType.DEFAULT_DIR_NUM_RESERVED),
	
	/* End. */
	;
	
	/** The key for the field. */
	protected final String key;
	
	/** The description ID. */
	protected final int id;
	
	/**
	 * Initializes the field key.
	 *
	 * @param __key The key.
	 * @param __id The ID.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/16
	 */
	InfoVmDescription(String __key, int __id)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	public final String key()
	{
		return this.key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/16
	 */
	@Override
	public String value()
	{
		try
		{
			return RuntimeShelf.vmDescription(this.id);
		}
		catch (MLECallError ignored)
		{
			return null;
		}
	}
}
