package AllureUtils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;

public class AllureUtils {

    public static void clearSuiteLabels() {
        Allure.getLifecycle().updateTestCase(tc ->
                tc.getLabels().removeIf(l ->
                        l.getName().equals("suite")
                                || l.getName().equals("parentSuite")
                                || l.getName().equals("subSuite")
                )
        );
    }

    public static void addParentSuite(String parentSuiteName) {
        if (parentSuiteName != null && !parentSuiteName.isBlank()) {
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.getLabels().add(new Label().setName("parentSuite").setValue(parentSuiteName))
            );
        }
    }

    public static void addSuite(String suiteName) {
        if (suiteName != null && !suiteName.isBlank()) {
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.getLabels().add(new Label().setName("suite").setValue(suiteName))
            );
        }
    }

    public static void addSubSuite(String subSuiteName) {
        if (subSuiteName != null && !subSuiteName.isBlank()) {
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.getLabels().add(new Label().setName("subSuite").setValue(subSuiteName))
            );
        }
    }

    public static void updateAllureLabels(String parentSuiteName, String suiteName, String subSuiteName){
        clearSuiteLabels();
        addParentSuite(parentSuiteName);
        addSuite(suiteName);
        addSubSuite(subSuiteName);
    }
}
