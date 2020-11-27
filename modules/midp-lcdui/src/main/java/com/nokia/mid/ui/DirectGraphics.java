// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import javax.microedition.lcdui.Image;

/**
 * This interface provides direct framebuffer pixel access.
 *
 * @since 2019/10/07
 */
public interface DirectGraphics
{
	int FLIP_HORIZONTAL  =
		8192;

	int FLIP_VERTICAL  =
		16384;

	int ROTATE_180  =
		180;

	int ROTATE_270  =
		270;

	int ROTATE_90  =
		90;

	int TYPE_BYTE_1_GRAY  =
		1;

	int TYPE_BYTE_1_GRAY_VERTICAL  =
		-1;

	int TYPE_BYTE_2_GRAY  =
		2;

	int TYPE_BYTE_332_RGB  =
		332;

	int TYPE_BYTE_4_GRAY  =
		4;

	int TYPE_BYTE_8_GRAY  =
		8;

	int TYPE_INT_888_RGB  =
		888;

	int TYPE_INT_8888_ARGB  =
		8888;

	int TYPE_USHORT_1555_ARGB  =
		1555;

	int TYPE_USHORT_444_RGB  =
		444;

	int TYPE_USHORT_4444_ARGB  =
		4444;

	int TYPE_USHORT_555_RGB  =
		555;

	int TYPE_USHORT_565_RGB  =
		565;
	
	@ApiDefinedDeprecated
    void drawImage(Image __var1, int __var2, int __var3, int __var4,
    	int __var5);
    
    void drawPixels(byte[] __var1, byte[] __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8, int __var9,
    	int __var10);
    	
    void drawPixels(short[] __var1, boolean __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8, int __var9,
    	int __var10);
    	
    void drawPixels(int[] __var1, boolean __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8, int __var9,
    	int __var10);
    	
    void drawPolygon(int[] __var1, int __var2, int[] __var3, int __var4,
    	int __var5, int __var6);
    	
    void drawTriangle(int __var1, int __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7);
    	
    void fillPolygon(int[] __var1, int __var2, int[] __var3, int __var4,
    	int __var5, int __var6);
    	
    void fillTriangle(int __var1, int __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7);
    	
    void getPixels(byte[] __var1, byte[] __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8, int __var9);
    	
    void getPixels(int[] __var1, int __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8);
    	
    void getPixels(short[] __var1, int __var2, int __var3, int __var4,
    	int __var5, int __var6, int __var7, int __var8);
    	
    void setARGBColor(int __var1);
}

