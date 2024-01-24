// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPCommand;
import cc.squirreljme.jdwp.JDWPCommandSetVirtualMachine;
import cc.squirreljme.jdwp.JDWPErrorType;
import cc.squirreljme.jdwp.JDWPException;
import cc.squirreljme.jdwp.JDWPIdKind;
import cc.squirreljme.jdwp.JDWPIdSizes;
import cc.squirreljme.jdwp.JDWPPacket;
import cc.squirreljme.jdwp.host.views.JDWPViewHasInstance;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.jdwp.host.views.JDWPViewThreadGroup;
import cc.squirreljme.jdwp.host.views.JDWPViewType;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Virtual machine command set.
 *
 * @since 2021/03/12
 */
public enum JDWPHostCommandSetVirtualMachine
	implements JDWPCommandHandler
{
	/** Version information. */
	VERSION(JDWPCommandSetVirtualMachine.VERSION)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// VM Description
			rv.writeString(__controller.bind().vmDescription());
			
			// JDWP version (assuming Java 7?)
			rv.writeInt(1);
			rv.writeInt(7);
			
			// VM Information
			rv.writeString(__controller.bind().vmVersion());
			rv.writeString(__controller.bind().vmName());
			
			return rv;
		}
	},
	
	/** Class search by signature. */
	CLASSES_BY_SIGNATURE(JDWPCommandSetVirtualMachine.CLASSES_BY_SIGNATURE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// What are we looking for?
			String wantSig = __packet.readString();
			
			// Search all types for a signature
			List<Object> found = new LinkedList<>();
			JDWPViewType viewType = __controller.viewType();
			for (Object type : __controller.allTypes(false))
				if (wantSig.equals(viewType.signature(type)))
					found.add(type);
				
			// Write result
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Record all classes
			rv.writeInt(found.size());
			for (Object type : found)
			{
				// Store type for later grabbing
				__controller.getState().items.put(type);
				
				// Write the class type
				__controller.writeTaggedId(rv, type);
				
				// Classes are always loaded
				rv.writeInt(JDWPHostCommandSetVirtualMachine._CLASS_INITIALIZED);
			}
			
			return rv;
		}
	},
	
	/** All classes. */
	ALL_CLASSES(JDWPCommandSetVirtualMachine.ALL_CLASSES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return JDWPHostCommandSetVirtualMachine.__allClasses(
				__controller, __packet, false);
		}
	},
	
	/** All Threads. */
	ALL_THREADS(JDWPCommandSetVirtualMachine.ALL_THREADS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return JDWPHostCommandSetVirtualMachine.__writeInstances(
				__controller,
				__packet, __controller.viewThread(),
				__controller.allThreads(true));
		}
	},
	
	/** Top level thread groups. */
	TOP_LEVEL_THREAD_GROUPS(JDWPCommandSetVirtualMachine.TOP_LEVEL_THREAD_GROUPS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return JDWPHostCommandSetVirtualMachine.__writeInstances(__controller, __packet,
				__controller.viewThreadGroup(),
				__controller.allThreadGroups());
		}
	},
	
	/** Dispose of the debugging connection. */
	DISPOSE(JDWPCommandSetVirtualMachine.DISPOSE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Terminate the connection
			try
			{
				// Terminate the connection forcibly
				__controller.close();
			}
			
			// Perform final cleanup always
			finally
			{
				try
				{
					// Force all threads to resume
					JDWPViewThread viewThread = __controller.viewThread();
					for (Object thread : __controller
						.allThreads(false))
					{
						JDWPHostThreadSuspension suspension =
							viewThread.suspension(thread);
						while (suspension.query() > 0)
							suspension.resume();
					}
				}
				finally
				{
					// Clear all events
					__controller.getEventManager().clearAll();
				}
			}
			
			// No result
			return null;
		}
	},
	
	/** Returns the size of variable data. */
	ID_SIZES(JDWPCommandSetVirtualMachine.ID_SIZES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// field, method, object, reference, frame
			JDWPIdSizes idSizes = __packet.idSizes();
			for (int i = 0; i < JDWPIdKind.NUM_KINDS; i++)
				rv.writeInt(idSizes.getSize(i));
			
			return rv;
		}
	},
	
	/** Suspend all threads. */
	SUSPEND(JDWPCommandSetVirtualMachine.SUSPEND)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Tell all threads to suspend
			JDWPViewThread view = __controller.viewThread();
			for (Object thread : __controller.allThreads(false))
				view.suspension(thread).suspend();
			
			return null;
		}
	},
	
	/** Resume all threads. */
	RESUME(JDWPCommandSetVirtualMachine.RESUME)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Tell all threads to resume
			JDWPViewThread view = __controller.viewThread();
			for (Object thread : __controller.allThreads(false))
				view.suspension(thread).resume();
			
			return null;
		}
	},
	
	/** Force exit the virtual machine. */
	EXIT(JDWPCommandSetVirtualMachine.EXIT)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/04/30
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			int code = __packet.readInt();
			
			// Our main VM does not have an exit, however we can tell every
			// group that is running to terminate
			JDWPViewThreadGroup view = __controller.viewThreadGroup();
			for (Object threadGroup : __controller.allThreadGroups())
				view.exit(threadGroup, code);
			
			return null;
		}
	},
	
	/** Capabilities. */
	CAPABILITIES(JDWPCommandSetVirtualMachine.CAPABILITIES)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__capabilities(false, __controller, __packet);
		}
	},
	
	/** Class paths. */
	CLASS_PATHS(JDWPCommandSetVirtualMachine.CLASS_PATHS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/14
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			JDWPPacket rv = __controller.reply(
				__packet.id(), JDWPErrorType.NO_ERROR);
			
			// Base directory is the current directory
			rv.writeString(".");
			
			// There are no non-platform class paths
			rv.writeInt(0);
			
			// However the boot class path is used to refer to everything
			String[] classPaths = __controller.bind().debuggerLibraries();
			rv.writeInt(classPaths.length);
			for (String p : classPaths)
				rv.writeString(p);
			
			return rv;
		}
	},
	
	/** Hold events, keep them queued and not transmit them. */
	HOLD_EVENTS(JDWPCommandSetVirtualMachine.HOLD_EVENTS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Hold any events
			__controller.setHoldingEvents(true);
			return null;
		}
	},
	
	/** Release any events. */
	RELEASE_EVENTS(JDWPCommandSetVirtualMachine.RELEASE_EVENTS)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/12
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			// Resume events and let them flow
			__controller.setHoldingEvents(false);
			return null;
		}
	},
	
	/** New Capabilities. */
	CAPABILITIES_NEW(JDWPCommandSetVirtualMachine.CAPABILITIES_NEW)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return this.__capabilities(true, __controller, __packet);
		}
	},
	
	/** All loaded classes with generic signature included. */ 
	ALL_CLASSES_WITH_GENERIC_SIGNATURE(JDWPCommandSetVirtualMachine.ALL_CLASSES_WITH_GENERIC_SIGNATURE)
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/03/13
		 */
		@Override
		public JDWPPacket execute(JDWPHostController __controller,
			JDWPPacket __packet)
			throws JDWPException
		{
			return JDWPHostCommandSetVirtualMachine.__allClasses(
				__controller, __packet, true);
		}
	},
		
	/* End. */
	;
	
	/** Class is initialized. */
	private static final int _CLASS_INITIALIZED =
		4;
	
	/** The base command. */
	public final JDWPCommand command;
	
	/** The ID of the packet. */
	public final int id;
	
	/**
	 * Returns the ID used.
	 * 
	 * @param __id The ID used.
	 * @since 2021/03/12
	 */
	JDWPHostCommandSetVirtualMachine(JDWPCommand __id)
	{
		this.command = __id;
		this.id = __id.debuggerId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/23
	 */
	@Override
	public final JDWPCommand command()
	{
		return this.command;
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
	
	/**
	 * Writes all classes.
	 *
	 * @param __controller The output controller.
	 * @param __packet The packet to write to.
	 * @param __generic Include the generic field?
	 * @return The resultant packet.
	 * @since 2024/01/24
	 */
	@NotNull
	private static JDWPPacket __allClasses(
		JDWPHostController __controller, JDWPPacket __packet,
		boolean __generic)
	{
		List<Object> allTypes = __controller.allTypes(false);
		
		// If we have zero classes then JDB will crash since it always
		// expects at least a single class! So we need to go out of our
		// way to find a class!
		if (allTypes.isEmpty())
		{
			// Try to find a type from the first group we find
			JDWPViewThreadGroup viewGroup = __controller.viewThreadGroup();
			for (Object group : __controller.allThreadGroups())
			{
				// Do we have the object class?
				Object type = viewGroup.findType(group,
					"java/lang/Object");
				if (type != null)
				{
					// Use this type and register it
					allTypes.add(type);
					__controller.getState().items.put(type);
					
					// We found one, so we need not try more
					break;
				}
			}
			
			// If we did not find a class still, just say the VM is dead
			if (allTypes.isEmpty())
				throw JDWPErrorType.VM_DEAD.toss(null, 0);
		}
		
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// Write down all the known classes
		JDWPViewType viewType = __controller.viewType();
		rv.writeInt(allTypes.size());
		for (Object type : allTypes)
		{
			// The type ID
			__controller.writeTaggedId(rv, type);
			
			// The signatures
			rv.writeString(viewType.signature(type));
			
			// There are no actual generics
			if (__generic) 
				rv.writeString("");
			
			// All classes are considered initialized
			rv.writeInt(
				JDWPHostCommandSetVirtualMachine._CLASS_INITIALIZED);
		}
		
		return rv;
	}
	
	/**
	 * Returns the capabilities of the debugger and virtual machine.
	 * 
	 * @param __new Use new capabilities?
	 * @param __controller The controller used.
	 * @param __packet The packet to read from.
	 * @return The capabilities of the debugger.
	 * @throws JDWPException If the capabilities could not be returned.
	 * @since 2021/04/30
	 */
	JDWPPacket __capabilities(boolean __new, JDWPHostController __controller,
		JDWPPacket __packet)
		throws JDWPException
	{
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// canWatchFieldModification
		rv.writeBoolean(true);
		
		// canWatchFieldAccess
		rv.writeBoolean(true);
		
		// canGetBytecodes (supported by SquirrelJME)
		rv.writeBoolean(true);
		
		// canGetSyntheticAttribute
		rv.writeBoolean(false);
		
		// canGetOwnedMonitorInfo
		rv.writeBoolean(false);
		
		// canGetCurrentContendedMonitor
		rv.writeBoolean(false);
		
		// canGetMonitorInfo
		rv.writeBoolean(false);
		
		// New Capabilities
		if (__new)
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
			rv.writeBoolean(true);
			
			// canForceEarlyReturn
			rv.writeBoolean(false);
			
			// Reserved
			for (int i = 22; i <= 32; i++)
				rv.writeBoolean(false);
		}
		
		return rv;
	}
	
	/**
	 * Writes all instances of the given view.
	 * 
	 * @param __controller The controller used.
	 * @param __packet The target packet.
	 * @param __viewInstance The view for instances.
	 * @param __objects The objects to write.
	 * @return The packet.
	 * @since 2022/09/24
	 */
	static JDWPPacket __writeInstances(JDWPHostController __controller,
		JDWPPacket __packet, JDWPViewHasInstance __viewInstance,
		Object[] __objects)
	{
		// Write result
		JDWPPacket rv = __controller.reply(
			__packet.id(), JDWPErrorType.NO_ERROR);
		
		// Write it all out
		rv.writeInt(__objects.length);
		for (Object object : __objects)
		{
			Object objectInstance = __viewInstance.instance(object);
			
			__controller.writeObject(rv, objectInstance);
			
			// Store for later
			__controller.getState().items.put(object);
			__controller.getState().items.put(objectInstance);
		}
		
		return rv;
	}
}
