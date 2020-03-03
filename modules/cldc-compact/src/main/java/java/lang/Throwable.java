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

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import java.io.PrintStream;

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
	/** Exception message. */
	private static final int _TYPE_EXCEPTION =
		0;
	
	/** Caused by message. */
	private static final int _TYPE_CAUSED_BY =
		1;
	
	/** Suppressed message. */
	private static final int _TYPE_SUPPRESSED =
		2;
	
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
		this._stack = Throwable.__getStackTrace(__clip);
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
		
		// {@squirreljme.error ZZ26 Cannot add a suppressed exception which
		// is this exception.}
		if (__t == this)
			throw new IllegalArgumentException("ZZ26");
		
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
		this._stack = Throwable.__getStackTrace(1);
		
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
	 * Calls to this method, {@link #Throwable(Throwable)},
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
		// {@squirreljme.error ZZ27 Cannot initialize the cause of the
		// throwable with itself as the cause.}
		if (__t == this)
			throw new IllegalArgumentException("ZZ27");
		
		// {@squirreljme.error ZZ28 The cause of the throwable has already
		// been initialized.}
		if (this._initcause)
			throw new IllegalStateException("ZZ28");
		
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
		Throwable.__printStackTrace(this, System.err, 0,
			Throwable._TYPE_EXCEPTION);
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
		
		Throwable.__printStackTrace(this, __ps, 0, Throwable._TYPE_EXCEPTION);
	}
	
	/**
	 * Returns a string representation of this throwable.
	 *
	 * If there is a localized message, the form is {@code <class>: <message>}.
	 * If there is no message, the form is {@code <class>}.
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
	 * @param __clip The number of entries on the top to clip.
	 * @return The stack trace for the current stack.
	 * @throws IllegalArgumentException If the clip is negative.
	 * @since 2018/09/16
	 */
	private static int[] __getStackTrace(int __clip)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ29 Cannot specify a negative clip for a
		// stack trace.}
		if (__clip < 0)
			throw new IllegalArgumentException("ZZ29");
		
		// Get the raw trace here
		int[] rawstack = CallTraceElement.traceRaw();
		
		// Just use the raw stack
		return rawstack;
	}
	
	/**
	 * Prints the indentation of the stack trace.
	 *
	 * @param __ps The stream to print to.
	 * @param __indent The current indentation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/11
	 */
	private static void __printStackIndent(PrintStream __ps, int __indent)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Doing nothing
		if (__indent == 0)
			return;
		
		// Base space indent
		__ps.print("  ");
		
		// Print bars for indentation level
		for (int i = 0; i < __indent; i++)
			__ps.print('|');
	}
	
	/**
	 * Prints the stack trace to the specified stream. This is internal so that
	 * one stack printing does not call the other since it is not specified if
	 * it actually does it.
	 *
	 * @param __t The throwable to print for.
	 * @param __ps The stream to print to.
	 * @param __indent The indentation level.
	 * @param __type The string type to use for the message base.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	private static void __printStackTrace(Throwable __t, PrintStream __ps,
		int __indent, int __type)
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
		CallTraceElement[] stack = CallTraceElement.traceResolve(rawstack);
		
		// Indent and print exception type
		Throwable.__printStackIndent(__ps, __indent);
		switch (__type)
		{
			case Throwable._TYPE_CAUSED_BY:
				__ps.print("> CAUSED BY ");
				break;
			
			case Throwable._TYPE_SUPPRESSED:
				__ps.print("> SUPPRESSED ");
				break;
			
			case Throwable._TYPE_EXCEPTION:
			default:
				__ps.print("EXCEPTION ");
				break;
		}
		
		// Then the string representation of it, which may be replaced
		__ps.println(__t.toString());
		
		// Increase indentation to get more bars
		__indent++;
		
		// The first entry is the top of the stack so it gets printed first 
		String wasclass = "<Unknown>";
		for (int i = 0, n = stack.length; i < n; i++)
		{
			// Ignore any elements that may happen to be null
			CallTraceElement e = stack[i];
			if (e == null)
				continue;
			
			// Get the element class, always make sure the class is valid
			// even if it is not known
			String nowclass = e.className();
			if (nowclass == null)
				nowclass = "<Unknown>";
			
			// If the class changed, specify that it has
			if (!nowclass.equals(wasclass))
			{
				// Indent
				Throwable.__printStackIndent(__ps, __indent);
				
				__ps.print(" IN ");
				__ps.println(e.toClassHeaderString());
				
				// Changed
				wasclass = nowclass;
			}
			
			// Indent and print the at line
			Throwable.__printStackIndent(__ps, __indent);
			__ps.print("- ");
			__ps.println(e.toAtLineString());
		}
		
		// Print cause of the exception
		Throwable cause = __t.getCause();
		if (cause != null)
			Throwable.__printStackTrace(cause, __ps, __indent,
				Throwable._TYPE_CAUSED_BY);
		
		// Print suppressed exceptions
		for (Throwable sup : __t.getSuppressed())
			Throwable.__printStackTrace(sup, __ps, __indent,
				Throwable._TYPE_SUPPRESSED);
	}
}

