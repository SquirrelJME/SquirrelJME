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

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;
import cc.squirreljme.runtime.cldc.lang.OperatingSystemType;
import cc.squirreljme.runtime.cldc.SquirrelJME;

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
	 * Returns the API level of the virtual machine.
	 *
	 * @return The API level.
	 * @since 2018/12/05
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static int apiLevel()
	{
		return ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225;
	}
	
	/**
	 * Returns the depth of the guests within the virtual machine.
	 *
	 * @return The number of guests.
	 * @since 2018/11/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int guestDepth()
	{
		return 0;
	}
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine.
	 * @since 2017/10/02
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String javaVMEmail()
	{
		return "xer@multiphasicapps.net";
	}
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine.
	 * @since 2017/10/02
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String javaVMName()
	{
		return "JavaSE Virtual Environment";
	}
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine.
	 * @since 2017/10/02
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String javaVMURL()
	{
		return "http://multiphasicapps.net/";
	}
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine.
	 * @since 2017/10/02
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String javaVMVendor()
	{
		return "Stephanie Gawroriski";
	}
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version.
	 * @since 2017/08/13
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String javaVMVersion()
	{
		return "0.2.0";
	}
	
	/**
	 * Returns the type of operating SquirrelJME is running on.
	 *
	 * @return The type of operating system SquirrelJME is running on.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final int operatingSystemType()
	{
		return OperatingSystemType.UNKNOWN;
	}
	
	/**
	 * Returns a system property for the given value.
	 *
	 * @param __k The key to get.
	 * @return The value of the property, will be {@code null} if it is not
	 * valid.
	 * @since 2018/09/20
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static String systemProperty(String __k)
	{
		return System.getProperty(__k);
	}
	
	/**
	 * Returns the version of the class libraries.
	 *
	 * @return The class library version.
	 * @since 2017/10/02
	 */
	public static String javaRuntimeVersion()
	{
		return SquirrelJME.RUNTIME_VERSION;
	}
}

