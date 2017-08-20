// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This contains support for environmental factors within the build system.
 *
 * @since 2017/08/10
 */
final class __Ext_systemenvironment__
{
	/**
	 * Not used.
	 *
	 * @since 2017/08/10
	 */
	private __Ext_systemenvironment__()
	{
	}
	
	/**
	 * Maps the given class service name to a class which implements the
	 * given service.
	 *
	 * @param __v The class to map a service for.
	 * @return The class which implements the given service or {@code null}
	 * if it does not exist.
	 * @sincem 2017/08/10 
	 */
	public static String mapService(String __v)
	{
		switch (__v)
		{
				// Display engine for swing output
			case "net.multiphasicapps.squirreljme.lcdui.DisplayHeadProvider":
				return "net.multiphasicapps.squirreljme.build.host." +
					"javase.SwingDisplayHeadProvider";
				
				// Record store manager
			case "net.multiphasicapps.squirreljme.rms.RecordClusterManager":
				return "net.multiphasicapps.squirreljme.rms.file." +
					"FileRecordClusterManager";
				
				// Not mapped
			default:
				return null;
		}
	}
}

