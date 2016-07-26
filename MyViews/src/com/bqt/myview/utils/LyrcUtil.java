package com.bqt.myview.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.bqt.myview.utils.bean.Lyrc;

public class LyrcUtil {
	private static List<Lyrc> lyrcList;

	/**
	 * ��ȡ�ļ�
	 */
	public static List<Lyrc> readLRC(File f) {
		try {
			if (f == null || !f.exists()) {
				lyrcList = null;
			} else {
				lyrcList = new Vector<Lyrc>();
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				BufferedReader br = new BufferedReader(new InputStreamReader(is, getCharset(f)));
				String strTemp = "";
				while ((strTemp = br.readLine()) != null) {
					strTemp = analyzeLRC(strTemp);
				}
				br.close();
				is.close();
				// �Ը�ʽ�������
				Collections.sort(lyrcList, new Sort());
				// ����ÿ�и�ʵ�ͣ��ʱ��
				for (int i = 0; i < lyrcList.size(); i++) {
					Lyrc one = lyrcList.get(i);
					if (i + 1 < lyrcList.size()) {
						Lyrc two = lyrcList.get(i + 1);
						one.sleepTime = two.timePoint - one.timePoint;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lyrcList;
	}

	/**
	 * ����һ������
	 */
	private static String analyzeLRC(String text) {
		try {
			int pos1 = text.indexOf("[");
			int pos2 = text.indexOf("]");
			if (pos1 >= 0 && pos2 != -1) {
				Long time[] = new Long[getPossiblyTagCount(text)];
				time[0] = timeToLong(text.substring(pos1 + 1, pos2));
				if (time[0] == -1) return "";
				String strLineRemaining = text;
				int i = 1;
				while (pos1 >= 0 && pos2 != -1) {
					strLineRemaining = strLineRemaining.substring(pos2 + 1);
					pos1 = strLineRemaining.indexOf("[");
					pos2 = strLineRemaining.indexOf("]");
					if (pos2 != -1) {
						time[i] = timeToLong(strLineRemaining.substring(pos1 + 1, pos2));
						if (time[i] == -1) return ""; // LRCText
						i++;
					}
				}
				Lyrc tl = null;
				for (int j = 0; j < time.length; j++) {
					if (time[j] != null) {
						tl = new Lyrc();
						tl.timePoint = time[j].intValue();
						tl.lrcString = strLineRemaining;
						lyrcList.add(tl);
					}
				}
				return strLineRemaining;
			} else return "";
		} catch (Exception e) {
			return "";
		}
	}

	private static int getPossiblyTagCount(String Line) {
		String strCount1[] = Line.split("\\[");
		String strCount2[] = Line.split("\\]");
		if (strCount1.length == 0 && strCount2.length == 0) return 1;
		else if (strCount1.length > strCount2.length) return strCount1.length;
		else return strCount2.length;
	}

	/**
	 * ʱ��ת��
	 */
	public static long timeToLong(String Time) {
		try {
			String[] s1 = Time.split(":");
			int min = Integer.parseInt(s1[0]);
			String[] s2 = s1[1].split("\\.");
			int sec = Integer.parseInt(s2[0]);
			int mill = 0;
			if (s2.length > 1) mill = Integer.parseInt(s2[1]);
			return min * 60 * 1000 + sec * 1000 + mill * 10;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * �ж��ļ�����
	 */
	public static String getCharset(File file) {
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) return charset;
			if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0) break;
					if (0x80 <= read && read <= 0xBF) break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) continue;
						else break;
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else break;
						} else break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return charset;
	}

	private static class Sort implements Comparator<Lyrc> {
		public Sort() {
		}

		public int compare(Lyrc tl1, Lyrc tl2) {
			return sortUp(tl1, tl2);
		}

		private int sortUp(Lyrc tl1, Lyrc tl2) {
			if (tl1.timePoint < tl2.timePoint) return -1;
			else if (tl1.timePoint > tl2.timePoint) return 1;
			else return 0;
		}
	}
}