// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.brackets.TypeBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.TypeObject;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * SpringCoat support for {@link ReflectionShelf}.
 *
 * @since 2022/09/07
 */
public enum MLEReflection
	implements MLEFunction
{
	/** {@link ReflectionShelf#invokeMain(TypeBracket, String...)}. */ 
	INVOKE_MAIN("invokeMain:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TypeBracket;[Ljava/lang/String;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/09/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Get parameters
			SpringClass type = MLEType.__type(__args[0]).getSpringClass();
			SpringArrayObject args =
				(SpringArrayObject)MLEType.__notNullObject(__args[1]);
			
			// Lookup the main method
			SpringMethod main = type.lookupMethod(true,
				new MethodNameAndType("main",
					"([Ljava/lang/String;)V"));
			
			// Enter the main method with all the passed arguments
			int deepness = __thread.thread.numFrames();
			__thread.thread.enterFrame(main, args);
			
			// Run until it finishes execution
			__thread.run(deepness);
			
			// Returns no value
			return null;
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
	 * @since 2022/09/07
	 */
	MLEReflection(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
