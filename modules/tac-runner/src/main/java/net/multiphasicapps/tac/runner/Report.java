// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import java.io.PrintStream;
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This class contains the partial test report which contains all of the
 * information on which tests have passed and which have failed.
 *
 * @since 2019/01/23
 */
public final class Report
{
	/** Nanoseconds per second. */
	public static final double NANOS_PER_SECOND =
		1_000_000_000D;
		
	/** Properties to print. */
	private static final String[] _PROPERTIES =
		new String[]
		{
			"java.version",
			"java.vendor",
			"java.vendor.email",
			"java.vendor.url",
			"java.vm.name",
			"java.vm.version",
			"cc.squirreljme.apilevel",
			"java.vm.vendor",
			"java.vm.vendor.email",
			"java.vm.vendor.url",
			"java.runtime.name",
			"java.runtime.version",
			"os.name",
			"os.arch",
			"os.version",
			"microedition.locale",
			"microedition.profiles",
			"cc.squirreljme.vm.execpath",
			"cc.squirreljme.vm.freemem",
			"cc.squirreljme.vm.totalmem",
			"cc.squirreljme.vm.maxmem",
			"cc.squirreljme.debug",
		};
	
	/** Report mapping. */
	private final Map<String, ReportItem> _items =
		new SortedTreeMap<>();
	
	/** Number of tests ran. */
	private volatile int _numtests;
	
	/** Number of passes. */
	private volatile int _numpass;
	
	/** Number of failures. */
	private volatile int _numfail;
	
	/** The time spent running tests. */
	private volatile long _totalns;
	
	/**
	 * Adds a single test to the report.
	 *
	 * @param __n The test name.
	 * @param __p Did the test pass?
	 * @param __ns The duration of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void add(String __n, boolean __p, long __ns)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Pass/fail counts
			this._numtests++;
			if (__p)
				this._numpass++;
			else
				this._numfail++;
			
			// Time spent in test
			this._totalns += __ns;
			
			// Add item
			this._items.put(__n, new ReportItem(__n, __p, __ns));
		}
	}
	
	/**
	 * Generates the test report.
	 *
	 * @param __out The stream to write to.
	 * @param __t The type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void generate(PrintStream __out, ReportType __t)
		throws NullPointerException
	{
		if (__out == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Generate report depending on the format
		switch (__t)
		{
				// JUnit test report
			case JUNIT:
				this.generateJUnit(__out);
				break;
			
				// {@squirreljme.error AI01 Unknown report type.}
			default:
				throw new RuntimeException("AI01 " + __t);
		}
	}
	
	/**
	 * Generates a JUnit test report.
	 *
	 * JUnit test reports are XML based, which is quite complex however
	 * the test names and such are simple so we just need to output them
	 * correctly.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final void generateJUnit(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		Map<String, ReportItem> items = this._items;
		int numtests = this._numtests,
			numpass = this._numpass,
			numfail = this._numfail;
		long totalns = this._totalns;
		
		// Lock
		synchronized (this)
		{
			// XML header
			__out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			// Testsuites
			__out.printf("<testsuites failures=\"%d\" " +
				"tests=\"%d\" time=\"%s\">%n",
				numfail, numpass,
				Report.doubleToString(totalns / NANOS_PER_SECOND));
			
			// There is just a single test suite
			__out.printf("<testsuite name=\"SquirrelJME\" " +
				"tests=\"%d\" failures=\"%d\" id=\"0\" time=\"%s\">%n",
				numtests, numfail,
				Report.doubleToString(totalns / NANOS_PER_SECOND));
			
			// Dump environment showing the details of the VM, this is taken
			// from the hello demo. Just used to identify the VM and such.
			__out.println("<properties>");
			for (String p : _PROPERTIES)
				try
				{
					String v = System.getProperty(p);
					if (v != null)
						__out.printf("<property name=\"%s\" value=\"%s\" />%n",
							p, v.replace('"', '\''));
				}
				catch (SecurityException e)
				{
				}
			__out.println("</properties>");
			
			// Print every item
			for (ReportItem i : items.values())
			{
				__out.printf("<testcase name=\"%s\" " +
					"classname=\"%s\" status=\"%s\" time=\"%s\">%n",
					i.name, i.name, (i.passed ? "pass" : "fail"),
					Report.doubleToString(i.duration / NANOS_PER_SECOND));
				
				// Failure gets a note attached
				if (!i.passed)
					__out.println("<failure message=\"Failed.\" />");

				// End
				__out.println("</testcase>");
			}
			
			// End everything
			__out.println("</testsuite>");
			__out.println("</testsuites>");
		}
	}
	
	/**
	 * Converts a double to a string. This is needed because at the time of
	 * this writing doubles are not supported in {@link java.util.Formatter}.
	 *
	 * @param __d The double to convert.
	 * @return The resulting string.
	 * @since 2019/01/23
	 */
	public static final String doubleToString(double __d)
	{
		// Add whole number portion (just cast to number value)
		StringBuilder sb = new StringBuilder();
		sb.append((long)__d);
		
		// Add decimal point
		sb.append(".");
		
		// Add some fraction parts
		long frac = ((long)(__d * 1000.0D)) % 1000L;
		if (frac < 0)
			frac = -frac;
		sb.append(String.format("%03d", frac));
		
		// Build it
		return sb.toString();
	}
}

