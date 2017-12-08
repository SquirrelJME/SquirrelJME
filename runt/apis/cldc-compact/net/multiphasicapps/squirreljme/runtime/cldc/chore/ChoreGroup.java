// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.chore;

/**
 * This represents a group of chores which a running application may be within,
 * it is used for security purposes. All chores within the same group share
 * the same set of permissions.
 *
 * Groups do contain chores but this class is used in a way as a marker by
 * chores to indicate the group they are in.
 *
 * @since 2017/12/08
 */
public abstract class ChoreGroup
{
	/** Is this a system group? */
	public static final int FLAG_SYSTEM =
		0b0000_0000__0000_0000___0000_0000__0000_0001;
	
	/** Client can manage tasks? */
	public static final int BASIC_PERMISSION_CLIENT_MANAGE_TASKS =
		0b0000_0000__0000_0000___0000_0000__0000_0001;
	
	/** Cross client can manage tasks? */
	public static final int BASIC_PERMISSION_CROSSCLIENT_MANAGE_TASKS =
		0b0000_0000__0000_0000___0000_0000__0000_0010;
	
	/**
	 * Returns the basic permissions of the group.
	 *
	 * @return The basic permissions.
	 * @since 2017/12/08
	 */
	public abstract int basicPermissions();
	
	/**
	 * Returns the flags for this group.
	 *
	 * @return The group flags.
	 * @since 2017/12/08
	 */
	public abstract int flags();
	
	/**
	 * Is this a system chore group?
	 *
	 * @return {@code true} if this is the system chore group.
	 * @since 2017/12/08
	 */
	public final boolean isSystem()
	{
		return 0 != (this.flags() & FLAG_SYSTEM);
	}
}

