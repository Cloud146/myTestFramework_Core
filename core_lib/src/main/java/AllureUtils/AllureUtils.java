package AllureUtils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;

public class AllureUtils {

    /** Удалить дефолтные suite/parentSuite/subSuite метки, которые Allure проставляет автоматически. */
    public static void clearSuiteLabels() {
        Allure.getLifecycle().updateTestCase(tc ->
                tc.getLabels().removeIf(l ->
                        l.getName().equals("suite")
                                || l.getName().equals("parentSuite")
                                || l.getName().equals("subSuite")
                )
        );
    }

    /** Добавить parentSuite с указанным значением (если оно не пустое). */
    public static void addParentSuite(String parentSuiteName) {
        if (parentSuiteName != null && !parentSuiteName.isBlank()) {
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.getLabels().add(new Label().setName("parentSuite").setValue(parentSuiteName))
            );
        }
    }

    /** Добавить suite с указанным значением (если оно не пустое). */
    public static void addSuite(String suiteName) {
        if (suiteName != null && !suiteName.isBlank()) {
            Allure.getLifecycle().updateTestCase(tc ->
                    tc.getLabels().add(new Label().setName("suite").setValue(suiteName))
            );
        }
    }

    /** Добавить subSuite с указанным значением (если оно не пустое). */
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
