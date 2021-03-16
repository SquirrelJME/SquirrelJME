// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Synthetics.
 *
 * @since 2021/03/14
 */
abstract class __Synthetics__
	implements JDWPId
{
	/** Fake class. */
	protected static final JDWPClass FAKE_CLASS =
		new __Class__();
	
	/** Fake thread type. */
	protected static final JDWPClass FAKE_THREAD =
		new __Thread__();
	
	/** Fake thread group. */
	protected static final JDWPClass FAKE_THREAD_GROUP =
		new __ThreadGroup__();
	
	/**
	 * Protects the constructor.
	 * 
	 * @since 2021/03/14
 	 */	
	protected __Synthetics__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/14
	 */
	@Override
	public int debuggerId()
	{
		return System.identityHashCode(this);
	}
	
	/**
	 * Base for any class types.
	 * 
	 * @since 2021/03/14
	 */
	private abstract static class __BaseClass__
		extends __Synthetics__
		implements JDWPClass
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPClass debuggerClass()
		{
			return this;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPClassType debuggerClassType()
		{
			return JDWPClassType.INTERFACE;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public String debuggerFieldDescriptor()
		{
			return "L" + this.debuggerBinaryName() + ";";
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public Object debuggerFieldValue(JDWPObjectLike __obj,
			JDWPField __field)
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPField[] debuggerFields()
		{
			return new JDWPField[0];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPClass[] debuggerInterfaceClasses()
		{
			return new JDWPClass[0];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPMethod[] debuggerMethods()
		{
			return new JDWPMethod[0];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public String debuggerSourceFile()
		{
			return null;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPClass debuggerSuperClass()
		{
			return null;
		}
	} 
	
	/**
	 * Represents a synthetic class type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __Class__
		extends __BaseClass__
	{
		__Class__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public String debuggerBinaryName()
		{
			return "cc/squirreljme/jvm/debug/SyntheticClass";
		}
	}
	
	/**
	 * Represents a synthetic thread group type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __ThreadGroup__
		extends __BaseClass__
		implements JDWPClass
	{
		__ThreadGroup__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public String debuggerBinaryName()
		{
			return "cc/squirreljme/jvm/debug/SyntheticThreadGroup";
		}
	}
	
	/**
	 * Represents a synthetic thread type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __Thread__
		extends __BaseClass__
		implements JDWPClass
	{
		__Thread__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public String debuggerBinaryName()
		{
			return "cc/squirreljme/jvm/debug/SyntheticThread";
		}
	}
}
