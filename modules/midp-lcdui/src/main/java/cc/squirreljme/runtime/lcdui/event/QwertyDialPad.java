// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.event;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Canvas;

/**
 * This is a generic QWERTY keyboard to dial-pad.
 *
 * @since 2026/05/13
 */
@SquirrelJMEVendorApi
public class QwertyDialPad
	implements KeyCodeTranslator
{
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public boolean accepts(String __identifier, boolean __exact)
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		return __identifier.equals("cc.squirreljme.generic.qwertydialpad");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public int gameActionToVendor(int __ga, boolean __last)
	{
		// Always generic handling
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public int keyCodeToVendor(int __kc)
	{
		switch (__kc)
		{
				// Row 1
			case '1':
			case '!':
				return Canvas.KEY_NUM1;
				
			case '2':
			case '@':
				return Canvas.KEY_NUM2;
				
			case '3':
			case '$':
				return Canvas.KEY_NUM3;
				
				// Row 2
			case 'q':
			case 'Q':
				return Canvas.KEY_NUM4;
				
			case 'w':
			case 'W':
				return Canvas.KEY_NUM5;
				
			case 'e':
			case 'E':
				return Canvas.KEY_NUM6;
				
				// Row 3
			case 'a':
			case 'A':
				return Canvas.KEY_NUM7;
				
			case 's':
			case 'S':
				return Canvas.KEY_NUM8;
				
			case 'd':
			case 'D':
				return Canvas.KEY_NUM9;
				
				// Row 4
			case 'z':
			case 'Z':
				return Canvas.KEY_STAR;
				
			case 'x':
			case 'X':
				return Canvas.KEY_NUM0;
				
			case 'c':
			case 'C':
				return Canvas.KEY_POUND;
				
				// Arrow keys
			case 'f':
			case 'F':
				return Canvas.KEY_LEFT;
				
			case 'h':
			case 'H':
				return Canvas.KEY_RIGHT;
				
			case 't':
			case 'T':
				return Canvas.KEY_UP;
				
			case 'g':
			case 'G':
				return Canvas.KEY_DOWN;
		}
		
		// Fallback to default handling
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public int vendorToGameAction(int __vc, boolean __last)
	{
		// Always generic handling
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/05/13
	 */
	@Override
	public int vendorToKeyCode(int __vc)
	{
		switch (__vc)
		{
				// Row 1
			case Canvas.KEY_NUM1:	return '1';
			case Canvas.KEY_NUM2:	return '2';
			case Canvas.KEY_NUM3:	return '3';
			
				// Row 2
			case Canvas.KEY_NUM4:	return 'q';
			case Canvas.KEY_NUM5:	return 'w';
			case Canvas.KEY_NUM6:	return 'e';
			
				// Row 3
			case Canvas.KEY_NUM7:	return 'a';
			case Canvas.KEY_NUM8:	return 's';
			case Canvas.KEY_NUM9:	return 'd';
			
				// Row 4
			case Canvas.KEY_STAR:	return 'z';
			case Canvas.KEY_NUM0:	return 'x';
			case Canvas.KEY_POUND:	return 'c';
			
				// Arrow keys
			case Canvas.KEY_LEFT:	return 'f';
			case Canvas.KEY_RIGHT:	return 'h';
			case Canvas.KEY_UP:		return 't';
			case Canvas.KEY_DOWN:	return 'g';
		}
		
		// Fallback to default handling
		return 0;
	}
}
