import org.apache.poi.xssf.usermodel.*;

public class SheetTools {
    public static void shiftCell(XSSFSheet sheet, XSSFRow row, int sourceCellNum, int destinationCellNum) {
        XSSFCell newCell = row.getCell(destinationCellNum);
        XSSFCell sourceCell = row.getCell(sourceCellNum);
        if (sourceCell != null) {
            if (newCell != null) {
                shiftCell(sheet, row, sourceCellNum+1, destinationCellNum+1);
                newCell.setCellValue(sourceCell.getStringCellValue());
                newCell.setCellStyle(sourceCell.getCellStyle());
            } else {
                newCell = row.createCell(destinationCellNum);
                newCell.setCellValue(sourceCell.getStringCellValue());
                newCell.setCellStyle(sourceCell.getCellStyle());
            }
        }
    }

    public static void shiftRow(XSSFWorkbook book, XSSFSheet sheet, int sourceRowNum, int destinationRowNum) {
        XSSFRow newRow = sheet.getRow(destinationRowNum);
        XSSFRow sourceRow = sheet.getRow(sourceRowNum);
        if (newRow != null) {
            sheet.shiftRows(destinationRowNum, sheet.getLastRowNum(), 1);
        } else {
            newRow = sheet.createRow(destinationRowNum);

        }
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            XSSFCell oldCell = sourceRow.getCell(i);
            XSSFCell newCell = newRow.createCell(i);
            if (oldCell == null) {
                newCell = null;
                continue;
            }
            XSSFCellStyle newCellStyle = book.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);
            newCell.setCellValue(oldCell.getStringCellValue());
        }
    }
}
