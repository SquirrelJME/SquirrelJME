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
 * This is the description stream for methods within a class.
 *
 * @since 2016/09/09
 */
public interface MethodDescriptionStream
	extends MemberDescriptionStream
{
	/**
	 * This is called when the method parser wants to provide the details of
	 * decoded byte code operations.
	 *
	 * @return The description stream which would get the class decoding
	 * details.
	 * @since 2016/09/09
	 */
	public abstract CodeDescriptionStream code();
	
	/**
	 * This is called to report that the method has no associated byte-code.
	 *
	 * @since 2016/09/09
	 */
	public abstract void noCode();
}

