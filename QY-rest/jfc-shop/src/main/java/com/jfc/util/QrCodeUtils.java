package com.jfc.util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LiuNaiJie
 * on 2019-04-01
 */
public class QrCodeUtils {
	/**
	 * 黑色
	 */

	private static final int QRCOLOR = 0xFF000000;
	/**
	 * 白色
	 */
	private static final int BGWHITE = 0xFFFFFFFF;
	/**
	 * 二维码宽
	 */
	private static final int WIDTH = 400;
	/**
	 * 二维码高
	 */
	private static final int HEIGHT = 400;
	/**
	 * 用于设置QR二维码参数
	 */
	private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
		private static final long serialVersionUID = 1L;

		{
			// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
			put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置编码方式
			put(EncodeHintType.CHARACTER_SET, "utf-8");
			put(EncodeHintType.MARGIN, 0);
		}
	};

	/**
	 * @param logoFile logo图片
	 * @param codeFile 生成的二维码
	 * @param qrUrl    二维码内容
	 * @param note     二维码下方说明文字
	 */
	public static void drawLogoQRCode(File logoFile, File codeFile, String qrUrl, String note) {
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：二维码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}
			int width = image.getWidth();
			int height = image.getHeight();
			//添加logo图片
			if (Objects.nonNull(logoFile) && logoFile.exists()) {
				// 构建绘图对象l
				Graphics2D g = image.createGraphics();
				// 读取logo图片
				BufferedImage logo = ImageIO.read(logoFile);
				// 开始绘制logo图片 logo大小为整体的 1/4  开始绘制的x,y为3/8处。
				g.drawImage(logo, width * 3 / 8, height * 3 / 8, width / 4, height / 4, null);
				g.dispose();
				logo.flush();
			}
			// 添加下方说明文字
			if (StringUtils.isNotEmpty(note)) {
				// 新的图片，把带logo的二维码下面加上文字
				BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D outg = outImage.createGraphics();
				// 画二维码到新的面板
				outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
				// 画文字到新的面板
				outg.setColor(Color.BLACK);
				// 字体、字型、字号
				outg.setFont(new Font("黑体", Font.PLAIN, 30));
				int strWidth = outg.getFontMetrics().stringWidth(note);
				// 画文字
				outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12);
				outg.dispose();
				outImage.flush();
				image = outImage;
			}
			image.flush();
			ImageIO.write(image, "png", codeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @param inputStream logo图片
	 * @param qrUrl    二维码内容
	 * @param note     二维码下方说明文字
	 */
	public static String drawLogoQRCodePrint(InputStream inputStream, String qrUrl, String note) {
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			// 参数顺序分别为：二维码内容，编码类型，生成图片宽度，生成图片高度，设置参数
			BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
				}
			}
			int width = image.getWidth();
			int height = image.getHeight();
			//添加logo图片
			if (Objects.nonNull(inputStream)) {
				// 构建绘图对象l
				Graphics2D g = image.createGraphics();
				// 读取logo图片
				BufferedImage logo = ImageIO.read(inputStream);
				// 开始绘制logo图片 logo大小为整体的 1/4  开始绘制的x,y为3/8处。
				g.drawImage(logo, width * 3 / 8, height * 3 / 8, width / 4, height / 4, null);
				g.dispose();
				logo.flush();
			}
			// 添加下方说明文字
			if (StringUtils.isNotEmpty(note)) {
				// 新的图片，把带logo的二维码下面加上文字
				BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
				Graphics2D outg = outImage.createGraphics();
				// 画二维码到新的面板
				outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
				// 画文字到新的面板
				outg.setColor(Color.BLACK);
				// 字体、字型、字号
				outg.setFont(new Font("黑体", Font.PLAIN, 30));
				int strWidth = outg.getFontMetrics().stringWidth(note);
				// 画文字
				outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12);
				outg.dispose();
				outImage.flush();
				image = outImage;
			}
			image.flush();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			BASE64Encoder encoder = new BASE64Encoder();
			String base64Img = encoder.encode(outputStream.toByteArray());
			return "data:image/png;base64,"+base64Img;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}