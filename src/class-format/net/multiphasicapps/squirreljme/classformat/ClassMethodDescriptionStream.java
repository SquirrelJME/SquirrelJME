// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This is the description stream for methods within a class.
 *
 * @since 2016/09/09
 */
public interface ClassMethodDescriptionStream
	extends ClassMemberDescriptionStream
{
	/**
	 * This is called to report that the method has no associated byte-code.
	 *
	 * @since 2016/09/09
	 */
	public abstract void noCode();
}

