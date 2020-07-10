// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package security;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that implied permissions are correct.
 *
 * @since 2020/07/09
 */
public class TestImpliedPermission
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/09
	 */
	@Override
	public void test()
		throws Throwable
	{
		__NameOnly__ nShort = new __NameOnly__("squirrel");
		__NameOnly__ nWild = new __NameOnly__("squirrel*");
		__NameOnly__ nLong = new __NameOnly__("squirrel.cute");
		
		__SubNameOnly__ sShort = new __SubNameOnly__("squirrel");
		__SubNameOnly__ sWild = new __SubNameOnly__("squirrel*");
		__SubNameOnly__ sLong = new __SubNameOnly__("squirrel.cute");
		
		this.secondary("nsns", nShort.implies(nShort));
		this.secondary("nsnw", nShort.implies(nWild));
		this.secondary("nsnl", nShort.implies(nLong));
		
		this.secondary("nsss", nShort.implies(sShort));
		this.secondary("nssw", nShort.implies(sWild));
		this.secondary("nssl", nShort.implies(sLong));
		
		this.secondary("nwns", nWild.implies(nShort));
		this.secondary("nwnw", nWild.implies(nWild));
		this.secondary("nwnl", nWild.implies(nLong));
		
		this.secondary("nwss", nWild.implies(sShort));
		this.secondary("nwsw", nWild.implies(sWild));
		this.secondary("nwsl", nWild.implies(sLong));
		
		this.secondary("nlns", nLong.implies(nShort));
		this.secondary("nlnw", nLong.implies(nWild));
		this.secondary("nlnl", nLong.implies(nLong));
		
		this.secondary("nlss", nLong.implies(sShort));
		this.secondary("nlsw", nLong.implies(sWild));
		this.secondary("nlsl", nLong.implies(sLong));
		
		this.secondary("ssns", sShort.implies(nShort));
		this.secondary("ssnw", sShort.implies(nWild));
		this.secondary("ssnl", sShort.implies(nLong));
		
		this.secondary("ssss", sShort.implies(sShort));
		this.secondary("sssw", sShort.implies(sWild));
		this.secondary("sssl", sShort.implies(sLong));
		
		this.secondary("swns", sWild.implies(nShort));
		this.secondary("swnw", sWild.implies(nWild));
		this.secondary("swnl", sWild.implies(nLong));
		
		this.secondary("swss", sWild.implies(sShort));
		this.secondary("swsw", sWild.implies(sWild));
		this.secondary("swsl", sWild.implies(sLong));
		
		this.secondary("slns", sLong.implies(nShort));
		this.secondary("slnw", sLong.implies(nWild));
		this.secondary("slnl", sLong.implies(nLong));
		
		this.secondary("slss", sLong.implies(sShort));
		this.secondary("slsw", sLong.implies(sWild));
		this.secondary("slsl", sLong.implies(sLong));
	}
}
