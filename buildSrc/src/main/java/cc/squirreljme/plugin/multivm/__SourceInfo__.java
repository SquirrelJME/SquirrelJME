// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;

/**
 * Information on a source code file.
 *
 * @since 2020/10/09
 */
final class __SourceInfo__
{
	/** The name of the current class. */
	public final String thisClass;
	
	/** The super-class of this one, if one is set. */
	public final String superClass;
	
	/**
	 * Initializes the source information.
	 * 
	 * @param __thisClass The current class.
	 * @param __superClass The super class, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	__SourceInfo__(String __thisClass, String __superClass)
		throws NullPointerException
	{
		if (__thisClass == null)
			throw new NullPointerException("NARG");
		
		this.thisClass = __thisClass;
		this.superClass = __superClass;
	}
	
	/**
	 * Loads the source information.
	 * 
	 * @param __in The file to read from.
	 * @return The information on the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	public static __SourceInfo__ load(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		StreamTokenizer stream = new StreamTokenizer(__in);
		
		// Initialize tokenizer parameters
		stream.slashStarComments(true);
		stream.slashSlashComments(true);
		stream.parseNumbers();
		stream.wordChars('.', '.'); 
		stream.wordChars('_', '_');
		
		
		throw new Error("TODO");
	}
}
