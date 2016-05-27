// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.multiphasicapps.descriptors.ClassLoaderNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.bytecode.NBCException;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CILookup;
import net.multiphasicapps.squirreljme.ci.CIMethodID;

/**
 * This is the core of the interpreter, this dispatches and maintains all of
 * the threads.
 *
 * @since 2016/04/21
 */
public class TerpCore
{
	/** The string class. */
	public static final ClassNameSymbol STRING_CLASS =
		ClassNameSymbol.of("java/lang/String");
	
	/** The library which contains classes to load. */
	protected final TerpLibrary classlib;
	
	/** The mapping of real threads to interpreter threads. */
	protected final Map<Thread, TerpThread> threadmap =
		new HashMap<>();
	
	/** Already loaded binary classes? */
	protected final Map<ClassNameSymbol, Reference<TerpClass>> loaded =
		new HashMap<>();
	
	/** Hash code generator function. */
	protected final Random hashcodegen =
		new Random(0x590144E723E_1989L);
	
	/** Virtual machine statistics. */
	protected final TerpStatistics stats =
		new TerpStatistics();
	
	/** Is the virtual machine running? */
	private volatile boolean _isrunning;
	
	/**
	 * This initializes the interpreter.
	 *
	 * @param __cl The library which contains classes.
	 * @param __main The main class.
	 * @param __args Main program arguments.
	 */
	public TerpCore(TerpLibrary __cl,
		ClassLoaderNameSymbol __main, String... __args)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __main == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		classlib = __cl;
		
		// Map in the current thread as the system "daemon" thread, although
		// Java ME lacks daemon threads, the interpreter internally will need
		// a thread so it can determine where execution is being performed.
		Thread mt = Thread.currentThread();
		threadmap.put(mt, new TerpThread(this, mt));
		
		// Locate the main class
		TerpClass maincl = initClass(__main.asClassName());
		
		// {@squirreljme.error AN0m The main class could not be found.
		// (The main class)}
		if (maincl == null)
			throw new IllegalArgumentException(String.format("AN0m %s",
				__main));
		
		// Find the main method
		TerpMethod mainme = maincl.methods().get(new CIMethodID(
			IdentifierSymbol.of("main"),
			MethodSymbol.of("([Ljava/lang/String;)V")));
		
		// {@squirreljme.error AN0g The main class does not contain a static
		// method which take a string argument, returns void, and is called
		// "main". (The main class)}
		if (mainme == null || !mainme.flags().isStatic())
			throw new TerpException(this,
				TerpException.Issue.METHOD_DOES_NOT_EXIST, String.format(
				"AN0g %s", __main));
		
		// Is running
		_isrunning = true;
		
		// Locate the string class
		TerpClass strclass = initClass(STRING_CLASS);
		
		// Allocate an array for the input arguments
		int an = __args.length;
		TerpObject argarr = new TerpObject(this, strclass, an);
		
		// Wrap the passed arguments to the target VM
		for (int i = 0; i < an; i++)
			throw new Error("TODO");
		
		if (true)
			throw new Error("TODO");
	}
	
	/**
	 * Returns the current interpreter thread which is mapped to the current
	 * executing thread.
	 *
	 * @return The interpreter thread for the current thread.
	 * @since 2016/05/12
	 */
	public TerpThread currentThread()
	{
		return thread(Thread.currentThread());
	}
	
	/**
	 * Locates and initializes the given class.
	 *
	 * @param __cn The class to initialize.
	 * @return The initialized interpreter class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public TerpClass initClass(ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Lock on the loaded classes
		Map<ClassNameSymbol, Reference<TerpClass>> map = loaded;
		synchronized (map)
		{
			// Get ref
			Reference<TerpClass> ref = map.get(__cn);
			TerpClass rv;
			
			// Needs to be loaded?
			if (ref == null || null == (rv = ref.get()))
				try
				{
					rv = new TerpClass(this,
						classlib.lookupClass(__cn.asBinaryName()), __cn, map);
				}
				
				// Failed to load properly
				catch (NBCException|CIException e)
				{
					// {@squirreljme.error AN0r Failed to initialize the
					// given class. (The name of the class)}
					throw new TerpException(this, TerpException.Issue.
						CLASS_ITerpT_FAILURE, String.format("AN0r %s", __cn), e);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns {@code true} if there is at least one thread running.
	 *
	 * @return If the virtual machine is still running.
	 * @since 2016/04/21
	 */
	public boolean isRunning()
	{
		return _isrunning;
	}
	
	/**
	 * Returns the class library interface which is used to obtain classes for
	 * initialization.
	 *
	 * @return The class library interface.
	 * @since 2016/04/22
	 */
	public TerpLibrary library()
	{
		return classlib;
	}
	
	/**
	 * Calculates and returns the next hash code.
	 *
	 * @return The next hash code to use.
	 * @since 2016/05/13
	 */
	public int nextHashCode()
	{
		Random r = hashcodegen;
		synchronized (r)
		{
			return r.nextInt();
		}
	}
	
	/**
	 * Returns the statistics of the virtual machine.
	 *
	 * @return The virtual machine statistics.
	 * @since 2016/05/14
	 */
	public TerpStatistics statistics()
	{
		return this.stats;
	}
	
	/**
	 * Obtains the interpreter based thread which wraps the given thread.
	 *
	 * @param __t The thread to obtain the interpreter based thread from.
	 * @return The interpreter thread which owns the given thread, {@code null}
	 * is returned if there is no mapped thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public TerpThread thread(Thread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock on the thread map
		Map<Thread, TerpThread> tm = threadmap;
		synchronized (tm)
		{
			return tm.get(__t);
		}
	}
	
	/**
	 * Registers the given thread with the thread mapping.
	 *
	 * @param __t The owning thread.
	 * @param __it The interpreter thread.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	TerpCore __registerThread(Thread __t, TerpThread __it)
		throws NullPointerException
	{
		// Check
		if (__t == null || __it == null)
			throw new NullPointerException("NARG");
				
		// Lock on the thread map
		Map<Thread, TerpThread> tm = threadmap;
		synchronized (tm)
		{
			tm.put(__t, __it);
		}
		
		// Self
		return this;
	}
}

