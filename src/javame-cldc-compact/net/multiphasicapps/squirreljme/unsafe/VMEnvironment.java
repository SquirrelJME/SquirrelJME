// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This contains the virtual environment details.
 *
 * @since 2016/06/16
 */
public abstract class VMEnvironment
{
	/**
	 * Returns the unique identifier which identifies this device.
	 *
	 * @return The device unique identifier.
	 * @since 2016/06/16
	 */
	public abstract String deviceUUID();
	
	/**
	 * Returns the architecture that this virtual machine is running on.
	 *
	 * @return The architecture name.
	 * @since 2016/06/16
	 */
	public abstract String osArch();
	
	/**
	 * Returns the name of the operating system that this virtual machine is
	 * running on.
	 *
	 * @return The operating system name.
	 * @since 2016/06/16
	 */
	public abstract String osName();
	
	/**
	 * Returns the name of the device that SquirrelJME is running on, this may
	 * be the name of the operating system or the name of the actual hardware
	 * running underneath.
	 *
	 * The format for the returned value is:
	 * {@code (Manufacturer)(DeviceModelNumber)[/version[/comments]]}.
	 *
	 * @return The platform SquirrelJME is running on.
	 * @since 2016/06/15
	 */
	public abstract String osPlatform();
	
	/**
	 * Returns the version of the operating system this is running on.
	 *
	 * @return The operating system version.
	 * @since 2016/06/16
	 */
	public abstract String osVersion();
	
	/**
	 * Returns the host name of this system as it appears on the network to
	 * other systems.
	 *
	 * @return The host name of this system.
	 * @since 2016/06/16
	 */
	public String osHostName()
	{
		return "localhost";
	}
}

