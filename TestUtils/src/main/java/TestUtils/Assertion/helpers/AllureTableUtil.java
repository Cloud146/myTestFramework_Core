package TestUtils.Assertion.helpers;

import TestUtils.Assertion.model.AssertionEntry;
import TestUtils.Assertion.model.AssertionFailure;
import TestUtils.Assertion.model.StateAssertionEntry;
import io.qameta.allure.Allure;
import java.util.List;

public final class AllureTableUtil {

    private AllureTableUtil() {}

    private static final String STYLE = "<style>" +
            "table {border-collapse: collapse; width: 100%; font-family: sans-serif; font-size: 14px;} " +
            "th, td {border: 1px solid #ddd; padding: 8px; text-align: left;} " +
            "th {background-color: #f2f2f2; font-weight: bold;} " +
            "tr:hover {background-color: #f5f5f5;}" +
            "</style>";

    public static void attachTextChecks(String name, List<AssertionEntry> checks) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>").append(STYLE).append("</head><body>");
        sb.append("<h4>").append(name).append("</h4>");
        sb.append("<table>");

        // Заголовок
        sb.append("<thead><tr>")
                .append("<th>Элемент</th>")
                .append("<th>Состояние</th>")
                .append("<th>Значение</th>")
                .append("</tr></thead>");

        // Тело
        sb.append("<tbody>");
        for (AssertionEntry check : checks) {
            sb.append("<tr>")
                    .append("<td>").append(escape(check.getElement())).append("</td>")
                    .append("<td>").append(escape(check.getCondition())).append("</td>")
                    .append("<td>").append(escape(check.getValue())).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table></body></html>");

        Allure.addAttachment(name, "text/html", sb.toString(), ".html");
    }

    public static void attachStateChecks(String name, List<StateAssertionEntry> checks) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>").append(STYLE).append("</head><body>");
        sb.append("<h4>").append(name).append("</h4>");
        sb.append("<table>");

        sb.append("<thead><tr>")
                .append("<th>Элемент</th>")
                .append("<th>Состояние</th>")
                .append("</tr></thead>");

        sb.append("<tbody>");
        for (StateAssertionEntry check : checks) {
            sb.append("<tr>")
                    .append("<td>").append(escape(check.getElement())).append("</td>")
                    .append("<td>").append(escape(check.getState())).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table></body></html>");

        Allure.addAttachment(name, "text/html", sb.toString(), ".html");
    }

    private static String escape(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /**
     * Формирует красивый отчет об ошибках SoftAssertions.
     */
    public static void attachAssertionFailure(List<String> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>").append(STYLE).append("</head><body>");
        sb.append("<h4 style='color: red;'>Обнаружены несоответствия:</h4>");
        sb.append("<table>");
        sb.append("<thead><tr><th>№</th><th>Ошибка</th></tr></thead>");
        sb.append("<tbody>");

        int count = 1;
        for (String errMessage : errors) {
            sb.append("<tr>");
            sb.append("<td>").append(count++).append("</td>");

            String safeMessage = escape(errMessage).replace("\n", "<br>");

            sb.append("<td style='color: #d9534f; white-space: pre-wrap;'>").append(safeMessage).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody></table></body></html>");

        Allure.addAttachment("Список ошибок", "text/html", sb.toString(), ".html");
    }

    /**
     * Формирует таблицу несоответствий: Элемент | Состояние | Ожидалось | Фактически
     */
    public static void attachAssertionFailureTable(List<AssertionFailure> failures) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head>").append(STYLE).append("</head><body>");
        sb.append("<h4 style='color: #d9534f;'>Детали ошибок:</h4>");
        sb.append("<table>");

        sb.append("<thead><tr>")
                .append("<th>Элемент</th>")
                .append("<th>Условие</th>")
                .append("<th>Ожидаемое значение</th>")
                .append("<th>Фактическое значение</th>")
                .append("</tr></thead>");

        sb.append("<tbody>");
        for (AssertionFailure fail : failures) {
            sb.append("<tr>");
            sb.append("<td>").append(escape(fail.getElement())).append("</td>");
            sb.append("<td>").append(escape(fail.getCondition())).append("</td>");
            sb.append("<td style='background-color: #e6fffa;'>").append(escape(fail.getExpected())).append("</td>");
            sb.append("<td style='background-color: #ffe6e6;'>").append(escape(fail.getActual())).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody></table></body></html>");

        Allure.addAttachment("Таблица несоответствий", "text/html", sb.toString(), ".html");
    }
}
