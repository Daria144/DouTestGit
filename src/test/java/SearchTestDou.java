

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class SearchTestDou {
    WebDriver driver;
    WebElement searchField;

    @BeforeMethod(alwaysRun = true)
    public void runChromeDriverAndNavigateToDou(){
        System.setProperty("webdriver.chrome.driver","/Users/daria_yatsenko/Documents/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://dou.ua/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement inputfield=driver.findElement(By.id("txtGlobalSearch"));
        inputfield.sendKeys(Keys.ENTER);
        searchField = driver.findElement(By.id("gsc-i-id1"));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }
   @AfterMethod(alwaysRun = true)
    public void cleanSearchField(){
        searchField.clear();
    }

    @Test
            (groups = "NumberOfSearchedResults")
    public void displayTotalNumberOfSearchedResults(){
        searchField.sendKeys("qa");
        searchField.sendKeys(Keys.ENTER);
        String resultsTotalNumber = driver.findElement(By.id("resInfo-0")).getText();
        assertNotNull(resultsTotalNumber,"Total number of searched results is not null");
    }
    @DataProvider(name="keywords")
    public static Object[][] keywords() {
        return new Object[][]{
                {"java"},{"python"},{"dotnet"},{"typescript"}
        };
    }

    @Test
            (groups="ContainsSearchedKeyword",
    dataProvider = "keywords")
    public void resultsContainsSearchedKeyword(String keywords){
        String keyword="qa";
        int resultsCount=0;
        searchField.sendKeys(keyword);
        searchField.sendKeys(Keys.ENTER);
        List<WebElement>  searchedResult =driver.findElements(By.xpath("//div[@class=\"gsc-thumbnail-inside\"]//a/b"));
        for (WebElement webElement : searchedResult) {
            if (webElement.getText().toLowerCase(Locale.ROOT).contains(keyword)) {
                resultsCount++;
            }
        }

        assertEquals(resultsCount,searchedResult.size(),"All results from search list contains keyword");
        searchedResult.clear();
    }

    @Test
            (groups = "ClearedSearchedQuery")
    public void clearSearchedQuery(){
        String query = "searchedQuery";
        searchField.sendKeys(Keys.ENTER);
        WebElement inputField = driver.findElement(By.id("gsc-i-id1"));
        inputField.sendKeys(query);
        WebElement closeTab = driver.findElement(By.id("gs_cb50"));
        closeTab.click();
        assertEquals(inputField.getText(),"","Inputfield is cleared clicking on cross icon");

    }

    @Test
            (groups = "FailedSearchResult")
    public void failedSearchResult(){
        String query="%";
        searchField.sendKeys(query);
        searchField.sendKeys(Keys.ENTER);
        boolean searchIsEmpty = driver.findElements(By
                        .xpath("//*[@class=\"gsc-expansionArea\"]//*[@class=\"gs-title\"]/a"))
                .isEmpty();
        assertFalse(searchIsEmpty,"Fail in rearch result is NOT NULL");
    }
        @Test
            (groups = "PassedSearchResult")
    public void passedSearchResult(){
        String query="%";
        searchField.sendKeys(query);
        searchField.sendKeys(Keys.ENTER);
        boolean searchIsEmpty = driver.findElements(By
                        .xpath("//*[@class=\"gsc-expansionArea\"]//*[@class=\"gs-title\"]/a"))
                .isEmpty();
        assertTrue(searchIsEmpty,"Pass in rearch result is NOT NULL");
    }

    @Test
            (groups = "FailedPagination")
    public void failedPagination(){
        String query="s";
        int expectedSize=10;
        searchField.sendKeys(query);
        searchField.sendKeys(Keys.ENTER);

        List<WebElement> paginationPages = driver.findElements(By.xpath("//*[@class=\"gsc-cursor\"]//div"));
        assertNotEquals(paginationPages.size(),expectedSize,"Fail if size is 10");
        paginationPages.clear();

    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver(){
        driver.quit();
    }
}
