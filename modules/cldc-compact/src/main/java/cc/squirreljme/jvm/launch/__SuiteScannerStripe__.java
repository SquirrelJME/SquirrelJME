// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import java.util.List;
import java.util.Map;

/**
 * Represents a suite scanner stripe.
 *
 * @since 2022/10/03
 */
final class __SuiteScannerStripe__
	implements Runnable
{
	/** The done tracker. */
	private final __SuiteScannerCounter__ _done;
	
	/** The end position. */
	private final int _endPos;
	
	/** The JAR index counter. */
	private final __SuiteScannerCounter__ _jarIndexCount;
	
	/** The JARs to scan. */
	private final JarPackageBracket[] _jars;
	
	/** The library cache. */
	private final __Libraries__ _libs;
	
	/** The suite scanner listener. */
	private final SuiteScanListener _listener;
	
	/** The name to JAR mapping. */
	private final Map<String, JarPackageBracket> _nameToJar;
	
	/** The number of JARs total. */
	private final int _numJars;
	
	/** The result. */
	private final List<Application> _result;
	
	/** The start position of the scan. */
	private final int _startPos;
	
	/** The suite scanner this is under. */
	private final SuiteScanner _suiteScanner;
	
	/**
	 * Initializes the stripe runner settings.
	 *
	 * @param __listener The listener used.
	 * @param __jars The jars to load.
	 * @param __numJars The number of maximum JARs.
	 * @param __nameToJar The name to JAR mapping.
	 * @param __libs The library cache.
	 * @param __result The output result.
	 * @param __startPos The start position to run the scan
	 * @param __endPos The end position, exclusive.
	 * @param __jarIndexCount The JAR index counter, for a more accurate count
	 * when multithreaded.
	 * @param __suiteScanner The suite scanner this is under.
	 * @since 2022/10/03
	 */
	public __SuiteScannerStripe__(__SuiteScannerCounter__ __done,
		SuiteScanListener __listener, JarPackageBracket[] __jars,
		int __numJars, Map<String, JarPackageBracket> __nameToJar,
		__Libraries__ __libs, List<Application> __result, int __startPos,
		int __endPos, __SuiteScannerCounter__ __jarIndexCount,
		SuiteScanner __suiteScanner)
	{
		this._done = __done;
		this._listener = __listener;
		this._jars = __jars;
		this._numJars = __numJars;
		this._nameToJar = __nameToJar;
		this._libs = __libs;
		this._result = __result;
		this._startPos = __startPos;
		this._endPos = __endPos;
		this._jarIndexCount = __jarIndexCount;
		this._suiteScanner = __suiteScanner;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/10/03
	 */
	@Override
	public void run()
	{
		try
		{
			// Forward to stripe loader
			this._suiteScanner.__loadStripe(this._listener, this._jars,
				this._numJars, this._nameToJar, this._libs, this._result,
				this._startPos, this._endPos, this._jarIndexCount);
		}
		
		finally
		{
			// Call the done tracker
			__SuiteScannerCounter__ done = this._done;
			synchronized (done)
			{
				// Increase the done count for this library
				done._count++;
				
				// Signal main thread to continue in the event it was waiting
				// on the monitor
				done.notifyAll();
			}
		}
	}
}
