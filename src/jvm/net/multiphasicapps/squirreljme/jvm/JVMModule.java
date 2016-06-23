// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jvm;

/**
 * This interface describes a module which provides specific categorized
 * functionality that the JVM may use (such as accessing filesystems).
 *
 * @since 2016/06/23
 */
public interface JVMModule
{
	/**
	 * Returns the category that this module is within.
	 *
	 * @return The category of the module.
	 * @since 2016/06/23
	 */
	public abstract JVMModule.Category category();
	
	/**
	 * This returns the name of the module.
	 *
	 * @return The module name.
	 * @since 2016/06/23
	 */
	public abstract String name();
	
	/**
	 * This is the category that a module exists in.
	 *
	 * @since 2016/06/23
	 */
	public static enum Category
	{
		/**
		 * This is a module which exposes block devices, block devices may be
		 * partitioned using a common disk partition scheme or may be raw.
		 * Block devices are used to store potential filesystem data, so that
		 * if a block device is found an attempt will be made to mount a
		 * filesystem on it.
		 */
		BLOCK_DEVICE,
		
		/**
		 * Provides a filesystem for accessing files on the disk or some
		 * other storage medium. Filesystems may be backed by block devices
		 * although it is not required.
		 */
		FILESYSTEM,
		
		/**
		 * This module provides a framebuffer for which graphics may be
		 * displayed upon.
		 * If the line based interface is used then a text console will be
		 * virtualized on top of the framebuffer.
		 */
		FRAMEBUFFER,
		
		/**
		 * This module provides a text based console which may be used by the
		 * line based user interface.
		 */
		TEXT_CONSOLE,
		
		/** End. */
		;
	}
}

