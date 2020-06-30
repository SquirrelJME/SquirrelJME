// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.UIFormShelf;

/**
 * This interface is used as a wrapper around {@link UIFormShelf}, it is
 * implemented by a class and essentially is used to forward calls to the
 * true backing implementation. This is here so that in the event that
 * {@link UIFormShelf} is not supported, that there is a fall-back.
 *
 * @since 2020/06/30
 */
public interface UIFormEngine
{
}
