// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.event;

/**
 * The list of system event codes that may occur.
 *
 * @since 2016/05/15
 */
public interface SystemEventCode
{
	/** A controller was attached to a port. */
	public static final int CONTROLLER_ATTACHED =
		1;
	
	/** A controller was removed from a port. */
	public static final int CONTROLLER_DETACHED =
		2;
	
	/** A block device was inserted. */
	public static final int BLOCK_DEVICE_INSERTED =
		3;
	
	/** A block device was removed. */
	public static final int BLOCK_DEVICE_REMOVED =
		4;
}

