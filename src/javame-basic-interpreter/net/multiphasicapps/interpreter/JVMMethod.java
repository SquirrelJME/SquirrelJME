// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.classfile.CFMethodFlags;
import net.multiphasicapps.classprogram.CPProgram;
import net.multiphasicapps.classprogram.CPProgramBuilder;
import net.multiphasicapps.classprogram.CPProgramException;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a bound method within a class.
 *
 * @since 2016/04/04
 */
public class JVMMethod
	extends JVMMember<MethodSymbol, CFMethodFlags, CFMethod, JVMMethod>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The current program. */
	private volatile Reference<CPProgram> _program;
	
	/**
	 * Initializes the method.
	 *
	 * @param __o The owning group.
	 * @param __b The base for it.
	 * @since 2016/04/05
	 */
	JVMMethod(JVMMethods __o, CFMethod __b)
	{
		super(__o, __b);
	}
	
	/**
	 * Returns the program of the current method.
	 *
	 * @return The method's program.
	 * @since 2016/04/06
	 */
	public CPProgram program()
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<CPProgram> ref = _program;
			CPProgram rv;
			
			// Needs loading?
			if (ref == null || null == (rv = ref.get()))
				try (InputStream is = base.codeAttribute())
				{
					// {@squirreljme.error IN0a The current method has no
					// defined program, it is likely {@code abstract} or
					// {@code native}. (The current method)}
					if (is == null)
						throw new JVMClassFormatError(String.format("IN0a %s",
							this));
					
					// Load it
					_program = new WeakReference<>((rv = new CPProgramBuilder(
						container().outerClass().base(), base).parse(is)));
				}
				
				// Failed to load program
				catch (CPProgramException|IOException e)
				{
					// {@squirreljme.error IN09 Could not get the program for
					// the current method either because it does not exist or
					// it is not a valid program. (The current method)}
					throw new JVMClassFormatError(String.format("IN09 %s",
						this), e);
				}
			
			return rv;
		}
	}
}

