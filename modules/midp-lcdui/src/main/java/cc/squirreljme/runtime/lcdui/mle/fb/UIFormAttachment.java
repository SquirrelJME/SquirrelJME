// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;

/**
 * This is an attachment which bases itself on top of {@link UIFormBracket}
 * via the {@link UIFormShelf}, it implements the simplest means of a
 * framebuffer by initializing just a {@link UIItemType#CANVAS} as its
 * display.
 *
 * @since 2022/07/20
 */
@Deprecated
public class UIFormAttachment
	implements FBAttachment
{
}
