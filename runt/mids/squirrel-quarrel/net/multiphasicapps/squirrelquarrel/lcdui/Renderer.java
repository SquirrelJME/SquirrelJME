// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

/**
 * This contains the renderer for the level and performs the actual drawing
 * of the game to the canvas.
 *
 * @since 2018/03/18
 */
public final class Renderer
{
	/** Debug image. */
	protected final Image debugimage;
	
	/** The width of the level in pixels. */
	protected final int levelpxw;
	
	/** The height of the level in pixels. */
	protected final int levelpxh;
	
	/** The width of the level in megatiles. */
	protected final int levelmtw;
	
	/** The height of the level in megatiles. */
	protected final int levelmth;
	
	/** The mega tile cacher, used to combine megatiles into one image. */
	protected final MegaTileCacher mtcacher;
	
	/** Units to draw. */
	private final List<Unit.Pointer> _drawunits =
		new ArrayList<>();
	
	/**
	 * Initializes some parts of the rendered.
	 *
	 * @since 2018/03/18
	 */
	{
		// Draw debug image
		int ww = 200, hh = 100;
		Image debugimage = Image.createImage(ww, hh, true, 0);
		this.debugimage = debugimage;
		ww--;
		hh--;
		Graphics g = debugimage.getGraphics();
		g.setColor(0xFF0000);
		g.drawLine(0, 0, ww, 0);
		g.drawLine((ww / 2), (hh / 2), (ww / 2), 0);
		g.setColor(0x00FF00);
		g.drawLine(ww, 0, ww, hh);
		g.drawLine((ww / 2), (hh / 2), ww, (hh / 2));
		g.setColor(0x0000FF);
		g.drawLine(ww, hh, 0, hh);
		g.drawLine((ww / 2), (hh / 2), (ww / 2), hh);
		g.setColor(0xFFFF00);
		g.drawLine(0, hh, 0, 0);
		g.drawLine((ww / 2), (hh / 2), 0, (hh / 2));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void paint(Graphics __g)
	{
		// If already painting, do not duplicate a paint
		if (this._inpaint)
			return;
		this._inpaint = true;
		
		// Get the current frame the game is on
		Game game = this.game;
		this._renderframe = game.frameCount();
		Level level = game.level();
		int framenum = game.frameCount();
		Player viewplayer = this._viewplayer;
		
		// Get the viewport
		int viewx = this._viewx,
			viewy = this._viewy,
			vieww = this._vieww,
			viewh = this._viewh;
		
		// MegaTiles do not often change
		MegaTileCacher mtcacher = this.mtcacher;
		
		// Megatile draw loop
		int msx = this._msx,
			msy = this._msy,
			mex = this._mex,
			mey = this._mey;
		for (int my = msy,
			sy = mapToScreenY(my * MegaTile.MEGA_TILE_PIXEL_SIZE),
			bsx = mapToScreenX(msx * MegaTile.MEGA_TILE_PIXEL_SIZE);
			my < mey; my++, sy += MegaTile.MEGA_TILE_PIXEL_SIZE)
			for (int mx = msx, sx = bsx; mx < mex; mx++,
				sx += MegaTile.MEGA_TILE_PIXEL_SIZE)
			{
				// Get the megatile here
				MegaTile mt = level.megaTile(mx, my);
				
				// Draw it
				__g.drawImage(mtcacher.cacheMegaTile(mt), sx, sy, 0);
					
				// Draw units in this mega tile
				__drawUnits(__g, mt);
				
				// Setup flags for fog drawing
				__g.setColor(0x000000);
				__g.setStrokeStyle(Graphics.DOTTED);
				
				// Draw fog scanlines
				for (int sty = 0, psy = sy, bpsx = sx;
					sty < MegaTile.TILES_PER_MEGA_TILE;
					sty++, psy += MegaTile.TILE_PIXEL_SIZE)
					for (int stx = 0, psx = bpsx;
						stx < MegaTile.TILES_PER_MEGA_TILE;
						stx++, psx += MegaTile.TILE_PIXEL_SIZE)
					{
						// Ignore revealed tiles
						if (mt.subTileRevealed(viewplayer, stx, sty))
							continue;
						
						// Otherwise find the next revealed tile (or the end)
						int end;
						for (end = stx + 1; end < MegaTile.TILES_PER_MEGA_TILE;
							end++)
							if (mt.subTileRevealed(viewplayer, end, sty))
								break;
						
						// Draw dotted lines for fog
						int endpx = sx + (stx * MegaTile.TILE_PIXEL_SIZE) +
							(end * MegaTile.TILE_PIXEL_SIZE),
							endpy = psy + MegaTile.TILE_PIXEL_SIZE;
						for (int py = psy; py < endpy; py++)
						{
							// Lines off to the side can get clipped where the
							// dot patterns stop being correct
							int bx = (psx < 0 ? 0 : psx);
							__g.drawLine(bx + (py & 1), py, endpx, py);
						}
						
						// Set current spot to the end
						stx = end;
						psx = bpsx + (end * MegaTile.TILE_PIXEL_SIZE);
					}
			}
		
		// Reset translation for the HUD
		__g.translate(-__g.getTranslateX(), -__g.getTranslateY());
		
		// Draw the automap in the bottom left corner
		Image map = this.automap.update();
		__g.setAlpha(160);
		__g.drawImage(map, 0, viewh, Graphics.LEFT | Graphics.BOTTOM);
		
		// Draw debug image
		__g.setAlpha(255);
		Image debugimage = this.debugimage;
		int ww = debugimage.getWidth(), hh = debugimage.getHeight();
		int sx = 50 - 5, sy = 50 - 5,
			ex = sx + ((ww * 3) + 10), ey = sy + ((hh * 3) + 10);
		/*__g.drawRegion(debugimage, 0, 0, ww, hh, Sprite.TRANS_NONE,
			sx + 5, sy + 5, 0, ww * 3, hh * 3);*/
		__g.setColor(0xFF0000);
		__g.drawLine(sx, sy, ex, sy);
		__g.setColor(0x00FF00);
		__g.drawLine(ex, sy, ex, ey);
		__g.setColor(0x0000FF);
		__g.drawLine(ex, ey, sx, ey);
		__g.setColor(0xFFFF00);
		__g.drawLine(sx, ey, sx, sy);
		
		// No longer painting
		this._inpaint = false;
	}
	
	/**
	 * Draws units in this megatile.
	 *
	 * @param __g The target graphics.
	 * @param __mt The megatile to source from.
	 * @since 2017/02/17
	 */
	private void __drawUnits(Graphics __g, MegaTile __mt)
	{
		// Store the old clip
		int oldcx = __g.getClipX(),
			oldcy = __g.getClipY(),
			oldcw = __g.getClipWidth(),
			oldch = __g.getClipHeight();
		
		// Used for selection boxes
		__g.setColor(0xFFFF00);
		__g.setStrokeStyle(Graphics.SOLID);
		
		// Set new clipping so units are not drawn outside of the tile and
		// potentially into other tiles
		int mdx = mapToScreenX(__mt.x() * MegaTile.MEGA_TILE_PIXEL_SIZE),
			mdy = mapToScreenY(__mt.y() * MegaTile.MEGA_TILE_PIXEL_SIZE);
		__g.setClip(mdx, mdy, mdx + MegaTile.MEGA_TILE_PIXEL_SIZE,
			mdy + MegaTile.MEGA_TILE_PIXEL_SIZE);
		
		// Load units
		List<Unit.Pointer> drawunits = this._drawunits;
		drawunits.clear();
		__mt.loadLinkedUnits(drawunits);
		
		// Draw them
		for (int i = 0, n = drawunits.size(); i < n; i++)
			try
			{
				// Get unit, and its information
				Unit unit = drawunits.get(i).get();
				UnitType type = unit.type();
				if (type == null)
					continue;
				UnitInfo info = type.info();
				
				// Get draw position on the screen
				int dx = mapToScreenX(unit.centerX()),
					dy = mapToScreenY(unit.centerY());
				
				// Draw sprite
				__g.drawRect(dx - 10, dy - 10, 20, 20);
			}
			
			// Ignore due to threading potential
			catch (UnitDeletedException e)
			{
			}
		
		// Restore the old clip
		__g.setClip(oldcx, oldcy, oldcw, oldch);
	}
}

