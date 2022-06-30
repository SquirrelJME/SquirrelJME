// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
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
 * Represents a font.
 *
 * @see javax.microedition.lcdui.Font
 * @since 2021/11/30
 */
public class Font
{
	public static final int FACE_MONOSPACE =
		0x7200_0000;

	public static final int FACE_PROPORTIONAL =
		0x7300_0000;

	public static final int FACE_SYSTEM =
		0x7100_0000;

	public static final int SIZE_LARGE =
		0x70000300;

	public static final int SIZE_MEDIUM =
		0x70000200;

	public static final int SIZE_SMALL =
		0x70000100;

	public static final int STYLE_BOLD =
		0x70110000;

	public static final int STYLE_BOLDITALIC =
		0x70130000;

	public static final int STYLE_ITALIC =
		0x70120000;

	public static final int STYLE_PLAIN =
		0x70100000;

	public static final int TYPE_DEFAULT =
		0x00000000;
	public static final int TYPE_HEADING =
		0x00000001;
	
	public int getAscent()
	{
		throw Debugging.todo();
	}
	
	public int getBBoxHeight(String __s)
	{
		throw Debugging.todo();
	}
	
	public int getBBoxWidth(String __s)
	{
		throw Debugging.todo();
	}
	
	public int getDescent()
	{
		throw Debugging.todo();
	}
	
	public int getHeight()
	{
		throw Debugging.todo();
	}
	
	public int getLineBreak(String __s, int __off, int __len, int __w)
	{
		throw Debugging.todo();
	}
	
	public int stringWidth(String __s)
	{
		throw Debugging.todo();
	}
	
	public static Font getDefaultFont()
	{
		throw Debugging.todo();
	}
	
	public static Font getFont(int __type)
	{
		throw Debugging.todo();
	}
}
