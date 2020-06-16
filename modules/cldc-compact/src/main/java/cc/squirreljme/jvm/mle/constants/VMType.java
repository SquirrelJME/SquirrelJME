// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This represents the type of SquirrelJME virtual machine that is currently
 * being used, this is used for example to see which set of MLE calls are
 * valid and otherwise.
 *
 * @since 2020/06/16
 */
public interface VMType
{
	/** Not known. */
	byte UNKNOWN =
		0;
	
	/** Running on Standard Java SE. */
	byte JAVA_SE =
		1;
	
	/** Running on SpringCoat. */
	byte SPRINGCOAT =
		2;
	
	/** Running on SummerCoat. */
	byte SUMMERCOAT =
		3;
	
	/** The number of VM types. */
	byte NUM_VMTYPES =
		4;
}
