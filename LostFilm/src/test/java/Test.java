import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    @org.testng.annotations.Test
            public void test() {

        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to("http://www.lostfilm.tv");
        driver.findElement(By.xpath("//a[@href='/serials.php']")).click();


        WebElement element = driver.findElement(By.xpath("//div[@class='mid']/div/a[contains(@href,'cat=')]"));
        LostFilm_Data_Show show = new LostFilm_Data_Show(driver, element);

    }








    ChromeDriver WebDriver;
    List<WebElement> list;

    XSSFWorkbook book;
    XSSFSheet sheet;

    XSSFCellStyle finished, ongoing, currentSeriesStyle, seriesStyle;

    @BeforeMethod
    public void Lostfilm_Start_And_GetData() throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
        WebDriver = new ChromeDriver();
        WebDriver.manage().window().maximize();
        WebDriver.navigate().to("http://www.lostfilm.tv");
        WebDriver.findElement(By.xpath("//a[@href='/serials.php']")).click();
        Thread.sleep(1000);
        list = WebDriver.findElements(By.xpath("//div[@class='mid']/div/a[contains(@href,'cat=')]"));

        book = new XSSFWorkbook(new FileInputStream("E:\\file.xlsx"));

        //// // TODO: 08.10.2016

        sheet = book.getSheet("LostFilm");


        finished = book.createCellStyle();
        XSSFFont finishedFont = book.createFont();
        finishedFont.setFontName("Consolas");
        finishedFont.setBold(true);
        finishedFont.setColor(new XSSFColor(new Color(0x86868F)));
        finished.setFont(finishedFont);

        ongoing = book.createCellStyle();
        XSSFFont ongoingFont = finishedFont;
        ongoingFont.setColor(new XSSFColor(new Color(0x000000)));
        ongoing.setFillForegroundColor(new XSSFColor(new Color(0x3F983C)));
        ongoing.setFillPattern(CellStyle.SOLID_FOREGROUND);
        ongoing.setFont(ongoingFont);

        currentSeriesStyle = book.createCellStyle();
        XSSFFont cellSeriesStyle = finishedFont;
        cellSeriesStyle.setColor(new XSSFColor(new Color(0x151898)));
        currentSeriesStyle.setFont(cellSeriesStyle);

        seriesStyle = book.createCellStyle();
        XSSFFont seriesFontStyle = finishedFont;
        seriesFontStyle.setColor(new XSSFColor(new Color(0xFFFFFF)));
        seriesStyle.setFont(seriesFontStyle);
    }

    @org.testng.annotations.Test(groups = "LostFilm")
    public void LostFilmTV_Read() throws InterruptedException, IOException {
        int i = 0;
        for (WebElement element : list) {
            XSSFRow row = sheet.createRow(i);
            XSSFCell nameRU = row.createCell(0);
            nameRU.setCellValue(element.getText());
            XSSFCell current = row.createCell(1);
            current.setCellValue("->");
            current.setCellStyle(currentSeriesStyle);
            XSSFCell start = row.createCell(2);
            start.setCellValue("->");
            start.setCellStyle(seriesStyle);

            sheet.autoSizeColumn(0);

            (new Actions(WebDriver)).keyDown(Keys.SHIFT).click(element).keyUp(Keys.SHIFT).perform();
            Thread.sleep(1000);
            ArrayList<String> tabs = new ArrayList<String>(WebDriver.getWindowHandles());
            WebDriver.switchTo().window(tabs.get(1));

            WebElement temp = WebDriver.findElement(By.xpath("//div[@class='mid']/div"));

            if (temp.getText().contains("Статус: закончен")) {
                nameRU.setCellStyle(finished);
                XSSFCell end = row.createCell(3);
                end.setCellValue("The End");
                end.setCellStyle(seriesStyle);

            }
            if (temp.getText().contains("Статус: снимается")) {
                nameRU.setCellStyle(ongoing);
                XSSFCell end = row.createCell(3);
                end.setCellValue("To Be Continue");
                end.setCellStyle(seriesStyle);
            }

            List<WebElement> series = WebDriver.findElements(By.xpath("//div[contains(@class, 't_row')]/table/tbody"));

            for (WebElement item : series) {
                String body = "";
                body += item.findElement(By.xpath("tr/td/span[@class='micro']/span[2]")).getText();
                body += " | ";
                body += item.findElement(By.xpath("tr/td/div/div/nobr/span")).getText();

                SheetTools.shiftCell(sheet, row, 3, 4);
                XSSFCell itemCell = row.getCell(3);
                if (itemCell == null) {
                    itemCell = row.createCell(3);
                }
                itemCell.setCellValue(body);
            }

            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);

            CellReference CellAddress1 = new CellReference(i, 2);
            CellReference CellAddress2 = new CellReference(i, row.getLastCellNum() - 1);
            String AddressRange = String.format("%s:%s", CellAddress1.formatAsString(), CellAddress2.formatAsString());
            XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint)
                    dvHelper.createFormulaListConstraint(AddressRange);
            CellRangeAddressList addressList = new CellRangeAddressList(i, i, 1, 1);

            XSSFDataValidation dataValidation = (XSSFDataValidation) dvHelper.createValidation(
                    dvConstraint, addressList);

            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
            sheet.addValidationData(dataValidation);


            //TODO HIDE CELLS (COLUMS)


            WebDriver.close();
            WebDriver.switchTo().window(tabs.get(0));

            i++;
        }


    }

    //TODO READ/UPDATE ROWS

    @AfterMethod
    public void thearDown() throws IOException {
        if (WebDriver != null) {
            book.write(new FileOutputStream("E:\\file.xlsx"));
            book.close();
            WebDriver.quit();
        }
    }
}
