// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Supported hardware math functions.
 *
 * @since 2025/05/03
 */
@SquirrelJMEVendorApi
public interface MathAccelFlag
{
	/** acos. */
	@SquirrelJMEVendorApi
	byte ACOS =
		0x01;
	
	/** asin. */
	@SquirrelJMEVendorApi
	byte ASIN =
		0x02;
	
	/** atan. */
	@SquirrelJMEVendorApi
	byte ATAN =
		0x04;
	
	/** atan2. */
	@SquirrelJMEVendorApi
	byte ATAN2 =
		0x08;
	
	/** ceil. */
	@SquirrelJMEVendorApi
	byte CEIL =
		0x10;
	
	/** cos. */
	@SquirrelJMEVendorApi
	byte COS =
		0x20;
	
	/** floor. */
	@SquirrelJMEVendorApi
	byte FLOOR =
		0x40;
	
	/** log. */
	@SquirrelJMEVendorApi
	short LOG =
		0x80;
	
	/** pow. */
	@SquirrelJMEVendorApi
	short POW =
		0x100;
	
	/** round. */
	@SquirrelJMEVendorApi
	short ROUND =
		0x200;
	
	/** signum. */
	@SquirrelJMEVendorApi
	short SIGNUM =
		0x400;
	
	/** sin. */
	@SquirrelJMEVendorApi
	short SIN =
		0x800;
	
	/** sqrt. */
	@SquirrelJMEVendorApi
	short SQRT =
		0x1000;
	
	/** tan. */
	@SquirrelJMEVendorApi
	short TAN =
		0x2000;
	
	/** exp. */
	@SquirrelJMEVendorApi
	short EXP =
		0x4000;
}
