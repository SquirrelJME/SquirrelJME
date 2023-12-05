// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.Closeable;
import java.io.IOException;

/**
 * Output for tokens.
 *
 * @since 2023/06/19
 */
public interface CTokenOutput
	extends Closeable
{
	/**
	 * Adjusts the indentation level, this is used as a helper of sorts.
	 * 
	 * @param __adjust The adjustment to make.
	 * @since 2023/06/19
	 */
	void indent(int __adjust);
	
	/**
	 * Emits a newline.
	 * 
	 * @param __force If the newline should be forced because this is a start
	 * of a preprocessor line.
	 * @throws IOException On write errors.
	 * @since 2023/06/19
	 */
	void newLine(boolean __force)
		throws IOException;
	
	/**
	 * This is called to indicate what is about to be output so that the
	 * token output can handle that accordingly if needed.
	 *
	 * @param __pivot The pivot to use.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/22
	 */
	void pivot(CPivotPoint __pivot)
		throws IOException, NullPointerException;
	
	/**
	 * Emits a space.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/06/19
	 */
	void space()
		throws IOException;
	
	/**
	 * Emits a tab.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/06/19
	 */
	void tab()
		throws IOException;
	
	/**
	 * Emits a token.
	 * 
	 * @param __cq The token to emit.
	 * @param __forceNewline If a newline is needed at the end because this is
	 * a preprocessor line or single line comment.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException;
}
