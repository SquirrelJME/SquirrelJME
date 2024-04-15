// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.CallTraceUtils;
import java.io.PrintStream;
import java.util.Arrays;

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
@Api
public class Throwable
{
	/** The message for this exception. */
	private final String _message;
	
	/** Suppressed exceptions. */
	private Throwable[] _suppressed;
	
	/** Was a cause initialized already? */
	private boolean _initCause;
	
	/**
	 * The cause of this exception, note this is writeable because of
	 * {@link #initCause(Throwable)}. This is mostly just for older versions
	 * of the class library which did not have a cause specified in the
	 * constructor.
	 */
	private Throwable _cause;
	
	/** The stack trace for this throwable (in mostly-raw form). */
	@SuppressWarnings("unused")
	private TracePointBracket[] _stack;
	
	/**
	 * Initializes a throwable with no cause or message.
	 *
	 * @since 2018/09/15
	 */
	@Api
	public Throwable()
	{
		this._message = null;
		
		this._stack = DebugShelf.traceStack();
	}
	
	/**
	 * Initializes a throwable with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/09/15
	 */
	@Api
	public Throwable(String __m)
	{
		this._message = __m;
		
		this._stack = DebugShelf.traceStack();
	}
	
	/**
	 * Initializes a throwable with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2018/09/15
	 */
	@Api
	public Throwable(Throwable __t)
	{
		this._message = null;
		
		this._initCause = true;
		this._cause = __t;
		
		this._stack = DebugShelf.traceStack();
	}
	
	/**
	 * Initializes a throwable with the given cause and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2018/09/15
	 */
	@Api
	public Throwable(String __m, Throwable __t)
	{
		this._message = __m;
		
		this._initCause = true;
		this._cause = __t;
		
		this._stack = DebugShelf.traceStack();
	}
	
	/**
	 * Adds a suppressed throwable which will be thrown alongside this
	 * throwable. This is mainly used with try-with-resources although a
	 * programmer may wish to add related {@link Throwable}s that additionally
	 * happened.
	 *
	 * This method is thread safe.
	 *
	 * @param __t The throwable to suppress.
	 * @throws IllegalArgumentException If the passed throwable is this.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	@Api
	public final void addSuppressed(Throwable __t)
		throws IllegalArgumentException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ26 Cannot add a suppressed exception which
		is this exception.} */
		if (__t == this)
			throw new IllegalArgumentException("ZZ26");
		
		// No suppressed exceptions were set, initialize
		Throwable[] suppressed = this._suppressed;
		if (suppressed == null)
			this._suppressed = new Throwable[]{__t};
		
		// Otherwise rebuild the array and add it
		else
		{
			// Add one extra spot to this array
			int n = suppressed.length;
			suppressed = Arrays.copyOf(suppressed, n + 1);
			
			suppressed[n] = __t;
			
			// Use this new one instead
			this._suppressed = suppressed;
		}
	}
	
	/**
	 * Fills in the stack trace of this throwable for the current thread.
	 *
	 * @return {@code this}.
	 * @since 2018/09/15
	 */
	@Api
	public Throwable fillInStackTrace()
	{
		// Get stack trace, ignore this method
		this._stack = DebugShelf.traceStack();
		
		return this;
	}
	
	/**
	 * Returns the throwable which caused this throwable to occur.
	 *
	 * @return The throwable which caused this throwable.
	 * @since 2018/09/15
	 */
	@Api
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
	@Api
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
	@Api
	public String getMessage()
	{
		return this._message;
	}
	
	/**
	 * Returns an array of all the {@link Throwable}s which were suppressed.
	 *
	 * This method is thread safe.
	 *
	 * @return An array of all the suppressed {@link Throwable}s.
	 * @since 2018/09/15
	 */
	@Api
	public final Throwable[] getSuppressed()
	{
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
	@Api
	public Throwable initCause(Throwable __t)
		throws IllegalArgumentException, IllegalStateException
	{
		/* {@squirreljme.error ZZ27 Cannot initialize the cause of the
		throwable with itself as the cause.} */
		if (__t == this)
			throw new IllegalArgumentException("ZZ27");
		
		/* {@squirreljme.error ZZ28 The cause of the throwable has already
		been initialized.} */
		if (this._initCause)
			throw new IllegalStateException("ZZ28");
		
		// Set
		this._initCause = true;
		this._cause = __t;
		
		return this;
	}
	
	/**
	 * Prints the stack trace to the standard error stream.
	 *
	 * @since 2018/09/15
	 */
	@Api
	public void printStackTrace()
	{
		CallTraceUtils.printStackTrace(System.err, this, 0);
	}
	
	/**
	 * Prints the stack trace to the specified stream.
	 *
	 * @param __ps The stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	@Api
	public void printStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		CallTraceUtils.printStackTrace(__ps, this, 0);
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
		String className = this.getClass().getName();
		String lm = this.getLocalizedMessage();
		
		if (lm == null)
			return className;
		return className + ": " + lm;
	}
}

