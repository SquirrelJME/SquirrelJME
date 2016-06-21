// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

import net.multiphasicapps.squirreljme.ci.CICodeAttribute;
import net.multiphasicapps.classwriter.OutputCode;

/**
 * This is a translator which is used to translate byte code so that it
 * executes in a fashion which is easily virtualized by the PVM environment.
 *
 * All references to classes instead refer to the mangled variants of them.
 *
 * Arrays are also purely virtualized by special classes since calling
 * {@link Object} methods on these classes will cause environment breaking
 * events to occur.
 *
 * @since 2016/06/21
 */
public class PVMCodeTranslator
{
	/** The class loader which owns this code translator. */
	protected final PVMClassLoader classloader;
	
	/** The input code attribute. */
	protected final CICodeAttribute codeattribute;
	
	/** The output code. */
	protected final OutputCode output;
	
	/**
	 * Initializes the code translator.
	 *
	 * @param __cl The owning class loader.
	 * @param __ic The input code.
	 * @param __oc The output code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	public PVMCodeTranslator(PVMClassLoader __cl, CICodeAttribute __ic,
		OutputCode __oc)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __ic == null || __oc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classloader = __cl;
		this.codeattribute = __ic;
		this.output = __oc;
	}
	
	/**
	 * Performs the actual translation.
	 *
	 * @since 2016/06/21
	 */
	public void translate()
	{
		// Get the output
		OutputCode output = this.output;
		
		throw new Error("TODO");
	}
}

