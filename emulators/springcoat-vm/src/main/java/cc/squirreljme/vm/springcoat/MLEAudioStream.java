// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamPlayer;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.AudioConnectionObject;
import cc.squirreljme.vm.springcoat.brackets.AudioStreamObject;
import cc.squirreljme.vm.springcoat.callbacks.AudioStreamRendererAdapter;

/**
 * Wrapper for {@link AudioStreamShelf}.
 *
 * @since 2025/06/07
 */
public enum MLEAudioStream
	implements MLEFunction
{
	/**
	 * {@link AudioStreamShelf#attach(AudioStreamBracket, AudioStreamRenderer,
	 * int, int, int)}.
	 */
	ATTACH(MLEDispatcher.methodKey("attach",
		AudioConnectionBracket.class,
		AudioStreamBracket.class, AudioStreamRenderer.class,
		"I", "I", "I"))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			AudioStreamBracket __stream = MLEObjects.audioStream(__args[0]);
			SpringObject __renderer = MLEObjects.notNull(__args[1]);
			int __format = (int)__args[2];
			int __rate = (int)__args[3];
			int __channels = (int)__args[4];
			
			return new AudioConnectionObject(__thread.machine,
				AudioStreamShelf.attach(__stream,
					new AudioStreamRendererAdapter(__thread.machine,
						__renderer), __format,
					__rate, __channels));
		}
	},
	
	/**
	 * {@link AudioStreamShelf#decoder(String, String, int, int, int, byte[],
	 * int, int)}
	 */
	DECODER(MLEDispatcher.methodKey("decoder", AudioStreamPlayer.class,
		String.class, String.class, "I", "I", "I", "[B", "I", "I"))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link AudioStreamShelf#decoderSupports(String)}. */
	DECODER_SUPPORTS(MLEDispatcher.methodKey("decoderSupports", "Z",
		String.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			String __contentType = MLEObjects.string(__args[0]);
			
			return AudioStreamShelf.decoderSupports(__contentType) ? 1 : 0;
		}
	},
	
	/** {@link AudioStreamShelf#disconnect(AudioConnectionBracket)}. */
	DISCONNECT(MLEDispatcher.methodKey("disconnect", "V",
		AudioConnectionBracket.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			AudioConnectionBracket __connection =
				MLEObjects.audioConnection(__args[0]);
			
			AudioStreamShelf.disconnect(__connection);
			return null;
		}
	},
	
	/** {@link AudioStreamShelf#midiPort(String, int, int, int)}. */
	MIDI_PORT(MLEDispatcher.methodKey("midiPort", MidiPortBracket.class,
		"I", "I", "I"))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link AudioStreamShelf#midiRenderer(MidiPortBracket)}. */
	MIDI_RENDERER(MLEDispatcher.methodKey("midiRenderer",
		AudioStreamRenderer.class, MidiPortBracket.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	},
	
	/** {@link AudioStreamShelf#stream(int, int, int)}. */
	STREAM(MLEDispatcher.methodKey("stream", AudioStreamBracket.class,
		"I", "I", "I"))
	{
		/**
		 * {@inheritDoc}
		 * @since 2025/06/07
		 */
		@SuppressWarnings("MagicConstant")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return new AudioStreamObject(__thread.machine,
				AudioStreamShelf.stream((Integer)__args[0],
					(Integer)__args[1],
					(Integer)__args[2]));
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
	 * @since 2020/06/18
	 */
	MLEAudioStream(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
