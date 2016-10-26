import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;

public class LostFilm_Data_Show {
    private String title;
    private String status;
    private String URL;
    private ArrayList<LostFilm_Data_Episode> episodesList = new ArrayList<LostFilm_Data_Episode>();
    private int count = 0;

    public LostFilm_Data_Show(WebDriver driver, WebElement parent) {
        (new Actions(driver)).keyDown(Keys.SHIFT).click(parent).keyUp(Keys.SHIFT).perform();
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        URL = driver.getCurrentUrl();
        WebElement temp = driver.findElement(By.xpath("//div[@class='mid']/div"));
        title = temp.findElement(By.xpath("h1")).getText();
        String[] infoItem = temp.getText().split("\n");
        for (String infoStatus : infoItem) {
            if (infoStatus.contains("Статус:")) {
                this.status = infoStatus;
            }
        }
        List<WebElement> series = driver.findElements(By.xpath("//div[contains(@class, 't_row')]"));
        for (WebElement episode : series) {
            episodesList.add(count, new LostFilm_Data_Episode(episode));
            count++;
        }
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getURL() {
        return URL;
    }

    public LostFilm_Data_Episode getEpisode(int N) {
        return episodesList.get(N);
    }

    public int getEpisodesCount() {
        return count;
    }

    public boolean isEpisodePresent(LostFilm_Data_Episode episode) {
        return episodesList.contains(episode);
    }

    public boolean isListTheSame(LostFilm_Data_Show list) {
        return episodesList.equals(list);
    }
}
