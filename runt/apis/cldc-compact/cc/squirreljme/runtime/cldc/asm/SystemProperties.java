// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * Access to system properties.
 *
 * @since 2018/09/20
 */
public final class SystemProperties
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/20
	 */
	private SystemProperties()
	{
	}
	
	/**
	 * Returns the depth of the guests within the virtual machine.
	 *
	 * @return The number of guests.
	 * @since 2018/11/04
	 */
	public static final native int guestDepth();
	
	/**
	 * Returns the version of the class libraries.
	 *
	 * @return The class library version.
	 * @since 2017/10/02
	 */
	public static String javaRuntimeVersion()
	{
		return "0.2.0";
	}
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine.
	 * @since 2017/10/02
	 */
	public static native String javaVMEmail();
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine.
	 * @since 2017/10/02
	 */
	public static native String javaVMName();
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine.
	 * @since 2017/10/02
	 */
	public static native String javaVMURL();
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine.
	 * @since 2017/10/02
	 */
	public static native String javaVMVendor();
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version.
	 * @since 2017/08/13
	 */
	public static native String javaVMVersion();
	
	/**
	 * Returns a system property for the given value.
	 *
	 * @param __k The key to get.
	 * @return The value of the property, will be {@code null} if it is not
	 * valid.
	 * @since 2018/09/20
	 */
	public static native String systemProperty(String __k);
}

