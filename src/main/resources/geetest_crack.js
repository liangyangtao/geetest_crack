/****
 * 获取userresponse 的js
 * 
 */
var b = function(a, b) {
	for (var c = b.slice(32), d = [], e = 0; e < c.length; e++) {
		var f = c.charCodeAt(e);
		d[e] = f > 57 ? f - 87 : f - 48
	}
	c = 36 * d[0] + d[1];
	var g = Math.round(a) + c;
	b = b.slice(0, 32);
	var h,
		i = [ [], [], [], [], [] ],
		j = {},
		k = 0;
	e = 0;
	for (var l = b.length; e < l; e++) h = b.charAt(e),
		j[h] || (j[h] = 1, i[k].push(h), k++, k = 5 == k ? 0 : k);
	for (var m, n = g,
			o = 4,
			p = "",
			q = [ 1, 2, 5, 10, 50 ]; n > 0;) n - q[o] >= 0 ? (m = parseInt(Math.random() * i[o].length, 10), p += i[o][m], n -= q[o]) : (i.splice(o, 1), q.splice(o, 1), o -= 1);
	return p
}

/***
 * 还原图片的js
 * //		int[] n = new int[] { 39, 38, 48, 49, 41, 40, 46, 47, 35, 34, 50, 51, 33, 32, 28, 29, 27, 26, 36, 37, 31,
//				30, 44, 45, 43, 42, 12, 13, 23, 22, 14, 15, 21, 20, 8, 9, 25, 24, 6, 7, 3, 2, 0, 1, 11, 10, 4, 5,
//				19, 18, 16, 17 };
 * 
 * 
 */

var a = function() {
	for (var a, b = "6_11_7_10_4_12_3_1_0_5_2_9_8".split("_"), c = [], d = 0, e = 52; d < e; d++) a = 2 * parseInt(b[parseInt(d % 26 / 2)]) + d % 2,
		parseInt(d / 2) % 2 || (a += d % 2 ? -1 : 1),
		a += d < 26 ? 26 : 0,
		c.push(a);
	return c
}

/****
 * 获取A的js
 * 
 */

var c = function(a) {
	for (var b, c, d, e = [], f = 0, g = [], h = 0, i = a.length - 1; h < i; h++) b = Math.round(a[h + 1][0] - a[h][0]),
		c = Math.round(a[h + 1][1] - a[h][1]),
		d = Math.round(a[h + 1][2] - a[h][2]),
		g.push([ b, c, d ]),
		0 == b && 0 == c && 0 == d || (0 == b && 0 == c ? f += d : (e.push([ b, c, d + f ]), f = 0));
	return 0 !== f && e.push([ b, c, f ]),
		e
}
var d = function(a) {
	var b = "()*,-./0123456789:?@ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqr",
		c = b.length,
		d = "",
		e = Math.abs(a),
		f = parseInt(e / c);
	f >= c && (f = c - 1),
	f && (d = b.charAt(f)),
	e %= c;
	var g = "";
	return a < 0 && (g += "!"),
		d && (g += "$"),
		g + d + b.charAt(e)
}
var e = function(a) {
	for (var b = [ [ 1, 0 ], [ 2, 0 ], [ 1, -1 ], [ 1, 1 ], [ 0, 1 ], [ 0, -1 ], [ 3, 0 ], [ 2, -1 ], [ 2, 1 ] ], c = "stuvwxyz~", d = 0, e = b.length; d < e; d++)
		if (a[0] == b[d][0] && a[1] == b[d][1]) return c[d];
	return 0
}
var f = function(a) {

	for (var b, f = a, g = [], h = [], i = [], j = 0, k = f.length; j < k; j++) b = e(f[j]),
		b ? h.push(b) : (g.push(d(f[j][0])), h.push(d(f[j][1]))),
		i.push(d(f[j][2]));
	return g.join("") + "!!" + h.join("") + "!!" + i.join("")
};