// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This interface specifies that the class provides row and column information
 * which can be used to locate errors and warnings in code.
 *
 * @since 2018/03/06
 */
public interface LineAndColumn
{
	/**
	 * Returns the current column.
	 *
	 * @return The current column.
	 * @since 2018/03/06
	 */
	public abstract int column();
	
	/**
	 * Returns the current line.
	 *
	 * @return The current line.
	 * @since 2018/03/06
	 */
	public abstract int line();
}

