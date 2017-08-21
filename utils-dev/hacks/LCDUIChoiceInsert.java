// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import javax.microedition.midlet.MIDlet;

import javax.microedition.lcdui.ChoiceGroup;

/**
 * This tests whether insert for choice allows size() or not.
 *
 * @since 2017/08/21
 */
public class LCDUIChoiceInsert
	extends MIDlet
{
	public void destroyApp(boolean __v)
	{
	}
	
	public void startApp()
	{
		ChoiceGroup cg = new ChoiceGroup("Test", ChoiceGroup.EXCLUSIVE);
		
		cg.insert(cg.size(), "A", null);
		cg.insert(cg.size(), "B", null);
	}
}

