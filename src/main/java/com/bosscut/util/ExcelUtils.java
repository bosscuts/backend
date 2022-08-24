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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class ExcelUtils {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADER = { "Product Name", "Price", "Price Old", "Percent sale", "Status", "New/Old", "Link" };
    static String[] NEW_PRODUCT_HEADER = { "Product Name", "Price", "Link" };
    static String SHEET = "Products";
    static String NEW_PRODUCT_SHEET = "New Products";
    public static ByteArrayInputStream productsToExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.setColumnWidth(0, 50 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 12 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 15 * 256);
            sheet.setColumnWidth(5, 15 * 256);
            sheet.setColumnWidth(6, 100 * 256);
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
                row.createCell(1).setCellValue(Objects.nonNull(product.getPrice()) ? priceWithDecimal(product.getPrice()) : "");
                row.createCell(2).setCellValue(Objects.nonNull(product.getPriceOld()) ? priceWithDecimal(product.getPriceOld()) : "");
                row.createCell(3).setCellValue(Objects.nonNull(product.getPercentSaleString()) ? product.getPercentSaleString() : "");
                row.createCell(4).setCellValue(Objects.nonNull(product.getStatus()) ? product.getStatus() : "");
                row.createCell(5).setCellValue(Objects.nonNull(product.getStatusType()) ? product.getStatusType() : "");
                row.createCell(6).setCellValue(Objects.nonNull(product.getLink()) ? product.getLink() : "");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream newProductsToExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(NEW_PRODUCT_SHEET);
            sheet.setColumnWidth(0, 50 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 100 * 256);
            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < NEW_PRODUCT_HEADER.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(NEW_PRODUCT_HEADER[col]);
            }
            int rowIdx = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(Objects.nonNull(product.getProductName()) ? product.getProductName() : "");
                row.createCell(1).setCellValue(Objects.nonNull(product.getPrice()) ? priceWithDecimal(product.getPrice()) : "");
                row.createCell(2).setCellValue(Objects.nonNull(product.getLink()) ? product.getLink() : "");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static String priceWithDecimal(BigDecimal price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }
}
