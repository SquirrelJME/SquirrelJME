// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

/**
 * Java field information.
 *
 * @since 2025/12/06
 */
public enum InfoJava
	implements SpecificField
{
	/** Version. */
	VERSION("Version", "java.version"),
	
	/** Vendor. */
	VENDOR("Vendor", "java.vendor"),
	
	/** Vendor e-mail. */
	VENDOR_EMAIL("Vendor E-Mail", "java.vendor.email"),
	
	/** Vendor URL. */
	VENDOR_URL("Vendor URL", "java.vendor.url"),
	
	/** Runtime Name. */
	RUNTIME_NAME("Runtime", "java.runtime.name"),
	
	/** Runtime Version. */
	RUNTIME_VERSION("Runtime Version", "java.runtime.version"),
	
	/** VM Info. */
	VM_INFO("VM Info", "java.vm.info"),
	
	/** VM Version. */
	VM_VERSION("VM Version", "java.vm.version"),
	
	/** VM Name. */
	VM_NAME("VM Name", "java.vm.name"),
	
	/** VM Vendor. */
	VM_VENDOR("VM Vendor", "java.vm.vendor"),
	
	/** VM Vendor E-Mail. */
	VM_VENDOR_EMAIL("VM Vendor E-Mail", "java.vm.vendor.email"),
	
	/** VM Vendor URL. */
	VM_VENDOR_URL("VM Vendor URL", "java.vm.vendor.url"),
	
	/** Configuration. */
	CONFIGURATION("Configuration",
		"microedition.configuration"),
	
	/** Platform. */
	PLATFORM("Platform",
		"microedition.platform"),
	
	/* End. */
	;
	
	/** The key for the field. */
	protected final String key;
	
	/** The property for the field. */
	protected final String property;
	
	/**
	 * Initializes the field key.
	 *
	 * @param __key The key.
	 * @param __property The property.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/06
	 */
	InfoJava(String __key, String __property)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
		this.property = __property;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	public final String key()
	{
		return this.key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	public String value()
	{
		return System.getProperty(this.property);
	}
}
