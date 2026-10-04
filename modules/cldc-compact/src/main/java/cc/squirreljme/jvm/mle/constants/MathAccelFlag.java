// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Supported hardware math functions.
 *
 * @since 2025/05/03
 */
public interface MathAccelFlag
{
	/** acos. */
	byte ACOS =
		0x01;
	
	/** asin. */
	byte ASIN =
		0x02;
	
	/** atan. */
	byte ATAN =
		0x04;
	
	/** atan2. */
	byte ATAN2 =
		0x08;
	
	/** ceil. */
	byte CEIL =
		0x10;
	
	/** cos. */
	byte COS =
		0x20;
	
	/** floor. */
	byte FLOOR =
		0x40;
	
	/** log. */
	short LOG =
		0x80;
	
	/** pow. */
	short POW =
		0x100;
	
	/** round. */
	short ROUND =
		0x200;
	
	/** signum. */
	short SIGNUM =
		0x400;
	
	/** sin. */
	short SIN =
		0x800;
	
	/** sqrt. */
	short SQRT =
		0x1000;
	
	/** tan. */
	short TAN =
		0x2000;
	
	/** exp. */
	short EXP =
		0x4000;
}
