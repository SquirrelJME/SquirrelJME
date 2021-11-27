// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * Performs form layout by calling {@link Form#__updateSerially()}.
 *
 * @since 2021/11/27
 */
final class __DoFormLayout__
	implements Runnable
{
	/** The form to be updated. */
	private final Form _form;
	
	/**
	 * Initializes the form update.
	 * 
	 * @param __form The form to update.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/27
	 */
	__DoFormLayout__(Form __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this._form = __form;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/1/27
	 */
	@Override
	public void run()
	{
		this._form.__updateSerially();
	}
}
