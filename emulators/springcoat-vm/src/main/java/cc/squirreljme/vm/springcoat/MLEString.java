// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.StringShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.util.Map;

/**
 * String functions.
 *
 * @since 2025/01/22
 */
public enum MLEString
	implements MLEFunction
{
	/** {@link StringShelf#stringCharAt(String, int)}. */
	CHAR_AT("stringCharAt:(Ljava/lang/String;I)C")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			int index = (Integer)__args[1];
			
			try
			{
				return object.toString().charAt(index);
			}
			catch (IndexOutOfBoundsException __e)
			{
				throw new SpringMLECallError("Index out of bounds.", __e);
			}
		}
	},
	
	/** {@link StringShelf#hashCode()}. */
	HASH("stringHash:(Ljava/lang/String;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			
			return object.toString().hashCode();
		}
	},
	
	/** {@link StringShelf#stringInit(String)}. */
	INIT("stringInit:(Ljava/lang/String;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			
			// Optionally set the string
			object.optional("");
			
			return null;
		}
	},
	
	/** {@link StringShelf#stringInit(String, char[], int, int)}. */
	INIT_CHAR("stringInit:(Ljava/lang/String;[CII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			SpringArrayObjectChar c = MLEObjects.notNull(
				SpringArrayObjectChar.class, __args[1]);
			int o = (Integer)__args[2];
			int l = (Integer)__args[3];
			
			// Set the string
			try
			{
				object.set(new String(c.array(), o, l));
			}
			catch (NullPointerException|IndexOutOfBoundsException __e)
			{
				throw new SpringMLECallError("Characters not valid.", __e);
			}
			
			return null;
		}
	},
	
	/** {@link StringShelf#stringInit(String, String)}. */
	INIT_STRING("stringInit:(Ljava/lang/String;Ljava/lang/String;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			SpringStringObject string = MLEObjects.notNull(
				SpringStringObject.class, __args[1]);
			
			// Set the string
			try
			{
				object.set(string.toString());
			}
			catch (NullPointerException __e)
			{
				throw new SpringMLECallError("String not valid.", __e);
			}
			
			return null;
		}
	},
	
	/** {@link StringShelf#stringIsIntern(String)}. */
	IS_INTERN("stringIsIntern:(Ljava/lang/String;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			
			// This is a bit of an expensive operation
			Map<String, SpringStringObject> stringMap =
				__thread.machine.__stringMap();
			synchronized (stringMap)
			{
				return (stringMap.containsValue(object) ? 1 : 0);
			}
		}
	},
	
	/** {@link StringShelf#stringLength(String)}. */
	LENGTH("stringLength:(Ljava/lang/String;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringStringObject object = MLEObjects.notNull(
				SpringStringObject.class, __args[0]);
			
			return object.toString().length();
		}
	},
	
	/** {@link StringShelf#stringValueOf(boolean, char[], int, int)}. */
	VALUE_OF_CHAR("stringValueOf:(Z[CII)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			boolean intern = (((Integer)__args[0]) != 0);
			SpringArrayObjectChar c = MLEObjects.notNull(
				SpringArrayObjectChar.class, __args[1]);
			int o = (Integer)__args[2];
			int l = (Integer)__args[3];
			
			// Create a new string
			try
			{
				return __thread.stringObject(intern,
					new String(c.array(), o, l));
			}
			catch (NullPointerException|IndexOutOfBoundsException __e)
			{
				throw new SpringMLECallError("String not valid.", __e);
			}
		}
	},
	
	/** {@link StringShelf#stringValueOf(boolean, String)}. */
	VALUE_OF_STRING("stringValueOf:" +
		"(ZLjava/lang/String;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/01/23
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			boolean intern = (((Integer)__args[0]) != 0);
			SpringStringObject string = MLEObjects.notNull(
				SpringStringObject.class, __args[1]);
			
			return __thread.stringObject(intern, string.toString());
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
	 * @since 2025/01/22
	 */
	MLEString(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/01/22
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
