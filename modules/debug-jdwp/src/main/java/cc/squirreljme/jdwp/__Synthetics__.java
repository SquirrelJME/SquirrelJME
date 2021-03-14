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
	 * Represents a synthetic class type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __Class__
		extends __Synthetics__
		implements JDWPClass
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
			return "Lcc/squirreljme/jvm/debug/SyntheticClass;";
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
	 * Represents a synthetic thread type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __Thread__
		extends __Synthetics__
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
			return "Lcc/squirreljme/jvm/debug/SyntheticThread;";
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
	 * Represents a synthetic thread group type for classes.
	 * 
	 * @since 2021/03/14
	 */
	private static final class __ThreadGroup__
		extends __Synthetics__
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
			return "Lcc/squirreljme/jvm/debug/SyntheticThreadGroup;";
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
}
