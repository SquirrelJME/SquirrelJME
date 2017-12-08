// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This class provides access to versioning information for SquirrelJME.
 *
 * @since 2017/11/10
 */
@Deprecated
public abstract class VersionFunctions
{
	
	/**
	 * Returns the UUID of the system device.
	 *
	 * @return The UUID of the host system.
	 * @since 2017/08/13
	 */
	public abstract String deviceUUID();
	
	/**
	 * Returns the hostname of the current system.
	 *
	 * @return The hostname of the system.
	 * @since 2017/08/13
	 */
	public abstract String hostName();
	
	/**
	 * Is the virtual machine itself SquirrelJME or this being used as a
	 * runtime on an existing virtual machine?
	 *
	 * @return If this is a SquirrelJME's VM.
	 * @since 2017/11/10
	 */
	public abstract boolean isSquirrelJMEJVM();
	
	/**
	 * Returns the separator which is used for directories.
	 *
	 * @return The path separator for directories.
	 * @since 2017/08/13
	 */
	public abstract String directorySeparator();
	
	/**
	 * Returns the line separator that is used on the system.
	 *
	 * @return The line separator that is used on the system.
	 * @since 2017/08/13
	 */
	public abstract String lineSeparator();
	
	/**
	 * Returns the architecture the OS is running on.
	 *
	 * @return The architecture of the operating system.
	 * @since 2017/08/13
	 */
	public abstract String osArchitecture();
	
	/**
	 * Returns the name of the operating system.
	 *
	 * @return The operating system name.
	 * @since 2017/08/13
	 */
	public abstract String osName();
	
	/**
	 * Returns the version of the operating system.
	 *
	 * @return The operating system version.
	 * @since 2017/08/13
	 */
	public abstract String osVersion();
	
	/**
	 * Returns the {@code PATH} variable separator that the system uses.
	 *
	 * @return The separator used for the {@code PATH} variable.
	 * @since 2017/08/13
	 */
	public abstract String pathSeparator();
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/10/02
	 */
	protected abstract String protectedJavaVMEmail();
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/10/02
	 */
	protected abstract String protectedJavaVMName();
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/10/02
	 */
	protected abstract String protectedJavaVMURL();
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/10/02
	 */
	protected abstract String protectedJavaVMVendor();
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/08/13
	 */
	protected abstract String protectedJavaVMVersionFull();
	
	/**
	 * Returns the short version of the Java virtual machine.
	 *
	 * @return The short Java virtual machine version, if this is a
	 * SquirrelJME VM then the return value can be {@code null} because it is
	 * not used.
	 * @since 2017/08/13
	 */
	protected abstract String protectedJavaVMVersionShort();
	
	/**
	 * Returns the account name of the user. This is the simplified account
	 * name of the user, rather than {@code Stephanie Gawroriski} this would
	 * return {@code stephanie} assuming that is what the account exists under
	 * on the system.
	 *
	 * @return The account name of the user.
	 * @since 2017/08/13
	 */
	public abstract String userAccountName();
	
	/**
	 * Returns the version of the class libraries.
	 *
	 * @return The class library version.
	 * @since 2017/10/02
	 */
	public final String javaRuntimeVersion()
	{
		return "0.0.2";
	}
	
	/**
	 * Returns the e-mail to contact for the virtual machine.
	 *
	 * @return The contact e-mail for the virtual machine.
	 * @since 2017/10/02
	 */
	public final String javaVMEmail()
	{
		if (isSquirrelJMEJVM())
			return "xer@multiphasicapps.net";
		return protectedJavaVMEmail();
	}
	
	/**
	 * Returns the name of the Java virtual machine.
	 *
	 * @return The name of the virtual machine.
	 * @since 2017/10/02
	 */
	public final String javaVMName()
	{
		if (isSquirrelJMEJVM())
			return "SquirrelJME";
		return protectedJavaVMName();
	}
	
	/**
	 * Returns the URL to the virtual machine's vendor's URL.
	 *
	 * @return The URL of the JVM's virtual machine.
	 * @since 2017/10/02
	 */
	public final String javaVMURL()
	{
		if (isSquirrelJMEJVM())
			return "http://multiphasicapps.net/";
		return protectedJavaVMURL();
	}
	
	/**
	 * Returns the vendor of the Java virtual machine.
	 *
	 * @return The vendor of the Java virtual machine.
	 * @since 2017/10/02
	 */
	public final String javaVMVendor()
	{
		if (isSquirrelJMEJVM())
			return "Stephanie Gawroriski";
		return protectedJavaVMVendor();
	}
	
	/**
	 * Returns the full version of the Java virtual machine.
	 *
	 * @return The full Java virtual machine version.
	 * @since 2017/08/13
	 */
	public final String javaVMVersionFull()
	{
		if (isSquirrelJMEJVM())
			return "1.8.0-0.0.2";
		return protectedJavaVMVersionFull();
	}
	
	/**
	 * Returns the short version of the Java virtual machine.
	 *
	 * @return The short Java virtual machine version.
	 * @since 2017/08/13
	 */
	public final String javaVMVersionShort()
	{
		if (isSquirrelJMEJVM())
			return "1.8.0";
		return protectedJavaVMVersionShort();
	}
}

