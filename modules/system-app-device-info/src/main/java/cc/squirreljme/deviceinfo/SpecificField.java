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
 * Specific fields that are within a group.
 *
 * @since 2025/12/06
 */
public interface SpecificField
{
	/**
	 * Returns the key of the field.
	 *
	 * @return The field key.
	 * @since 2025/12/06
	 */
	String key();
	
	/**
	 * Returns the value of the field.
	 *
	 * @return The field value.
	 * @since 2025/12/06
	 */
	String value();
	
	/*"os.arch"
		"os.name"
		"os.version"
	
	"java.version"
		"java.vm.info"
		"java.vm.version"
		"java.vm.name"
		"java.vm.vendor"
		"java.vm.vendor.email"
		"java.vm.vendor.url"
		"java.vendor"
		"java.vendor.email"
		"java.vendor.url"
		"java.runtime.name"
		"java.runtime.version"
		
		"microedition.configuration"
		"microedition.encoding"
		"microedition.locale"
		"microedition.platform"
		
		"file.separator"
		"line.separator"
	
	cell network*/
	
	/* End. */
	;
	
	/**
	 * Returns the result of the information.
	 *
	 * @return The information result or {@code null} if it is not known.
	 * @since 2025/12/06
	 */
	/*public abstract String result();*/
}
