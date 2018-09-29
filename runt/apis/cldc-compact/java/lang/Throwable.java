// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.asm.DebugAccess;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the base class for all types which are thrown, itself being a
 * checked exception handled by the compiler.
 *
 * Each {@link Throwable} contains internal stack trace information which is
 * initialized when the {@link Throwable} is.
 *
 * A {@link Throwable} may have suppressed exceptions, which are exceptions
 * which may be added to a {@link Throwable} to indicate that there was an
 * exception that was caused in a try-with-resources when the resource was
 * attempted to be closed.
 *
 * @since 2018/09/15
 */
public class Throwable
{
	/** String ID for the throwable. */
	private static final int _THROWABLE_ID_HI, _THROWABLE_ID_LO;
	
	/** The message for this exception. */
	private final String _message;
	
	/** Suppressed exceptions. */
	private volatile Throwable[] _suppressed;
	
	/** Was a cause initialized already? */
	private volatile boolean _initcause;
	
	/**
	 * The cause of this exception, note this is writeable because of
	 * {@link #initCause(Throwable)}. This is mostly just for older versions
	 * of the class library which did not have a cause specified in the
	 * constructor.
	 */
	private volatile Throwable _cause;
	
	/** The stack trace for this throwable (in raw form). */
	private volatile int[] _stack;
	
	/**
	 * Initializes the throwable ID.
	 *
	 * @since 2018/09/29
	 */
	static
	{
		long id = DebugAccess.unresolveString("java/lang/Throwable");
		_THROWABLE_ID_HI = (int)(id >>> 32);
		_THROWABLE_ID_LO = (int)id;
	}
	
	/**
	 * Initializes a throwable with no cause or message.
	 *
	 * @since 2018/09/15
	 */
	public Throwable()
	{
		this(false, 2, null, null);
	}
	
	/**
	 * Initializes a throwable with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/09/15
	 */
	public Throwable(String __m)
	{
		this(false, 2, __m, null);
	}
	
	/**
	 * Initializes a throwable with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2018/09/15
	 */
	public Throwable(Throwable __t)
	{
		this(true, 2, null, __t);
	}
	
	/**
	 * Initializes a throwable with the given cause and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2018/09/15
	 */
	public Throwable(String __m, Throwable __t)
	{
		this(true, 2, __m, __t);
	}
	
	/**
	 * Since the cause can only be set once, this constructor needs to keep
	 * track of whether it was set by a constructor or not.
	 *
	 * @param __ic Is the cause initialized?
	 * @param __clip The number of stack trace entries to clip.
	 * @param __m The exception message.
	 * @param __t The cause.
	 * @since 2018/09/15
	 */
	private Throwable(boolean __ic, int __clip, String __m, Throwable __t)
	{
		// These are trivially set
		this._message = __m;
		this._cause = __t;
		this._initcause = __ic;
		
		// The stack trace is implicitly filled in by this constructor, it
		// matches the stack trace of the current thread of execution
		this._stack = this.__getStackTrace(this, __clip, true);
	}
	
	/**
	 * Adds a suppressed throwable which will be thrown alongside this
	 * throwable. This is mainly used with try-with-resources although a
	 * programmer may wish to add related throwables that additionally
	 * happened.
	 *
	 * This method is thread safe.
	 *
	 * @param __t The throwable to suppress.
	 * @throws IllegalArgumentException If the passed throwable is this.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	public final void addSuppressed(Throwable __t)
		throws IllegalArgumentException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0v Cannot add a suppressed exception which
		// is this exception.}
		if (__t == this)
			throw new IllegalArgumentException("ZZ0v");
		
		// Just lock on this to add suppressed exceptions, it is unspecified
		// where the lock is done, but this prevent creation of an object just
		// to hold a lock.
		synchronized (this)
		{
			// No suppressed exceptions were set, initialize
			Throwable[] suppressed = this._suppressed;
			if (suppressed == null)
				this._suppressed = new Throwable[]{__t};
			
			// Otherwise rebuild the array and add it
			else
			{
				int n = suppressed.length;
				Throwable[] copy = new Throwable[n + 1];
				for (int i = 0; i < n; i++)
					copy[i] = suppressed[i];
				copy[n] = __t;
				
				// Use this instead
				this._suppressed = copy;
			}
		}
	}
	
	/**
	 * Fills in the stack trace of this throwable for the current thread.
	 *
	 * @return {@code this}.
	 * @since 2018/09/15
	 */
	public Throwable fillInStackTrace()
	{
		// Get stack trace, ignore this method
		this._stack = this.__getStackTrace(this, 1, false);
		
		// Returns self
		return this;
	}
	
