// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import net.multiphasicapps.tac.TestConsumer;

/**
 * Tests that standard MIDI files play properly.
 *
 * @since 2022/05/17
 */
public class TestStandardMidiPlay
	extends TestConsumer<String>
{
	/** The song playing timeout in nanoseconds. */
	private static final long SONG_TIMEOUT_NS =
		10_000_000_000L;
	
	/**
	 * {@inheritDoc}
	 *
	 * @param __file The MIDI to load.
	 * @throws Exception On any exception
	 * @since 2022/05/17
	 */
	@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter", 
		"BusyWait"})
	@Override
	public void test(String __file)
		throws Exception
	{
		// Load in the MIDI
		Player player;
		try (InputStream in = this.getClass().getResourceAsStream(__file))
		{
			// Load the song
			player = Manager.createPlayer(in, "audio/midi");
		
			// Start playing the song
			__PlayerListener__ listener = new __PlayerListener__();
			player.addPlayerListener(listener);
			player.start();
			
			// Flags for cases
			boolean hasDurationUpdated = false;
			boolean hasStarted = false;
			boolean hasEndOfMedia = false;
			boolean hasStopped = false;
			
			// Wait for the end for the song to happen
			long baseTime = System.nanoTime();
			for (;;)
			{
				// Has the media end event happened?
				synchronized (listener)
				{
					hasDurationUpdated |= listener._hasDurationUpdated;
					hasStarted |= listener._hasStarted;
					hasEndOfMedia |= listener._hasEndOfMedia;
					hasStopped |= listener._hasStopped;
					
					if (hasDurationUpdated &&
						hasStarted &&
						hasEndOfMedia &&
						hasStopped)
						break;
				}
					
				// Debug
				Debugging.debugNote("MIDI Has: %b %b %b %b",
					hasDurationUpdated, hasStarted, hasEndOfMedia, hasStopped);
				
				// Stop if the end was reached
				if (hasStopped && hasEndOfMedia)
					break;
				
				// Too late?
				if ((System.nanoTime() - baseTime) >
					TestStandardMidiPlay.SONG_TIMEOUT_NS)
				{
					this.secondary("timeout", System.nanoTime());
					break;
				}
				
				// Wait a bit?
				try
				{
					Thread.sleep(250);
				}
				catch (InterruptedException ignored)
				{
				}
			}
			
			// Stop the song
			player.stop();
			
			// Make sure stop signal was sent
			boolean wasEverStopped = false;
			for (int i = 0; i < 10; i++)
			{
				// Check it
				synchronized (listener)
				{
					wasEverStopped |= listener._hasStopped;
				}
				
				// Stop early
				if (wasEverStopped)
					break;
				
				// Wait a bit?
				synchronized (listener)
				{
					// Did a stop occur?
					wasEverStopped |= listener._hasStopped;
				
					// Stop early
					if (wasEverStopped)
						break;
					
					try
					{
						listener.wait(250);
					}
					catch (InterruptedException ignored)
					{
					}
				}
			}
			
			// Is the time of the media okay?
			/*long mediaTime = player.getMediaTime();
			this.secondary("time",
				mediaTime != Player.TIME_UNKNOWN && mediaTime > 0);*/
			
			// Store flags
			/*this.secondary("duration", hasDurationUpdated);*/
			this.secondary("started", hasStarted);
			this.secondary("end", hasEndOfMedia);
			this.secondary("stopped", wasEverStopped);
		}
	}
}
