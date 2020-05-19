package com.hx.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * 
 * @author Huathy
 * @date 2020年4月4日
 */
public class FileUploadUtil {
	/**
	 * 单图片上传
	 * 
	 * @param pic
	 *            图片
	 * @param req
	 * @return savePath 文件相对路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String uploadPic(MultipartFile pic, HttpServletRequest req)
			throws IllegalStateException, IOException {
		String savePath = "../freshPics";
		String picName = pic.getOriginalFilename().trim();
		String path = req.getServletContext().getRealPath("");
		String temp = req.getServletContext().getInitParameter("uploadPath");
		if (!ParamsUtil.checkNull(temp)) {
			savePath = temp;
		}
		savePath += "/" + new Date().getTime() + "_" + new Random().nextInt(10000) + "-" + picName;
		File dest = new File(path, savePath);
		pic.transferTo(dest);
		return savePath;
	}

	/**
	 * 单图片上传
	 * 
	 * @param pic
	 *            图片
	 * @param req
	 * @return savePath 文件相对路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String uploadPicWatermark(MultipartFile pic, HttpServletRequest req) throws IllegalStateException, IOException {
		String savePath = "../freshPics";
		String picName = pic.getOriginalFilename().trim();
		String path = req.getServletContext().getRealPath("");
		String temp = req.getServletContext().getInitParameter("uploadPath");
		if (!ParamsUtil.checkNull(temp)) {
			savePath = temp;
		}
		savePath += "/" + new Date().getTime() + "_" + new Random().nextInt(10000) + "-" + picName;
		
		File dest = new File(path, savePath);
		pic.transferTo(dest);
		synchronized(pic){
			addWaterMark(dest, savePath);	//重新把图片写上
		}
		return savePath;
	}
	
	/**
	 * 添加文字水印
	 * @param beforImg
	 * @param afterImg
	 */
	public static void addWaterMark(File file , String afterImg) {
		String waterMarkContent = "Fresh Every Day BY HUATHY IT"; // 水印内容
		try {
			BufferedImage buImage = ImageIO.read(file);
			int width = buImage.getWidth(); // 图片宽
			int height = buImage.getHeight(); // 图片高
			Font font = new Font("宋体", Font.PLAIN, width/waterMarkContent.length()); // 水印字体
			// 添加水印
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(buImage, 0, 0, width, height, null);
			g.setColor(new Color(0, 0, 0)); // 水印颜色
			g.setBackground(new Color(255, 255, 255));// 水印背景颜色
			g.setFont(font); // 水印字体
			int x = width-waterMarkContent.length()*font.getSize();
			int y = height-font.getSize();
			System.err.println(x +"-"+ y);
			g.drawString(waterMarkContent, x, y); // 水印位置
			g.dispose(); // 释放资源

			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			ImageIO.write(bufferedImage, "jpg", fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
