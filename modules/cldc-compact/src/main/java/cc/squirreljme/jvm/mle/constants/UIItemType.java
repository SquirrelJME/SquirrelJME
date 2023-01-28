// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * The represents the type of a {@link UIItemBracket}.
 *
 * @since 2020/07/16
 */
@Exported
public interface UIItemType
{
	/** Special ID type for displays. */
	@Exported
	byte DISPLAY =
		-2;
	
	/** Special ID type for forms. */
	@Exported
	byte FORM =
		-1;
	
	/** Canvas with customized drawing and inputs. */
	@Exported
	byte CANVAS =
		0;
	
	/** Plain text and/or image label. */
	@Exported
	byte LABEL =
		1;
	
	/** Hyperlink. */
	@Exported
	byte HYPERLINK =
		2;
	
	/** Button. */
	@Exported
	byte BUTTON =
		3;
	
	/** Single-Line Text Box. */
	@Exported
	byte SINGLE_LINE_TEXT_BOX =
		4;
	
	/** Multi-line Text Box. */
	@Exported
	byte MULTI_LINE_TEXT_BOX =
		5;
	
	/** Spacer. */
	@Exported
	byte SPACER =
		6;
	
	/** Adjustable gauge. */
	@Exported
	byte ADJUSTABLE_GAUGE =
		7;
	
	/** Progress indicator. */
	@Exported
	byte PROGRESS_INDICATOR =
		8;
	
	/** Date selection. */
	@Exported
	byte DATE =
		9;
	
	/** Time selection. */
	@Exported
	byte TIME =
		10;
	
	/** Check box. */
	@Exported
	byte CHECK_BOX =
		11;
	
	/** Radio button. */
	@Exported
	byte RADIO_BUTTON =
		12;
	
	/** List of items. */
	@Exported
	byte LIST =
		13;
	
	/** The number of item types. */
	@Exported
	byte NUM_TYPES =
		14;
}
