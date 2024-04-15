// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package security;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that permission equality is valid.
 *
 * @since 2020/07/09
 */
public class TestEqualPermission
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/09
	 */
	@SuppressWarnings("EqualsWithItself")
	@Override
	public void test()
		throws Throwable
	{
		__NameOnly__ nameA = new __NameOnly__("hello");
		__NameOnly__ nameB = new __NameOnly__("world");
		__SubNameOnly__ subA = new __SubNameOnly__("hello");
		__SubNameOnly__ subB = new __SubNameOnly__("world");
		
		this.secondary("nana", nameA.equals(nameA));
		this.secondary("nanb", nameA.equals(nameB));
		this.secondary("nasa", nameA.equals(subA));
		this.secondary("nasb", nameA.equals(subB));
		
		this.secondary("nbna", nameB.equals(nameA));
		this.secondary("nbnb", nameB.equals(nameB));
		this.secondary("nbsa", nameB.equals(subA));
		this.secondary("nbsb", nameB.equals(subB));
		
		this.secondary("sana", subA.equals(nameA));
		this.secondary("sanb", subA.equals(nameB));
		this.secondary("sasa", subA.equals(subA));
		this.secondary("sasb", subA.equals(subB));
		
		this.secondary("sbna", subB.equals(nameA));
		this.secondary("sbnb", subB.equals(nameB));
		this.secondary("sbsa", subB.equals(subA));
		this.secondary("sbsb", subB.equals(subB));
	}
}
