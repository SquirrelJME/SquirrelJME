// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This describes the code attribute of a method.
 *
 * @since 2016/04/20
 */
public final class CFCodeAttribute
{
	/** The owning method. */
	protected final CFMethod method;
	
	/** The attribute data. */
	private final byte[] _data;
	
	/**
	 * Initializes the code attribute.
	 *
	 * @param __meth The owning method.
	 * @param __ad The attribute data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/20
	 */
	CFCodeAttribute(CFMethod __meth, byte... __ad)
		throws NullPointerException
	{
		// Check
		if (__meth == null || __ad == null)
			throw new NullPointerException("NARG");
		
		// Set
		method = __meth;
		_data = __ad;
	}
	
	/**
	 * Returns an input stream over the attribute data.
	 *
	 * @return The input over the attribute data.
	 * @since 2016/04/20
	 */
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(_data);
	}
}

