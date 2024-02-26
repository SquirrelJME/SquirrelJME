// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/**
 * Listener for any player updates.
 *
 * @since 2022/05/17
 */
final class __PlayerListener__
	implements PlayerListener
{
	/** Has the duration updated? */
	boolean _hasDurationUpdated;
	
	/** Has the media stopped? */
	boolean _hasStopped;
	
	/** Has the media started? */
	boolean _hasStarted;
	
	/** Has the media ended? */
	boolean _hasEndOfMedia;
	
	/**
	 * {@inheritDoc}
	 * @since 2022/05/17
	 */
	@Override
	public void playerUpdate(Player __player, String __eventType,
		Object __eventValue)
	{
		Debugging.debugNote("EVENT: %h %s %s",
			__player, __eventType, __eventValue);
		
		synchronized (this)
		{
			switch (__eventType)
			{
				case PlayerListener.STARTED:
					this._hasStarted = true;
					break;
				
				case PlayerListener.DURATION_UPDATED:
					this._hasDurationUpdated = true;
					break;
				
				case PlayerListener.END_OF_MEDIA:
					this._hasEndOfMedia = true;
					break;
				
				case PlayerListener.STOPPED:
					this._hasStopped = true;
					break;
			}
			
			// Notify the monitor
			this.notifyAll();
		}
	}
}
