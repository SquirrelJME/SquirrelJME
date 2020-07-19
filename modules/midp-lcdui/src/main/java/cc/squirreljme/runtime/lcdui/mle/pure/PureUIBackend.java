// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.pure;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * This is a form engine which is used when UI Forms are supported by the
 * native implementation.
 *
 * @since 2020/06/30
 */
public class PureUIBackend
	implements UIBackend
{
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public UIDisplayBracket[] displays()
	{
		return UIFormShelf.displays();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public void formDelete(UIFormBracket __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		// Natively delete the form
		UIFormShelf.formDelete(__form);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public UIFormBracket formNew()
	{
		return UIFormShelf.formNew();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/01
	 */
	@Override
	public void displayShow(UIDisplayBracket __display, UIFormBracket __form)
		throws NullPointerException
	{
		if (__display == null || __form == null)
			throw new NullPointerException("NARG");
		
		UIFormShelf.displayShow(__display, __form);
	}
	
}
