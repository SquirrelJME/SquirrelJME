/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/scritchui.h"
#include "lib/scritchui/scritchuiPencil.h"
#include "lib/scritchui/scritchuiPencilFont.h"
#include "lib/scritchui/scritchuiPencilFontSqf.h"
#include "sjme/dylib.h"

const sjme_jbyte sqf_font_sanserif_12_0_huffBits[] = 
{
};
const sjme_jbyte sqf_font_sanserif_12_0_charWidths[] = 
{
10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 7, 6, 10, 8, 2, 3, 3, 6, 4, 3, 4, 
3, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 3, 3, 5, 4, 5, 6, 7, 6, 6, 6, 6, 
6, 6, 6, 6, 2, 6, 7, 6, 8, 6, 6, 6, 6, 6, 6, 6, 6, 6, 8, 6, 6, 6, 4, 
5, 4, 6, 4, 3, 6, 6, 5, 6, 5, 5, 6, 6, 2, 3, 6, 2, 10, 6, 6, 6, 6, 5, 
6, 4, 6, 6, 6, 6, 6, 6, 4, 2, 4, 6, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
2, 5, 5, 0, 8, 0, 6, 0, 7, 4, 7, 5, 3, 7, 5, 4, 4, 3, 3, 0, 6, 6, 4, 
0, 4, 4, 7, 0, 0, 0, 6, 6, 6, 6, 6, 6, 7, 8, 5, 5, 5, 5, 5, 3, 2, 3, 
3, 7, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 5, 8, 6, 6, 6, 6, 6, 6, 
8, 5, 5, 5, 5, 5, 3, 2, 3, 3, 6, 6, 5, 5, 5, 5, 5, 4, 6, 6, 6, 6, 6, 
6, 7, 6
};
const sjme_jbyte sqf_font_sanserif_12_0_charXOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_0_charYOffset[] = 
{
4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 10, 7, 8, 4, 6, 12, 11, 11, 8, 10, 
11, 10, 11, 9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 11, 11, 9, 10, 9, 8, 7, 
8, 8, 8, 8, 8, 8, 8, 8, 12, 8, 7, 8, 6, 8, 8, 8, 8, 8, 8, 8, 8, 8, 6, 
8, 8, 8, 10, 9, 10, 8, 9, 11, 7, 8, 9, 8, 9, 9, 8, 8, 12, 11, 8, 12, 
4, 8, 8, 8, 8, 9, 8, 9, 8, 8, 8, 8, 8, 8, 10, 12, 10, 8, 4, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 12, 9, 9, 0, 6, 0, 8, 0, 7, 9, 7, 9, 10, 7, 9, 10, 
10, 11, 11, 0, 8, 8, 10, 0, 10, 10, 7, 0, 0, 0, 8, 8, 8, 8, 8, 8, 8, 
6, 9, 9, 9, 9, 9, 11, 11, 10, 10, 7, 8, 8, 8, 8, 8, 8, 8, 7, 8, 8, 8, 
8, 8, 9, 8, 7, 7, 7, 7, 7, 7, 6, 9, 9, 9, 9, 9, 11, 11, 10, 10, 8, 8, 
9, 9, 9, 9, 9, 10, 8, 8, 8, 8, 8, 8, 9, 8
};
const sjme_jbyte sqf_font_sanserif_12_0_charFlags[] = 
{
1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 
0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1
};
const sjme_jshort sqf_font_sanserif_12_0_charBmpOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24, 36, 48, 60, 72, 96, 108, 120, 132, 
144, 156, 168, 180, 192, 204, 216, 228, 240, 252, 264, 276, 288, 300, 
312, 324, 336, 348, 360, 372, 384, 396, 408, 420, 432, 444, 456, 468, 
480, 492, 504, 516, 528, 540, 552, 564, 576, 588, 600, 612, 624, 636, 
648, 660, 672, 684, 696, 708, 720, 732, 744, 756, 768, 780, 792, 804, 
816, 828, 840, 852, 864, 876, 888, 900, 912, 924, 936, 948, 972, 984, 
996, 1008, 1020, 1032, 1044, 1056, 1068, 1080, 1092, 1104, 1116, 1128, 
1140, 1152, 1164, 1176, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1200, 1212, 1224, 
0, 1236, 0, 1248, 0, 1260, 1272, 1284, 1296, 1308, 1320, 1332, 1344, 
1356, 1368, 1380, 0, 1392, 1404, 1416, 0, 1428, 1440, 1452, 0, 0, 0, 
1464, 1476, 1488, 1500, 1512, 1524, 1536, 1548, 1560, 1572, 1584, 1596, 
1608, 1620, 1632, 1644, 1656, 1668, 1680, 1692, 1704, 1716, 1728, 1740, 
1752, 1764, 1776, 1788, 1800, 1812, 1824, 1836, 1848, 1860, 1872, 1884, 
1896, 1908, 1920, 1932, 1944, 1956, 1968, 1980, 1992, 2004, 2016, 2028, 
2040, 2052, 2064, 2076, 2088, 2100, 2112, 2124, 2136, 2148, 2160, 2172, 
2184, 2196, 2208, 2220, 2232
};
const sjme_jbyte sqf_font_sanserif_12_0_charBmpScan[] = 
{
2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 
0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 1, 1
};
const sjme_jbyte sqf_font_sanserif_12_0_charBmp[] = 
{
16, 0, 56, 0, 108, 0, -42, 0, -33, 1, -18, 0, 124, 0, 40, 0, 16, 0, 0, 
0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 5, 5, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 18, 18, 63, 18, 18, 18, 63, 18, 18, 0, 0, 0, 4, 14, 
21, 5, 14, 20, 20, 21, 14, 4, 0, 0, 6, 1, -119, 0, 73, 0, 38, 0, 16, 
0, -56, 0, 36, 1, 34, 1, -63, 0, 0, 0, 0, 0, 0, 0, 12, 18, 1, 1, 2, 69, 
41, 17, 46, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 
1, 1, 1, 1, 2, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 21, 14, 31, 
14, 21, 0, 0, 0, 0, 0, 0, 0, 2, 7, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 
3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 4, 4, 2, 2, 1, 1, 0, 0, 0, 0, 
14, 17, 17, 21, 21, 21, 17, 17, 14, 0, 0, 0, 7, 4, 4, 4, 4, 4, 4, 4, 
31, 0, 0, 0, 14, 17, 16, 8, 4, 2, 1, 1, 31, 0, 0, 0, 14, 17, 16, 16, 
14, 16, 16, 17, 14, 0, 0, 0, 17, 17, 17, 17, 31, 16, 16, 16, 16, 0, 0, 
0, 31, 1, 1, 15, 16, 16, 16, 17, 14, 0, 0, 0, 30, 1, 1, 1, 15, 17, 17, 
17, 14, 0, 0, 0, 31, 16, 16, 8, 8, 4, 4, 2, 2, 0, 0, 0, 14, 17, 17, 17, 
14, 17, 17, 17, 14, 0, 0, 0, 14, 17, 17, 17, 30, 16, 16, 16, 16, 0, 0, 
0, 3, 3, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 3, 2, 0, 0, 0, 
0, 0, 8, 4, 2, 1, 2, 4, 8, 0, 0, 0, 0, 0, 7, 0, 7, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 1, 2, 4, 8, 4, 2, 1, 0, 0, 0, 0, 0, 14, 17, 16, 16, 8, 4, 4, 
0, 4, 0, 0, 0, 30, 33, 45, 45, 45, 45, 61, 1, 62, 0, 0, 0, 14, 17, 17, 
17, 31, 17, 17, 17, 17, 0, 0, 0, 15, 17, 17, 17, 15, 17, 17, 17, 15, 
0, 0, 0, 30, 1, 1, 1, 1, 1, 1, 1, 30, 0, 0, 0, 15, 17, 17, 17, 17, 17, 
17, 17, 15, 0, 0, 0, 31, 1, 1, 1, 7, 1, 1, 1, 31, 0, 0, 0, 31, 1, 1, 
1, 7, 1, 1, 1, 1, 0, 0, 0, 30, 1, 1, 1, 29, 17, 17, 17, 14, 0, 0, 0, 
17, 17, 17, 17, 31, 17, 17, 17, 17, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 
1, 0, 0, 0, 16, 16, 16, 16, 16, 16, 17, 17, 14, 0, 0, 0, 33, 17, 9, 5, 
3, 5, 9, 17, 33, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 31, 0, 0, 0, 99, 85, 
73, 65, 65, 65, 65, 65, 65, 0, 0, 0, 17, 19, 19, 21, 21, 21, 25, 25, 
17, 0, 0, 0, 14, 17, 17, 17, 17, 17, 17, 17, 14, 0, 0, 0, 15, 17, 17, 
17, 15, 1, 1, 1, 1, 0, 0, 0, 14, 17, 17, 17, 17, 17, 21, 9, 22, 0, 0, 
0, 15, 17, 17, 17, 15, 9, 17, 17, 17, 0, 0, 0, 14, 17, 1, 1, 14, 16, 
16, 17, 14, 0, 0, 0, 31, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 17, 17, 17, 
17, 17, 17, 17, 17, 14, 0, 0, 0, 17, 17, 17, 17, 17, 17, 18, 20, 24, 
0, 0, 0, 65, 65, 65, 65, 65, 73, 73, 73, 54, 0, 0, 0, 17, 17, 10, 4, 
4, 10, 17, 17, 17, 0, 0, 0, 17, 17, 17, 10, 4, 4, 4, 4, 4, 0, 0, 0, 31, 
16, 16, 8, 4, 2, 1, 1, 31, 0, 0, 0, 7, 1, 1, 1, 1, 1, 1, 1, 7, 0, 0, 
0, 1, 1, 2, 2, 4, 4, 8, 8, 0, 0, 0, 0, 7, 4, 4, 4, 4, 4, 4, 4, 7, 0, 
0, 0, 4, 10, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 14, 17, 17, 17, 46, 0, 
0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 15, 17, 17, 17, 15, 0, 0, 0, 14, 1, 1, 
1, 14, 0, 0, 0, 0, 0, 0, 0, 16, 16, 16, 16, 30, 17, 17, 17, 30, 0, 0, 
0, 14, 9, 7, 1, 14, 0, 0, 0, 0, 0, 0, 0, 12, 2, 2, 7, 2, 2, 2, 2, 0, 
0, 0, 0, 30, 17, 17, 17, 30, 16, 15, 0, 0, 0, 0, 0, 1, 1, 1, 15, 17, 
17, 17, 17, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 0, 2, 
2, 2, 2, 2, 1, 0, 0, 0, 0, 1, 1, 17, 17, 15, 17, 17, 17, 0, 0, 0, 0, 
1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, -1, 0, 17, 1, 17, 1, 17, 1, 17, 1, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 17, 17, 17, 17, 0, 0, 0, 
0, 0, 0, 0, 14, 17, 17, 17, 14, 0, 0, 0, 0, 0, 0, 0, 15, 17, 17, 17, 
15, 1, 1, 0, 0, 0, 0, 0, 30, 17, 17, 17, 30, 16, 16, 0, 0, 0, 0, 0, 14, 
1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 30, 1, 14, 16, 15, 0, 0, 0, 0, 0, 0, 
0, 2, 2, 7, 2, 2, 2, 12, 0, 0, 0, 0, 0, 17, 17, 17, 17, 14, 0, 0, 0, 
0, 0, 0, 0, 17, 17, 18, 20, 24, 0, 0, 0, 0, 0, 0, 0, 17, 17, 21, 21, 
10, 0, 0, 0, 0, 0, 0, 0, 17, 10, 4, 10, 17, 0, 0, 0, 0, 0, 0, 0, 17, 
17, 17, 30, 16, 16, 15, 0, 0, 0, 0, 0, 31, 8, 4, 2, 31, 0, 0, 0, 0, 0, 
0, 0, 4, 2, 2, 2, 1, 2, 2, 2, 4, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
0, 0, 0, 1, 2, 2, 2, 4, 2, 2, 2, 1, 0, 0, 0, 22, 9, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 16, 0, 56, 0, 108, 0, -42, 0, -33, 1, -18, 0, 124, 0, 40, 
0, 16, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 4, 14, 
5, 5, 5, 14, 4, 0, 0, 0, 0, 0, 12, 2, 2, 2, 7, 2, 2, 2, 15, 0, 0, 0, 
65, 65, 34, 20, 8, 28, 8, 28, 8, 0, 0, 0, 30, 1, 3, 13, 17, 22, 24, 16, 
15, 0, 0, 0, 30, 33, 45, 35, 35, 35, 45, 33, 30, 0, 0, 0, 2, 5, 5, 10, 
0, 15, 0, 0, 0, 0, 0, 0, 40, 20, 10, 5, 10, 20, 40, 0, 0, 0, 0, 0, 15, 
8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
30, 33, 39, 43, 39, 43, 43, 33, 30, 0, 0, 0, 15, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 2, 5, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 7, 2, 0, 7, 0, 0, 
0, 0, 0, 0, 0, 3, 2, 3, 1, 3, 0, 0, 0, 0, 0, 0, 0, 3, 2, 3, 2, 3, 0, 
0, 0, 0, 0, 0, 0, 17, 17, 17, 15, 1, 1, 0, 0, 0, 0, 0, 0, 22, 23, 23, 
22, 20, 20, 20, 20, 20, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
3, 2, 2, 7, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 2, 0, 7, 0, 0, 0, 0, 0, 0, 
0, 5, 10, 20, 40, 20, 10, 5, 0, 0, 0, 0, 0, 4, 0, 4, 4, 2, 1, 17, 17, 
14, 0, 0, 0, 2, 4, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0, 8, 4, 0, 14, 17, 
17, 31, 17, 17, 0, 0, 0, 4, 10, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0, 22, 
9, 0, 14, 17, 17, 31, 17, 17, 0, 0, 0, 10, 0, 0, 14, 17, 17, 31, 17, 
17, 0, 0, 0, 4, 10, 4, 14, 17, 17, 31, 17, 17, 0, 0, 0, 124, 10, 9, 9, 
63, 9, 9, 9, 121, 0, 0, 0, 14, 1, 1, 1, 1, 1, 1, 14, 4, 6, 0, 0, 2, 4, 
0, 15, 1, 7, 1, 1, 15, 0, 0, 0, 4, 2, 0, 15, 1, 7, 1, 1, 15, 0, 0, 0, 
4, 10, 0, 15, 1, 7, 1, 1, 15, 0, 0, 0, 10, 0, 0, 15, 1, 7, 1, 1, 15, 
0, 0, 0, 1, 2, 0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 2, 1, 0, 1, 1, 1, 1, 1, 
1, 0, 0, 0, 2, 5, 0, 2, 2, 2, 2, 2, 2, 0, 0, 0, 5, 0, 0, 2, 2, 2, 2, 
2, 2, 0, 0, 0, 30, 34, 34, 34, 39, 34, 34, 34, 30, 0, 0, 0, 22, 9, 0, 
19, 19, 21, 21, 25, 25, 0, 0, 0, 2, 4, 0, 14, 17, 17, 17, 17, 14, 0, 
0, 0, 8, 4, 0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 4, 10, 0, 14, 17, 17, 
17, 17, 14, 0, 0, 0, 22, 9, 0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 10, 0, 
0, 14, 17, 17, 17, 17, 14, 0, 0, 0, 17, 10, 4, 10, 17, 0, 0, 0, 0, 0, 
0, 0, 30, 49, 41, 41, 37, 37, 37, 35, 30, 0, 0, 0, 2, 4, 0, 17, 17, 17, 
17, 17, 14, 0, 0, 0, 8, 4, 0, 17, 17, 17, 17, 17, 14, 0, 0, 0, 4, 10, 
0, 17, 17, 17, 17, 17, 14, 0, 0, 0, 10, 0, 0, 17, 17, 17, 17, 17, 14, 
0, 0, 0, 8, 4, 0, 17, 17, 10, 4, 4, 4, 0, 0, 0, 1, 1, 7, 9, 9, 9, 7, 
1, 1, 0, 0, 0, 14, 17, 17, 9, 5, 9, 17, 17, 13, 0, 0, 0, 2, 4, 0, 14, 
17, 17, 17, 46, 0, 0, 0, 0, 8, 4, 0, 14, 17, 17, 17, 46, 0, 0, 0, 0, 
4, 10, 0, 14, 17, 17, 17, 46, 0, 0, 0, 0, 22, 9, 0, 14, 17, 17, 17, 46, 
0, 0, 0, 0, 10, 0, 14, 17, 17, 17, 46, 0, 0, 0, 0, 0, 4, 10, 4, 0, 14, 
17, 17, 17, 46, 0, 0, 0, 55, 72, 62, 9, 118, 0, 0, 0, 0, 0, 0, 0, 14, 
1, 1, 1, 14, 4, 6, 0, 0, 0, 0, 0, 2, 4, 0, 14, 9, 7, 1, 14, 0, 0, 0, 
0, 8, 4, 0, 14, 9, 7, 1, 14, 0, 0, 0, 0, 4, 10, 0, 14, 9, 7, 1, 14, 0, 
0, 0, 0, 10, 0, 14, 9, 7, 1, 14, 0, 0, 0, 0, 0, 1, 2, 0, 2, 2, 2, 2, 
0, 0, 0, 0, 0, 2, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 2, 5, 0, 2, 2, 2, 
2, 0, 0, 0, 0, 0, 5, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 24, 7, 10, 17, 
17, 17, 14, 0, 0, 0, 0, 0, 22, 9, 0, 15, 17, 17, 17, 0, 0, 0, 0, 0, 2, 
4, 0, 6, 9, 9, 6, 0, 0, 0, 0, 0, 4, 2, 0, 6, 9, 9, 6, 0, 0, 0, 0, 0, 
2, 5, 0, 6, 9, 9, 6, 0, 0, 0, 0, 0, 10, 5, 0, 6, 9, 9, 6, 0, 0, 0, 0, 
0, 10, 0, 6, 9, 9, 6, 0, 0, 0, 0, 0, 0, 2, 0, 7, 0, 2, 0, 0, 0, 0, 0, 
0, 0, 14, 25, 21, 19, 14, 0, 0, 0, 0, 0, 0, 0, 2, 4, 0, 17, 17, 17, 14, 
0, 0, 0, 0, 0, 8, 4, 0, 17, 17, 17, 14, 0, 0, 0, 0, 0, 4, 10, 0, 17, 
17, 17, 14, 0, 0, 0, 0, 0, 10, 0, 17, 17, 17, 14, 0, 0, 0, 0, 0, 0, 8, 
4, 0, 17, 17, 17, 30, 16, 15, 0, 0, 0, 1, 1, 7, 9, 9, 9, 7, 1, 1, 0, 
0, 0, 10, 0, 17, 17, 17, 30, 16, 15, 0, 0, 0, 0
};
const struct sjme_scritchui_sqf sqf_font_sanserif_12_0 = 
{
	.name = "sanserif", 
	.family = 2, 
	.pixelHeight = 12, 
	.ascent = 9, 
	.descent = 3, 
	.bbx = 0, 
	.bby = 0, 
	.bbw = 10, 
	.bbh = 12, 
	.codepointStart = 0, 
	.codepointCount = 256, 
	.huffBitsSize = 0, 
	.charBmpSize = 2244, 
	.huffBits = sqf_font_sanserif_12_0_huffBits, 
	.charWidths = sqf_font_sanserif_12_0_charWidths, 
	.charXOffset = sqf_font_sanserif_12_0_charXOffset, 
	.charYOffset = sqf_font_sanserif_12_0_charYOffset, 
	.charFlags = sqf_font_sanserif_12_0_charFlags, 
	.charBmpOffset = sqf_font_sanserif_12_0_charBmpOffset, 
	.charBmpScan = sqf_font_sanserif_12_0_charBmpScan, 
	.charBmp = sqf_font_sanserif_12_0_charBmp
};
const sjme_jbyte sqf_font_sanserif_12_1_huffBits[] = 
{
};
const sjme_jbyte sqf_font_sanserif_12_1_charWidths[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 7, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_1_charXOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_1_charYOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 6, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 8, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 8, 0, 0, 0, 0, 7, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_1_charFlags[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jshort sqf_font_sanserif_12_1_charBmpOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 24, 36, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 48, 0, 0, 0, 0, 60, 72, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_1_charBmpScan[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_1_charBmp[] = 
{
126, 9, 9, 9, 57, 9, 9, 9, 126, 0, 0, 0, 118, 73, 57, 9, 118, 0, 0, 0, 
0, 0, 0, 0, 10, 4, 30, 1, 1, 14, 16, 16, 15, 0, 0, 0, 10, 4, 0, 0, 14, 
1, 6, 8, 7, 0, 0, 0, 10, 17, 17, 17, 10, 4, 4, 4, 4, 0, 0, 0, 10, 4, 
63, 16, 8, 4, 2, 1, 63, 0, 0, 0, 10, 4, 0, 0, 15, 8, 6, 1, 15, 0, 0, 
0
};
const struct sjme_scritchui_sqf sqf_font_sanserif_12_1 = 
{
	.name = "sanserif", 
	.family = 2, 
	.pixelHeight = 12, 
	.ascent = 9, 
	.descent = 3, 
	.bbx = 0, 
	.bby = 0, 
	.bbw = 10, 
	.bbh = 12, 
	.codepointStart = 256, 
	.codepointCount = 256, 
	.huffBitsSize = 0, 
	.charBmpSize = 84, 
	.huffBits = sqf_font_sanserif_12_1_huffBits, 
	.charWidths = sqf_font_sanserif_12_1_charWidths, 
	.charXOffset = sqf_font_sanserif_12_1_charXOffset, 
	.charYOffset = sqf_font_sanserif_12_1_charYOffset, 
	.charFlags = sqf_font_sanserif_12_1_charFlags, 
	.charBmpOffset = sqf_font_sanserif_12_1_charBmpOffset, 
	.charBmpScan = sqf_font_sanserif_12_1_charBmpScan, 
	.charBmp = sqf_font_sanserif_12_1_charBmp
};
const sjme_jbyte sqf_font_sanserif_12_20_huffBits[] = 
{
};
const sjme_jbyte sqf_font_sanserif_12_20_charWidths[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_20_charXOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_20_charYOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_20_charFlags[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jshort sqf_font_sanserif_12_20_charBmpOffset[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_20_charBmpScan[] = 
{
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
0, 0, 0
};
const sjme_jbyte sqf_font_sanserif_12_20_charBmp[] = 
{
28, 34, 2, 15, 2, 15, 2, 34, 28, 0, 0, 0
};
const struct sjme_scritchui_sqf sqf_font_sanserif_12_20 = 
{
	.name = "sanserif", 
	.family = 2, 
	.pixelHeight = 12, 
	.ascent = 9, 
	.descent = 3, 
	.bbx = 0, 
	.bby = 0, 
	.bbw = 10, 
	.bbh = 12, 
	.codepointStart = 8192, 
	.codepointCount = 256, 
	.huffBitsSize = 0, 
	.charBmpSize = 12, 
	.huffBits = sqf_font_sanserif_12_20_huffBits, 
	.charWidths = sqf_font_sanserif_12_20_charWidths, 
	.charXOffset = sqf_font_sanserif_12_20_charXOffset, 
	.charYOffset = sqf_font_sanserif_12_20_charYOffset, 
	.charFlags = sqf_font_sanserif_12_20_charFlags, 
	.charBmpOffset = sqf_font_sanserif_12_20_charBmpOffset, 
	.charBmpScan = sqf_font_sanserif_12_20_charBmpScan, 
	.charBmp = sqf_font_sanserif_12_20_charBmp
};
const struct sjme_scritchui_sqf* sqf_font_sanserif_12_sqfs[] = 
{
&(sqf_font_sanserif_12_0), &(sqf_font_sanserif_12_1), &(sqf_font_sanserif_12_20)
};
const SJME_DYLIB_EXPORT struct sjme_scritchui_sqfCodepage sqf_font_sanserif_12 = 
{
	.name = "sanserif", 
	.numCodepages = 3, 
	.codepages = sqf_font_sanserif_12_sqfs
};
