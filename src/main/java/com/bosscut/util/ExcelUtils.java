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
    static String[] HEADER = { "Product Name", "Price", "Price Old", "Percent Sale", "Link" };
    static String SHEET = "Products";
    public static ByteArrayInputStream productsToExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.setColumnWidth(0, 50 * 256);
            sheet.setColumnWidth(4, 100 * 256);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < HEADER.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADER[col]);
            }
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(Objects.nonNull(product.getProductName()) ? product.getProductName() : "");
                row.createCell(1).setCellValue(Objects.nonNull(product.getPrice()) ? product.getPrice() : 0);
                row.createCell(2).setCellValue(Objects.nonNull(product.getPriceOld()) ? product.getPriceOld() : 0);
                row.createCell(3).setCellValue(Objects.nonNull(product.getPercentSale()) ? product.getPercentSale() : 0);
                row.createCell(4).setCellValue(Objects.nonNull(product.getLink()) ? product.getLink() : "");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
