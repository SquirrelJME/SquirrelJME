// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;

/**
 * The represents the type of a {@link UIItemBracket}.
 *
 * @since 2020/07/16
 */
public interface UIItemType
{
	/** Canvas with customized drawing and inputs. */
	byte CANVAS =
		0;
	
	/** Plain text and/or image. */
	byte PLAIN =
		1;
	
	/** Hyperlink. */
	byte HYPERLINK =
		2;
	
	/** Button. */
	byte BUTTON =
		3;
	
	/** Single-Line Text Box. */
	byte SINGLE_LINE_TEXT_BOX =
		4;
	
	/** Multi-line Text Box. */
	byte MULTI_LINE_TEXT_BOX =
		5;
	
	/** Spacer. */
	byte SPACER =
		6;
	
	/** Adjustable gauge. */
	byte ADJUSTABLE_GAUGE =
		7;
	
	/** Progress indicator. */
	byte PROGRESS_INDICATOR =
		8;
	
	/** Date selection. */
	byte DATE =
		9;
	
	/** Time selection. */
	byte TIME =
		10;
	
	/** Check box. */
	byte CHECK_BOX =
		11;
	
	/** Radio button. */
	byte RADIO_BUTTON =
		12;
	
	/** List of items. */
	byte LIST =
		13;
	
	/** The number of item types. */
	byte NUM_TYPES =
		14;
}
