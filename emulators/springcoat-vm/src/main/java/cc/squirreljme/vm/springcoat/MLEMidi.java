// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.brackets.MidiDeviceObject;
import cc.squirreljme.vm.springcoat.brackets.MidiPortObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * Implements {@link MidiShelf}.
 *
 * @since 2022/04/22
 */
public enum MLEMidi
	implements MLEFunction
{
	DATA_RECEIVE("dataReceive:(Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiPortBracket;[BII)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/04/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MidiPortBracket port = MLEMidi.__port(__args[0]);
				SpringArrayObjectByte buf = (SpringArrayObjectByte)__args[1];
				int off = (Integer)__args[2];
				int len = (Integer)__args[3];
				
				return MidiShelf.dataReceive(port, buf.array(), off, len);
			}
			catch (MLECallError e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},
	
	DATA_TRANSMIT("dataTransmit:(Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiPortBracket;[BII)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/04/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MidiPortBracket port = MLEMidi.__port(__args[0]);
				SpringArrayObjectByte buf = (SpringArrayObjectByte)__args[1];
				int off = (Integer)__args[2];
				int len = (Integer)__args[3];
				
				MidiShelf.dataTransmit(port, buf.array(), off, len);
				return null;
			}
			catch (MLECallError e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},
	
	DEVICE_NAME("deviceName:(Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiDeviceBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/04/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MidiDeviceBracket device = MLEMidi.__device(__args[0]);
				
				return MidiShelf.deviceName(device);
			}
			catch (MLECallError e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},
	
	DEVICES("devices:()[Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiDeviceBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/04/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				// Get devices
				MidiDeviceBracket[] devices = MidiShelf.devices();
				int len = devices.length;
				
				// Map to VM array
				SpringArrayObject result = __thread.allocateArray(
					__thread.resolveClass(
						"[Lcc/squirreljme/jvm/mle/brackets" +
						 "/MidiDeviceBracket;"),
					len);
				for (int i = 0; i < len; i++)
					result.set(i, new MidiDeviceObject(__thread.machine,
						devices[i]));
				
				return result;
			}
			catch (MLECallError e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},
	
	PORTS("ports:(Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiDeviceBracket;Z)[Lcc/squirreljme/jvm/mle/brackets/" +
		"MidiPortBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2022/04/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			try
			{
				MidiDeviceBracket device = MLEMidi.__device(__args[0]);
				boolean isTransmit = (((int)__args[1]) != 0);
				
				// Get ports the device has
				MidiPortBracket[] ports = MidiShelf.ports(device, isTransmit);
				int len = ports.length;
				
				// Map to VM array
				SpringArrayObject result = __thread.allocateArray(
					__thread.resolveClass(
						"[Lcc/squirreljme/jvm/mle/brackets" +
						 "/MidiPortBracket;"),
					len);
				for (int i = 0; i < len; i++)
					result.set(i, new MidiPortObject(__thread.machine,
						ports[i]));
				
				return result;
			}
			catch (MLECallError e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},

	/** End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/22
	 */
	MLEMidi(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/22
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
	/**
	 * Ensures that this is a {@link MidiDeviceObject} and returns the
	 * MIDI device.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link MidiDeviceBracket}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2022/04/22
	 */
	static MidiDeviceBracket __device(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof MidiDeviceObject))
			throw new SpringMLECallError("Not a MidiDeviceObject.");
		
		return ((MidiDeviceObject)__object).device; 
	}
	
	/**
	 * Ensures that this is a {@link MidiDeviceObject} and returns the
	 * MIDI port.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link MidiPortBracket}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2022/04/22
	 */
	static MidiPortBracket __port(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof MidiPortObject))
			throw new SpringMLECallError("Not a MidiPortObject.");
		
		return ((MidiPortObject)__object).port; 
	}
}
