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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.SortedTreeSet;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestKey;
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
	/** Secondary results. */
	final Map<String, String> _secondary =
		new SortedTreeMap<>();
	
	/** The status of the test. */
	volatile TestStatus _status =
		TestStatus.NOT_RUN;
	
	/**
	 * Runs the given test with the given arguments and resulting in the
	 * given result.
	 *
	 * @param __args The arguments to the test.
	 * @return The result of the test.
	 * @throws Throwable On any thrown exception.
	 * @since 2018/10/06
	 */
	abstract Object __runTest(Object... __args)
		throws Throwable;
	
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
		
		// Read input and output parameters
		JavaManifest man;
		try (InputStream in = self.getResourceAsStream(basename + ".in"))
		{
			if (in == null)
			{
				// Warn that it is missing
				System.err.printf("WARN: No .in for %s (%s.in)%n",
					classname, basename);
				
				// Use a blank manifest instead
				man = new JavaManifest();
			}
			else
				man = new JavaManifest(in);
		}
		catch (IOException e)
		{
			// {@squirreljme.error BU07 Could not read the argument input.}
			throw new InvalidTestException("BU07", e);
		}
		
		// The main attributes contain the arguments
		JavaManifestAttributes attr = man.getMainAttributes();
		
		// Read the inputs for the test
		Object[] args = this.__parseInput(classname, __mainargs, attr);
		
		// Run the test, catch any exception to report it
		Object rv, thrown;
		try
		{
			rv = this.__runTest(args);
			thrown = new __NoExceptionThrown__();
		}
		catch (Throwable t)
		{
			// The test parameter is not valid, so whoops!
			if (t instanceof InvalidTestException)
			{
				// Exception was thrown
				this._status = TestStatus.TEST_EXCEPTION;
				
				// {@squirreljme.error BU08 The test failed to run properly.
				// (The given test)}
				System.err.printf("BU08 %s%n", classname);
				t.printStackTrace(System.err);
				return;
			}
			
			// Indicate an exception was thrown
			rv = new __ExceptionThrown__();
			thrown = t;
		}
		
		// Get string result representation and the expected value from the
		// manifest
		String rvstr = __CoreTest__.__convertToString(rv),
			thstr = __CoreTest__.__convertToString(thrown),
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
		boolean passedrv = __CoreTest__.__equals(rvstr, expectrv),
			passedth = __CoreTest__.__equals(thstr, expectth),
			passedse = __CoreTest__.__equals(secondary, expectse);
		
		// Print the throwable stack since this was not expected
		if (!passedth && (thrown instanceof Throwable))
			((Throwable)thrown).printStackTrace();
		
		// Print test result, the passed format is shorter as expected values
		// are not needed
		boolean passed = passedrv && passedth && passedse;
		PrintStream out = System.out;
		if (passed)
			out.printf("%s: PASS %s %s %s%n",
				classname, rvstr, thstr, secondary);
		
		// Failures print more information so that bugs may be found, etc.
		else
		{
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
		}
		
		// Set test status
		this._status = (passed ? TestStatus.SUCCESS : TestStatus.FAILED);
	}
	
	/**
	 * Stores a secondary value which can be additionally used as test
	 * comparison.
	 *
	 * @param __key The key to check.
	 * @param __v The value to check.
	 * @throws NullPointerException If no key was specified.
	 * @since 2018/10/07
	 */
	public final void secondary(String __key, Object __v)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Make it thread safe
		Map<String, String> secondary = this._secondary;
		synchronized (secondary)
		{
			int n;
			StringBuilder sb = new StringBuilder((n = __key.length()));
			for (int i = 0; i < n; i++)
			{
				char c = __key.charAt(i);
				
				if (c >= 'A' && c <= 'Z')
					c = Character.toLowerCase(c);
				else if (c == '+')
					c = 'p';
				else if (c == '#')
					c = 'h';
				else if (c == '.')
					c = 'd';
				
				sb.append(c);
			}
			
			// Use this formatted key instead
			String keyval,
				strval;
			secondary.put((keyval = sb.toString()),
				(strval = __convertToString(__v)));
			
			// Debug
			todo.DEBUG.note("%s=%s", keyval, strval);
		}
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
		
		// There is just a single program, so exit with the test status
		System.exit(this._status.ordinal());
	}
	
	/**
	 * Returns the test status.
	 *
	 * @return The test status.
	 * @since 2018/10/07
	 */
	public final TestStatus status()
	{
		return this._status;
	}
	
	/**
	 * Parses the input file for arguments.
	 *
	 * @param __sysprefix System property prefix.
	 * @param __mainargs Main program arguments.
	 * @param __attr Test attributes.
	 * @return The input arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private Object[] __parseInput(String __sysprefix, String[] __mainargs,
		JavaManifestAttributes __attr)
		throws NullPointerException
	{
		if (__mainargs == null)
			__mainargs = new String[0];
		
		if (__sysprefix == null || __mainargs == null || __attr == null)
			throw new NullPointerException("NARG");
		
		List<Object> rv = new ArrayList<>();
		
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
					parse = __attr.getValue("argument-" + i);
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
				return new __NoResult__();
			
			case "UndefinedResult":
				return new __UndefinedResult__();
			
			case "ExceptionThrown":
				return new __ExceptionThrown__();
			
			case "NoExceptionThrown":
				return new __NoExceptionThrown__();
			
			case "true":
				return Boolean.TRUE;
			
			case "false":
				return Boolean.FALSE;
			
			default:
				break;
		}
		
		// A string
		if (__s.startsWith("string:"))
			return __CoreTest__.__stringDecode(__s.substring(7));
		
		// Byte
		else if (__s.startsWith("byte:"))
			return Byte.valueOf(__s.substring(5));
			
		// Short
		else if (__s.startsWith("short:"))
			return Short.valueOf(__s.substring(6));
			
		// Char
		else if (__s.startsWith("char:"))
			return Character.valueOf(
				(char)Integer.valueOf(__s.substring(5)).intValue());
		
		// Integer
		else if (__s.startsWith("int:"))
			return Integer.valueOf(__s.substring(4));
		
		// Long
		else if (__s.startsWith("long:"))
			return Long.valueOf(__s.substring(5));
		
		// {@squirreljme.error BU09 The specified string cannot be converted
		// to an object because it an unknown representation, the conversion
		// is only one way. (The encoded data)}
		else if (__s.startsWith("other:"))
			throw new InvalidTestParameterException(
				String.format("BU09 %s", __s));
		
		// {@squirreljme.error BU0a The specified object cannot be
		// decoded because it is not known or does not support decoding.
		// (The encoded data)}
		else
			throw new InvalidTestParameterException(
				String.format("BU0a %s", __s));
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
		else if (__o instanceof __NoResult__)
			return "NoResult";
		
		// Undefined
		else if (__o instanceof __UndefinedResult__)
			return "UndefinedResult";
		
		// Exception was thrown
		else if (__o instanceof __ExceptionThrown__)
			return "ExceptionThrown";
		
		// No exception was thrown
		else if (__o instanceof __NoExceptionThrown__)
			return "NoExceptionThrown";
		
		// Boolean values
		else if (__o instanceof Boolean)
			return __o.toString();
		
		// String
		else if (__o instanceof String)
			return "string:" + __CoreTest__.__stringEncode((String)__o);
		
		// Byte
		else if (__o instanceof Byte)
			return "byte:" + __o;
		
		// Short
		else if (__o instanceof Short)
			return "short:" + __o;
		
		// Character
		else if (__o instanceof Character)
			return "char:" + (int)((Character)__o).charValue();
		
		// Integer
		else if (__o instanceof Integer)
			return "int:" + __o;
		
		// Long
		else if (__o instanceof Long)
			return "long:" + __o;
		
		// Throwable, meta data is used
		else if (__o instanceof Throwable)
		{
			Throwable t = (Throwable)__o;
			
			StringBuilder sb = new StringBuilder("throwable:");
			
			// The last package to shorten the classes
			String lastpkg = "java.lang.";
			
			// Throwables may be of multiple class types and it usually is
			// expected that they are some base class. For example a class
			// can thrown some FooIndexOutOfBoundsException which is based
			// off IndexOutOfBoundsException, if the result expects the
			// base class then it must still pass. So store the entire class
			// tree.
			boolean comma = false;
			for (Class<?> x = t.getClass(); x != null && x != Object.class;
				x = x.getSuperclass())
			{
				// Clip off the package if it matches
				String clname = x.getName();
				if (clname.startsWith(lastpkg))
					clname = clname.substring(lastpkg.length());
				
				// Otherwise remember the package used
				else
				{
					int ld = clname.lastIndexOf('.');
					if (ld >= 0)
						lastpkg = clname.substring(0, ld + 1);
					
					// Maybe default package?
					else
						lastpkg = "";
				}
				
				// Split to encode multiple classes
				if (comma)
					sb.append(',');
				comma = true;
				
				// Append class name here
				sb.append(clname);
			}
			
			// For debug purposes, encode the message information
			String msg = t.getMessage();
			if (msg != null)
			{
				sb.append(':');
				sb.append(__CoreTest__.__stringEncode(msg));
			}
			
			// Now metadata is included for this
			return sb.toString();
		}
		
		// Unrepresented object, just use its string representation in an
		// encoded form
		else
			return "other:" + __o.getClass().getName() + ":" +
				__CoreTest__.__stringEncode(__o.toString());
	}
	
	/**
	 * Compares the strings together, handling the special case of thrown
	 * exception to match the class tree.
	 *
	 * @param __act The actual value.
	 * @param __exp The expected value.
	 * @return If the strings are a match.
	 * @throws InvalidTestParameterException If a throwable is not formatted
	 * correctly.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/06
	 */
	private static boolean __equals(String __act, String __exp)
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
				ld = snip.lastIndexOf('.');
				if (ld >= 0)
					snip = snip.substring(ld + 1);
				
				// Is a match
				if (snip.equals(__exp))
					return true;
				
				// Nothing else to check, stop
				if (ld < 0)
					break;
				
				// Skip
				i = ld + 1;
			}
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
			
			// Second is missing key
			if (!__exp.containsKey(key))
				return false;
			
			// Match value
			if (!__CoreTest__.__equals(a.getValue(), __exp.get(key)))
				return false;
		}
		
		// Just scan through the keys in the second map, if any keys are
		// missing then extra keys were added
		for (String k : __exp.keySet())
			if (!__act.containsKey(k))
				return false;
		
		// Is a match
		return true;
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
		
		StringBuilder sb = new StringBuilder(__s.length());
		
		// Decode all input characters
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Ignore whitespace, since this could be an artifact of whitespace
			// used in the manifest
			if (c == ' ' || c == '\r' || c == '\n' || c == '\t')
				continue;
			
			// Escaped sequence requires parsing
			else if (c == '\\')
			{
				// Read the next character
				c = __s.charAt(++i);
				
				// Hex sequence for any character
				if (c == '@')
				{
					// Build string to decode hex sequence from
					StringBuilder sub = new StringBuilder(4);
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					sub.append(__s.charAt(++i));
					
					// Decode character
					c = (char)(Integer.valueOf(sub.toString(), 16).intValue());
				}
				
				// Code for specific characters
				else
					switch (c)
					{
							// Unchanged
						case '\\':
						case '\"':
							break;
							
							// Space
						case '_':
							c = ' ';
							break;
							
							// Newline
						case 'n':
							c = '\n';
							break;
							
							// Carriage return
						case 'r':
							c = '\r';
							break;
							
							// Tab
						case 't':
							c = '\t';
							break;
							
							// Delete
						case 'd':
							c = (char)0x7F;
							break;
						
							// Used to represent all the other upper
							// sequences
						default:
							if (c >= '0' && c <= '9')
								c = (char)(c - '0');
							else if (c >= 'A' && c <= 'Z')
								c = (char)((c - 'A') + 10);
							break;
					}
				
				// Append normalized
				sb.append(c);
			}
			
			// Not escaped
			else
				sb.append(c);
		}
		
		return sb.toString();
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
		
		StringBuilder sb = new StringBuilder(__s.length());
		
		// Encode characters to normalize them
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Is the character to be translated?
			boolean escape = false;
			switch (c)
			{
					// Just escape these
				case '\\':
				case '"':
					escape = true;
					break;
					
					// Make spaces just in the form of an underline to make
					// them more spacey but easier to see
				case ' ':
					escape = true;
					c = '_';
					break;
					
					// Newline
				case '\n':
					escape = true;
					c = 'n';
					break;
					
					// Carriage return
				case '\r':
					escape = true;
					c = 'r';
					break;
					
					// Tab
				case '\t':
					escape = true;
					c = 't';
					break;
					
					// Delete
				case 0x7F:
					escape = true;
					c = 'd';
					break;
				
				case 0x00:
				case 0x01:
				case 0x02:
				case 0x03:
				case 0x04:
				case 0x05:
				case 0x06:
				case 0x07:
				case 0x08:
				case 0x0b:
				case 0x0c:
				case 0x0e:
				case 0x1f:
				case 0x10:
				case 0x11:
				case 0x12:
				case 0x13:
				case 0x14:
				case 0x15:
				case 0x16:
				case 0x17:
				case 0x18:
				case 0x19:
				case 0x1a:
				case 0x1b:
				case 0x1c:
				case 0x1d:
				case 0x1e:
					escape = true;
					c = (char)((c < 10 ? '0' + c : 'A' + (c - 10)));
					break;
				
					// Not changed
				default:
					break;
			}
			
			// Character is out of range?
			if (c >= 0x7F)
				sb.append(String.format("\\@%04x", (int)c));
			
			// Append printable
			else
			{
				if (escape)
					sb.append('\\');
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
}

