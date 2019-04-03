// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.ByteCode;

/**
 * The translation method used for converting byte code into register code.
 *
 * @since 2019/04/03
 */
public enum TranslationMethod
{
	/** Quick translation. */
	QUICK,
	
	/** Experimental. */
	EXPERIMENTAL,
	
	/** End. */
	;
	
	/** The default mode. */
	public static final TranslationMethod DEFAULT =
		TranslationMethod.QUICK;
	
	/**
	 * Creates a translator for the given byte code for the given method.
	 *
	 * @param __bc The byte code to translate.
	 * @return The translator.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public final Translator translator(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		switch (this)
		{
			case QUICK:			return new QuickTranslator(__bc);
			case EXPERIMENTAL:	return new ExperimentalTranslator(__bc);
		}
		
		throw new todo.OOPS();
	}
}

