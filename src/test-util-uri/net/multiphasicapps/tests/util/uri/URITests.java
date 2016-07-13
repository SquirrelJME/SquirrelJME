// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.util.uri;

import java.util.Arrays;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestComparison;
import net.multiphasicapps.tests.TestFamily;
import net.multiphasicapps.tests.TestFragmentName;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.util.uri.URI;

/**
 * This tests that the URI library operates correctly.
 *
 * @since 2016/06/23
 */
public class URITests
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/23
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException();
		
		// These tests are to make sure that URIs operate properly for the
		// most part. When URI support is complete, all tests will transition
		// to using URIs instead of Strings.
		__t.result("1").compareString(TestComparison.EQUALS,
			new URI("ScHeMe::", "very%21Special/##????",
			"Hello World! How are you?").toString(),
			"<TODO>");
		
		__t.result("2").compareString(TestComparison.EQUALS,
			new URI("http://foo:bar@multiphasicapps.net:80/" +
			"tarball/squirreljme-trunk.tar.gz?uuid=trunk#hello").toString(),
			"<TODO>");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/23
	 */
	@Override
	public TestFamily testFamily()
	{
		return new TestFamily("net.multiphasicapps.util.uri.URI",
			"general");
	}
}

