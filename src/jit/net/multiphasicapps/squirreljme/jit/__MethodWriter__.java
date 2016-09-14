// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This bridges the method description stream to the method writer.
 *
 * @since 2016/09/10
 */
class __MethodWriter__
	extends __MemberWriter__
	implements MethodDescriptionStream
{
	/** The method writer to write to. */
	protected final JITMethodWriter methodwriter;
	
	/** Method flags. */
	final MethodFlags _flags;
	
	/** Method descriptor. */
	final MethodSymbol _type;
	
	/** The current code writer. */
	private volatile __CodeWriter__ _code;
	
	/** Was code parsing performed? */
	private volatile boolean _didcode;
	
	/**
	 * Initializes the method writer.
	 *
	 * @param __jit The running JIT.
	 * @param __nsw The namespace writer.
	 * @param __cw The owning class writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	__MethodWriter__(JIT __jit, JITNamespaceWriter __nsw, __ClassWriter__ __cw,
		JITMethodWriter __jmw, MethodFlags __f, MethodSymbol __d)
		throws NullPointerException
	{
		super(__jit, __nsw, __cw);
		
		// Check
		if (__jmw == null || __f == null || __d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.methodwriter = __jmw;
		this._flags = __f;
		this._type = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public CodeDescriptionStream code()
	{
		// {@squirreljme.error ED05 Cannot add code to a method because code
		// or no code was already specified.}
		if (this._didcode)
			throw new JITException("ED05");
		this._didcode = true;
		
		// Create
		__CodeWriter__ code = new __CodeWriter__(this.jit, this.namespace,
			this, this.methodwriter.code());
		this._code = code;
		return code;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void endMember()
	{
		// {@squirreljme.error ED0a Whether there is code or is not code, was
		// never specified.}
		if (!this._didcode)
			throw new JITException("ED0a");
		
		// Close code before the member to closed
		__CodeWriter__ code = this._code;
		if (code != null)
		{
			code.close();
			this._code = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public void noCode()
	{
		// {@squirreljme.error ED09 Cannot specify no code for a method because
		// code or no code was already specified.}
		if (this._didcode)
			throw new JITException("ED09");
		this._didcode = true;
	}
}

