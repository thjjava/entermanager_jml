package com.sttri.excel;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.sttri.pojo.DevLog;

public class ExcelUtil {

	public static void exportExcel(HttpServletResponse response, List<DevLog> list) {
		try {
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition",
					"attachment; filename=fine.xls");// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型

			WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
			String tmptitle = "设备日志"; // 标题
			WritableSheet wsheet = wbook.createSheet(tmptitle, 0); // sheet名称

			// 设置excel标题
			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
			wcfFC.setBackground(Colour.AQUA);
			wsheet.addCell(new Label(1, 0, tmptitle, wcfFC));
			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					Colour.BLACK);
			wcfFC = new WritableCellFormat(wfont);

			// 开始生成主体内容
			wsheet.addCell(new Label(0, 2, "设备编号"));
			wsheet.addCell(new Label(1, 2, "设备名称"));

			for (int i = 0; i < list.size(); i++) {
				wsheet.addCell(new Label(0, i + 3, list.get(i).getDev()
						.getDevNo())); 
				wsheet.addCell(new Label(1, i + 3, list.get(i).getDev()
						.getDevName()));

			}
			// 主体内容生成结束
			wbook.write(); // 写入文件
			wbook.close();
			os.close(); // 关闭流
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
