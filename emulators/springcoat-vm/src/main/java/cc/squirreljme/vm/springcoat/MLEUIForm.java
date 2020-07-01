// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.UIDisplayObject;
import cc.squirreljme.vm.springcoat.brackets.UIFormObject;

/**
 * SpringCoat support for {@link UIFormShelf}.
 *
 * @since 2020/06/30
 */
public enum MLEUIForm
	implements MLEFunction
{
	/** {@link UIFormShelf#displays()}. */
	DISPLAYS("displays:()[Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			UIDisplayBracket[] natives = UIFormShelf.displays();
			
			// Wrap the array
			int n = natives.length;
			SpringObject[] result = new SpringObject[n];
			for (int i = 0; i < n; i++)
				result[i] = new UIDisplayObject(natives[i]);
			
			// Use array as result
			return __thread.asVMObjectArray(__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;"),
				result);
		}
	},
	
	/** {@link UIFormShelf#displayShow(UIDisplayBracket, UIFormBracket)}. */ 
	DISPLAY_SHOW("displayShow:(Lcc/squirreljme/jvm/mle/brackets/" +
		"UIDisplayBracket;Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;)V")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link UIFormShelf#formNew()}. */
	FORM_NEW("formNew:()Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new UIFormObject(UIFormShelf.formNew());
		}
	},
	
	/** {@link UIFormShelf#metric(int)}. */
	METRIC("metric:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int metric = (int)__args[0];
			
			return UIFormShelf.metric(metric);
		}
	}, 
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/30
	 */
	MLEUIForm(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/30
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
