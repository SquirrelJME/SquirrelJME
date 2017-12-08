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
}

