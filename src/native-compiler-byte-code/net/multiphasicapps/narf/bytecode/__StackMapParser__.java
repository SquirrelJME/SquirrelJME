// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.narf.classinterface.NCIMethod;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This parses the stack map table using either the modern Java 6 format or
 * the ancient CLDC 1.0 format.
 *
 * Since both formats are virtually the same (and the modern format being based
 * on the older format), the same code can be used during the parsing stage.
 *
 * @since 2016/03/25
 */
class __StackMapParser__
{
	/** Use the modern StackMapTable parser? */
	protected final boolean modern;
	
	/** The input source. */
	protected final NCIByteBuffer in;
	
	/** The owning code attribute. */
	protected final NBCByteCode code;
	
	/** The output verification map. */
	protected final Map<Integer, NBCStateVerification> outputmap =
		new HashMap<>();
	
	/**
	 * This initializes and performs the parsing.
	 *
	 * @param __m Parse the moderm format?
	 * @param __in The input stream containing the data.
	 * @param __code The code attribute.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	__StackMapParser__(boolean __m, NCIByteBuffer __in, NBCByteCode __code)
		throws NullPointerException
	{
		// Check
		if (__in == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.modern = __m;
		this.in = __in;
		this.code = __code;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the verification result.
	 *
	 * @return The result of verification.
	 * @since 2016/05/12
	 */
	public Map<Integer, NBCStateVerification> result()
	{
		return UnmodifiableMap.<Integer, NBCStateVerification>of(outputmap);
	}
}

