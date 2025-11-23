@regress
Feature: DEMOQA

  @smoke
  Scenario: Тест двойного клика
    * Открыть страницу «Buttons Page»
    * Двойной клик по элементу «Double Click Me»
    * Проверить текст элемента «Double Click Message» с ожидаемым текстом «{DoubleClickMessage.значение}» c описанием проверки «Проверка текста сообщения после клика по кнопке 'Double Click Me'»

  @smoke1
  Scenario: Тест правого клика
    * Открыть страницу «Buttons Page»
    * Правый клик по элементу «Right Click Me»
    * Проверить текст элемента «Right Click Message» с ожидаемым текстом «{RightClickMessage.значение}» c описанием проверки «Проверка текста сообщения после клика по кнопке 'Right Click Me'»

  Scenario: Тест клика
    * Открыть страницу «Buttons Page»
    * Клик по элементу «Click Me»
    * Проверить текст элемента «Dynamic Click Message» с ожидаемым текстом «{DynamicClickMessage.значение}» c описанием проверки «Проверка текста сообщения после клика по кнопке 'Click Me'»