	/**
	 * Returns the throwable which caused this throwable to occur.
	 *
	 * @return The throwable which caused this throwable.
	 * @since 2018/09/15
	 */
	public Throwable getCause()
	{
		return this._cause;
	}
	
	/**
	 * Returns a potentially localized message for this throwable, the default
	 * implementation just returns {@link #getMessage()}.
	 *
	 * @return A localized message.
	 * @since 2018/09/15
	 */
	public String getLocalizedMessage()
	{
		return this.getMessage();
	}
	
	/**
	 * Returns the message which was set for this throwable, if one was set.
	 *
	 * @return The message for this throwable, may be {@code null} if one was
	 * not set.
	 * @since 2018/09/15
	 */
	public String getMessage()
	{
		return this._message;
	}
	
	/**
	 * Returns an array of all the throwables which were suppressed.
	 *
	 * This method is thread safe.
	 *
	 * @return An array of all the suppresed throwables.
	 * @since 2018/09/15
	 */
	public final Throwable[] getSuppressed()
	{
		// Since this is volatile we can just read whatever value is here
		// without needing to lock
		Throwable[] rv = this._suppressed;
		if (rv == null)
			return new Throwable[0];
		return rv.clone();
	}
	
	/**
	 * Initializes the cause of the throwable if it has not been set.
	 *
	 * Calls to {@link #initCause(Throwable)}, {@link #Throwable(Throwable)},
	 * or {@link #Throwable(String, Throwable)} will cause the cause to be
	 * initialized, preventing this from being called or called again.
	 *
	 * @param __t The cause of the throwable.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the cause is this throwable.
	 * @throws IllegalStateException If a cause has already been set.
	 * @since 2018/09/15
	 */
	public Throwable initCause(Throwable __t)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error ZZ0t Cannot initialize the cause of the
		// throwable with itself as the cause.}
		if (__t == this)
			throw new IllegalArgumentException("ZZ0t");
		
		// {@squirreljme.error ZZ0u The cause of the throwable has already
		// been initialized.}
		if (this._initcause)
			throw new IllegalStateException("ZZ0u");
		
		// Set
		this._initcause = true;
		this._cause = __t;
		
