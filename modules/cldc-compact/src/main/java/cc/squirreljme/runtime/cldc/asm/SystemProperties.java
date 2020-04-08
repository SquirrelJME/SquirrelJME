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

import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * Access to system properties.
 *
 * @since 2018/09/20
 */
@Deprecated
public final class SystemProperties
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/20
	 */
	@Deprecated
	private SystemProperties()
	{
	}
	
	/**
	 * Returns the approximated path where the VM's executable exists. This
	 * will be the actual JVM's JAR or EXE file.
	 *
	 * @return The approximated executable path or {@code null} if it is not
	 * known.
	 * @since 2018/12/08
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String executablePath();
	
	/**
	 * Returns the depth of the guests within the virtual machine.
	 *
	 * @return The number of guests.
	 * @since 2018/11/04
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int guestDepth();
	
	/**
	 * The class to use for a given implementation of something.
	 *
	 * @param __n The class name to lookup.
	 * @return The class that should get its instance created or {@code null}
	 * if there is no implementation.
	 * @since 2018/12/13
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native String implementationClass(String __n);
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine.
	 * @since 2017/10/02
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String javaVMEmail();
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine.
	 * @since 2017/10/02
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String javaVMName();
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine.
	 * @since 2017/10/02
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String javaVMURL();
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine.
	 * @since 2017/10/02
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String javaVMVendor();
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version.
	 * @since 2017/08/13
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String javaVMVersion();
	
	/**
	 * Returns the type of operating SquirrelJME is running on.
	 *
	 * @return The type of operating system SquirrelJME is running on.
	 * @since 2018/10/14
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int operatingSystemType();
	
	/**
	 * Returns a system property for the given value.
	 *
	 * @param __k The key to get.
	 * @return The value of the property, will be {@code null} if it is not
	 * valid.
	 * @since 2018/09/20
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static native String systemProperty(String __k);
	
	/**
	 * Returns the version of the class libraries.
	 *
	 * @return The class library version.
	 * @since 2017/10/02
	 */
	@Deprecated
	public static String javaRuntimeVersion()
	{
		return SquirrelJME.RUNTIME_VERSION;
	}
}

