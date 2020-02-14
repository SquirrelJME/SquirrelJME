// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This interface represents a source for tokens.
 *
 * @since 2018/04/18
 */
public interface TokenSource
	extends FileNameLineAndColumn
{
	/**
	 * Returns the next token from the input source.
	 *
	 * @return The next token, if there are no more tokens left then a special
	 * end of file token should be used instead.
	 * @throws TokenizerException If the read input token is not valid.
	 * @since 2018/04/18
	 */
	public abstract Token next()
		throws TokenizerException;
}

