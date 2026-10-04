// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Supported hardware math functions.
 *
 * @since 2025/05/03
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface MathAccelFlag
{
	/** acos. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ACOS =
		0x01;
	
	/** asin. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ASIN =
		0x02;
	
	/** atan. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ATAN =
		0x04;
	
	/** atan2. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ATAN2 =
		0x08;
	
	/** ceil. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte CEIL =
		0x10;
	
	/** cos. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte COS =
		0x20;
	
	/** floor. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FLOOR =
		0x40;
	
	/** log. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short LOG =
		0x80;
	
	/** pow. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short POW =
		0x100;
	
	/** round. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short ROUND =
		0x200;
	
	/** signum. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SIGNUM =
		0x400;
	
	/** sin. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SIN =
		0x800;
	
	/** sqrt. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short SQRT =
		0x1000;
	
	/** tan. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short TAN =
		0x2000;
	
	/** exp. */
	@SquirrelJMENativeApi(min = "0.4.0")
	short EXP =
		0x4000;
}
