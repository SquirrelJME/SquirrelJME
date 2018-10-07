// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

/**
 * This is the core test framework which handles reading test information and
 * parameters, it forwards internally to other classes which handle
 * parameters and such.
 *
 * @since 2018/10/06
 */
abstract class __CoreTest__
	extends MIDlet
{
	/**
	 * Runs the given test with the given arguments and resulting in the
	 * given result.
	 *
	 * @param __args The arguments to the test.
	 * @return The result of the test.
	 * @since 2018/10/06
	 */
	abstract Object __runTest(Object... __args);
	
	/**
	 * Runs the specified test using the given main arguments, if any.
	 *
	 * @param __mainargs The main arguments to the test which allow parameters
	 * to be used accordingly.
	 * @since 2018/10/06
	 */
	public final void runTest(String... __mainargs)
	{
		if (__mainargs == null)
			__mainargs = new String[0];
		
		// Used to refer to resources for parameters and default results
		Class<?> self = this.getClass();
		
		// Get the basename of the class, used to refer to resources
		String classname = self.getName(),
			basename = classname;
		int ld = basename.lastIndexOf('.');
		if (ld >= 0)
			basename = basename.substring(ld + 1);
		
		// Read the inputs for the test
		Object[] args;
		try (InputStream in = self.getResourceAsStream(basename + ".in"))
		{
			args = this.__parseInput(classname, __mainargs, in);
		}
		catch (IOException e)
		{
			// {@squirreljme.error BU02 Could not read the argument input.}
			throw new InvalidTestException("BU02", e);
		}
		
		// Run the test, catch any exception to report it
		Object rv;
		try
		{
			rv = this.__runTest(args);
		}
		catch (Throwable t)
		{
			// The test parameter is not valid, so whoops!
			if (t instanceof InvalidTestException)
			{
				// {@squirreljme.error BU04 The test failed to run properly.
				// (The given test)}
				System.err.printf("BU04 %s%n", classname);
				t.printStackTrace(System.err);
				return;
			}
			
			// Make the result just the exception, to simplify things
			rv = t;
		}
		
		// Get string result representation
		String rvstr = __CoreTest__.__convertToString(rv),
			expected = null;
		
		// If an output file was specified, read the result from it and compare
		// it to see if it was expected
		try (InputStream out = self.getResourceAsStream(basename + ".out"))
		{
			if (out != null)
				try (BufferedReader br = new BufferedReader(
					new InputStreamReader(out, "utf-8")))
				{
					expected = br.readLine();
				}
		}
		catch (IOException e)
		{
			expected = null;
		}
		
		// Print the result of the test, in a manifest compatible format
		System.out.printf("%s: %s%n", classname, rvstr);
		
		// {@squirreljme.error BU05 An expected result was specified but it
		// did not match. (The class; The expected value; The result)}
		if (expected != null)
			if (!expected.equals(rvstr))
				System.err.printf("BU05 %s %s", classname, expected, rvstr);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	protected final void destroyApp(boolean __u)
	{
		// Not used
	}
	
	/**
	 * Runs the MIDlet, parses input test data then runs the test performing
	 * any test work that is needed.
	 *
	 * @since 2018/10/06
	 */
	protected final void startApp()
	{
		// Just forward to run, no main arguments are used at all
		this.runTest((String[])null);
	}
	
	/**
	 * Parses the input file for arguments.
	 *
	 * @param __sysprefix System property prefix.
	 * @param __mainargs Main program arguments.
	 * @param __in Input manifest data which may be null.
	 * @return The input arguments.
	 * @throws IOException On read errors.
	 * @since 2018/10/06
	 */
	private Object[] __parseInput(String __sysprefix, String[] __mainargs,
		InputStream __in)
		throws IOException
	{
		if (__mainargs == null)
			__mainargs = new String[0];
		
		List<Object> rv = new ArrayList<>();
		
		// Parse manifest
		JavaManifest man = (__in == null ? new JavaManifest() :
			new JavaManifest(__in));
		JavaManifestAttributes attr = man.getMainAttributes();
		
		// Read argument values in this order, to allow new ones to be
		// specified accordingly: main arguments, system properties, the
		// default input manifest
		for (int i = 1; i >= 1; i++)
		{
			String parse;
			
			// Main arguments first
			if (__mainargs != null && (i - 1) < __mainargs.length)
				parse = __mainargs[i - 1];
			
			// Then system properties
			else
			{
				String maybe = System.getProperty(__sysprefix + "." + i);
				if (maybe != null)
					parse = maybe;
				
				// Otherwise just read a value from the manifest
				else
					parse = attr.getValue("argument-" + i);
			}
			
			// Nothing to parse
			if (parse == null)
				break;
			
			// Parse the value
			rv.add(__CoreTest__.__convertToObject(parse));
		}
		
		return rv.<Object>toArray(new Object[rv.size()]);
	}
	
	/**
	 * Converts the given string to an object.
	 *
	 * @param __s The object to convert.
	 * @return The converted object.
	 * @throws InvalidTestParameterException If the input could not be
	 * converted.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private static Object __convertToObject(String __s)
		throws InvalidTestParameterException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Basic conversions
		switch (__s)
		{
			case "null":
				return null;
			
			case "NoResult":
				return new NoResult();
			
			case "true":
				return Boolean.TRUE;
			
			case "false":
				return Boolean.FALSE;
			
			default:
				break;
		}
		
		// {@squirreljme.error BU06 The specified string cannot be converted
		// to an object because it an unknown representation, the conversion
		// is only one way. (The encoded data)}
		if (__s.startsWith("other:"))
			throw new InvalidTestParameterException(
				String.format("BU06 %s", __s));
		
		throw new todo.TODO();
	}
	
	/**
	 * Converts the specified object to a string.
	 *
	 * @param __o The object to convert.
	 * @return The resulting string.
	 * @throws InvalidTestParameterException If the object cannot be
	 * converted.
	 * @since 2018/10/06
	 */
	private static String __convertToString(Object __o)
		throws InvalidTestParameterException
	{
		// Null
		if (__o == null)
			return "null";
		
		// No result generated
		else if (__o instanceof NoResult)
			return "NoResult";
		
		// Boolean values
		else if (__o instanceof Boolean)
			return __o.toString();
		
		// Unrepresented object, just use its string representation in an
		// encoded form
		else
			return "other:" + __o.getClass().getName() + ":" +
				__CoreTest__.__stringEncode(__o.toString());
	}
	
	/**
	 * Decodes the given string from a manifest safe format to a string.
	 *
	 * @param __s The string to decode.
	 * @return The decoded string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private static String __stringDecode(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Encodes the given string to a manifest safe format.
	 *
	 * @param __s The string to encode.
	 * @return The encoded string.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private static String __stringEncode(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

