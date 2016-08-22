// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.squirreljme.paths.posix;

import java.util.Objects;
import net.multiphasicapps.squirreljme.paths.posix.PosixPath;
import net.multiphasicapps.squirreljme.paths.posix.PosixPaths;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestComparison;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.tests.TestFamily;
import net.multiphasicapps.tests.TestFragmentName;

/**
 * This class is used to test that POSIX paths work and operate correctly.
 *
 * @since 2016/08/21
 */
public class POSIXPathTests
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public TestFamily testFamily()
	{
		return new TestFamily(
			"net.multiphasicapps.squirreljme.paths.posix",
			"all");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Path system to use
		PosixPaths ink = PosixPaths.instance();
		
		// Single root
		__t.result("singleroot").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/")), "/");
		
		// Double root
		__t.result("doubleroot").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//")), "//");
		
		// Condense triple to single
		__t.result("condenseroot3").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("///")), "/");
		
		// Condense quadruple to single
		__t.result("condenseroot4").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("////")), "/");
		
		// Condense multiple fragments to single
		__t.result("condenseroot16").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("////", "////", "////", "////")), "/");
		
		// Just name
		__t.result("name").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("squirrels")), "squirrels");
		
		// Non-absolute as a single string
		__t.result("nonabssingle").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("foo/bar")), "foo/bar");
			
		// Non-absolute as two strings
		__t.result("nonabstwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("foo", "bar")), "foo/bar");
			
		// Non-absolute as two strings and a directory
		__t.result("nonabstwod").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("foo", "bar/baz")), "foo/bar/baz");
			
		// Non-absolute as three strings
		__t.result("nonabsthree").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("foo", "bar", "baz")), "foo/bar/baz");
			
		// Non-absolute as a directory and two strings
		__t.result("nonabsdtwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("foo/bar", "baz")), "foo/bar/baz");
		
		// Absolute as a single string
		__t.result("abssingle").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/foo/bar")), "/foo/bar");
			
		// Absolute as two strings
		__t.result("abstwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/foo", "bar")), "/foo/bar");
			
		// Absolute as two strings and a directory
		__t.result("abstwod").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/foo", "bar/baz")), "/foo/bar/baz");
			
		// Absolute as three strings
		__t.result("absthree").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/foo", "bar", "baz")), "/foo/bar/baz");
			
		// Absolute as a directory and two strings
		__t.result("absdtwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/foo/bar", "baz")), "/foo/bar/baz");
		
		// Special as a single string
		__t.result("specsingle").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//foo/bar")), "//foo/bar");
			
		// Special as two strings
		__t.result("spectwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//foo", "bar")), "//foo/bar");
			
		// Special as two strings and a directory
		__t.result("spectwod").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//foo", "bar/baz")), "//foo/bar/baz");
			
		// Special as three strings
		__t.result("specthree").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//foo", "bar", "baz")), "//foo/bar/baz");
			
		// Special as a directory and two strings
		__t.result("specdtwo").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//foo/bar", "baz")), "//foo/bar/baz");
		
		// Is root absolute?
		__t.result("isrootabs").compareObject(TestComparison.EQUALS,
			ink.get("/").isAbsolute(), true);
			
		// Is special absolute?
		__t.result("isspecabs").compareObject(TestComparison.EQUALS,
			ink.get("//").isAbsolute(), true);
			
		// Standard path has no root
		__t.result("isstandardnonabs").compareObject(TestComparison.EQUALS,
			ink.get("squirrels").isAbsolute(), false);
		
		// Is root with component absolute?
		__t.result("isrootcabs").compareObject(TestComparison.EQUALS,
			ink.get("/squirrels").isAbsolute(), true);
		
		// Is root with components absolute?
		__t.result("isrootcabsb").compareObject(TestComparison.EQUALS,
			ink.get("/squirrels/are/awesome").isAbsolute(), true);
		
		// Is non root multiple directories not absolute?
		__t.result("isnonrootcnonabs").compareObject(TestComparison.EQUALS,
			ink.get("squirrels/are/awesome").isAbsolute(), false);
		
		// Relative against relative
		__t.result("resolverelrel").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("squirrels").resolve(ink.get("great"))),
			"squirrels/great");
		
		// Relative against absolute
		__t.result("resolverelabs").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("squirrels").resolve(ink.get("/great"))),
			"/great");
		
		// Absolute against relative
		__t.result("resolveabsrel").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/squirrels").resolve(ink.get("great"))),
			"/squirrels/great");
	}
}

