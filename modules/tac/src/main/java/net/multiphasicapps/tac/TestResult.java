// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.jvm.manifest.JavaManifest;
import cc.squirreljme.jvm.manifest.JavaManifestAttributes;
import cc.squirreljme.jvm.manifest.JavaManifestKey;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import cc.squirreljme.runtime.cldc.util.SortedTreeSet;
import cc.squirreljme.runtime.cldc.util.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifest;
import net.multiphasicapps.tool.manifest.writer.MutableJavaManifestAttributes;

/**
 * This class contains an immutable result of the test.
 *
 * @since 2019/05/08
 */
@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
		if (__o == this)
			return true;
		
		if (!(__o instanceof TestResult))
			return false;
		
		TestResult o = (TestResult)__o;
		return this.rvalue.equals(o.rvalue) &&
			this.tvalue.equals(o.tvalue) &&
			this._secondary.equals(o._secondary);
	}
	
	/**
	 * Gets the raw secondary value, if any.
	 *
	 * @param __key The key to get.
	 * @return The value of the key or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/16
	 */
	@SquirrelJMEVendorApi
	public final String getSecondaryRawValue(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		return this._secondary.get(__key);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = this.rvalue.hashCode() ^
				this.tvalue.hashCode() ^ this._secondary.hashCode());
		return rv;
	}
	
	/**
	 * Checks if this result is satisfied by the other result, note that this
	 * is not the same as equality just a result for a test.
	 *
	 * @param __o The other result.
	 * @return If it is satisfied by it.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/01
	 */
	@SquirrelJMEVendorApi
	public final boolean isSatisfiedBy(TestResult __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Use test comparison (note is expected and actual)
		return TestResult.valueEquals(__o.rvalue, this.rvalue) &&
			TestResult.valueEquals(__o.tvalue, this.tvalue) &&
			TestResult.__equals(__o._secondary, this._secondary);
	}
	
	/**
	 * Prints the comparison of this result and the other result.
	 *
	 * @param __ps The stream to write to.
	 * @param __o The result to compare against.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void printComparison(PrintStream __ps, TestResult __o)
		throws NullPointerException
	{
		if (__o == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Return value
		TestResult.__printSingleCompare(__ps, "return",
			this.rvalue, __o.rvalue);
		
		// Thrown exception
		TestResult.__printSingleCompare(__ps, "thrown",
			this.tvalue, __o.tvalue);
		
		// Secondary value comparison, more complex since both maps might not
		// contain the same values
		Map<String, String> as = this._secondary,
			bs = __o._secondary;
		
		// Create merged key set so that all keys from both maps are used
		Set<String> merged = new SortedTreeSet<>();
		merged.addAll(as.keySet());
		merged.addAll(bs.keySet());
		
		// Do comparisons on all values
		for (String k : merged)
			TestResult.__printSingleCompare(__ps, k,
				as.get(k), bs.get(k));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/08
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder();
			
			// Return value
			sb.append("{rv=");
			sb.append(this.rvalue);
			
			// Thrown values
			sb.append(", tv=");
			sb.append(this.tvalue);
			
			// Only add secondaries if they exist
			Map<String, String> secondary = this._secondary;
			if (!secondary.isEmpty())
			{
				sb.append(", sv=");
				sb.append(secondary);
			}
			
			// Done
			sb.append('}');
			
			// Build and cache
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Writes the test result as a manifest in SquirrelJME's test format.
	 *
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/08
	 */
	@SquirrelJMEVendorApi
	@SuppressWarnings({"FeatureEnvy", "resource"})
	public final void writeAsManifest(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Setup manifest
		MutableJavaManifest man = new MutableJavaManifest();
		MutableJavaManifestAttributes attr = man.getMainAttributes();
		
		// Add values to it
		attr.putValue("result", this.rvalue);
		attr.putValue("thrown", this.tvalue);
		for (Map.Entry<String, String> e : this._secondary.entrySet())
			attr.putValue(
				"secondary-" + DataSerialization.encodeKey(e.getKey()),
				e.getValue());
		
		// Write it
		man.write(__os);
	}
	
	/**
	 * Loads test results for the specified class.
	 *
	 * @param __cl The results to load.
	 * @param __otherKeys Other keys that were loaded and ignored, this is
	 * optional.
	 * @param __multiParam Optional multi-parameter which may be used to load
	 * results elsewhere.
	 * @return The results of the test.
	 * @throws NullPointerException If no class was specified.
	 * @since 2019/05/08
	 */
	@SquirrelJMEVendorApi
	@SuppressWarnings("FeatureEnvy")
	public static TestResult loadForClass(Class<?> __cl,
		Map<String, String> __otherKeys, String __multiParam)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
			
		// Multi-parameter set since multiples could be defined and declared
		// accordingly in old and newer style
		List<String> multiParams = null;
		if (__multiParam != null)
		{
			Set<String> working = new LinkedHashSet<>();
			String[] splits = StringUtils.basicSplit('@', __multiParam);
			
			// Add each individual split since there could be a regular
			// parameter
			working.addAll(Arrays.asList(splits));
			
			// Add growing splits i.e. a, a@b, a@b@c, etc.
			for (int i = 0, n = splits.length; i < n; i++)
			{
				StringBuilder sb = new StringBuilder();
				
				for (int j = 0; j <= i; j++)
				{
					// Add split at the end to divide
					if (sb.length() > 0)
						sb.append('@');
					
					sb.append(splits[j]);
				}
				
				working.add(sb.toString());
			}
			
			// Reverse the working list so precise multi parameters take
			// precedence before earlier ones
			multiParams = new ArrayList<>(working);
			Collections.reverse(multiParams);
		}
		
		// We are going to recursively go up the class chain and load values
		// from the manifest into our result
		TestResultBuilder rv = new TestResultBuilder();
		for (Class<?> at = __cl; at != null; at = at.getSuperclass())
		{
			// Load values for this class
			TestResult.__loadClassValues(__otherKeys, multiParams, rv, at);
			
			// Then load any results that are specified in interfaces
			for (TypeBracket implement :
				TypeShelf.interfaces(TypeShelf.classToType(at)))
				TestResult.__loadClassValues(__otherKeys, multiParams, rv,
					TypeShelf.typeToClass(implement));
		}
		
		// Done
		return rv.build();
	}
	
	/**
	 * Decodes a list of throwable and returns them.
	 *
	 * @param __ts The throwables to decode.
	 * @return The list of used throwables.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09 
	 */
	@SquirrelJMEVendorApi
	public static List<String> throwableList(String __ts)
		throws NullPointerException
	{
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error BU07 Not a throwable.} */
		if (!__ts.startsWith("throwable:"))
			throw new IllegalArgumentException("BU07");
		__ts = __ts.substring(10);
		
		// Snip off the optional debug message
		int lm = __ts.indexOf(':');
		if (lm >= 0)
			__ts = __ts.substring(0, lm);
		
		// Snip into string list, note that java.lang is initially implicit
		// and remaining values use a shortened form
		List<String> rv = new ArrayList<>();
		String baseform = "java.lang";
		for (int i = 0, n = __ts.length(); i < n;)
		{
			// Find split or where this ends
			int lc = __ts.indexOf(',', i);
			if (lc < 0)
				lc = n;
			
			// Snip this part out
			String sub = __ts.substring(i, lc);
			
			// Change of base?
			int ld = sub.lastIndexOf('.');
			if (ld >= 0)
			{
				baseform = sub.substring(0, ld);
				sub = sub.substring(ld + 1);
			}
			
			// Add full form
			rv.add(baseform + "." + sub);
			
			// Process next split
			i = lc + 1;
		}
		
		return rv;
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
	@SquirrelJMEVendorApi
	public static boolean valueEquals(String __act, String __exp)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__act == null || __exp == null)
			throw new NullPointerException("NARG");
		
		// Throwables are special cases since they represent multiple classes
		if (__act.startsWith("throwable:") && __exp.startsWith("throwable:"))
		{
			// Get all elements for both
			List<String> la = TestResult.throwableList(__act),
				lb = TestResult.throwableList(__exp);
			
			Debugging.debugNote("%s ~~= %s", la, lb);
			
			// These are considered equal if they have anything in common
			la.retainAll(lb);
			return !la.isEmpty();
		}
		
		// Compare against long but ignoring whatever sign is used
		else if (__act.startsWith("long:") &&
			__exp.startsWith("long-ignore-sign:"))
		{
			// Parse values
			long act = Long.parseLong(__act.substring("long:".length()));
			long exp = Long.parseLong(
				__exp.substring("long-ignore-sign:".length()));
			
			return (act & ~Long.MIN_VALUE) == (exp & ~Long.MIN_VALUE);
		}
		
		// Comparing against fudged long value (which is a plus or minus value)
		else if (__act.startsWith("long:") && __exp.startsWith("long-fudge:"))
		{
			// Parse actual value
			long act = Long.parseLong(__act.substring("long:".length()));
			
			// The expected value has a fudge
			int xfc = __exp.indexOf(':');
			int xlc = __exp.lastIndexOf(':');
			if (xlc == xfc)
				xlc = -1;
			
			// Parse values
			long exp = Long.parseLong(__exp.substring(xfc + 1,
				(xlc >= 0 ? xlc : __exp.length())));
			long fudge = Math.abs((xlc >= 0 ?
				Long.parseLong(__exp.substring(xlc + 1)) : 0));
			
			// Matches as long as we are within the fudge range
			return act == exp ||
				(act >= (exp - fudge) && act <= (exp + fudge));
		}
		
		// Use normal string comparison
		return __exp.equals(__act);
	}
	
	/**
	 * Compares the map of strings to see that they are equal.
	 *
	 * @param __act The actual values.
	 * @param __exp The expected values.
	 * @return If the maps are a match.
	 * @throws InvalidTestParameterException If a throwable is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	private static boolean __equals(Map<String, String> __act,
		Map<String, String> __exp)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__act == null || __exp == null)
			throw new NullPointerException("NARG");
		
		// Compare from the first map
		for (Map.Entry<String, String> a : __act.entrySet())
		{
			String key = a.getKey();
			
			// Ignore values which have been dropped
			if ("Drop".equals(__exp.get(key)))
				continue;
			
			// Second is missing key
			if (!__exp.containsKey(key))
				return false;
			
			// Match value
			if (!TestResult.valueEquals(a.getValue(), __exp.get(key)))
				return false;
		}
		
		// Just scan through the keys in the second map, if any keys are
		// missing then extra keys were added
		for (String key : __exp.keySet())
			if (!__act.containsKey(key))
			{
				// Ignore values which have been dropped
				if ("Drop".equals(__exp.get(key)))
					continue;
				
				return false;
			}
		
		// Is a match
		return true;
	}
	
	/**
	 * Extracts results from test result JSONs.
	 * 
	 * @param __otherKeys The other keys to test.
	 * @param __result The resultant test results.
	 * @param __pivot The pivot for loading resources.
	 * @param __baseName The base name for the resource.
	 * @since 2022/07/26
	 */
	private static void __extractResults(Map<String, String> __otherKeys,
		TestResultBuilder __result, Class<?> __pivot, String __baseName)
	{
		JavaManifestAttributes attr;
		try (InputStream in = __pivot.getResourceAsStream(
			__baseName + ".in"))
		{
			// No manifest here, ignore
			if (in == null)
				return;
			
			// Parse
			attr = new JavaManifest(in).getMainAttributes();
		}
		
		// Ignore
		catch (IOException e)
		{
			return;
		}
		
		// Work with attributes and decode them
		for (Map.Entry<JavaManifestKey, String> e :
			attr.entrySet())
		{
			String ekey = e.getKey().toString().toLowerCase(),
				eval = e.getValue();
			
			// Depends on the encoded key
			switch (ekey)
			{
					// Returned value
				case "result":
					if (__result.getReturn() == null)
						__result.setReturnEncoded(eval);
					break;
				
					// Thrown value
				case "thrown":
					if (__result.getThrown() == null)
						__result.setThrownEncoded(eval);
					break;
					
					// Possibly handle secondary values
				default:
					if (ekey.startsWith("secondary-"))
					{
						String skey = DataDeserialization
							.decodeKey(ekey.substring(10));
						
						if (__result.getSecondary(skey) == null)
							__result.putSecondaryEncoded(skey, eval);
					}
					
					// Another key, put into the other map, but
					// never replace values that already exist
					else if (__otherKeys != null &&
						!__otherKeys.containsKey(ekey))
						__otherKeys.put(ekey, eval);
					break;
			}
		}
	}
	
	/**
	 * Loads class values for tests.
	 *
	 * @param __otherKeys The other keys.
	 * @param __multiParams The current multi parameters.
	 * @param __rv The result.
	 * @param __at The current class to check.
	 * @since 2022/09/05
	 */
	private static void __loadClassValues(Map<String, String> __otherKeys,
		List<String> __multiParams, TestResultBuilder __rv, Class<?> __at)
	{
		// Determine base name of the class
		String atName = __at.getName();
		int ld = atName.lastIndexOf('.');
		String atBase = (ld < 0 ? atName : atName.substring(ld + 1));
		
		// Try to load multi-parameter and standard results, the multi
		// parameters take precedence since they are more specific for
		// tests
		if (__multiParams != null && !__multiParams.isEmpty())
			for (String multiParam : __multiParams)
			{
				TestResult.__extractResults(__otherKeys, __rv, __at,
					atBase + "@" + multiParam);
			}
		
		// Standard results last
		TestResult.__extractResults(__otherKeys, __rv, __at, atBase);
	}
	
	/**
	 * Prints single comparison.
	 *
	 * @param __ps The stream to print to.
	 * @param __key The key being printed.
	 * @param __a The first value.
	 * @param __b The second value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	private static void __printSingleCompare(PrintStream __ps,
		String __key, String __a, String __b)
		throws NullPointerException
	{
		if (__ps == null || __key == null)
			throw new NullPointerException("NARG");
		
		// Not the same?
		boolean equals;
		if (__a == null || __b == null)
			equals = Objects.equals(__a, __b);
		
		// Compare values otherwise
		else
			equals = TestResult.valueEquals(__a, __b);
		
		// Do not print null as is because it can be confused for actual null
		// values
		if (__a == null)
			__a = "-???-";
		if (__b == null)
			__b = "-???-";
		
		// Print out
		__ps.printf("\t%-15s %c %s %s%n", __key, (equals ? '=' : '!'),
			__a, __b);
	}
}
