// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents a single display for an application, it is equivalent to
 * {@link javax.microedition.lcdui.Display}.
 *
 * @see javax.microedition.lcdui.Display
 * @since 2021/11/30
 */
public class Display
{
	public static final int KEY_0 =
		0x00;
		
	public static final int KEY_1 =
		0x01;
		
	public static final int KEY_2 =
		0x02;
		
	public static final int KEY_3 =
		0x03;
		
	public static final int KEY_4 =
		0x04;
		
	public static final int KEY_5 =
		0x05;
		
	public static final int KEY_6 =
		0x06;
		
	public static final int KEY_7 =
		0x07;
		
	public static final int KEY_8 =
		0x08;
		
	public static final int KEY_9 =
		0x09;
		
	public static final int KEY_ASTERISK =
		0x0A;

	public static final int KEY_DOWN =
		0x13;

	public static final int KEY_LEFT =
		0x10;

	public static final int KEY_POUND =
		0x0B;

	public static final int KEY_PRESSED_EVENT =
		0;

	public static final int KEY_RELEASED_EVENT =
		1;

	public static final int KEY_RIGHT =
		0x12;

	public static final int KEY_SELECT =
		0x14;

	public static final int KEY_SOFT1 =
		0x15;

	public static final int KEY_SOFT2 =
		0x16;

	public static final int KEY_UP =
		0x11;

	public static final int MAX_VENDOR_EVENT =
		127;

	public static final int MAX_VENDOR_KEY =
		0x1F;

	public static final int MEDIA_EVENT =
		8;

	public static final int MIN_VENDOR_EVENT =
		64;

	public static final int MIN_VENDOR_KEY =
		0x1A;

	public static final int RESET_VM_EVENT =
		5;

	public static final int RESUME_VM_EVENT =
		4;

	public static final int TIMER_EXPIRED_EVENT =
		7;

	public static final int UPDATE_VM_EVENT =
		6;
	
	/**
	 * Internal only.
	 * 
	 * @since 2021/11/30
	 */
	private Display()
	{
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final Frame getCurrent()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final int getHeight()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final int getWidth()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final boolean isColor()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final int numColors()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final void setCurrent(Frame __frame)
	{
		throw Debugging.todo();
	}
}
