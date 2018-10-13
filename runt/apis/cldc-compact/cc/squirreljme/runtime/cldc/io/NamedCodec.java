// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

/**
 * This is used to get the name of the encoder or decoder that was used.
 *
 * @since 2018/10/13
 */
public interface NamedCodec
{
	/**
	 * Returns the name of the encoding.
	 *
	 * @return The encoding name.
	 * @since 2018/10/13
	 */
	public abstract String encodingName();
}

