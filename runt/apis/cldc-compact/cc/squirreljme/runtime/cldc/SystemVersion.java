// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * This contains the version information.
 *
 * @since 2017/12/10
 */
public final class SystemVersion
{
	/**
	 * Not used.
	 *
	 * @since 2017/12/10
	 */
	private SystemVersion()
	{
	}
	
	/**
	 * Returns the version of the class libraries.
	 *
	 * @return The class library version.
	 * @since 2017/10/02
	 */
	public static final String javaRuntimeVersion()
	{
		return "0.0.2";
	}
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine.
	 * @since 2017/10/02
	 */
	public static final String javaVMEmail()
	{
		return "xer@multiphasicapps.net";
	}
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine.
	 * @since 2017/10/02
	 */
	public static final String javaVMName()
	{
		return "SquirrelJME";
	}
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine.
	 * @since 2017/10/02
	 */
	public static final String javaVMURL()
	{
		return "http://multiphasicapps.net/";
	}
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine.
	 * @since 2017/10/02
	 */
	public static final String javaVMVendor()
	{
		return "Stephanie Gawroriski";
	}
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version.
	 * @since 2017/08/13
	 */
	public static final String javaVMVersionFull()
	{
		return "1.8.0-0.0.2";
	}
	
	/**
	 * Returns the short version of the Java virtual machine.
	 *
	 * @return The short Java virtual machine version.
	 * @since 2017/08/13
	 */
	public static final String javaVMVersionShort()
	{
		return "1.8.0";
	}
}

