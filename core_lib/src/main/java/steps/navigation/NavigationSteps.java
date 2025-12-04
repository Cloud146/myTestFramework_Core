package steps.navigation;

import AllureUtils.AllureUtils;
import CurrentPageUtils.PageContext;
import FileUtils.PageReader;
import driverAdapter.AdapterHolder;
import driverAdapter.adapter_api_contracts.navigation.NavigationAdapter;
import io.cucumber.java.bg.И;
import io.qameta.allure.Step;
import logging.Log;
import org.slf4j.Logger;

public class NavigationSteps {

    private static final Logger log = Log.get(NavigationSteps.class);

    public NavigationSteps() {}

    public NavigationSteps(NavigationAdapter ignored) {}

    private NavigationAdapter getAdapter() {
        return AdapterHolder.get();
    }

    @И("Перейти по url «(.+)»$")
    @Step("Переход по url: {url}")
    public void openURL(String url){
        getAdapter().openURL(url);
        log.info("Выполнен переход по url: {}", url);
    }

    @И("Открыть страницу «(.+)»$")
    @Step("Открываем страницу: {pageName}")
    public void openPage(String pageName){
        PageReader page = PageReader.usePageYAML(pageName);
        openURL(page.getPageUrl());
        PageContext.getHolder().set(page);

        AllureUtils.updateAllureLabels(page.getProject(), "", "");
        log.info("Используется страница: {}", pageName);
    }
}
