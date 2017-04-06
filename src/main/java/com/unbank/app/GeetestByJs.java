package com.unbank.app;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.unbank.fetch.Fetcher;
import com.unbank.fetch.HttpClientBuilder;

import net.sf.json.JSONObject;

public class GeetestByJs {
	static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000)
			.setCircularRedirectsAllowed(true).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY)
			// .setProxy(proxy)
			.setMaxRedirects(50).build();
	static BasicCookieStore cookieStore = new BasicCookieStore();
	static Map<String, String> cookieMap = new HashMap<String, String>();

	public static void main(String[] args) {
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();

		CloseableHttpClient httpClient = new HttpClientBuilder(poolingHttpClientConnectionManager, cookieStore)
				.getHttpClient();
		String url = "http://uems.sysu.edu.cn/jwxt/";

		// 第一步 访问首页获取Cookie
		// http://uems.sysu.edu.cn/jwxt/
		getHtml(httpClient, url, "utf-8", getCookieString(url));
		// 第二步
		// http://uems.sysu.edu.cn/jwxt/StartCaptchaServlet?ts=0.2148168714277996
		// 获取 gt 80a8ed78d1d22e16f5b6c478f03b9212 、 challeange
		// 292a057f053878889f4372c95e429cf6
		String url2 = "http://uems.sysu.edu.cn/jwxt/StartCaptchaServlet?ts=0.2148168714277996";
		String json2 = getHtml(httpClient, url2, "utf-8", getCookieString(url2));
		JSONObject jsonObject2 = JSONObject.fromObject(json2);
		String gt = (String) jsonObject2.get("gt");
		String challenge = (String) jsonObject2.get("challenge");
		Integer success = (Integer) jsonObject2.get("success");
		long dateStr = new Date().getTime();
		// 第三步
		// 注册返回domain
		String url3 = "http://api.geetest.com/getfrontlib.php?gt=" + gt + "&callback=geetest_" + dateStr;
		getHtml(httpClient, url3, "utf-8", getCookieString(url3));
		// 第四步
		dateStr = new Date().getTime();
		// getCookiesString();
		String url4 = "http://api.geetest.com/get.php?gt=" + gt + "&challenge=" + challenge
				+ "&product=float&offline=false&type=slide&callback=geetest_" + dateStr;
		String html4 = getHtml(httpClient, url4, "utf-8", getCookieString(url4));
		// System.out.println(html4);
		String json4 = StringUtils.substringBetween(html4, "(", ")");

		// geetest_1489570470373({"version": "5.7.0", "id":
		// "acf6d4c1aab17bfa2434334a796c6685f", "link": "", "static_servers":
		// ["uems.sysu.edu.cn/jwxt/geetest/"], "xpos": 0, "mobile": false,
		// "theme": "golden", "ypos": 41, "theme_version": "3.1.1", "gt":
		// "80a8ed78d1d22e16f5b6c478f03b9212", "https": false, "height": 116,
		// "api_server": "http://api.geetest.com/", "type": "slide", "fullbg":
		// "pictures/gt/579066de6/579066de6.jpg", "benchmark": false, "product":
		// "float", "clean": true, "bg":
		// "pictures/gt/579066de6/bg/bf184dd55.jpg", "hide_delay": 800, "logo":
		// false, "challenge": "cf6d4c1aab17bfa2434334a796c6685fk1", "slice":
		// "pictures/gt/579066de6/slice/bf184dd55.png", "fullpage": false,
		// "feedback": "", "show_delay": 250})

		JSONObject jsonObject4 = JSONObject.fromObject(json4);
		String fullbg = jsonObject4.getString("fullbg");
		Integer height = jsonObject4.getInt("height");
		String bg = jsonObject4.getString("bg");
		String show_delay = jsonObject4.getString("show_delay");
		Integer ypos = jsonObject4.getInt("ypos");
		Integer hide_delay = jsonObject4.getInt("hide_delay");
		String slice = jsonObject4.getString("slice");
		String id = jsonObject4.getString("id");
		challenge = jsonObject4.getString("challenge");
		// System.out.println(challenge);
		// http://static.geetest.com/pictures/gt/5629ed821/5629ed821.jpg
		// 下载两个图片fullbg ，bg 比较出要滑动的距离
		String host = "http://static.geetest.com/";
		downFile(host + fullbg, "geetest", "fullbg.jpg");
		downFile(host + bg, "geetest", "bg.jpg");
		// downFile(host + slice, "geetest", "slice.png");
		// 需要滑动的距离
		int distance = compareImage("geetest/fullBg.jpg", "geetest/bg.jpg");
		 System.out.println(distance);
		// 得到移动轨迹
		// getTans(distance);
		/***
		 * 第 5 步
		 * 
		 * http://api.geetest.com/ajax.php?gt=80a8ed78d1d22e16f5b6c478f03b9212&
		 * challenge=292a057f053878889f4372c95e429cf6gg&userresponse=
		 * 45554454455440880ee&passtime=3584&imgload=120&a=@(!!?
		 * ssssssstttssusstssssssttstttsttytttyyytyttsysststssstsssssssssssszsss
		 * (!!($?SE0$)E$*KEB,86:*119:891203/3/5-5-6-7)111111111204.3/96,69,8)7,
		 * T$*q19T2$)8$)LA9-$9E&callback=geetest_1489569684683
		 * 
		 */

		String userrespense = getUserrespense(distance, challenge);

		String imgload = getImgload(id);
		int[][] tans = getTans(distance);
		String a = getA(tans);
		int passtime = tans[tans.length - 1][2];
		dateStr = new Date().getTime();
		// String
		// url5="http://api.geetest.com/ajax.php?gt=80a8ed78d1d22e16f5b6c478f03b9212&challenge=292a057f053878889f4372c95e429cf6gg&userresponse=45554454455440880ee&passtime=3584&imgload=120&a=@(!!?ssssssstttssusstssssssttstttsttytttyyytyttsysststssstsssssssssssszsss(!!($?SE0$)E$*KEB,86:*119:891203/3/5-5-6-7)111111111204.3/96,69,8)7,T$*q19T2$)8$)LA9-$9E&callback=geetest_1489569684683";
		String url5 = "http://api.geetest.com/ajax.php?gt=" + gt + "&challenge=" + challenge + "&userresponse="
				+ userrespense + "&passtime=" + passtime + "&imgload=" + imgload + "&a=" + a + "&callback=geetest_"
				+ dateStr;
		System.out.println(url5);
		System.out.println(getHtml(httpClient, url5, "utf-8", getCookieString(url5)));

	}

	private static String getCookieString(String url) {
		return cookieMap.get(getDomain(url)) == null ? "" : cookieMap.get(getDomain(url));
	}

	/***
	 * 还原图片
	 * 
	 * @param fullbg
	 * @param bg
	 * @param slice
	 * @param height
	 * @return
	 */
	public static int compareImage(String fullbg, String bg) {
		int result = 0;
		try {
			int rows = 2, columns = 26, sliceWidth = 10, sliceHeight = 58;
			// 读取第一张图片
			String path = GeetestByJs.class.getClassLoader().getResource("").toURI().getPath();

			File fullBgfile = new File(path + File.separator + fullbg);
			BufferedImage fullBgImage = ImageIO.read(fullBgfile);
			BufferedImage newFullBgImage = new BufferedImage(columns * sliceWidth, sliceHeight * rows,
					BufferedImage.TYPE_INT_RGB);

			File bgfile = new File(path + File.separator + bg);
			BufferedImage bgImage = ImageIO.read(bgfile);
			BufferedImage newBgImage = new BufferedImage(columns * sliceWidth, sliceHeight * rows,
					BufferedImage.TYPE_INT_RGB);

			WritableRaster r1 = fullBgImage.getRaster();
			WritableRaster r2 = newFullBgImage.getRaster();
			WritableRaster r3 = newBgImage.getRaster();
			WritableRaster r4 = bgImage.getRaster();
			//
			int[] n = new int[] { 39, 38, 48, 49, 41, 40, 46, 47, 35, 34, 50, 51, 33, 32, 28, 29, 27, 26, 36, 37, 31,
					30, 44, 45, 43, 42, 12, 13, 23, 22, 14, 15, 21, 20, 8, 9, 25, 24, 6, 7, 3, 2, 0, 1, 11, 10, 4, 5,
					19, 18, 16, 17 };

			int[] p = new int[3];
			int[] q = new int[3];
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					int right = n[row * columns + column] % 26 * 12 + 1;
					int down = n[row * columns + column] > 25 ? 58 : 0;
					for (int x = 0; x < sliceWidth; x++) {
						for (int y = 0; y < sliceHeight; y++) {
							int ht = 58 * row + y;
							int wd = 10 * column + x;
							r2.setPixel(wd, ht, r1.getPixel(x + right, y + down, p));
							r3.setPixel(wd, ht, r4.getPixel(x + right, y + down, q));
						}
					}
				}
			}
			// ImageIO.write(newFullBgImage, "jpg", new File( "1.jpg"));
			// ImageIO.write(newBgImage, "jpg", new File("2.jpg"));

			// 得到还原的图片
			// newFullBgImage
			// newBgImage
			// 比较还原的图片像素不一样的地方
			for (int x = 0; x < columns * sliceWidth; x++) {
				if (!columnSimilar(newFullBgImage, newBgImage, x)) {
					result = x;
					break;
				}

			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean colorSimilar(int fbg[], int bg[]) {
		int max = 60;
		int diffR = Math.abs(fbg[0] - bg[0]);
		int diffG = Math.abs(fbg[1] - bg[1]);
		int diffB = Math.abs(fbg[2] - bg[2]);
		return diffR < max && diffG < max && diffB < max;
	}

	// 比较两个图片某一列相似
	public static boolean columnSimilar(BufferedImage newFullBgImage, BufferedImage newBgImage, int x) {
		WritableRaster r2 = newFullBgImage.getRaster();
		WritableRaster r3 = newBgImage.getRaster();
		int[] p = new int[3];
		int[] q = new int[3];
		for (int y = 0; y < newFullBgImage.getHeight(); y++) {
			int fbg[] = r2.getPixel(x, y, p);
			int bg[] = r3.getPixel(x, y, q);
			if (!colorSimilar(fbg, bg)) {
				return false;
			}
		}
		return true;
	}

	/***
	 * Q.f("endTime", a.id).getTime() - Q.f("startTime", a.id),
	 * 
	 * @return
	 */
	// public static String getPasstime(String id) {
	// int num = (int) (Math.random() * 4000) + 2000;
	// return num + "";
	//
	// }

	/***
	 * Q.f("imgload", a.id),
	 * 
	 * @param id
	 * @return
	 */
	public static String getImgload(String id) {
		// 随机获取1000-200之间的数字
		int num = (int) (Math.random() * 200) + 100;
		return num + "";
	}

	public static int[][] getTans(int distance) {
		int[] x_move = tans_random(70, distance);
		int[] y_move = tans_y_random(70);
		double temp = distance / 100.0;
		int lasttime = (int) ((temp + Math.random()) * 1000);
		int[][] tans = new int[72][3];

		tans[0] = new int[] { -24, -16, 0 };
		tans[1] = new int[] { 0, 0, 0 };

		int[] time_move = lasttime_random(70, lasttime);
		for (int i = 2; i < 72; i++) {
			tans[i] = new int[] { x_move[i - 2], y_move[i - 2], time_move[i - 2] };
		}
		return tans;

	}

	private static int[] tans_y_random(int n) {
		int[] result = new int[n];
		int j = 0;
		for (int i = 0; i < n; i++) {
			result[i] = j;
			if (i % 10 == 0) {
				j++;
			}

		}
		System.out.println(Arrays.toString(result));
		return result;
	}

	private static int[] tans_random(int n, int distance) {
		int[] result = new int[n];
		int right = 260 - distance;
		if (right > 10) {
			Random rand = new Random();
			right = rand.nextInt(8) + 3;
		}
		for (int i = 0; i < right; i++) {
			result[n - 1 - i] = distance - i;
			result[n - 1 - right - i] = distance - right + i;
			result[n - 1 - right - right - i] = distance + i;
		}
		int temp = 0;
		int move = (distance + right) / (n - 1 - right - right - right);
		int last = (distance + right) % (n - 1 - right - right - right);
		for (int i = 0; i <= n - 1 - right - right - right; i++) {
			if (last > i) {
				temp += move + 1;
			} else {
				temp += move;
			}
			result[i] = temp;
		}
		System.out.println(Arrays.toString(result));
		return result;

	}

	public static int[] lasttime_random(int n, int L) {
		int move = (L - 500) / n;
		int last = (L - 500) % n;
		int[] result = new int[n];

		int temp = 500;

		for (int i = 0; i < n - 1; i++) {
			if (last > i) {
				temp += move + 1;
			} else {
				temp += move;
			}
			result[i] = temp;
		}
		result[n - 1] = L;
		System.out.println(Arrays.toString(result));
		return result;
	}

	public static String getA(int[][] id) {
		String a = null;
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine jsEngine = manager.getEngineByName("javascript");
			String jsFileName = "geetest_crack.js";
			Reader reader;
			InputStream in = GeetestByJs.class.getClassLoader().getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();

			Invocable invoke = (Invocable) jsEngine;
			a = (String) invoke.invokeFunction("f", id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/***
	 * h = (c.changedTouches && c.changedTouches[0].clientX || c.clientX) - f, b
	 * =challenge;
	 */
	// 89c87e9eb46033be25ccb06c6fbcf0bfek
	// 92bf595f88064120fce1b8ab62708d89
	// 92bf595f88064120fce1b8ab62708d89dc
	public static String getUserrespense(int move, String chanllenge) {
		String user = null;
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine jsEngine = manager.getEngineByName("javascript");
			String jsFileName = "geetest_crack.js";
			Reader reader;
			InputStream in = GeetestByJs.class.getClassLoader().getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();

			Invocable invoke = (Invocable) jsEngine;
			user = (String) invoke.invokeFunction("b", move, chanllenge);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;

	}

	public static String getHtml(CloseableHttpClient httpClient, String url, String charset, String cookie) {
		HttpClientContext context = HttpClientContext.create();
		context.setCookieStore(cookieStore);
		String useCharset = charset;
		HttpGet httpGet = new HttpGet(url);
		fillGetHeader(url, httpGet, cookie);
		httpGet.setConfig(requestConfig);
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				if (header.getName().contains("Set-Cookie")) {
					String cookieValue = header.getValue();
					cookieMap.put(getDomain(url), cookieValue);
				}
			}
			try {
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, useCharset);
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	private static void fillGetHeader(String url, HttpGet httpGet, String cookie) {

		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.2; rv:23.0) Gecko/20100101 Firefox/23.0");
		httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-us;q=0.8,en;q=0.6");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate,sdch");
		httpGet.setHeader("Host", getDomain(url));
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Cache-Control", "private");
		httpGet.setHeader("Cookie", cookie);
	}

	private static String getDomain(String url) {
		String domain = "";
		try {
			URL u = new URL(url);
			domain = u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return domain;
	}

	public static void downFile(String url, String pname, String filename) {
		Fetcher fetcher = Fetcher.getInstance();
		fetcher.downFile(url, pname, filename);
	}

}
