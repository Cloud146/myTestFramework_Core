@regress
Feature: DEMOQA

  Scenario: Тест двойного клика
    * Открыть страницу «Buttons Page»
    * Двойной клик по элементу «Double Click Me»
    * Проверить текст элемента «Double Click Message» с ожидаемым текстом «{DoubleClickMessage}»
    * Проверить текст элементов «Проверка текста сообщения после клика по кнопке 'Double Click Me'»:
      | элемент              | состояние | значение             |
      | Double Click Message | равен     | {DoubleClickMessage} |
      | Double Click Me      | равен     | Double Click Me      |
    * Проверить состояние элементов «Видимость кнопок»:
      | элемент         | состояние |
      | Double Click Me | активен   |
      | Right Click Me  | активен   |
      | Click Me        | активен   |

  Scenario: Тест правого клика
    * Открыть страницу «Buttons Page»
    * Правый клик по элементу «Right Click Me»
    * Проверить текст элемента «Right Click Message» с ожидаемым текстом «{RightClickMessage}»

  Scenario: Тест клика
    * Открыть страницу «Buttons Page»
    * Клик по элементу «Click Me»
    * Проверить текст элемента «Dynamic Click Message» с ожидаемым текстом «{DynamicClickMessage}»