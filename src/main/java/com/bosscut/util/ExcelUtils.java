package com.bosscut.util;

import com.bosscut.entity.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ExcelUtils {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Product Name", "Price", "Price Old", "Description" };
    static String SHEET = "Tutorials";
    public static ByteArrayInputStream productsToExcel(List<Product> tutorials) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }
            int rowIdx = 1;
            for (Product tutorial : tutorials) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(Objects.nonNull(tutorial.getProductName()) ? tutorial.getProductName() : "");
                row.createCell(1).setCellValue(Objects.nonNull(tutorial.getPrice()) ? tutorial.getPrice() : 0);
                row.createCell(2).setCellValue(Objects.nonNull(tutorial.getPercentSale()) ? tutorial.getPercentSale() : 0);
                row.createCell(3).setCellValue(Objects.nonNull(tutorial.getDescription()) ? tutorial.getDescription() : "");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
