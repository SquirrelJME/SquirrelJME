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
 * This is the stream that is used for describing fields.
 *
 * @since 2016/09/09
 */
public interface ClassFieldDescriptionStream
	extends ClassMemberDescriptionStream
{
	/**
	 * This is called when the constant value is known.
	 *
	 * @param __v The constant value, which may be {@code null} if there is
	 * none.
	 * @since 2016/09/09
	 */
	public abstract void constantValue(Object __v);
}

