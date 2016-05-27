// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIAccessibleObject;
import net.multiphasicapps.squirreljme.ci.CIByteBuffer;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.ci.CIClassReference;
import net.multiphasicapps.squirreljme.ci.CILookup;
import net.multiphasicapps.squirreljme.ci.CIPool;
import net.multiphasicapps.util.empty.EmptyList;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * Operation initialization data.
 *
 * @since 2016/05/13
 */
final class __OpInitData__
{
	/** The operation to modify. */
	final BCOperation operation;
	
	/** Arguments of the operation. */
	private volatile List<Object> _arguments;
	
	/** The local variables which are accessed. */
	private volatile List<BCLocalAccess> _localaccess;
	
	/** Variables types which are popped from the stack. */
	private volatile List<BCVariableType> _stackpop;
	
	/** Variable types which are pushed to the stack. */
	private volatile List<BCVariablePush> _stackpush;
	
	/** Rewritten operation. */
	private volatile int _rewrite;
	
	/** Was rewritten? */
	private volatile boolean _wasrewritten;
	
	/**
	 * Inititalizes the operation initialization data.
	 *
	 * @param __op The operation to set data for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/13
	 */
	__OpInitData__(BCOperation __op)
		throws NullPointerException
	{
		// Check
		if (__op == null)
			throw new NullPointerException("NARG");
		
		// Set
		operation = __op;
	}
	
	/**
	 * Returns the code buffer used.
	 *
	 * @return The code buffer data.
	 * @since 2016/05/13
	 */
	public CIByteBuffer byteBuffer()
	{
		return this.operation.owner().codeBuffer();
	}
	
	/**
	 * Forwards access check.
	 *
	 * @param __ao The object to check access against.
	 * @return {@code true} if it can be accessed.
	 * @since 2016/05/13
	 */
	public boolean canAccess(CIAccessibleObject __ao)
	{
		return this.operation.owner().canAccess(__ao);
	}
	
	/**
	 * Returns the operation arguments.
	 *
	 * @return The operation arguments.
	 * @since 2016/05/13
	 */
	public List<Object> getArguments()
	{
		List<Object> rv = this._arguments;
		if (rv == null)
			return setArguments();
		return rv;
	}
	
	/**
	 * Returns the accessed local variables.
	 *
	 * @return The accessed local variables.
	 * @since 2016/05/13
	 */
	public List<BCLocalAccess> getLocalAccess()
	{
		List<BCLocalAccess> rv = this._localaccess;
		if (rv == null)
			return setLocalAccess();
		return rv;
	}
	
	/**
	 * Returns the potentially rewritten operation ID.
	 *
	 * @return The rewrite ID.
	 * @since 2016/05/13
	 */
	public int getRewrite()
	{
		return _rewrite;
	}
	
	/**
	 * Returns the popped stack values.
	 *
	 * @return The popped stack values.
	 * @since 2016/05/13
	 */
	public List<BCVariableType> getStackPop()
	{
		List<BCVariableType> rv = this._stackpop;
		if (rv == null)
			return setStackPop();
		return rv;
	}
	
	/**
	 * Returns the pushed stack values.
	 *
	 * @return The popped stack values.
	 * @since 2016/05/13
	 */
	public List<BCVariablePush> getStackPush()
	{
		List<BCVariablePush> rv = this._stackpush;
		if (rv == null)
			return setStackPush();
		return rv;
	}
	
	/**
	 * Returns the class lookup.
	 *
	 * @return The lookup for classes.
	 * @since 2016/05/14
	 */
	public CILookup lookup()
	{
		return this.operation.owner().lookup();
	}
	
	/**
	 * Finds the class by the given class name.
	 *
	 * @param __cl The class to find.
	 * @return The discovered class or {@code null} if not found.
	 * @since 2016/05/13
	 */
	public CIClass lookup(ClassNameSymbol __cl)
	{
		return lookup().lookupClass(__cl);
	}
	
	/**
	 * Returns the physical address of the operation.
	 *
	 * @return The operation physical address.
	 * @since 2016/05/13
	 */
	public int physicalAddress()
	{
		return this.operation.physicalAddress();
	}
	
	/**
	 * Returns the associated constant pool with this byte code.
	 *
	 * @return The constant pool of the byte code.
	 * @since 2016/05/13
	 */
	public CIPool pool()
	{
		return this.operation.owner().constantPool();
	}
	
	/**
	 * Rewrites the operation so it uses another opcode.
	 *
	 * @param __oc The operation to rewrite it as.
	 * @throws IllegalStateException If it was already rewritten.
	 * @since 2016/05/13
	 */
	public void rewrite(int __oc)
		throws IllegalStateException
	{
		// {@squirreljme.error AX0v The operation ID was already rewritten.}
		if (_wasrewritten)
			throw new IllegalStateException("AX0v");
		
		// Set it
		_rewrite = __oc;
		_wasrewritten = true;
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @throws IllegalStateException If the value are already set.
	 * @since 2016/05/12
	 */
	public List<Object> setArguments(Object... __a)
		throws IllegalStateException
	{
		// {@squirreljme.error AX0p Arguments already set.}
		if (this._arguments != null)
			throw new IllegalStateException("AX0p");
		
		if (__a == null || __a.length <= 0)
			return EmptyList.<Object>empty();
		return (this._arguments = UnmodifiableList.<Object>of(
			Arrays.<Object>asList(__a)));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @throws IllegalStateException If the value are already set.
	 * @since 2016/05/12
	 */
	public List<BCLocalAccess> setLocalAccess(BCLocalAccess... __a)
		throws IllegalStateException
	{
		// {@squirreljme.error AX0q Local access already set.}
		if (this._localaccess != null)
			throw new IllegalStateException("AX0q");
		
		if (__a == null || __a.length <= 0)
			return EmptyList.<BCLocalAccess>empty();
		return (this._localaccess = UnmodifiableList.<BCLocalAccess>of(
			Arrays.<BCLocalAccess>asList(__a)));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @throws IllegalStateException If the value are already set.
	 * @since 2016/05/12
	 */
	public List<BCVariableType> setStackPop(BCVariableType... __a)
		throws IllegalStateException
	{
		// {@squirreljme.error AX0r Stack pop already set.}
		if (this._stackpop != null)
			throw new IllegalStateException("AX0r");
		
		if (__a == null || __a.length <= 0)
			return EmptyList.<BCVariableType>empty();
		return (this._stackpop = UnmodifiableList.<BCVariableType>of(
			Arrays.<BCVariableType>asList(__a)));
	}
	
	/**
	 * Simple wrapper into unmodifiable list.
	 *
	 * @param __a Input arguments.
	 * @return The wrapped input arguments.
	 * @throws IllegalStateException If the value are already set.
	 * @since 2016/05/12
	 */
	public List<BCVariablePush> setStackPush(BCVariablePush... __a)
		throws IllegalStateException
	{
		// {@squirreljme.error AX0s Stack push already set.}
		if (this._stackpush != null)
			throw new IllegalStateException("AX0s");
		
		if (__a == null || __a.length <= 0)
			return EmptyList.<BCVariablePush>empty();
		return (this._stackpush = UnmodifiableList.<BCVariablePush>of(
			Arrays.<BCVariablePush>asList(__a)));
	}
	
	/**
	 * Returns the verification input.
	 *
	 * @return The verification input.
	 * @since 2016/05/13
	 */
	public BCStateVerification verificationInput()
	{
		return this.operation.verificationInput();
	}
}