		return this;
	}
	
	/**
	 * Prints the stack trace to the standard error stream.
	 *
	 * @since 2018/09/15
	 */
	public void printStackTrace()
	{
		Throwable.__printStackTrace(this, System.err, 0);
	}
	
	/**
	 * Prints the stack trace to the specified stream.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	public void printStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		Throwable.__printStackTrace(this, __ps, 0);
	}
	
	/**
	 * Returns a string representation of this throwable.
	 *
	 * If there is a localized message, the form is `<class>: <message>`.
	 * If there is no message, the form is `<class>`. 
	 *
	 * @return A string representing this throwable.
	 * @since 2018/09/15
	 */
	@Override
	public String toString()
	{
		String clname = this.getClass().getName(),
			lm = this.getLocalizedMessage();
		
		if (lm == null)
			return clname;
		return clname + ": " + lm;
	}
	
	/**
	 * Obtains the stack trace for the current thread in raw format.
	 *
	 * @param __this The this throwable object.
	 * @param __clip The number of entries on the top to clip.
	 * @param __initclip Clip off initializers?
	 * @return The stack trace for the current stack.
	 * @throws IllegalArgumentException If the clip is negative.
	 * @since 2018/09/16
	 */
	private static int[] __getStackTrace(Throwable __this,
		int __clip, boolean __initclip)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ0x Cannot specify a negative clip for a
		// stack trace.}
		if (__clip < 0)
			throw new IllegalArgumentException("ZZ0x");
		
		// Get the raw trace here
		int[] rawstack = DebugAccess.rawCallTrace();
		
		// Determine the new length of the raw data
		int len = rawstack.length,
			skippy = DebugAccess.TRACE_COUNT * (__clip + 1),
			newlen = len - skippy;
		if (newlen < 0)
			newlen = 0;
		
		// If this is initializer clipping, clip out any references that lead
		// up to this Throwable class so that they do not appear on the stack
		// at all
		int thi = Throwable._THROWABLE_ID_HI,
			tlo = Throwable._THROWABLE_ID_LO;
		if (__initclip && __this != null && thi != -1 && tlo != -1)
		{
			// Resolve the name for this class, because it will be at the
			// bottom of the constructor stack
			long lid = DebugAccess.unresolveString(
				__this.getClass()._binaryname);
			
			// Split off IDs
			int lhi = (int)(lid >>> 32),
				llo = (int)lid;
			
			// Go down the trace and find the first instance
			int scan = 0;
			boolean seek = true;
			for (; scan < len; scan += DebugAccess.TRACE_COUNT)
			{
				// Read scan here
				int xhi = rawstack[scan],
					xlo = rawstack[scan + 1];
				
				// Seek to the current class for this current object
				if (seek)
				{
					// We found our class, switch out of seek mode
					if (lhi == xhi && llo == xlo)
					{
						seek = false;
						break;
					}
				}
				else
				{
					// Stop on the first entry which is not in this class
					if (lhi != xhi || llo != xlo)
						break;
				}
			}
			
			// Use the new scan instead, if it was found
			if (!seek && scan != len && scan > skippy)
			{
				skippy = scan;
				newlen = len - skippy;
				
				if (newlen < 0)
					newlen = 0;
			}
		}
		
		// Copy the trace data into this one
		int[] rv = new int[newlen];
		for (int i = skippy, o = 0; o < newlen; i++, o++)
			rv[o] = rawstack[i];
		
		return rv;
	}
	
	/**
	 * Prints the stack trace to the specified stream. This is internal so that
	 * one stack printing does not call the other since it is not specified if
	 * it actually does it.
	 *
	 * @param __t The throwable to print for.
	 * @param __ps The stream to print to.
	 * @param __indent The indentation level.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	private static void __printStackTrace(Throwable __t, PrintStream __ps,
		int __indent)
		throws NullPointerException
	{
		if (__t == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Internally raw stacks are stored since that is the fastest way
		// to generate a stack trace, which will only be resolved when this
		// method is called to print.
		int[] rawstack = __t._stack;
		if (rawstack == null)
		{
			__ps.println("<No stack trace>");
			return;
		}
		
		// Resolve the stack trace so it is easier to work with
		CallTraceElement[] stack = DebugAccess.resolveRawCallTrace(rawstack);
		
		// The first thing is the string representation of this throwable
		__ps.println(__t.toString());
		
		// The first entry is the top of the stack so it gets printed first 
		for (int i = 0, n = stack.length; i < n; i++)
		{
			// Ignore any elements that may happen to be null
			CallTraceElement e = stack[i];
			if (e == null)
				continue;
			
			// Add indentation
			for (int p = 0; p <= __indent; p++)
				__ps.print("  ");
			
			// Use string representation of the element
			__ps.print("at ");
			__ps.println(e.toString());
		}
		
		// Print cause of the exception
		Throwable cause = __t.getCause();
		if (cause != null)
		{
			__ps.print("  Caused by:");
			Throwable.__printStackTrace(cause, __ps, __indent + 1);
		}
		
		// Print suppressed exceptions
		for (Throwable sup : __t.getSuppressed())
		{
			__ps.print("  Suppressed:");
			Throwable.__printStackTrace(sup, __ps, __indent + 2);
		}
	}
}

