import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LostFilm_Data_Episode {
    private String episode_num;
    private String title;

    public LostFilm_Data_Episode(WebElement parent) {
        episode_num = parent.findElement(By.xpath("table/tbody/tr/td[@class='']/span/span[2]")).getText();
        title = parent.findElement(By.xpath("table/tbody/tr/td/div/div/nobr")).getText();
    }

    public LostFilm_Data_Episode(String episode) {

    }



    public String getEpisodeNum() {
        return episode_num;
    }

    public String getTitleRU() {
        return title.split("\n")[0];
    }

    public String getTitleEN() {
        return title.split("\n")[1];
    }
}
