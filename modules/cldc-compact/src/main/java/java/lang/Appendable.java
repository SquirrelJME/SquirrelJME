// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

/**
 * This is an appendable which is given characters and character sequences
 * to be written into the output.
 *
 * @since 2018/12/07
 */
@Api
public interface Appendable
{
	/**
	 * Appends a character sequence to the output.
	 *
	 * @param __c The sequence to append.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2018/12/07
	 */
	@Api
	Appendable append(CharSequence __c)
		throws IOException;
	
	/**
	 * Appends a character sequence to the output.
	 *
	 * @param __c The sequence to append.
	 * @param __s The start index.
	 * @param __e The end index.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the start or end is outside of
	 * the bounds of the sequence, or the start exceeds the end.
	 * @throws IOException On write errors.
	 * @since 2018/12/07
	 */
	@Api
	Appendable append(CharSequence __c, int __s, int __e)
		throws IndexOutOfBoundsException, IOException;
	
	/**
	 * Appends a single character to the output.
	 *
	 * @param __c The character to append.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2018/12/07
	 */
	@Api
	Appendable append(char __c)
		throws IOException;
}

