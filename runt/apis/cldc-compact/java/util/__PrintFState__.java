// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This stores the state for printf parsing.
 *
 * @since 2018/09/24
 */
final class __PrintFState__
{
	/** Global state. */
	final __PrintFGlobal__ _global;
	
	/** Flags specified. */
	final boolean[] _flags =
		new boolean[__PrintFFlag__.COUNT];
	
	/** The argument index. */
	int _argdx =
		-1;
	
	/** The width. */
	int _width =
		-1;
	
	/** The precision. */
	int _precision =
		-1;
	
	/** The conversion used. */
	__PrintFConversion__ _conv;
	
	/** Is the conversion to be uppercase? */
	boolean _upper;
	
	/**
	 * Initializes the state.
	 *
	 * @param __pg The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/29
	 */
	__PrintFState__(__PrintFGlobal__ __pg)
		throws NullPointerException
	{
		if (__pg == null)
			throw new NullPointerException("NARG");
		
		this._global = __pg;
	}
	
	/**
	 * Returns the specified argument.
	 *
	 * @param <C> The return type.
	 * @param __i The index.
	 * @return The argument value.
	 * @throws IllegalArgumentException If the argument is not valid.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/09/29
	 */
	final <C> C __argument(Class<C> __cl)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ23 Null argument was passed.}
		C rv = this.<C>__argument(__cl, null);
		if (rv == null)
			throw new NullPointerException("ZZ23");
		return rv;
	}
	
	/**
	 * Returns the specified argument.
	 *
	 * @param <C> The return type.
	 * @param __i The index.
	 * @param __def The default value, if the input is null then this will
	 * be returned.
	 * @return The argument value or the default if it was null.
	 * @throws IllegalArgumentException If the argument is not valid.
	 * @throws NullPointerException If no class was specified.
	 * @since 2018/09/29
	 */
	final <C> C __argument(Class<C> __cl, C __def)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		__PrintFGlobal__ global = this._global;
		
		// Determine the argument index to use, if the argument was not
		// explicitely passed then it is tracked manually
		int argdx = this._argdx,
			usedx = (argdx < 0 ? global._lineardx++ : argdx - 1);
		
		// {@squirreljme.error ZZ24 Request to use argument which is not
		// within the bounds of the input arguments. (The argument index)}
		Object[] args = global._args;
		if (usedx < 0 || usedx >= args.length)
			throw new IllegalArgumentException("ZZ24 " + (usedx + 1));
		
		// Return default value if one was used and there was no value here
		Object rv = args[usedx];
		if (rv == null)
			return __def;
		
		// {@squirreljme.error ZZ25 Expected argument of one class however it
		// was instead another class. (The requested class; The actual class)}
		if (!__cl.isInstance(rv))
			throw new IllegalArgumentException("ZZ25 " + __cl + " " +
				rv.getClass());
		
		return __cl.cast(rv);
	}
	
	/**
	 * Was a width specified?
	 *
	 * @return If a width was specified.
	 * @since 2018/09/24
	 */
	final boolean __hasWidth()
	{
		return this._width >= 1;
	}
	
	/**
	 * Is this left justified?
	 *
	 * @return If this is left justified.
	 * @since 2018/09/29
	 */
	final boolean __isLeftJustified()
	{
		return this._flags[__PrintFFlag__.LEFT_JUSTIFIED.ordinal()];
	}
	
	/**
	 * Sets the argument index.
	 *
	 * @param __dx The index to set.
	 * @throws IllegalArgumentException If the index is not valid.
	 * @since 2018/09/24
	 */
	final void __setArgumentIndex(int __dx)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1n Argument index already set or is of an
		// invalid value. (The index used)}
		if (__dx <= 0 || this._argdx >= 1)
			throw new IllegalArgumentException("ZZ1n " + __dx);
		
		this._argdx = __dx;
	}
	
	/**
	 * Sets the conversion.
	 *
	 * @param __p The primary conversion.
	 * @param __s The secondary conversion.
	 * @throws IllegalArgumentException If the conversion is not valid.
	 * @since 2018/09/28
	 */
	final void __setConversion(int __p, int __s)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1y The conversion has already been specified.}
		if (this._conv != null)
			throw new IllegalArgumentException("ZZ1y");
		
		// {@squirreljme.error ZZ1x Invalid conversion specified. (The first
		// character; The second character)}
		__PrintFConversion__ conv = __PrintFConversion__.__decode(__p, __s);
		if (conv == null)
			throw new IllegalArgumentException("ZZ1x " + (char)__p + " " +
				(char)(__s < 0 ? ' ' : __s));
		
		// Set
		this._conv = conv;
		
		// Uppercase conversion?
		switch (__p)
		{
			case 'B':
			case 'H':
			case 'S':
			case 'C':
			case 'X':
			case 'E':
			case 'G':
			case 'T':
				this._upper = true;
				break;
			
			default:
				break;
		}
		
		// Need this to do sanity checks
		__PrintFCategory__ cat = conv.__category();
		
		// {@squirreljme.error ZZ22 The specified flag cannot be specified
		// for the given conversion. (The conversion; The flag)}
		boolean[] flags = this._flags;
		for (int i = 0, n = __PrintFFlag__.COUNT; i < n; i++)
		{
			__PrintFFlag__ flag = __PrintFFlag__.valueOf(i);
			if (flags[i] && !conv.__hasFlag(flag))
				throw new IllegalArgumentException("ZZ22 " + conv + " " +
					flag);
		}
		
		// {@squirreljme.error ZZ21 Width cannot be specified for the given
		// convesion. (The conversion)}
		if (this._width > 0 && !cat.__hasWidth())
			throw new IllegalArgumentException("ZZ21 " + conv);
		
		// {@squirreljme.error ZZ20 Precision cannot be specified for the
		// given conversion. (The conversion)}
		if (this._precision > 0 && !cat.__hasPrecision())
			throw new IllegalArgumentException("ZZ20 " + conv);
	}
	
	/**
	 * Sets the specified flag.
	 *
	 * @param __c The flag to set.
	 * @return If the flag is valid.
	 * @throws IllegalArgumentException If the flag was duplicated.
	 * @since 2018/09/24
	 */
	final boolean __setFlag(char __c)
		throws IllegalArgumentException
	{
		// Is this flag one that exists?
		__PrintFFlag__ f = __PrintFFlag__.__decode(__c);
		if (f == null)
			return false;
		
		boolean[] flags = this._flags;
		
		// {@squirreljme.error ZZ1p Duplicate flag specified. (The flag)}
		int ord = f.ordinal();
		if (flags[ord])
			throw new IllegalArgumentException("ZZ1p " + __c);
		
		// Use it
		flags[ord] = true;
		return true;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param __w The width to use.
	 * @throws IllegalArgumentException If the width is not valid.
	 * @since 2018/09/24
	 */
	final void __setWidth(int __w)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1o Width already or is of an invalid value.
		// (The width used)}
		if (__w <= 0 || this._width >= 1)
			throw new IllegalArgumentException("ZZ1o " + __w);
		
		this._width = __w;
	}
}

