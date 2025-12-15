package driverAdapter.adapter_api_contracts.element;

public interface ElementStateAdapter {

    boolean isElementDisplayed(String elementName);

    boolean isElementPresent(String elementName);

    boolean isElementEnabled(String elementName);
}
