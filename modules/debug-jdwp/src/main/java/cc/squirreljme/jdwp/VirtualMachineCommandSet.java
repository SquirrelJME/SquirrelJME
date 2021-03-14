// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

import java.util.LinkedList;
import java.util.List;

/**
 * Virtual machine command set.
 *
 * @since 2021/03/12
 */
public enum VirtualMachineCommandSet
	implements JDWPCommand
{
	/** Version information. */
	VERSION(1)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// VM Description
			rv.writeString(__controller.bind.vmDescription());
			
			// JDWP version (assuming Java 7?)
			rv.writeInt(1);
			rv.writeInt(7);
			
			// VM Information
			rv.writeString(__controller.bind.vmVersion());
			rv.writeString(__controller.bind.vmName());
			
			return rv;
		}
	},
	
	/** Class search by signature. */
	CLASSES_BY_SIGNATURE(2)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			String wantSig = __packet.readString();
			
			// Add any loaded classes, multiple VMs may result in multiple
			// classes being added
			List<JDWPClass> classes = new LinkedList<>();
			for (JDWPClass check : __controller.state.classes.values())
				if (wantSig.equals(check.debuggerFieldDescriptor()))
					classes.add(check);
				
			// Write result
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Record all classes
			rv.writeInt(classes.size());
			for (JDWPClass classy : classes)
			{
				// Write the class type
				rv.writeByte(classy.debuggerClassType().id);
				rv.writeId(classy);
				
				// Classes are always loaded
				rv.writeInt(VirtualMachineCommandSet._CLASS_INITIALIZED);
			}
			
			return rv;
		}
	},
	
	/** All Threads. */
	ALL_THREADS(4)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Request JVM Update
			List<JDWPThread> threads =
				__controller.debuggerUpdate(JDWPUpdateWhat.THREADS)
				.threads.values();
			
			// Write result
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeInt(threads.size());
			for (JDWPThread thread : threads)
				rv.writeId(thread);
			
			return rv;
		}
	},
	
	/** Top level thread groups. */
	TOP_LEVEL_THREAD_GROUPS(5)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Request JVM Update
			List<JDWPThreadGroup> groups =
				__controller.debuggerUpdate(JDWPUpdateWhat.THREAD_GROUPS)
				.threadGroups.values();
			
			// Write result
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			rv.writeInt(groups.size());
			for (JDWPThreadGroup group : groups)
				rv.writeId(group);
			
			return rv;
		}
	},
	
	/** Returns the size of variable data. */
	ID_SIZES(7)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// field, method, object, reference, frame
			for (int i = 0; i < 5; i++)
				rv.writeInt(JDWPConstants.ID_SIZE);
			
			return rv;
		}
	},
	
	/** Suspend all threads. */
	SUSPEND(8)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Update all threads available then tell every one to suspend
			for (JDWPThread thread : __controller
				.debuggerUpdate(JDWPUpdateWhat.THREADS).threads.values())
				thread.debuggerSuspend().suspend();
			
			return null;
		}
	},
	
	/** Resume all threads. */
	RESUME(9)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Update all threads available then tell every one to resume
			for (JDWPThread thread : __controller
				.debuggerUpdate(JDWPUpdateWhat.THREADS).threads.values())
				thread.debuggerSuspend().resume();
			
			return null;
		}
	},
	
	/** Capabilities. */
	CAPABILITIES(12)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Same as new capabilities
			if (true)
				return VirtualMachineCommandSet.CAPABILITIES_NEW
					.execute(__controller, __packet);
			
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// canWatchFieldModification
			rv.writeBoolean(false);
			
			// canWatchFieldAccess
			rv.writeBoolean(false);
			
			// canGetBytecodes
			rv.writeBoolean(false);
			
			// canGetSyntheticAttribute
			rv.writeBoolean(false);
			
			// canGetOwnedMonitorInfo
			rv.writeBoolean(false);
			
			// canGetCurrentContendedMonitor
			rv.writeBoolean(false);
			
			// canGetMonitorInfo
			rv.writeBoolean(false);
			
			return rv;
		}
	},
	
	/** Class paths. */
	CLASS_PATHS(13)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Base directory is the current directory
			rv.writeString(".");
			
			// There are no non-platform class paths
			rv.writeInt(0);
			
			// However the boot class path is used to refer to everything
			String[] classPaths = __controller.bind.debuggerLibraries();
			rv.writeInt(classPaths.length);
			for (String p : classPaths)
				rv.writeString(p);
			
			return rv;
		}
	},
	
	/** Hold events, keep them queued and not transmit them. */
	HOLD_EVENTS(15)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Hold any events
			__controller._holdEvents = true;
			return null;
		}
	},
	
	/** Release any events. */
	RELEASE_EVENTS(16)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Resume events and let them flow
			__controller._holdEvents = false;
			return null;
		}
	},
	
	/** New Capabilities. */
	CAPABILITIES_NEW(17)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// canWatchFieldModification
			rv.writeBoolean(false);
			
			// canWatchFieldAccess
			rv.writeBoolean(false);
			
			// canGetBytecodes
			rv.writeBoolean(false);
			
			// canGetSyntheticAttribute
			rv.writeBoolean(false);
			
			// canGetOwnedMonitorInfo
			rv.writeBoolean(false);
			
			// canGetCurrentContendedMonitor
			rv.writeBoolean(false);
			
			// canGetMonitorInfo
			rv.writeBoolean(false);
			
			// New Capabilities
			if (__packet.command() ==
				VirtualMachineCommandSet.CAPABILITIES_NEW.id)
			{
				// canRedefineClasses
				rv.writeBoolean(false);
				
				// canAddMethod
				rv.writeBoolean(false);
				
				// canUnrestrictedlyRedefineClasses
				rv.writeBoolean(false);
				
				// canPopFrames
				rv.writeBoolean(false);
				
				// canUseInstanceFilters
				rv.writeBoolean(false);
				
				// canGetSourceDebugExtension
				rv.writeBoolean(false);
				
				// canRequestVMDeathEvent
				rv.writeBoolean(false);
				
				// canSetDefaultStratum
				rv.writeBoolean(false);
				
				// canGetInstanceInfo
				rv.writeBoolean(false);
				
				// canRequestMonitorEvents
				rv.writeBoolean(false);
				
				// canGetMonitorFrameInfo
				rv.writeBoolean(false);
				
				// canUseSourceNameFilters
				rv.writeBoolean(false);
				
				// canGetConstantPool
				rv.writeBoolean(false);
				
				// canForceEarlyReturn
				rv.writeBoolean(false);
				
				// Reserved
				for (int i = 26; i <= 32; i++)
					rv.writeBoolean(false);
			}
			
			return rv;
		}
	},
	
	/** All loaded classes with generic signature included. */ 
	ALL_CLASSES_WITH_GENERIC_SIGNATURE(20)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.__reply(
				__packet.id(), ErrorType.NO_ERROR);
			
			// Get a list of every loaded class and return their information
			__controller.debuggerUpdate(JDWPUpdateWhat.LOADED_CLASSES);
			List<JDWPClass> classes = __controller.state.classes.values();
			rv.writeInt(classes.size());
			
			for (JDWPClass type : classes)
			{
				// The type ID
				rv.writeByte(type.debuggerClassType().id);
				rv.writeId(type);
				
				// The signatures, the generic is ignored
				rv.writeString(type.debuggerFieldDescriptor());
				rv.writeString("");
				
				// All classes are considered initialized
				rv.writeInt(VirtualMachineCommandSet._CLASS_INITIALIZED);
			}
			
			return rv;
		}
	},
		
	/* End. */
	;
	
	/** Class is initialized. */
	private static final int _CLASS_INITIALIZED =
		4;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/12
	 */
	VirtualMachineCommandSet(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/03/12
	 */
	@Override
	public final int debuggerId()
	{
		return this.id;
	}
}
