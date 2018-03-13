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
 * This interface represents something which represents the name of a file.
 *
 * @since 2018/03/12
 */
public interface FileName
	extends LocationAware
{
	/**
	 * Returns the file that is input represents or the file being processed.
	 *
	 * @return The file being process or {@code null} if it is not known.
	 * @since 2018/03/12
	 */
	public abstract String fileName();
}

