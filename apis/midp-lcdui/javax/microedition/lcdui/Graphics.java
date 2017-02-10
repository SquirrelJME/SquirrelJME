// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * The class describes the interface that is used for drawing operations.
 *
 * When the clipping area is used, no pixels outside of it are drawn. This may
 * be used to draw special effects or have similar maskings.
 *
 * The anchor points {@link #BASELINE}, {@link #BOTTOM}, {@link #HCENTER},
 * {@link #LEFT}, {@link #RIGHT}, {@link #TOP}, and {@link #VCENTER} modify how
 * text and images are placed by allowing their placement positions to be
 * shifted accordingly.
 *
 * @since 2017/02/09
 */
public abstract class Graphics
{
	/**
	 * This is the anchorpoint for the baseline of text. This is not valid for
	 * anything which is not text. The baseline is considered to be point where
	 * all letters rest on. The baseline is not the lowest point, so for
	 * letters such as {@code j} the baseline will be higher than the lowest
	 * point.
	 */
	public static final int BASELINE =
		64;
	
	/** The anchor point to position below the specified point. */
	public static final int BOTTOM =
		32;
	
	/** Dotted stroke line style. */
	public static final int DOTTED =
		1;
	
	/** The anchor point to position in the center horizontally. */
	public static final int HCENTER =
		1;
	
	/** The anchor point to position the item to the left. */
	public static final int LEFT =
		4;
	
	/** The anchor point to position the item on the right. */
	public static final int RIGHT =
		8;
	
	/** Solid stroke line style. */
	public static final int SOLID =
		0;
	
	/**
	 * The blending mode, the destination alpha becomes the source and as such
	 * the operation is a copy.
	 */
	public static final int SRC =
		1;
	
	/**
	 * The blending mode, the source alpha is a composited over the
	 * destination.
	 */
	public static final int SRC_OVER =
		0;
	
	/** The anchor point to position the item on the top. */
	public static final int TOP =
		16;
	
	/** The anchor point to position in the center vertically. */
	public static final int VCENTER =
		2;
	
	/**
	 * Base initialization of graphics sub-class.
	 *
	 * Note that extending this class is specific to SquirrelJME and that
	 * doing so will cause programs to only run on SquirrelJME.
	 *
	 * @since 2016/10/10
	 */
	protected Graphics()
	{
	}
	
	/**
	 * This reduces the clipping area of the drawing so that 
	 *
	 * This is only used to reduce the clipping area, to make it larger use
	 * {@link #setClip(int, int, int int)}.
	 *
	 * @param __x The X coordinate of the clipping rectangle.
	 * @param __y The Y coordinate of the clipping rectangle.
	 * @param __w The width of the rectangle.
	 * @param __h The height of the rectangle.
	 * @since 2017/02/10
	 */
	public abstract void clipRect(int __x, int __y, int __w, int __h);
	
	/**
	 * This copies one region of the image to another region.
	 *
	 * Copying to a display device is not permitted because it may impact how
	 * double buffering is implemented, as such it is not supported.
	 *
	 * Pixels are copied directly and no alpha compositing is performed.
	 *
	 * If the source and destination overlap then it must be as if they did not
	 * overlap at all, this means that the destination will be an exact copy of
	 * the source.
	 *
	 * @param __sx The source X position, will be translated.
	 * @param __sy The source Y position, will be translated.
	 * @param __w The width to copy.
	 * @param __h The height to copy.
	 * @param __dx The destination X position, will be translated.
	 * @param __dy The destination Y position, will be translated.
	 * @param __anchor The anchor point of the destination.
	 * @throws IllegalArgumentException If the source region exceeds the size
	 * of the source image.
	 * @throws IllegalStateException If the destination is a display device.
	 * @since 2017/02/10
	 */
	public abstract void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException;
	
	/**
	 * This draws the outer edge of the ellipse from the given angles using
	 * the color, alpha, and stroke style.
	 *
	 * The coordinates are treated as if they were in a rectangular region. As
	 * such the center of the ellipse to draw the outline of is in the center
	 * of the specified rectangle.
	 *
	 * Note that no lines are drawn to the center point, so the shape does not
	 * result in a pie slice.
	 *
	 * The angles are in degrees and visually the angles match those of the
	 * unit circle correctly transformed to the output surface. As such, zero
	 * degrees has the point of {@code (__w, __h / 2)}, that is it points to
	 * the right. An angle at 45 degrees will always point to the top right
	 * corner.
	 *
	 * If the width or height are zero, then nothing is drawn. The arc will
	 * cover an area of {@code __w + 1} and {@code __h + 1}.
	 *
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __sa The starting angle in degrees, 
	 * @param __aa The offset from the starting angle, negative values indicate
	 * clockwise direction while positive values are counter clockwise.
	 * @since 2017/02/10
	 */ 
	public abstract void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa);
	
	public abstract void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	public abstract void drawChar(char __a, int __b, int __c, int __d);
	
	public abstract void drawChars(char[] __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void drawImage(Image __a, int __b, int __c, int __d);
	
	public abstract void drawLine(int __a, int __b, int __c, int __d);
	
	public abstract void drawRGB(int[] __a, int __b, int __c, int __d, int __e,
		int __f, int __g, boolean __h);
	
	public abstract void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h);
	
	public abstract void drawRect(int __a, int __b, int __c, int __d);
	
	public abstract void drawRegion(Image __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h, int __i);
	
	public abstract void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest);
	
	public abstract void drawRoundRect(int __a, int __b, int __c, int __d,
		int __e,  int __f);
	
	public abstract void drawString(String __a, int __b, int __c, int __d);
	
	public abstract void drawSubstring(String __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void drawText(Text __t, int __x, int __y);
	
	/**
	 * This draws the filled slice of an ellipse (like a pie slice) from the
	 * given angles using the color, alpha, and stroke style.
	 *
	 * Unlike {@link #drawArc(int, int, int, int, int, int)}, the width and
	 * height are not increased by a single pixel.
	 *
	 * Otherwise, this follows the same set of rules as
	 * {@link #drawArc(int, int, int, int, int, int)}.
	 *
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __sa The starting angle in degrees, 
	 * @param __aa The offset from the starting angle, negative values indicate
	 * clockwise direction while positive values are counter clockwise.
	 * @see drawArc(int, int, int, int, int, int)
	 * @since 2017/02/10
	 */
	public abstract void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa);
	
	public abstract void fillRect(int __a, int __b, int __c, int __d);
	
	public abstract void fillRoundRect(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract void fillTriangle(int __a, int __b, int __c, int __d,
		int __e, int __f);
	
	public abstract int getAlpha();
	
	public abstract int getBlendingMode();
	
	public abstract int getBlueComponent();
	
	public abstract int getClipHeight();
	
	public abstract int getClipWidth();
	
	public abstract int getClipX();
	
	public abstract int getClipY();
	
	public abstract int getColor();
	
	/**
	 * This returns the actual color that would be drawn onto the given
	 * display if it were set.
	 *
	 * @param __rgb The color to use, the format is {@code 0xRRGGBB}.
	 * @return The color that will actually be drawn on the display.
	 * @since 2017/02/09
	 */
	public abstract int getDisplayColor(int __rgb);
	
	public abstract Font getFont();
	
	public abstract int getGrayScale();
	
	public abstract int getGreenComponent();
	
	public abstract int getRedComponent();
	
	public abstract int getStrokeStyle();
	
	public abstract int getTranslateX();
	
	public abstract int getTranslateY();
	
	public abstract void setAlpha(int __a);
	
	public abstract void setAlphaColor(int __argb);
	
	public abstract void setAlphaColor(int __a, int __r, int __g, int __b);
	
	public abstract void setBlendingMode(int __m);
	
	/**
	 * Sets the new clipping area of the destination image. The previous
	 * clipping area is replaced.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2017/02/10
	 */
	public abstract void setClip(int __x, int __y, int __w, int __h);
	
	/**
	 * Sets the combined RGB value to use for drawing.
	 *
	 * @param __rgb The color to use, the format is {@code 0xRRGGBB}.
	 * @since 2017/02/09
	 */
	public abstract void setColor(int __rgb);
	
	/**
	 * Sets the color to use for drawing.
	 *
	 * @param __r The red value.
	 * @param __g The green value.
	 * @param __b The blue value.
	 * @since 2017/02/09
	 */
	public abstract void setColor(int __r, int __g, int __b);
	
	/**
	 * Sets the font to use for drawing operations.
	 *
	 * @param __a The font to use for drawing.
	 * @since 2017/02/09
	 */
	public abstract void setFont(Font __a);
	
	/**
	 * Sets a grayscale color which has all the red, green, and blue
	 * components set as the same value.
	 *
	 * @param __v The value to use for the color.
	 * @since 2017/02/09
	 */
	public abstract void setGrayScale(int __v);
	
	/**
	 * Sets the stroke style to use for lines.
	 *
	 * @param __a The stroke style, either {@link #SOLID} or {@link #DOTTED}.
	 * @since 2017/02/09
	 */
	public abstract void setStrokeStyle(int __a);
	
	/**
	 * Translates all coordinates so that they are offset by the given
	 * values, the values which are set act as the new origin.
	 *
	 * @param __x The X value to use for the new origin.
	 * @param __y The Y value to use for the new origin.
	 * @since 2017/02/09
	 */
	public abstract void translate(int __x, int __y);
}


