// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is a connector that is used to allow the {@link DisplayInstance} (via
 * the {@link DisplayEngine}) to allow it to provide some events such as
 * drawing to the {@link Displayable}.
 *
 * @since 2017/02/08
 */
@Deprecated
public interface DisplayConnector
{
	/**
	 * This is caled to indicate that the size of the displayable changed
	 * size.
	 *
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2017/02/10
	 */
	public abstract void sizeChanged(int __w, int __h);
}

