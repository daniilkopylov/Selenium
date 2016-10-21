import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class LostFilm_Data {
    private ArrayList<LostFilm_Data_Show> showsList = new ArrayList<LostFilm_Data_Show>();
    private int count = 0;

    public LostFilm_Data(WebDriver driver) {
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='mid']/div/a[contains(@href,'cat=')]"));
        for (WebElement show: list) {
            showsList.add(count, new LostFilm_Data_Show(driver, show));
        }
    }
}
