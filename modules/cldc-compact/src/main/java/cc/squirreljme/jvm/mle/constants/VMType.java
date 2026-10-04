// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * This represents the type of SquirrelJME virtual machine that is currently
 * being used, this is used for example to see which set of MLE calls are
 * valid and otherwise.
 *
 * @since 2020/06/16
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface VMType
{
	/** Not known. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte UNKNOWN =
		0;
	
	/** Running on Standard Java SE. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte JAVA_SE =
		1;
	
	/** Running on SpringCoat. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SPRINGCOAT =
		2;
	
	/** Running on SummerCoat. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SUMMERCOAT =
		3;
	
	/** Running on NanoCoat. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NANOCOAT =
		4;
	
	/** The number of VM types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_VMTYPES =
		5;
}
