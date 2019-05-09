// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This class contains an immutable result of the test.
 *
 * @since 2019/05/08
 */
public final class TestResult
{
	/** Return value result. */
	protected final String rvalue;
	
	/** Thrown value, if any. */
	protected final String tvalue;
	
	/** Secondary values. */
	private final Map<String, String> _secondary;
	
	/** Hashcode. */
	private int _hash;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the test result.
	 *
	 * @param __rv Return value.
	 * @param __tv Thrown value, if any.
	 * @param __sec Secondary values.
	 * @throws NullPointerException On null arguments or if any secondary
	 * contains a null value.
	 * @since 2019/05/09
	 */
	public TestResult(String __rv, String __tv, Map<String, String> __sec)
		throws NullPointerException
	{
		if (__rv == null || __tv == null || __sec == null)
			throw new NullPointerException("NARG");
		
		// These are simple
		this.rvalue = __rv;
		this.tvalue = __tv;
		
		// Copy map values over
		Map<String, String> to = new SortedTreeMap<>();
		for (Map.Entry<String, String> e : __sec.entrySet())
		{
			String k = e.getKey(),
				v = e.getValue();
			
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			to.put(k, v);
		}
		
		// Just can use a linked map
		this._secondary = new LinkedHashMap<>(to);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
		/*
		// Get string result representation and the expected value from the
		// manifest
		String rvstr = DataSerialization.serialize(rv),
			thstr = DataSerialization.serialize(thrown),
			expectrv = attr.getValue("result", "ResultWasNotSpecified"),
			expectth = attr.getValue("thrown", "ExceptionWasNotSpecified");
		
		// Longest string, used for secondary value formatting when failure
		// happens
		int longskeylen = 1;
		
		// Find the longest secondary value and make a copy of it
		Map<String, String> secondary = this._secondary;
		synchronized (secondary)
		{
			for (Map.Entry<String, String> e : secondary.entrySet())
				longskeylen = Math.max(e.getKey().length(), longskeylen);
			
			// Make copy of it for usage
			secondary = new SortedTreeMap<>(secondary);
		}
		
		// Read in secondary values from the manifest
		Map<String, String> expectse = new SortedTreeMap<>();
		for (Map.Entry<JavaManifestKey, String> e : attr.entrySet())
		{
			String k = e.getKey().toString().toLowerCase();
			if (k.startsWith("secondary-"))
				expectse.put(k.substring(10), e.getValue());
		}
		
		// Is the test a success or failure?
		boolean passedrv = TestResult.valueEquals(rvstr, expectrv),
			passedth = TestResult.valueEquals(thstr, expectth),
			passedse = TestResult.valueEquals(secondary, expectse);
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints the comparison of this result and the other result.
	 *
	 * @param __o The result to compare against.
	 * @param __ps The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	public final void printComparison(TestResult __o, PrintStream __ps)
		throws NullPointerException
	{
		if (__o == null || __ps == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Print base values
		out.printf("%s: FAIL%n", classname);
		out.printf("\tRV %s %s%n", rvstr, expectrv);
		out.printf("\tTH %s %s%n", thstr, expectth);
		
		// Merge the two key sets
		Set<String> merged = new SortedTreeSet<>();
		merged.addAll(secondary.keySet());
		merged.addAll(expectse.keySet());
		
		// Secondary values are more complex to handle
		String valueform = "\t%" + longskeylen + "s %c %s %s%n";
		for (String k : merged)
		{
			String a = secondary.get(k),
				b = expectse.get(k);
			
			boolean isequal = (a != null && b != null &&
				__CoreTest__.__equals(a, b));
			
			out.printf(valueform, k,
				(isequal ? '=' : '!'),
				a,
				b);
		}
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Writes the test result as a manifest in SquirrelJME's test format.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/08
	 */
	public final void writeAsManifest(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads test results for the specified class.
	 *
	 * @param __cl The results to load.
	 * @return The results of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/08
	 */
	public static final TestResult loadForClass(Class<?> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// Used to refer to resources for parameters and default results
		Class<?> self = this.getClass();
		
		// Get the basename of the class, used to refer to resources
		String classname = self.getName();
		
		// Find result, look in super classes as needed
		JavaManifest man = null;
		for (Class<?> cl = self; cl != null; cl = cl.getSuperclass())
		{
			// Use basename of this form,
			String basename = cl.getName();
			int ld = basename.lastIndexOf('.');
			if (ld >= 0)
				basename = basename.substring(ld + 1);
			
			// Read input and output parameters
			try (InputStream in = self.getResourceAsStream(basename + ".in"))
			{
				if (in == null)
				{
					// Warn that it is missing
					System.err.printf("WARN: No .in for %s (%s.in)%n",
						classname, basename);
				}
				else
					man = new JavaManifest(in);
			}
			catch (IOException e)
			{
				// {@squirreljme.error BU07 Could not read the argument input.}
				throw new InvalidTestException("BU07", e);
			}
			
			// Stop if a manifest was read
			if (man != null)
				break;
		}
					
		// Use a blank manifest instead
		if (man == null)
			man = new JavaManifest();
		
		// The main attributes contain the arguments
		JavaManifestAttributes attr = man.getMainAttributes();
		*/
	}
	
	/**
	 * Compares two value strings against each other.
	 *
	 * @param __act The actual value.
	 * @param __exp The expected value.
	 * @return If the strings are a match.
	 * @throws InvalidTestParameterException If a throwable is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	public static final boolean valueEquals(String __act, String __exp)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__act == null || __exp == null)
			throw new NullPointerException("NARG");
		
		// Throwables are special cases
		if (__act.startsWith("throwable:") && __exp.startsWith("throwable:"))
		{
			// Snip off the throwable portions
			__act = __act.substring(10);
			__exp = __exp.substring(10);
			
			// Snip off the optional message in the actual
			int ld = __act.indexOf(':');
			if (ld >= 0)
				__act = __act.substring(0, ld);
			
			// Snip off the optional message in the expected
			ld = __exp.indexOf(':');
			if (ld >= 0)
				__exp = __exp.substring(0, ld);
			
			// Find the base expected class to find
			ld = __exp.indexOf(',');
			if (ld >= 0)
				__exp = __exp.substring(0, ld);
			
			// Only use the basename
			ld = __exp.lastIndexOf('.');
			if (ld >= 0)
				__exp = __exp.substring(ld + 1);
			
			// Go through the actual classes to find the class to match
			for (int i = 0, n = __act.length(); i < n;)
			{
				// Get sequence
				ld = __act.indexOf(',', i);
				if (ld < 0)
					ld = __act.length();
				
				// Snip off fragment
				String snip = __act.substring(i, ld);
				
				// Only consider the base name
				int xld = snip.lastIndexOf('.');
				if (xld >= 0)
					snip = snip.substring(xld + 1);
				
				// Is a match
				if (snip.equals(__exp))
					return true;
				
				// Skip
				i = ld + 1;
			}
		}
		
		// Use normal string comparison
		return __exp.equals(__act);
	}
}

