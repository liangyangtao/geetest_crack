package com.unbank;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Random;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.unbank.app.GeetestByJs;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	public static void main(String[] args) {
		System.out.println(new GeetestByJs().getUserrespense(114, "92bf595f88064120fce1b8ab62708d89dc"));
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
		return result;

	}

	public static int[] random(int n, int L) {
		int move = L / n;
		int last = L % n;
		int[] result = new int[n];
		int temp = 0;
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

	/****
	 * 跑出结果 int n[]
	 */
	public static void getA() {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine jsEngine = manager.getEngineByName("nashorn");
			String jsFileName = "geetest_crack.js";
			Reader reader;
			InputStream in = GeetestByJs.class.getClassLoader().getResourceAsStream(jsFileName);
			reader = new InputStreamReader(in);
			jsEngine.eval(reader);
			reader.close();
			Invocable invoke = (Invocable) jsEngine;
			ScriptObjectMirror obj = (ScriptObjectMirror) invoke.invokeFunction("a");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// int[] n = new int[] { 39, 38, 48, 49, 41, 40, 46, 47, 35, 34, 50, 51,
		// 33, 32, 28, 29, 27, 26, 36, 37, 31,
		// 30, 44, 45, 43, 42, 12, 13, 23, 22, 14, 15, 21, 20, 8, 9, 25, 24, 6,
		// 7, 3, 2, 0, 1, 11, 10, 4, 5,
		// 19, 18, 16, 17 };
	}

}
