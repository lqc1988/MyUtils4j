package com.lqc.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/** 
 * @ClassName: ImageGeneratorFZ 
 * @CreateTime 2016年7月29日 下午5:14:14 
 * @author : liqinchao 
 * @Description: 验证码生成工具类，生成计算题的验证码 
 *  
 */  
public class ImageGeneratorFZ {
	public String imgCode;
	public BufferedImage image;

	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static ImageGeneratorFZ make() {
		int width = 100, height = 40;
		int red = 0, green = 0, blue = 0;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		g.setFont(new Font("Times New Roman", Font.ITALIC, height - 3));
		g.setColor(getRandColor(160, 200));
		//生成验证码
		int aa=random.nextInt(20);
		int bb=random.nextInt(20);
		int cc=random.nextInt(3);
		String code="";
		int dd=0;
		switch (cc) {
			case 0:
				dd=aa+bb;
				code=aa+"加"+bb;
				break;
			case 1:
				if (aa<bb) {
					bb=random.nextInt(aa);
				}
				dd=aa-bb;
				code=aa+"减"+bb;
				break;
			case 2:
				aa=random.nextInt(9);
				bb=random.nextInt(9);
				dd=aa*bb;
				code=aa+"乘"+bb;
				break;
			default:
				dd=aa+bb;
				code=aa+"加"+bb;
				break;
		}
//		System.out.println("code==="+code);
		int si=random.nextInt(4);
		int fsize=random.nextInt(15)+30;
		switch (si) {
			case 0:
		        g.setFont(new Font("黑体", Font.BOLD, fsize));
				break;
			case 1:
		        g.setFont(new Font("宋体", Font.BOLD, fsize));
				break;
			case 2:
		        g.setFont(new Font("楷体", Font.BOLD, fsize));
				break;
			case 3:
		        g.setFont(new Font("微软雅黑", Font.BOLD, fsize));
				break;
			case 4:
		        g.setFont(new Font("仿宋", Font.BOLD, fsize));
				break;
	
			default:
		        g.setFont(new Font("黑体", Font.BOLD, fsize));
				break;
		}
		for (int i = 0; i < code.length(); i++) {
			String rand = code.substring(i, i+1);
			// 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawString(String.valueOf(rand), 16 * i + random.nextInt(5) + 4, height - 5 - random.nextInt(2));
		}

		//干扰线
		for (int i = 0; i < 100; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs+random.nextInt(width/8);
			int ye = ys+random.nextInt(height/8);
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawLine(xs, ys, xe, ye);
		}
		g.dispose();

		// 生成图片完毕
		ImageGeneratorFZ result = new ImageGeneratorFZ();
		result.imgCode = String.valueOf(dd);
		result.image = image;
		return result;
	}

	// 按字节输出
	public byte[] getImgBytes() {
		ByteArrayOutputStream bot = new ByteArrayOutputStream();

		byte[] b = null;
		try {
			ImageIO.write(image, "png", bot);
			b = bot.toByteArray();
			bot.close();
		} catch (Exception e) {
		}
		return b;
	}
}
