// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.classfile.CFCodeAttribute;
import net.multiphasicapps.classfile.CFMethod;

/**
 * This class is given a method which is then parsed.
 *
 * @since 2016/04/20
 */
public class NCPCodeParser
{
	/** The class file this is in. */
	protected final CFClass classfile;
	
	/** The method being parsed. */
	protected final CFMethod method;
	
	/** The input code attribute. */
	protected final CFCodeAttribute codeattr;
	
	/**
	 * Initializes the code parser.
	 *
	 * @param __cf The class containing the method to parse.
	 * @param __meth The method to parse.
	 * @throws IllegalArgumentException If the class does not contain the given
	 * method.
	 * @throws NullPointerException
	 */
	public NCPCodeParser(CFClass __cf, CFMethod __meth)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__cf == null || __meth == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NA01 The specified class does not contain the
		// given method. (The class; The method)}
		if (!__cf.methods().containsValue(__meth))
			throw new IllegalArgumentException(String.format("NA01 %s %s",
				__cf, __meth));
		
		// Set
		classfile = __cf;
		method = __meth;
		codeattr = method.codeAttribute();
	}
}

