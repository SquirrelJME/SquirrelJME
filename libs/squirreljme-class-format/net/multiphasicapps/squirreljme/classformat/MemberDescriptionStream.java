// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This is the base description stream for class members such as fields and
 * methods.
 *
 * @since 2016/09/09
 */
public interface MemberDescriptionStream
{
	/**
	 * This is called when the member has finished parsing.
	 *
	 * @since 2016/09/09
	 */
	public abstract void endMember();
}

