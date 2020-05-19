package fresh;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Watermark {
	public static void main(String[] args) throws IOException {
		Font font = new Font("04b_08", Font.PLAIN, 30);// 字体
		String sourceImg = "D:\\test\\123.jpg"; // 源图片地址
		String targetImg = "D:\\test\\watermark\\123.jpg"; // 新存储的地址
		String watermark = "图片出处:www.baidu.com";// 水印内容
		Color color = new Color(255, 255, 255, 128);
		addWatermark(sourceImg, targetImg, watermark, color, font);
	}

	/**
	 * 设置文字水印
	 * 
	 * @param sourceImg 源图片路径
	 * @param targetImg 保存的图片路径
	 * @param watermark 水印内容
	 * @param color 水印颜色
	 * @param font 水印字体
	 * @throws IOException
	 */
	public static void addWatermark(String sourceImg, String targetImg, String watermark, Color color, Font font) throws IOException {
		File srcImgFile = new File(sourceImg);
		Image srcImg = ImageIO.read(srcImgFile);
		int srcImgWidth = srcImg.getWidth(null);
		int srcImgHeight = srcImg.getHeight(null);
		BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufImg.createGraphics();
		g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
		g.setColor(color);
		g.setFont(font);
		// 设置水印的坐标
		int x = srcImgWidth
				- (g.getFontMetrics(g.getFont()).charsWidth(watermark.toCharArray(), 0, watermark.length()) + 20);
		int y = srcImgHeight - 25;
		g.drawString(watermark, x, y); // 加水印
		g.dispose();
		// 输出图片
		FileOutputStream outImgStream = new FileOutputStream(targetImg);
		ImageIO.write(bufImg, "jpg", outImgStream);
		System.out.println("添加水印完成");
		outImgStream.flush();
		outImgStream.close();
	}
}
