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

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.MethodFlags;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.util.boolset.BooleanSet;
import net.multiphasicapps.util.boolset.FixedSizeBooleanSet;

/**
 * This acts as the middle layer between the class format decoded class files
 * and the JIT that is used to generate native code.
 *
 * @since 2016/09/14
 */
class __CodeWriter__
	implements AutoCloseable, CodeDescriptionStream
{
	/** The owning JIT. */
	protected final JIT jit;
	
	/** The namespace writer. */
	protected final JITNamespaceWriter namespace;
	
	/** The method writer. */
	protected final __MethodWriter__ methodwriter;
	
	/** The code writer. */
	protected final JITCodeWriter codewriter;
	
	/** The state of the stack cache. */
	volatile __CacheState__ _cache;
	
	/** Jump target locations. */
	volatile BooleanSet _jumptargets;
	
	/**
	 * Initialize the code writer.
	 *
	 * @param __jit The JIT used for compilation.
	 * @param __nsw The namespace writer.
	 * @param __mw The owning method writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	__CodeWriter__(JIT __jit, JITNamespaceWriter __nsw, __MethodWriter__ __mw,
		JITCodeWriter __jcw)
		throws NullPointerException
	{
		// Check
		if (__jit == null || __nsw == null || __mw == null || __jcw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.jit = __jit;
		this.namespace = __nsw;
		this.methodwriter = __mw;
		this.codewriter = __jcw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		// Debug
		System.err.printf("DEBUG -- At %d (is %d) <%s>%n", __pos, __code,
			this._cache);
		
		// If this is a jump target then restore the current stack cache state
		if (this._jumptargets.get(__pos))
		{
			throw new Error("TODO");
		}
		
		// Forward
		this.codewriter.atInstruction(__code, __pos);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void close()
		throws JITException
	{
		// Forward
		this.codewriter.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public void codeLength(int __n)
	{
		// Valid jump target positions
		this._jumptargets = new FixedSizeBooleanSet(__n);
		
		// Forward
		this.codewriter.codeLength(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public void copy(StackMapType __type, CodeVariable __from,
		CodeVariable __to)
		throws NullPointerException
	{
		// Check
		if (__type == null || __from == null || __to == null)
			throw new NullPointerException("NARG");;
		
		// Debug
		System.err.printf("DEBUG -- %s:%s -> %s%n", __type, __from, __to);
		
		// Destination is the stack? Cache it
		if (__to.isStack())
		{
			__CacheState__ cache = this._cache;
			
			// If the source is the stack, see if it caches another value
			if (__from.isStack())
			{
				CodeVariable var = cache._stack[__from.id()];
				
				// If the variable is a local variable then cache its value
				// and not the stack entry it points to
				if (var.isLocal())
					__from = var;
			}
			
			// Store in the cache
			cache._stack[__to.id()] = __from;
		}
		
		// The destination is a local variable, the model for SquirrelJME is
		// that locals are always saved when they are written to (for exception
		// handling purposes).
		else
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		// Fill in jump targets
		BooleanSet jumptargets = this._jumptargets;
		for (int i : __t)
			jumptargets.set(i, true);
		
		// Forward
		this.codewriter.jumpTargets(__t);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/14
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		// Setup cache
		this._cache = new __CacheState__(__ms);
		
		// Forward to the sub-jit
		JITCodeWriter codewriter = this.codewriter;
		codewriter.variableCounts(__ms, __ml);
		
		// Setup arguments for priming
		__MethodWriter__ m = this.methodwriter;
		MethodFlags flags = m._flags;
		MethodSymbol mdes = m._type;
		
		// If the method is not static, add object
		List<StackMapType> vars = new ArrayList<>();
		if (!flags.isStatic())
			vars.add(StackMapType.OBJECT);
		
		// Go through all arguments and add their types accordingly
		int n = mdes.argumentCount();
		for (int i = 0; i < n; i++)
		{
			// Get argument and convert to stack map type
			FieldSymbol arg = mdes.get(i);
			StackMapType smt = StackMapType.bySymbol(arg);
			
			// Record it
			vars.add(smt);
			
			// If it is wide, add top
			if (smt.isWide())
				vars.add(StackMapType.TOP);
		}
		
		// Prime
		codewriter.primeArguments(vars.<StackMapType>toArray(
			new StackMapType[vars.size()]));
	}
}

